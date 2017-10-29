/*
 * Copyright 2017 Information and Computational Sciences,
 * The James Hutton Institute.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jhi.gatekeeper.server.util;

import java.util.*;
import java.util.concurrent.*;

import javax.servlet.http.*;

import jhi.database.shared.exception.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link Session} is a class containing methods to check session ids and user credentials.
 *
 * @author Sebastian Raubach
 */
public class Session
{
	public static final String SID  = "sid";
	public static final String USER = "user";

	private static ConcurrentHashMap<Long, Set<HttpSession>> userSessions = new ConcurrentHashMap<>();

	public static synchronized void storeSession(UserAuthentication user, HttpSession session)
	{
		Set<HttpSession> sessions = userSessions.get(user.getId());

		if (sessions == null)
			sessions = new HashSet<>();

		sessions.add(session);

		userSessions.put(user.getId(), sessions);
	}

	public static synchronized void terminateSessions(Long userId)
	{
		Set<HttpSession> sessions = userSessions.get(userId);

		if (!CollectionUtils.isEmpty(sessions))
		{
			for (HttpSession session : sessions)
			{
				try
				{
					session.invalidate();
				}
				catch (IllegalStateException e)
				{
					// Do nothing here, this indicates that the session already has been invalidated.
				}
			}
		}

		userSessions.remove(userId);
	}

	/**
	 * Checks if the current session is still valid
	 *
	 * @param payloadSessionId The session id (transmitted in the request as the payload)
	 * @param httpRequest      The http request containing the cookies
	 */
	public static void checkSession(String payloadSessionId, HttpServletRequest httpRequest) throws InvalidSessionException
	{
		String cookieSessionId = null;
		String sessionSessionId = null;

        /*
		 * For IE and Chrome we have to make sure the HTTPServletRequest and the
         * Cookies are really set
         */
		if (httpRequest != null)
		{
			UserAuthentication auth = (UserAuthentication) httpRequest.getSession().getAttribute(Session.USER);
			sessionSessionId = auth != null ? auth.getSessionId() : null;

			if (httpRequest.getCookies() != null)
			{
				for (Cookie cookie : httpRequest.getCookies())
				{
					if (cookie.getName().equals(SID))
					{
						cookieSessionId = cookie.getValue();
						break;
					}
				}
			}
		}

        /* Check if one of the properties is missing altogether */
		if (StringUtils.isEmpty(cookieSessionId))
			throw new InvalidSessionException(InvalidSessionException.InvalidProperty.INVALID_COOKIE);
		else if (StringUtils.isEmpty(payloadSessionId))
			throw new InvalidSessionException(InvalidSessionException.InvalidProperty.INVALID_PAYLOAD);
		else if (StringUtils.isEmpty(sessionSessionId))
			throw new InvalidSessionException(InvalidSessionException.InvalidProperty.INVALID_SESSION);

        /* If they are all identical, just return */
		else if (StringUtils.areEqual(payloadSessionId, cookieSessionId, sessionSessionId))
			return;

        /* If they are all distinct, we don't know which one is the wrong one */
		else if (StringUtils.areDistinct(payloadSessionId, cookieSessionId, sessionSessionId))
			throw new InvalidSessionException(InvalidSessionException.InvalidProperty.UNKNOWN);

        /* Else one of them has to be the odd one, find out which one */
		else if (StringUtils.areEqual(payloadSessionId, payloadSessionId))
			throw new InvalidSessionException(InvalidSessionException.InvalidProperty.INVALID_COOKIE);
		else if (StringUtils.areEqual(cookieSessionId, sessionSessionId))
			throw new InvalidSessionException(InvalidSessionException.InvalidProperty.INVALID_PAYLOAD);
		else if (StringUtils.areEqual(cookieSessionId, payloadSessionId))
			throw new InvalidSessionException(InvalidSessionException.InvalidProperty.INVALID_SESSION);

        /* If we get here, everything is fine... */
	}

	/**
	 * Checks if the current session is still valid
	 *
	 * @param requestProperties The {@link RequestProperties}
	 * @param httpRequest       The http request containing the cookies
	 */
	public static void checkSession(RequestProperties requestProperties, HttpServletRequest httpRequest) throws InvalidSessionException
	{
		checkSession(requestProperties.getSessionId(), httpRequest);
	}

	/**
	 * Generates and returns a session id (UUID)
	 *
	 * @param password The password
	 * @return The universally unique id
	 * @throws InvalidCredentialsException      Thrown if the username/password combination is not valid
	 * @throws InsufficientPermissionsException Thrown if the user is not associated with this database at all
	 * @throws DatabaseException                Thrown if the query fails on the server
	 */
	public static String generateSessionIdAndCheckDetails(UserInternal userDetails, String password) throws InvalidCredentialsException, InsufficientPermissionsException,
			DatabaseException
	{
		/* Check if the user exists at all */
		if (userDetails == null || userDetails.getId() == null)
		{
			throw new InvalidCredentialsException("Invalid username", InvalidCredentialsException.INVALID_USERNAME);
		}
		else if (!userDetails.isHasAccessToGatekeeper())
		{
			throw new InsufficientPermissionsException("The user doesn't have access to Gatekeeper");
		}
		/* Compare supplied password with stored password */
		else if (userDetails.getPassword() == null || !BCrypt.checkpw(password, userDetails.getPassword()))
		{
			throw new InvalidCredentialsException("Invalid password", InvalidCredentialsException.INVALID_PASSWORD);
		}
		/* Else everything is ok, return the new session id */
		else
		{
			/* The salt might have changed in the meantime, update the password */
			UserManager.updatePassword(userDetails, password);

			return generateSessionId();
		}
	}

	/**
	 * Generates a new session id
	 *
	 * @return The session id
	 */
	public static String generateSessionId()
	{
		return UUID.randomUUID().toString();
	}
}

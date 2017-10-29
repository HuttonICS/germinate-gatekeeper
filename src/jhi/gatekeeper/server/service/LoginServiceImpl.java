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

package jhi.gatekeeper.server.service;

import com.google.gwt.user.server.rpc.*;

import javax.servlet.annotation.*;

import jhi.database.shared.exception.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.server.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link LoginServiceImpl} is the implementation of {@link LoginService}.
 *
 * @author Sebastian Raubach
 */
@WebServlet(urlPatterns = {"/gatekeeper/login"})
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService
{
	private static final long serialVersionUID = 5883096971269433956L;

	private static final int SESSION_LIFETIME_MIN = PropertyReader.getPropertyInteger(PropertyReader.GATEKEEPER_COOKIE_LIFESPAN_MINUTES, 30);

	@Override
	public void logout(RequestProperties properties) throws InvalidSessionException, SuspendedUserException
	{
		Session.checkSession(properties, getThreadLocalRequest());

		getThreadLocalRequest().getSession().invalidate();
	}

	@Override
	public UserAuthentication login(RequestProperties properties, UserCredentials credentials) throws InvalidCredentialsException, AdminAccountMissingException, SuspendedUserException,
			InsufficientPermissionsException, DatabaseException
	{
		if (!UserManager.doesAdminAccountExist())
			throw new AdminAccountMissingException();

		/* Get the relative path of the current instance of Germinate3 */
		String path = PropertyReader.getContextPath(this.getThreadLocalRequest());

		boolean sessionIsValid = true;
		UserInternal details = null;
		UserAuthentication result = null;

		try
		{
			details = UserManager.getUserByUsername(credentials.getUsername());
			result = new UserAuthentication();
			result.setPath(path);
			result.setUsername(credentials.getUsername());
			result.setDetails(details);
			result.setCookieLifespanMinutes(SESSION_LIFETIME_MIN);

			Session.checkSession(properties, this.getThreadLocalRequest());
		}
		catch (InvalidSessionException e)
		{
			sessionIsValid = false;
		}

        /* If the session id is invalid */
		if (!sessionIsValid)
		{
			/* Generate a new one */
			properties.setSessionId(Session.generateSessionIdAndCheckDetails(details, credentials.getPassword()));

            /* If this doesn't work, just return */
			if (StringUtils.isEmpty(properties.getSessionId()))
				return null;
		}

		/* See if an old authentication exists in the session (might do, if this is an auto-login using the session) */
		UserAuthentication oldAuth = UserAuthentication.getFromSession(getThreadLocalRequest());
		if (oldAuth != null && StringUtils.areEqual(oldAuth.getSessionId(), properties.getSessionId()))
		{
			result = oldAuth;
		}

		result.setSessionId(properties.getSessionId());

		getThreadLocalRequest().getSession().setAttribute(Session.USER, result);
		/* Extend the session */
		this.getThreadLocalRequest().getSession().setMaxInactiveInterval(SESSION_LIFETIME_MIN * 60);

		Session.storeSession(result, getThreadLocalRequest().getSession());

		return result;
	}
}

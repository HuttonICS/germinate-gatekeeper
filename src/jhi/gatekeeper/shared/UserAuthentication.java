/*
 *  Copyright 2017 Sebastian Raubach, Toby Philp and Paul Shaw from the
 *  Information and Computational Sciences Group at The James Hutton Institute, Dundee
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jhi.gatekeeper.shared;

import com.google.gwt.core.shared.*;

import java.io.*;

import javax.servlet.http.*;

import jhi.database.shared.exception.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.server.util.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link UserAuthentication} is a simple bean class containing user id, session id and the path of the application
 *
 * @author Sebastian Raubach
 */
public class UserAuthentication implements Serializable
{
	private static final long serialVersionUID = -4829970355425355192L;

	private Long   id;
	private String username;
	private String sessionId;
	private String emailAddress;
	private String path;
	private int    cookieLifespanMinutes;
	private boolean isAdmin = false;

	public UserAuthentication()
	{
	}

	public UserAuthentication(Long id, String username, String sessionId, String emailAddress, String path, int cookieLifespanMinutes, boolean isAdmin)
	{
		this.id = id;
		this.username = username;
		this.sessionId = sessionId;
		this.emailAddress = emailAddress;
		this.path = path;
		this.cookieLifespanMinutes = cookieLifespanMinutes;
		this.isAdmin = isAdmin;
	}

	@GwtIncompatible
	public static UserAuthentication getFromSession(HttpServletRequest req)
	{
		return (UserAuthentication) req.getSession().getAttribute(Session.USER);
	}

	@GwtIncompatible
	public static void checkIfAdmin(HttpServletRequest req) throws InsufficientPermissionsException, DatabaseException
	{
		UserAuthentication auth = getFromSession(req);

		auth.setIsAdmin(UserManager.isUserAdmin(auth.getId()));

		if (!auth.isAdmin())
			throw new InsufficientPermissionsException();
	}

	public void setDetails(User details)
	{
		if (details != null)
		{
			this.id = details.getId();
			this.username = details.getUsername();
			this.emailAddress = details.getEmail();
			this.isAdmin = details.isGatekeeperAdmin();
		}
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public int getCookieLifespanMinutes()
	{
		return cookieLifespanMinutes;
	}

	public void setCookieLifespanMinutes(int cookieLifespanMinutes)
	{
		this.cookieLifespanMinutes = cookieLifespanMinutes;
	}

	public boolean isAdmin()
	{
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString()
	{
		return "UserAuthentication{" +
				"id='" + id + '\'' +
				", sessionId='" + sessionId + '\'' +
				", path='" + path + '\'' +
				", cookieLifespanMinutes=" + cookieLifespanMinutes +
				", isAdmin=" + isAdmin +
				", username='" + username + '\'' +
				'}';
	}
}

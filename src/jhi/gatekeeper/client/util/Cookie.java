/**
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

package jhi.gatekeeper.client.util;

import com.google.gwt.i18n.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.datepicker.client.*;

import java.util.*;

import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link Cookie} is a utility class containing methods to set {@link Cookies}. Moreover, this class holds the current username, sessionId and
 * userId.
 *
 * @author Sebastian Raubach
 */
public class Cookie
{
	public static final String SID     = "sid";
	public static final String USER_ID = "user-id";

	private static Long lifespan = null;

	private static String path;
	private static String sessionId;
	private static String userId;

	/**
	 * Removes all cookies
	 */
	public static void removeAll()
	{
		Cookies.removeCookie(SID, path);
		sessionId = null;
		Cookies.removeCookie(USER_ID, path);
		userId = null;
	}

	/**
	 * Sets up a cookie containing the session id (duration: 15 minutes)
	 *
	 * @param userAuthentication The session id, path and user id
	 */
	public static void setUp(UserAuthentication userAuthentication)
	{
		if (!Cookies.isCookieEnabled())
			GatekeeperEventBus.BUS.fireEvent(new ExceptionEvent(new InvalidSessionException(InvalidSessionException.InvalidProperty.INVALID_COOKIE)));

		path = userAuthentication.getPath();
		sessionId = userAuthentication.getSessionId();
		lifespan = 1000L * 60 * userAuthentication.getCookieLifespanMinutes();

		LocalStorage.setPrefix(path);

		if (path == null)
			return;

		setCookie(SID, sessionId, getExpiryDate(), null, path, false); // FIXME: find a solution that determines the path
//        setCookie(SID, sessionId, getExpiryDate(), null, "/", false); 

		if (userAuthentication.getId() != null)
		{
			userId = userAuthentication.getId() + "";
			setCookie(USER_ID, userId, getExpiryDate(), null, path, false);
		}
	}

	private static Date getExpiryDate()
	{
		return new Date(System.currentTimeMillis() + lifespan);
	}

	/**
	 * Sets a new cookie
	 *
	 * @param name  The name of the cookie
	 * @param value The value of the cookie
	 */
	public static void setCookie(String name, String value)
	{
		setCookie(name, value, true);
	}

	/**
	 * Sets a new cookie
	 *
	 * @param name    The name of the cookie
	 * @param value   The value of the cookie
	 * @param expires Set to <code>true</code> if the cookie should expire
	 */
	public static void setCookie(String name, String value, boolean expires)
	{
		if (expires)
		{
			setCookie(name, value, getExpiryDate(), null, path, false);
		}
		else
		{
			/* Make it last for a year */
			Date toExpire = new Date(System.currentTimeMillis());
			CalendarUtil.addMonthsToDate(toExpire, 12);
			setCookie(name, value, toExpire, null, path, false);
		}
	}

	/**
	 * Returns the cookie with the given name
	 *
	 * @param name The name/key
	 * @return The cookie value
	 */
	public static String getCookie(String name)
	{
		return Cookies.getCookie(name);
	}

	/**
	 * Removes the cookie with the given name
	 *
	 * @param name The name of the cookie to remove
	 */
	public static void removeCookie(String name)
	{
		Cookies.removeCookie(name, path);
	}

	private static void setCookie(String name, String value, Date expires, String domain, String path, boolean secure)
	{
		Cookies.setCookie(name, value, expires, domain, path, secure);
	}

	/**
	 * Extends the cookie with the given name (if exists)
	 *
	 * @param name    The name of the cookie to extend
	 * @param expires <code>true</code> If the expiry date should be set to the default, <code>false</code> otherwise
	 * @return <code>true</code> if the cookie has been extended
	 */
	public static boolean extend(String name, boolean expires)
	{
		String cookie = getCookie(name);

		if (!StringUtils.isEmpty(cookie))
		{
			setCookie(name, cookie, expires);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Extends the cookie lifespan of {@link #SID} and {@link #USER_ID}.
	 */
	public static void extend()
	{
		if (path != null)
		{
			Date expires = getExpiryDate();

			if (sessionId != null)
			{
				setCookie(SID, sessionId, expires, null, path, false);
			}
			else if (Cookies.getCookie(SID) != null)
			{
				setCookie(SID, Cookies.getCookie(SID), expires, null, path, false);
			}

			if (userId != null)
			{
				setCookie(USER_ID, userId, expires, null, path, false);
			}
			else if (Cookies.getCookie(USER_ID) != null)
			{
				setCookie(USER_ID, Cookies.getCookie(USER_ID), expires, null, path, false);
			}
		}
	}

	/**
	 * Checks if the session cookie is still alive
	 *
	 * @return True if the session is still alive
	 */
	public static boolean isStillAlive()
	{
		return !Cookies.isCookieEnabled() || Cookies.getCookie(SID) != null;
	}

	public static RequestProperties getRequestProperties()
	{
		String localeString = LocaleInfo.getCurrentLocale().getLocaleName();
		return new RequestProperties(getSessionId(), getUserId(), localeString);
	}

	/**
	 * Returns the session id
	 *
	 * @return The session id
	 */
	public static String getSessionId()
	{
		if (Cookies.isCookieEnabled())
		{
			return Cookies.getCookie(SID);
		}
		else
		{
			return sessionId;
		}
	}

	/**
	 * Returns the user id
	 *
	 * @return The user id
	 */
	public static String getUserId()
	{
		if (Cookies.isCookieEnabled())
		{
			return Cookies.getCookie(USER_ID);
		}
		else
		{
			return userId;
		}
	}
}

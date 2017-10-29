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

package jhi.gatekeeper.shared;

import com.google.gwt.core.shared.*;

import java.io.*;
import java.util.*;

/**
 * {@link RequestProperties} is passed to the server with (almost) every request. It contains the {@link #sessionId}, {@link #locale} and {@link
 * #userId}
 *
 * @author Sebastian Raubach
 */
public class RequestProperties implements Serializable
{
	private static final long serialVersionUID = 2364469389016857836L;

	/** The current session id */
	private String sessionId;
	/** The locale of the client user interface */
	private String locale;
	/** The id of the user */
	private String userId;

	public RequestProperties()
	{

	}

	public RequestProperties(String sessionId, Long userId, String locale)
	{
		this.sessionId = sessionId;
		this.userId = userId != null ? Long.toString(userId) : null;
		this.locale = locale;
	}

	public RequestProperties(String sessionId, String userId, String locale)
	{
		this.sessionId = sessionId;
		this.userId = userId;
		this.locale = locale;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getLocaleString()
	{
		return locale;
	}

	@GwtIncompatible
	public Locale getLocale()
	{
		return Locale.forLanguageTag(locale.replace("_", "-"));
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	@Override
	public String toString()
	{
		return "RequestProperties{" +
				"sessionId='" + sessionId + '\'' +
				", locale='" + locale + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}
}

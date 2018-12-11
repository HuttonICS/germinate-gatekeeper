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

import java.io.*;
import java.util.*;

import javax.servlet.annotation.*;
import javax.servlet.http.*;

import jhi.database.shared.exception.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.server.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

@WebServlet(urlPatterns = {"/gatekeeper/germinate"})
public class GerminateCommunicationServlet extends HttpServlet
{
	private static final long serialVersionUID = -1601139980377228900L;

	private static final String GATEKEEPER_ERROR_EMAIL        = "GATEKEEPER_ERROR_EMAIL";
	private static final String GATEKEEPER_ERROR_INVALID_DATA = "GATEKEEPER_ERROR_INVALID_DATA";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		/* Check parameters */
		String isNewUserParameter = req.getParameter("isNewUser");
		String needsApprovalParameter = req.getParameter("needsApproval");
		String activationKeyParameter = req.getParameter("activationKey");
		String userIdParameter = req.getParameter("userId");
		String localeParameter = req.getParameter("locale");

		Locale locale = StringUtils.isEmpty(localeParameter) ? Locale.ENGLISH : Locale.forLanguageTag(localeParameter.replace("_", "-"));

		if (StringUtils.isEmpty(isNewUserParameter, needsApprovalParameter, activationKeyParameter, userIdParameter))
		{
			resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_INVALID_DATA);
			return;
		}

		boolean isNewUser = Boolean.parseBoolean(isNewUserParameter);
		boolean needsApproval = Boolean.parseBoolean(needsApprovalParameter);

		if (isNewUser)
			registerNewUser(req, resp, locale, activationKeyParameter, userIdParameter, needsApproval);
		else
			requestPermissions(req, resp, locale, activationKeyParameter, userIdParameter, needsApproval);
	}

	private void requestPermissions(HttpServletRequest req, HttpServletResponse resp, Locale locale, String activationKeyParameter, String userIdParameter, boolean needsApproval)
	{
		AccessRequest request;

		try
		{
			request = AccessRequestManager.getByActivationKey(activationKeyParameter);

			if (request == null)
			{
				resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_INVALID_DATA);
				return;
			}

            /* Check if the ids match */
			try
			{
				Long userId = Long.parseLong(userIdParameter);
				if (!request.getUser().getId().equals(userId))
					throw new Exception();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_INVALID_DATA);
				return;
			}

			if (needsApproval)
			{
				try
				{
					Email.sendAwaitingApproval(locale, request);
					Email.sendAdministratorNotification(locale, request, true);
				}
				catch (EmailException e)
				{
					e.printStackTrace();
					resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_EMAIL);
				}
			}
			else
			{
				try
				{
					sendActivationEmail(locale, request);
					resp.setStatus(HttpServletResponse.SC_OK);
					AccessRequestManager.delete(request);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_EMAIL);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
	}

	private void registerNewUser(HttpServletRequest req, HttpServletResponse resp, Locale locale, String activationKeyParameter, String userIdParameter, boolean needsApproval)
	{
		UnapprovedUser user;

		try
		{
			user = UnapprovedUserManager.getByActivationKey(activationKeyParameter);

			if (user == null)
			{
				resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_INVALID_DATA);
				return;
			}

            // Check if the ids match
			try
			{
				Long userId = Long.parseLong(userIdParameter);
				if (!user.getId().equals(userId))
					throw new Exception();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_INVALID_DATA);
				return;
			}

			try
			{
				sendActivationEmail(req, locale, user);
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				resp.sendError(HttpServletResponse.SC_CONFLICT, GATEKEEPER_ERROR_EMAIL);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
	}

	private void sendActivationEmail(Locale locale, AccessRequest request) throws EmailException, IllegalArgumentException
	{
		if (request != null)
		{
			Email.sendActivationConfirmation(locale, request.getUser());
			Email.sendAdministratorNotification(locale, request, false);
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	private void sendActivationEmail(HttpServletRequest req, Locale locale, UnapprovedUser user) throws DatabaseException, EmailException, IllegalArgumentException
	{
		if (user != null)
		{
			String url = PropertyReader.getServerBase(req);
			String uuid = UUID.randomUUID().toString();

			UnapprovedUserManager.setActivationKey(user, uuid);

			if (!url.endsWith("/"))
				url += "/";

			url += "?key=" + uuid + "#" + Page.activateUser.getUrl();

			Email.sendActivationPrompt(locale, user, url);
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	public String getBaseUrl(HttpServletRequest req)
	{
		String scheme = req.getScheme();             // http
		String serverName = req.getServerName();     // hostname.com
		int serverPort = req.getServerPort();        // 80
		String contextPath = req.getContextPath();   // /mywebapp

		// Reconstruct original requesting URL
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443))
		{
			url.append(":").append(serverPort);
		}

		url.append(contextPath);

		return url.toString();
	}
}

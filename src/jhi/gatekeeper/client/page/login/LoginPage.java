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

package jhi.gatekeeper.client.page.login;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.rpc.*;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class LoginPage extends GatekeeperPage
{
	private LoginForm form;

	/**
	 * Creates a new instance of the login page
	 */
	public LoginPage()
	{
		/* Clear the help panel and parameter store to make sure that no
		 * confidential information is accessible after logout */
		ParameterStore.clear();
	}

	/**
	 * Attempts a login. Will "refresh" the page
	 */
	private void doLogin()
	{
		final String username = form.getUsername();
		String password = form.getPassword();

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
		{
			Notification.notify(Notification.Type.INFO, I18n.LANG.notificationFillAllFields());
			return;
		}

        /* Set up the callback object for login */
		LoginService.Instance.getInstance().login(Cookie.getRequestProperties(), new UserCredentials(username, password), new AsyncCallback<UserAuthentication>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				boolean highlightUsername = false;
				boolean highlightPassword = false;
				/* Handle the exception */
				if (caught instanceof InvalidCredentialsException)
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationLoginUnsuccessful());
					highlightPassword = true;
				}
				else if (caught instanceof InsufficientPermissionsException)
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationInsufficientPermissions());
					highlightUsername = true;
				}
				else if (caught instanceof SuspendedUserException)
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationLoginSuspendedUser());
					highlightUsername = true;
				}
				else if (caught instanceof AdminAccountMissingException)
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationInsufficientPermissions());
					highlightUsername = true;
				}
				else
				{
					Notification.notify(Notification.Type.ERROR, caught.getLocalizedMessage());
					highlightPassword = true;
				}

				if (highlightUsername)
					form.highlightUsername();
				if (highlightPassword)
					form.highlightPassword();
			}

			@Override
			public void onSuccess(UserAuthentication result)
			{
				GatekeeperEventBus.BUS.fireEvent(new LoginEvent(result, false));
			}
		});
	}

	@Override
	protected void setUpContent()
	{
		form = new LoginForm(event -> doLogin());
		panel.getElement().appendChild(form.getElement());

        /* Set the focus to the username box */
		Scheduler.get().scheduleDeferred(() -> form.forceFocus());
	}

	@Override
	protected void onUnload()
	{
		form.clear();

		super.onUnload();
	}
}

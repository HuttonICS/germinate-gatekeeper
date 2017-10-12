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

package jhi.gatekeeper.client.util;

import com.google.gwt.user.client.rpc.*;

import jhi.database.shared.exception.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.login.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link ExceptionHandler} is the centralized instance that handles exceptions. The handled exceptions include:
 * <p/>
 * <ul> <li>{@link InvalidSessionException}</li> <li>{@link InvalidCredentialsException}</li> <li>{@link InsufficientPermissionsException}</li>
 * <li>{@link SuspendedUserException}</li> <li>{@link DatabaseException}</li> <li>{@link InvalidPageException}</li> </ul>
 *
 * @author Sebastian Raubach
 */
public class ExceptionHandler
{
	private static boolean initialized = false;

	public static void init()
	{
		if (!initialized)
		{
			GatekeeperEventBus.BUS.addHandler(ExceptionEvent.TYPE, event -> handleException(event.getThrowable()));

			initialized = true;
		}
	}

	private static void handleException(Throwable caught)
	{
		if (caught instanceof AdminAccountMissingException)
		{
			GatekeeperEventBus.BUS.fireEvent(new PageNavigationEvent(Page.createAdmin));
		}
		else if (caught instanceof EmailException)
		{
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationEmailError(caught.getLocalizedMessage()));
		}
		else if (caught instanceof InvalidSessionException)
		{
			/*
			 * Show notification and set logged-in status to false -> login page
             * will be shown
             */
			String notificationMessage;

			InvalidSessionException.InvalidProperty property = ((InvalidSessionException) caught).getInvalidProperty();

			switch (property)
			{
				case INVALID_COOKIE:
					notificationMessage = I18n.LANG.notificationInvalidCookie();
					break;
				case INVALID_PAYLOAD:
					notificationMessage = I18n.LANG.notificationInvalidPayload();
					break;
				case INVALID_SESSION:
				default:
					notificationMessage = I18n.LANG.notificationInvalidSession();
			}

			Notification.notify(Notification.Type.ERROR, notificationMessage);
			GatekeeperEventBus.BUS.fireEvent(new LogoutEvent(caught));
		}
		else if (caught instanceof InvalidCredentialsException)
		{
			/*
			 * Show notification and set logged-in status to false -> login page
             * will be shown
             */
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationLoginUnsuccessful());
			GatekeeperEventBus.BUS.fireEvent(new LogoutEvent(caught));
		}
		else if (caught instanceof InsufficientPermissionsException)
		{
			/*
			 * Show notification and set logged-in status to false -> login page
             * will be shown
             */
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationInsufficientPermissions());
			GatekeeperEventBus.BUS.fireEvent(new LogoutEvent(caught));
		}
		else if (caught instanceof SuspendedUserException)
		{
			/*
			 * Show notification and set logged-in status to false -> login page
             * will be shown
             */
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationInvalidSession());
			GatekeeperEventBus.BUS.fireEvent(new LogoutEvent(caught));
		}
		else if (caught instanceof DatabaseException)
		{
			/* Just show a notification, no need to log the user out */
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationDatabaseError());
		}
		else if (caught instanceof InvalidPageException)
		{
			/* We redirect the user to the login page */
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationPageUnavailable());
			ContentHolder.getInstance().setContent(Page.login, new LoginPage());
		}
		else if (caught instanceof StatusCodeException)
		{
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationUnspecifiedServerError());
		}
		else
		{
            /*
             * Something went really wrong... Just show a notification (we don't
             * really know what happened, so logging the user out might be too
             * much)
             */
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationUnknownError(caught.getClass().getName()));
		}
	}
}

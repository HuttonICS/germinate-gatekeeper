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

import com.google.gwt.core.client.*;

import jhi.gatekeeper.client.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.about.*;
import jhi.gatekeeper.client.page.account.*;
import jhi.gatekeeper.client.page.databasesystem.*;
import jhi.gatekeeper.client.page.login.*;
import jhi.gatekeeper.client.page.unapproveduser.*;
import jhi.gatekeeper.client.page.user.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class NavigationHandler
{
	/**
	 * Redirects the user to the page with the given name.
	 *
	 * @param event The new {@link PageNavigationEvent}
	 */
	public static void onPageNavigation(PageNavigationEvent event)
	{
		final Page page = event.getPage();

		/****************************** LOGIN ******************************/
		if (page.equals(Page.login))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new LoginPage());
				}
			});
		}
		/****************************** ACCOUNT SETTINGS ******************************/
		else if (page.equals(Page.accountSettings))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new AccountSettingsPage());
				}
			});
		}
		/****************************** USER LIST ******************************/
		else if (page.equals(Page.viewUsers))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new UserPage());
				}
			});
		}
		else if (page.equals(Page.addNewUser))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new CreateUserPage());
				}
			});
		}
		else if (page.equals(Page.createAdmin) && !Gatekeeper.doesAdminExists())
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new CreateAdminPage());
				}
			});
		}
		else if (page.equals(Page.approveUsers))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new UnapprovedUserPage());
				}
			});
		}
		else if (page.equals(Page.activateUser))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new ActivateUserPage());
				}
			});
		}
		else if (page.equals(Page.systemsUserList))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new DatabaseSystemUserPage());
				}
			});
		}
		else if (page.equals(Page.systemsList))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new DatabaseSystemPage());
				}
			});
		}
		else if (page.equals(Page.lostPassword))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new ForgottenPasswordPage());
				}
			});
		}
		else if (page.equals(Page.about))
		{
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(page, new AboutPage());
				}
			});
		}
		/****************************** LOGOUT ***********************************************/
		else if (page.equals(Page.logout))
		{
			/* Clear the parameter store and show the login page */
			GatekeeperEventBus.BUS.fireEvent(new LogoutEvent());
		}
		/****************************** FALLBACK *********************************************/
		else
		{
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationPageUnavailable());
			GWT.runAsync(new RunAsyncNotifyCallback()
			{
				@Override
				public void onSuccess()
				{
					ContentHolder.getInstance().setContent(Page.accountSettings, new AccountSettingsPage());
				}
			});
		}
	}
}

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

package jhi.gatekeeper.client;

import com.google.gwt.core.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.*;
import com.google.gwt.user.client.ui.*;

import java.util.logging.*;

import jhi.database.shared.exception.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.account.*;
import jhi.gatekeeper.client.page.login.*;
import jhi.gatekeeper.client.page.template.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class Gatekeeper implements EntryPoint
{
	private static boolean isLoggedIn = false;
	private static UserAuthentication userAuthentication;
	private static boolean adminExists = true;
	private Logger logger = Logger.getLogger("Gatekeeper");

	public static UserAuthentication getUserAuthentication()
	{
		return userAuthentication;
	}

	public static void setUserAuthentication(UserAuthentication userAuthentication)
	{
		Gatekeeper.userAuthentication = userAuthentication;
	}

	public static boolean isLoggedIn()
	{
		return isLoggedIn;
	}

	public static boolean doesAdminExists()
	{
		return adminExists;
	}

	@Override
	public void onModuleLoad()
	{
		/* First, take care of any exceptions that aren't explicitly caught */
		if (GWT.isProdMode() && GWT.isClient())
		{
			GWT.setUncaughtExceptionHandler(e ->
			{
				unwrap(e).printStackTrace();
				logger.log(Level.SEVERE, "Exception caught!", e);
			});
		}

        /* Second, move the other code to a separate method, since the
		 * UncaughtExceptionHandler only works if onModuleLoad() returns
         * successfully. By doing this, we ensure that all exceptions are caught */
		Scheduler.get().scheduleDeferred(this::onModuleLoadForReal);
	}

	/**
	 * If the given {@link Throwable} is an {@link UmbrellaException}, this method will "unwrap" it, i.e., it will return the actual {@link
	 * Throwable}
	 *
	 * @param e The given {@link Throwable}
	 * @return The unwrapped {@link Throwable}
	 */
	private Throwable unwrap(Throwable e)
	{
		if (e instanceof UmbrellaException)
		{
			UmbrellaException ue = (UmbrellaException) e;
			if (ue.getCauses().size() == 1)
			{
				return unwrap(ue.getCauses().iterator().next());
			}
		}
		return e;
	}

	/**
	 * This method initializes the notification system and exception handling. The it loads the Google Maps library.
	 */
	protected void onModuleLoadForReal()
	{
		/**
		 * Take care of page navigation changes
		 */
		History.addValueChangeHandler(this::handleHistoryChange);

		Notification.init("#" + Ids.DIV_CONTENT);
		TopBar.init();
		/* Set up the exception handling system */
		ExceptionHandler.init();

		/* Add the internationalized cookie text */
		RootPanel.get(Ids.DIV_COOKIE).getElement().appendChild(new CookiePolicyView().getElement());
		RootPanel.get(Ids.A_COOKIE_BUTTON).getElement().setInnerText(I18n.LANG.cookiePolicyTitle());

		attemptSessionLogin();
	}

	protected void onPageNavigation(PageNavigationEvent event)
	{
		NavigationHandler.onPageNavigation(event);
	}

	protected void attemptSessionLogin()
	{
		GatekeeperEventBus.BUS.addHandler(AdminCreationEvent.TYPE, event -> adminExists = true);

		/* Listen for PageNavigationEvents */
		GatekeeperEventBus.BUS.addHandler(PageNavigationEvent.TYPE, Gatekeeper.this::onPageNavigation);

        /* Listen for LogoutEvents */
		GatekeeperEventBus.BUS.addHandler(LogoutEvent.TYPE, event ->
		{
			isLoggedIn = false;

			Cookie.removeAll();
			ParameterStore.clear();

			LoginService.Instance.getInstance().logout(Cookie.getRequestProperties(), new AsyncCallback<Void>()
			{
				@Override
				public void onFailure(Throwable caught)
				{
					GatekeeperEventBus.BUS.fireEvent(new PageNavigationEvent(Page.login));
				}

				@Override
				public void onSuccess(Void result)
				{
					GatekeeperEventBus.BUS.fireEvent(new PageNavigationEvent(Page.login));
				}
			});
		});

        /* Listen for LoginEvents */
		GatekeeperEventBus.BUS.addHandler(LoginEvent.TYPE, event ->
		{
			userAuthentication = event.getUserAuthentication();
			isLoggedIn = true;

			if (event.isAutomaticLogin())
			{
				ContentHolder.getInstance().initContent();
			}
			else
			{
				/* Show notification */
				Notification.notify(Notification.Type.SUCCESS, I18n.LANG.notificationLoginSuccessful());
			}

			/* Set up the cookie */
			Cookie.setUp(event.getUserAuthentication());

			/* Fire a page update */
			String historyToken = History.getToken();

			/* If there are URL parameters and there is a valid page to
			 * navigate to, then do so */
			if ((!StringUtils.isEmpty(historyToken) && !Page.logout.name().equals(historyToken) && !Page.login.name().equals(historyToken)) || event.isAutomaticLogin())
			{
				History.fireCurrentHistoryState();
			}
			else
			{
				if (Page.accountSettings.getUrl().equals(historyToken))
					History.fireCurrentHistoryState();
				else
					History.newItem(Page.accountSettings.getUrl());
			}
		});

        /* This is the first login attempt when the page is loaded for the first
		 * time or when refresh is pressed. Set up the callback object for first
         * authentication. Try to log in without user credentials. This will
         * succeed if the session is valid and fail if it's not */
		LoginService.Instance.getInstance().login(Cookie.getRequestProperties(), new UserCredentials("", ""), new AsyncCallback<UserAuthentication>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				if (caught instanceof AdminAccountMissingException)
				{
					adminExists = false;
					ContentHolder.getInstance().initContent();
					ContentHolder.getInstance().setContent(Page.createAdmin, new CreateAdminPage());
				}
				else if (caught instanceof InvalidCredentialsException || caught instanceof SuspendedUserException || caught instanceof InsufficientPermissionsException
						|| caught instanceof InvalidSessionException)
				{
					/* UserManager has to log in */
					ParameterStore.clear();

					ContentHolder.getInstance().initContent();

					try
					{
						Page page = getPageFromString(History.getToken());

						if (page.isPublic())
							GatekeeperEventBus.BUS.fireEvent(new PageNavigationEvent(page));
						else
							ContentHolder.getInstance().setContent(Page.login, new LoginPage());
					}
					catch (InvalidPageException e)
					{
						GatekeeperEventBus.BUS.fireEvent(new ExceptionEvent(e));
					}
				}
				else if (caught instanceof DatabaseException)
				{
					Notification.notify(Notification.Type.ERROR, caught.getLocalizedMessage());
				}
			}

			@Override
			public void onSuccess(UserAuthentication result)
			{
				GatekeeperEventBus.BUS.fireEvent(new LoginEvent(result, true));
			}
		});
	}

	/**
	 * Checks if the history token matches any of the available {@link Page}s.
	 *
	 * @param historyToken The history token, i.e. the part after the hashtag in the URL.
	 * @return The corresponding page
	 * @throws InvalidPageException Thrown if the history token doesn't match any {@link Page}.
	 */
	protected Page getPageFromString(String historyToken) throws InvalidPageException
	{
		Page page = null;

		for (int i = historyToken.length(); i > 0; i--)
		{
			try
			{
				page = Page.getValueOf(historyToken.substring(0, i));
				break;
			}
			catch (IllegalArgumentException e)
			{
			}
		}

		if (StringUtils.isEmpty(historyToken))
			return Page.accountSettings;
		else if (page == null)
			throw new InvalidPageException(historyToken);
		else
			return page;
	}

	/**
	 * Takes care of history change events
	 *
	 * @param event The {@link ValueChangeEvent} fired by the {@link History}
	 */
	protected void handleHistoryChange(final ValueChangeEvent<String> event)
	{
		String historyToken = event.getValue();

		if (StringUtils.isEmpty(historyToken))
			historyToken = Page.accountSettings.getUrl();

		try
		{
			/* Redirect to the requested page */
			Page page = getPageFromString(historyToken);

			if (!adminExists)
			{
				ContentHolder.getInstance().setContent(Page.createAdmin, new CreateAdminPage());
			}
			else if (page.equals(Page.lostPassword) && isLoggedIn)
			{
				throw new InvalidPageException();
			}
			else if ((page.isPublic() || isLoggedIn) && !page.equals(Page.createAdmin))
			{
				GatekeeperEventBus.BUS.fireEvent(new PageNavigationEvent(page));
			}
			else
			{
				ContentHolder.getInstance().setContent(Page.login, new LoginPage());
			}
		}
		catch (InvalidPageException e)
		{
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationPageUnavailable());
			History.newItem(Page.accountSettings.getUrl());
		}
	}
}

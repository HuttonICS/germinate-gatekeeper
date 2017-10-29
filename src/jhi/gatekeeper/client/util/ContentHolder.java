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

package jhi.gatekeeper.client.util;


import com.google.gwt.dom.client.*;
import com.google.gwt.query.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.constants.*;

import java.util.*;

import jhi.gatekeeper.client.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.*;
import jhi.gatekeeper.client.page.account.*;
import jhi.gatekeeper.client.page.login.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;

/**
 * The {@link ContentHolder} contains all the methods necessary to manipulate the page content.
 *
 * @author Sebastian Raubach
 */
public class ContentHolder
{
	private Panel contentPanel;
	private Set<Page> availablePages = new HashSet<>();

	private ContentHolder()
	{
		contentPanel = RootPanel.get(Ids.DIV_CONTENT);
	}

	/**
	 * Returns the instance of {@link ContentHolder}
	 *
	 * @return The instance of {@link ContentHolder}
	 */
	public static ContentHolder getInstance()
	{
		return Instance.INSTANCE;
	}

	public boolean isPageAvailable(Page page)
	{
		return availablePages.contains(page);
	}

	/**
	 * Initializes the menu, the language box, the help, the search and the entries
	 */
	public void initContent()
	{
		updateAvailablePages();

		/* Initialize various page contents */
		NavigationBar.init();
	}

	private void updateAvailablePages()
	{
		if (!CollectionUtils.isEmpty(availablePages))
			return;


		UserAuthentication auth = Gatekeeper.getUserAuthentication();
		boolean isAdmin = auth != null && auth.isAdmin();

		if (isAdmin)
		{
			availablePages.addAll(Arrays.asList(Page.values()));
		}
		else
		{
			availablePages.add(Page.login);
			availablePages.add(Page.accountSettings);
			availablePages.add(Page.logout);
			availablePages.add(Page.lostPassword);
			availablePages.add(Page.createAdmin);
		}

		NavigationBar.init();
	}

	/**
	 * Appends the new content to the dynamic content div
	 *
	 * @param page       The new {@link Page}
	 * @param newContent The new content to append
	 */
	public void setContent(Page page, final GatekeeperPage newContent)
	{
		/* Make sure username and password field are cleared all the time */
		((InputElement) Document.get().getElementById(Ids.INPUT_LOGIN_USERNAME)).setValue(null);
		((InputElement) Document.get().getElementById(Ids.INPUT_LOGIN_PASSWORD)).setValue(null);

		updateAvailablePages();

		if (newContent instanceof LoginPage || page.isPublic())
		{
			if (newContent instanceof LoginPage || newContent instanceof CreateAdminPage)
			{
				GQuery.$("#" + Ids.DIV_SIDEBAR)
					  .hide();
				GQuery.$("#" + Ids.DIV_CONTENT)
					  .removeClass(ColumnSize.MD_9.getCssName())
					  .addClass(ColumnSize.MD_12.getCssName());

				Cookie.removeAll();
				availablePages.clear();

				NavigationBar.setEnabled(false);
			}

			NavigationBar.setHighlight(null);

			if (contentPanel != null)
			{
				contentPanel.clear();
				contentPanel.add(newContent);
			}

			Window.scrollTo(0, 0);
		}
		else
		{
			GQuery.$("#" + Ids.DIV_SIDEBAR)
				  .show();
			GQuery.$("#" + Ids.DIV_CONTENT)
				  .removeClass(ColumnSize.MD_12.getCssName())
				  .addClass(ColumnSize.MD_9.getCssName());

			if (!availablePages.contains(page))
			{
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationPageUnavailable());
				History.newItem(Page.accountSettings.getUrl());
				return;
			}

			NavigationBar.update();

			/* Check if the cookie is still valid */
			if (!Cookie.isStillAlive())
			{
				/* If not, redirect to the login page */
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationInvalidSession());
				GatekeeperEventBus.BUS.fireEvent(new LogoutEvent());
			}
			else
			{
				NavigationBar.setEnabled(true);
				NavigationBar.setHighlight(page);

				/* Else just show the content */
				Cookie.extend();

				navigateToPage(newContent);
			}
		}
	}

	private void navigateToPage(GatekeeperPage newContent)
	{
		if (contentPanel != null)
		{
			/* Remove the old content */
			contentPanel.clear();
			/* Add the new content */
			contentPanel.add(newContent);
		}

		Window.scrollTo(0, 0);
	}

	/**
	 * {@link Instance} is loaded on the first execution of {@link ContentHolder#getInstance()} or the first access to {@link Instance#INSTANCE}, not
	 * before. <p/> This solution (<a href= "http://en.wikipedia.org/wiki/Initialization_on_demand_holder_idiom" >Initialization-on-demand holder
	 * idiom</a>) is thread-safe without requiring special language constructs (i.e. <code>volatile</code> or <code>synchronized</code>).
	 *
	 * @author Sebastian Raubach
	 */
	private static final class Instance
	{
		private static final ContentHolder INSTANCE = new ContentHolder();
	}
}

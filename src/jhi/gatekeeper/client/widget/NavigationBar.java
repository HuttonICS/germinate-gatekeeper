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

package jhi.gatekeeper.client.widget;


import com.google.gwt.dom.client.*;
import com.google.gwt.query.client.*;
import com.google.gwt.user.client.rpc.*;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;

import java.util.*;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;


/**
 * @author Sebastian Raubach
 */
public final class NavigationBar
{
	private static NavigationMenu menu;

	private static Panel menuPanel;

	private NavigationBar()
	{

	}

	private static void initEventHandlers()
	{
		GatekeeperEventBus.BUS.addHandler(UserListChangedEvent.TYPE, event -> update());
		GatekeeperEventBus.BUS.addHandler(UnapprovedUserListChangeEvent.TYPE, event -> update());
		GatekeeperEventBus.BUS.addHandler(AccessRequestListChangeEvent.TYPE, event -> update());
		GatekeeperEventBus.BUS.addHandler(DatabaseSystemListChangeEvent.TYPE, event -> update());
	}

	public static void setEnabled(boolean enabled)
	{
		if (enabled)
		{
			/* Enable menu items */
			GQuery.$(menuPanel)
				  .find("a")
				  .removeAttr("disabled")
				  .removeClass(Styles.DISABLED);
		}
		else
		{
			/* Disable menu items */
			GQuery.$(menuPanel)
				  .find("a")
				  .attr("disabled", "true")
				  .addClass(Styles.DISABLED);

			/* Hide badges */
			GQuery.$(menuPanel)
				  .find("span.badge")
				  .hide();
		}
	}

	public static void setHighlight(Page page)
	{
		GQuery.$(menuPanel)
			  .find("a")
			  .removeClass(Styles.ACTIVE);


		MenuItem item = menu.getMenuItem(page);

		if (item != null)
			item.element.addClassName(Styles.ACTIVE);
	}

	public static void update()
	{
		UserService.Instance.getInstance().getUserCount(Cookie.getRequestProperties(), null, new AsyncCallback<Long>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				menu.setBadge(Page.viewUsers, "0");
			}

			@Override
			public void onSuccess(Long result)
			{
				menu.setBadge(Page.viewUsers, Long.toString(result));
			}
		});

		ParallelAsyncCallback<Long> unapprovedCount = new ParallelAsyncCallback<Long>()
		{
			@Override
			protected void start()
			{
				UnapprovedUserService.Instance.getInstance().getCount(Cookie.getRequestProperties(), this);
			}
		};

		ParallelAsyncCallback<Long> accessRequestCount = new ParallelAsyncCallback<Long>()
		{
			@Override
			protected void start()
			{
				AccessRequestService.Instance.getInstance().getCount(Cookie.getRequestProperties(), this);
			}
		};

		new ParallelParentAsyncCallback(unapprovedCount, accessRequestCount)
		{
			@Override
			public void handleSuccess()
			{
				Long unapproved = getCallbackData(0);
				Long accessRequests = getCallbackData(1);

				menu.setBadge(Page.approveUsers, Long.toString(unapproved + accessRequests));
			}

			@Override
			public void handleFailure(Exception reason)
			{
				menu.setBadge(Page.approveUsers, "0");
			}
		};

		DatabaseSystemService.Instance.getInstance().getDatabaseSystemCount(Cookie.getRequestProperties(), new AsyncCallback<Long>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				menu.setBadge(Page.systemsList, "0");
			}

			@Override
			public void onSuccess(Long result)
			{
				menu.setBadge(Page.systemsList, Long.toString(result));
			}
		});
	}

	public static void init()
	{
		menuPanel = RootPanel.get(Ids.DIV_MENU);
		menuPanel.getElement().removeAllChildren();

		menu = new NavigationMenu();

		MenuItem item;

		if (ContentHolder.getInstance().isPageAvailable(Page.viewUsers))
		{
			item = new MenuItem();
			item.href = Page.viewUsers;
			item.fa = Classes.FA_USERS;
			item.text = I18n.LANG.menuItemViewUsers();
			menu.add(item.href.getCategory(), item);
		}

		if (ContentHolder.getInstance().isPageAvailable(Page.approveUsers))
		{
			item = new MenuItem();
			item.href = Page.approveUsers;
			item.fa = Classes.FA_CHECK_SQUARE;
			item.text = I18n.LANG.menuItemApproveUsers();
			menu.add(item.href.getCategory(), item);
		}

		if (ContentHolder.getInstance().isPageAvailable(Page.addNewUser))
		{
			item = new MenuItem();
			item.href = Page.addNewUser;
			item.fa = Classes.FA_USER_PLUS;
			item.text = I18n.LANG.menuItemAddUser();
			menu.add(item.href.getCategory(), item);
		}

		if (ContentHolder.getInstance().isPageAvailable(Page.systemsList))
		{
			item = new MenuItem();
			item.href = Page.systemsList;
			item.fa = Classes.FA_LIST;
			item.text = I18n.LANG.menuItemViewDatabases();
			menu.add(item.href.getCategory(), item);
		}

		if (ContentHolder.getInstance().isPageAvailable(Page.accountSettings))
		{
			item = new MenuItem();
			item.href = Page.accountSettings;
			item.fa = Classes.FA_GEAR;
			item.text = I18n.LANG.menuItemAdminSettings();
			menu.add(item.href.getCategory(), item);
		}

		/* Add the menu to its panel */
		menuPanel.getElement().appendChild(menu.build());

		/* Remove the menuPanel, since we cannot get the children as RootPanels if their parent is still attached */
		menuPanel.removeFromParent();

		initEventHandlers();

		update();
	}

	private static class NavigationMenu
	{
		private Map<Page.Category, List<MenuItem>> menuItems  = new LinkedHashMap<>();
		private Map<Page, MenuItem>                pageToItem = new HashMap<>();

		public void setBadge(Page page, String text)
		{
			MenuItem item = pageToItem.get(page);
			if (item != null)
			{
				Badge badge = item.badge;

				if (badge != null)
				{
					badge.setText(text);

					if (StringUtils.isEmpty(text) || StringUtils.areEqual("0", text))
						badge.setVisible(false);
					else
						badge.setVisible(true);
				}
			}
		}

		public MenuItem getMenuItem(Page page)
		{
			return pageToItem.get(page);
		}

		public void add(Page.Category category, MenuItem menuItem)
		{
			List<MenuItem> items = menuItems.get(category);

			if (items == null)
				items = new ArrayList<>();

			items.add(menuItem);

			menuItems.put(category, items);

			pageToItem.put(menuItem.href, menuItem);
		}

		public Element build()
		{
			DivElement wrapper = Document.get().createDivElement();

			for (Page.Category category : menuItems.keySet())
			{
				DivElement listGroup = Document.get().createDivElement();
				listGroup.setClassName(Styles.LIST_GROUP);

				for (MenuItem item : menuItems.get(category))
				{
					AnchorElement listGroupItem = Document.get().createAnchorElement();

					Element i = Document.get().createElement("i");
					i.setClassName(Classes.combine(Classes.FA, Classes.FA_FW, item.fa));

					item.element = listGroupItem;
					item.badge = new Badge();
					item.badge.setVisible(false);

					listGroupItem.setClassName(Styles.LIST_GROUP_ITEM);
					listGroupItem.setInnerHTML("&nbsp;" + item.text);
					listGroupItem.setHref("#" + item.href.getUrl());
					listGroupItem.insertFirst(i);
					listGroupItem.insertFirst(item.badge.getElement());

					listGroup.appendChild(listGroupItem);
				}

				wrapper.appendChild(listGroup);
			}

			return wrapper;
		}
	}

	private static class MenuItem
	{
		String text;
		Page   href;
		String fa;

		Element element;
		Badge   badge;
	}
}

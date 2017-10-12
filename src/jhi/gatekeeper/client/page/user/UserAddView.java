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

package jhi.gatekeeper.client.page.user;

import com.google.gwt.core.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class UserAddView extends Composite
{
	private static UserAddViewUiBinder ourUiBinder = GWT.create(UserAddViewUiBinder.class);
	@UiField
	NavTabs navTabs;
	@UiField
	AnchorListItem existingDbTab;
	@UiField
	AnchorListItem newDbTab;
	@UiField
	UserAddExisting existingDb;
	@UiField
	UserAddNew newDb;
	@UiField
	Button button;
	private User user;
	private HandlerRegistration handlerRegistration;

	public UserAddView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		this.setVisible(false);

		existingDbTab.addClickHandler(event -> tabSelected(existingDb));
		newDbTab.addClickHandler(event -> tabSelected(newDb));
		button.addClickHandler(event -> onAddClicked());
		handlerRegistration = GatekeeperEventBus.BUS.addHandler(UserSelectionEvent.TYPE, event ->
		{
			setUser(event.getUser());

			/* Select the default tab */
			tabSelected(existingDb);
		});
	}

	private void setUser(User user)
	{
		this.setVisible(user != null);

		this.user = user;
	}

	private void onAddClicked()
	{
		if (this.isVisible() && user != null)
		{
			DatabasePermission permission = getDatabasePermission();

			if (permission != null)
			{
				DatabasePermissionService.Instance.getInstance().grantPermissionForUser(Cookie.getRequestProperties(), permission, new AsyncCallbackLogoutOnFailure<Void>()
				{
					@Override
					protected void onSuccessImpl(Void result)
					{
						Notification.notify(Notification.Type.INFO, I18n.LANG.notificationDatabasePermissionsUpdated());
						GatekeeperEventBus.BUS.fireEvent(new UserSelectionEvent(user));
					}
				});
			}
			else
			{
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationFillAllFields());
			}
		}
	}

	private DatabasePermission getDatabasePermission()
	{
		if (existingDbTab.isActive())
			return existingDb.getPermission();
		else
			return newDb.getPermission();
	}

	private void tabSelected(Composite tab)
	{
		if (tab instanceof UserAddExisting)
		{
			existingDbTab.setActive(true);
			newDbTab.setActive(false);

			existingDb.setVisible(true);
			newDb.setVisible(false);
		}
		else if (tab instanceof UserAddNew)
		{
			newDbTab.setActive(true);
			existingDbTab.setActive(false);

			newDb.setVisible(true);
			existingDb.setVisible(false);
		}
	}

	@Override
	protected void onUnload()
	{
		if (handlerRegistration != null)
			handlerRegistration.removeHandler();

		super.onUnload();
	}

	interface UserAddViewUiBinder extends UiBinder<Panel, UserAddView>
	{

	}
}
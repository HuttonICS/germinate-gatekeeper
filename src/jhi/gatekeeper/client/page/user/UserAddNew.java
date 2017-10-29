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

package jhi.gatekeeper.client.page.user;

import com.google.gwt.core.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.TextArea;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.client.widget.select.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class UserAddNew extends Composite
{
	private static UserAddViewUiBinder ourUiBinder = GWT.create(UserAddViewUiBinder.class);
	@UiField
	BootstrapInputGroup databaseName;
	@UiField
	BootstrapInputGroup serverName;
	@UiField
	TextArea description;
	@UiField
	FormGroup accessLevelGroup;
	@UiField
	UserTypeListBox accessLevel;
	private User user;
	private HandlerRegistration handlerRegistration;

	public UserAddNew()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		this.setVisible(false);

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(UserSelectionEvent.TYPE, event -> setUser(event.getUser()));
	}

	private void setUser(User user)
	{
		/* This view is invisible after user selection */
		this.setVisible(false);

		this.user = user;

		accessLevelGroup.setVisible(this.user != null);
	}

	public DatabasePermission getPermission()
	{
		if (StringUtils.isEmpty(databaseName.getValue(), serverName.getValue()))
		{
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationFillAllFields());
			return null;
		}
		else
		{
			DatabasePermission permission = new DatabasePermission();
			permission.setUser(user);
			permission.setUserType(accessLevel.getValue());

			DatabaseSystem system = new DatabaseSystem();
			system.setSystemName(databaseName.getValue());
			system.setServerName(serverName.getValue());
			system.setDescription(description.getValue());

			permission.setDatabaseSystem(system);

			return permission;
		}
	}

	@Override
	protected void onUnload()
	{
		if (handlerRegistration != null)
			handlerRegistration.removeHandler();

		super.onUnload();
	}

	interface UserAddViewUiBinder extends UiBinder<FlowPanel, UserAddNew>
	{
	}
}
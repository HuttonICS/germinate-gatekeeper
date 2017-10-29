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

package jhi.gatekeeper.client.page.databasesystem;

import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.user.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class DatabaseSystemView extends Composite
{
	private static DatabaseSystemViewUiBinder ourUiBinder = GWT.create(DatabaseSystemViewUiBinder.class);
	@UiField
	UserAddNew newDb;

	public DatabaseSystemView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		newDb.setVisible(true);
	}

	@UiHandler(value = {"button"})
	void handleClick(ClickEvent e)
	{
		DatabasePermission permission = newDb.getPermission();

		if (permission != null)
		{
			DatabaseSystemService.Instance.getInstance().add(Cookie.getRequestProperties(), permission.getDatabaseSystem(), new AsyncCallbackLogoutOnFailure<Boolean>()
			{
				@Override
				protected void onSuccessImpl(Boolean result)
				{
					if (result)
					{
						Notification.notify(Notification.Type.SUCCESS, I18n.LANG.notificationDatabaseSystemAdded());
						GatekeeperEventBus.BUS.fireEvent(new DatabaseSystemListChangeEvent());
					}
					else
					{
						Notification.notify(Notification.Type.WARNING, I18n.LANG.notificationDatabaseSystemExists());
					}
				}
			});
		}
		else
		{
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationFillAllFields());
		}
	}

	interface DatabaseSystemViewUiBinder extends UiBinder<FlowPanel, DatabaseSystemView>
	{
	}
}
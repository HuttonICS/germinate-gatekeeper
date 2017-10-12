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

package jhi.gatekeeper.client.page.databasesystem;

import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.pagination.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class DatabaseSystemPermissionsView extends Composite
{
	private static UserPermissionsViewUiBinder ourUiBinder = GWT.create(UserPermissionsViewUiBinder.class);
	private final HandlerRegistration handlerRegistration;
	@UiField
	FlowPanel            tablePanel;
	@UiField
	DatabaseSystemAddNew newUser;

	private DatabasePermissionsTable table;
	private Long                     id;

	public DatabaseSystemPermissionsView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(DatabasePermissionListChangeEvent.TYPE, event -> update());

		update();
	}

	private void update()
	{
		id = (Long) ParameterStore.get(Parameter.databaseSystemId);

		tablePanel.clear();

		if (id != null)
		{
			table = new DatabasePermissionsTable(id, DatabasePermissionsTable.Type.DATABASE_SYSTEM);
			tablePanel.add(table);
		}
		else
		{
			newUser.removeFromParent();
		}
	}

	@Override
	protected void onUnload()
	{
		if (handlerRegistration != null)
			handlerRegistration.removeHandler();

		super.onUnload();
	}

	@UiHandler("button")
	void onButtonPressed(ClickEvent e)
	{
		DatabasePermission permission = new DatabasePermission()
				.setUser(newUser.getUser())
				.setDatabaseSystem(new DatabaseSystem(id))
				.setUserType(newUser.getUserType());

		DatabasePermissionService.Instance.getInstance().grantPermissionForUser(Cookie.getRequestProperties(), permission, new AsyncCallbackLogoutOnFailure<Void>()
		{
			@Override
			protected void onSuccessImpl(Void result)
			{
				table.update();
			}
		});
	}

	interface UserPermissionsViewUiBinder extends UiBinder<HTMLPanel, DatabaseSystemPermissionsView>
	{
	}
}
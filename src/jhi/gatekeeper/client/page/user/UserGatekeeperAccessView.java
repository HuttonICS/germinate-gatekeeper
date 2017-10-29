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

import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Panel;

import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class UserGatekeeperAccessView extends Composite
{
	private static UserGatekeeperAccessViewUiBinder ourUiBinder = GWT.create(UserGatekeeperAccessViewUiBinder.class);
	@UiField
	CheckBox hasAccess;
	private User user;
	private HandlerRegistration handlerRegistration;

	public UserGatekeeperAccessView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		this.setVisible(false);

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(UserSelectionEvent.TYPE, event -> setUser(event.getUser()));

		hasAccess.addValueChangeHandler(event ->
		{
			if (user != null)
			{
				user.setHasAccessToGatekeeper(event.getValue());
				UserService.Instance.getInstance().setHasAccessToGatekeeper(Cookie.getRequestProperties(), user, new AsyncCallbackLogoutOnFailure<>());
			}
		});
	}

	private void setUser(User user)
	{
		this.user = user;

		this.setVisible(user != null);

		if (user != null)
			hasAccess.setValue(user.isHasAccessToGatekeeper());
	}

	@Override
	protected void onUnload()
	{
		if (handlerRegistration != null)
			handlerRegistration.removeHandler();

		super.onUnload();
	}

	interface UserGatekeeperAccessViewUiBinder extends UiBinder<Panel, UserGatekeeperAccessView>
	{

	}
}
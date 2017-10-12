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
import com.google.gwt.user.client.ui.Panel;

import org.gwtbootstrap3.client.ui.Button;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class UserDeleteView extends Composite
{
	private static UserDeleteViewUiBinder ourUiBinder = GWT.create(UserDeleteViewUiBinder.class);
	@UiField
	Button button;
	private User user;
	private HandlerRegistration handlerRegistration;

	public UserDeleteView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		this.setVisible(false);

		button.addClickHandler(event -> new ModalDialog(I18n.LANG.dialogTitleDeleteUser(), I18n.LANG.dialogMessageDeleteUser())
		{
			@Override
			protected void onButtonClicked(ModalDialogButton button)
			{
				if (button == ModalDialogButton.YES)
					onDeleteClicked();
			}
		}
				.setType(ModalDialog.ModalDialogType.YES_NO)
				.show());

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(UserSelectionEvent.TYPE, event -> setUser(event.getUser()));
	}

	private void setUser(User user)
	{
		this.setVisible(user != null);

		this.user = user;
	}

	private void onDeleteClicked()
	{
		if (this.isVisible() && user != null)
		{
			UserService.Instance.getInstance().deleteUser(Cookie.getRequestProperties(), user.getId(), new AsyncCallbackLogoutOnFailure<Void>()
			{
				@Override
				protected void onSuccessImpl(Void result)
				{
					GatekeeperEventBus.BUS.fireEvent(new UserSelectionEvent(null));
					GatekeeperEventBus.BUS.fireEvent(new UserListChangedEvent());
				}
			});
		}
	}

	@Override
	protected void onUnload()
	{
		if (handlerRegistration != null)
			handlerRegistration.removeHandler();

		super.onUnload();
	}

	interface UserDeleteViewUiBinder extends UiBinder<Panel, UserDeleteView>
	{

	}
}
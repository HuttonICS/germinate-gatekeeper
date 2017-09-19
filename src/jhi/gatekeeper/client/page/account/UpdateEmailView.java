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

package jhi.gatekeeper.client.page.account;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.TextBox;

import jhi.gatekeeper.client.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class UpdateEmailView extends Composite
{
	private static UpdateEmailViewUiBinder ourUiBinder = GWT.create(UpdateEmailViewUiBinder.class);
	@UiField
	TextBox email;
	@UiField
	Button button;

	public UpdateEmailView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		email.setText(Gatekeeper.getUserAuthentication().getEmailAddress());

		button.addClickHandler(event ->
		{
			if (!StringUtils.isEmpty(email.getValue()))
			{
				UserService.Instance.getInstance().updateUserEmail(Cookie.getRequestProperties(), email.getValue(), new AsyncCallbackLogoutOnFailure<Void>()
				{
					@Override
					protected void onSuccessImpl(Void result)
					{
						Notification.notify(Notification.Type.SUCCESS, I18n.LANG.notificationUserEmailChanged());
						GatekeeperEventBus.BUS.fireEvent(new UserUpdatedEvent());
					}
				});
			}
			else
			{
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationFillAllFields());
			}
		});
	}

	interface UpdateEmailViewUiBinder extends UiBinder<Panel, UpdateEmailView>
	{
	}
}
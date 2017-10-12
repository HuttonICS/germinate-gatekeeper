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

package jhi.gatekeeper.client.page.account;


import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class UpdatePasswordView extends Composite
{
	private static UpdatePasswordViewUiBinder ourUiBinder = GWT.create(UpdatePasswordViewUiBinder.class);
	@UiField
	BootstrapInputGroup password;

	@UiField
	BootstrapInputGroup passwordConfirm;

	@UiField
	Button button;

	public UpdatePasswordView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		button.addClickHandler(event ->
		{
			String p = password.getValue();
			String pc = passwordConfirm.getValue();

			if (StringUtils.isEmpty(p, pc) || !StringUtils.areEqual(p, pc))
			{
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationUserPasswordsDontMatch());
				return;
			}

			UserService.Instance.getInstance().updatePassword(Cookie.getRequestProperties(), new UserCredentials(null, p), new AsyncCallbackLogoutOnFailure<Void>()
			{
				@Override
				protected void onSuccessImpl(Void result)
				{
					password.setValue("");
					passwordConfirm.setValue("");
					Notification.notify(Notification.Type.SUCCESS, I18n.LANG.notificationUserPasswordChanged());
				}
			});
		});
	}

	interface UpdatePasswordViewUiBinder extends UiBinder<Panel, UpdatePasswordView>
	{
	}
}
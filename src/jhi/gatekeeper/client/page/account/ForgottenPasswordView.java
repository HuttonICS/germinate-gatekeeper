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

package jhi.gatekeeper.client.page.account;

import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class ForgottenPasswordView extends Composite
{
	private static ForgottenPasswordViewUiBinder ourUiBinder = GWT.create(ForgottenPasswordViewUiBinder.class);
	@UiField
	BootstrapInputGroup username;
	@UiField
	BootstrapInputGroup email;
	@UiField
	Button button;

	public ForgottenPasswordView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		button.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				UserService.Instance.getInstance().sendNewPassword(Cookie.getRequestProperties(), username.getValue(), email.getValue(), new AsyncCallbackLogoutOnFailure<Void>()
				{
					@Override
					protected void onFailureImpl(Throwable caught)
					{
						if (caught instanceof InvalidCredentialsException)
						{
							Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationUserNotExist());
						}
						else
						{
							super.onFailureImpl(caught);
						}
					}

					@Override
					protected void onSuccessImpl(Void result)
					{
						Notification.notify(Notification.Type.INFO, I18n.LANG.notificationUserNewPasswordSent());
						History.newItem(Page.login.getUrl());
					}
				});
			}
		});
	}

	interface ForgottenPasswordViewUiBinder extends UiBinder<Panel, ForgottenPasswordView>
	{
	}
}
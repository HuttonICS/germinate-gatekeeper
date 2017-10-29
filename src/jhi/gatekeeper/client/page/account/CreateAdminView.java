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
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.TextArea;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class CreateAdminView extends Composite
{
	private static CreateAdminViewUiBinder ourUiBinder = GWT.create(CreateAdminViewUiBinder.class);
	@UiField
	BootstrapInputGroup username;
	@UiField
	BootstrapInputGroup email;
	@UiField
	Input password;
	@UiField
	Input passwordConfirm;
	@UiField
	BootstrapInputGroup fullName;
	@UiField
	BootstrapInputGroup institutionName;
	@UiField
	BootstrapInputGroup institutionAcronym;
	@UiField
	TextArea institutionAddress;
	@UiField
	Button button;

	public CreateAdminView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		button.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (!checkConditions())
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationFillAllFields());
					return;
				}
				if (!checkPasswords())
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationUserPasswordsDontMatch());
					return;
				}

				User user = new User();
				user.setUsername(username.getValue());
				user.setEmail(email.getValue());
				user.setFullName(fullName.getValue());

				Institution institution = new Institution();
				institution.setName(institutionName.getValue());
				institution.setAcronym(institutionAcronym.getValue());
				institution.setAddress(institutionAddress.getValue());

				user.setInstitution(institution);

				UserCredentials credentials = new UserCredentials(username.getValue(), password.getValue());

				UserService.Instance.getInstance().createAdmin(Cookie.getRequestProperties(), user, credentials, new AsyncCallbackLogoutOnFailure<Void>()
				{
					@Override
					protected void onFailureImpl(Throwable caught)
					{
						if (caught instanceof UserExistsException)
						{
							Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationAdminAccountExists());
						}
						else
						{
							super.onFailureImpl(caught);
						}
					}

					@Override
					protected void onSuccessImpl(Void result)
					{
						Notification.notify(Notification.Type.SUCCESS, I18n.LANG.notificationAdminAccountCreated());
						GatekeeperEventBus.BUS.fireEvent(new AdminCreationEvent());

						Window.Location.reload();
					}
				});
			}
		});
	}

	private boolean checkPasswords()
	{
		return StringUtils.areEqual(password.getValue(), passwordConfirm.getValue());
	}

	private boolean checkConditions()
	{
		return !StringUtils.isEmpty(username.getValue(), email.getValue(), fullName.getValue(), institutionName.getValue(), institutionAcronym.getValue(), institutionAddress.getValue());
	}

	interface CreateAdminViewUiBinder extends UiBinder<Panel, CreateAdminView>
	{
	}
}
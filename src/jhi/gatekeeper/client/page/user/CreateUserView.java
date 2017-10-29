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
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.institution.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class CreateUserView extends Composite
{
	private static CreateUserViewUiBinder ourUiBinder = GWT.create(CreateUserViewUiBinder.class);
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
	Button button;
	@UiField
	NavTabs navTabs;
	@UiField
	AnchorListItem existingInstTab;
	@UiField
	AnchorListItem newInstTab;
	@UiField
	InstitutionExistingView existingInst;
	@UiField
	InstitutionNewView newInst;

	public CreateUserView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		existingInstTab.addClickHandler(event -> tabSelected(existingInst));
		newInstTab.addClickHandler(event -> tabSelected(newInst));
		button.addClickHandler(event -> onAddClicked());
	}

	private void onAddClicked()
	{
		Institution institution = getInstitution();

		if (institution != null)
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

			final User user = new User();
			user.setUsername(username.getValue());
			user.setEmail(email.getValue());
			user.setFullName(fullName.getValue());
			user.setInstitution(institution);

			UserCredentials credentials = new UserCredentials(user.getUsername(), password.getValue());

			UserService.Instance.getInstance().addUser(Cookie.getRequestProperties(), user, credentials, new AsyncCallbackLogoutOnFailure<Void>()
			{
				@Override
				protected void onFailureImpl(Throwable caught)
				{
					if (caught instanceof UserExistsException)
					{
						Notification.notify(Notification.Type.INFO, I18n.LANG.notificationUserExists());
					}
					else
					{
						super.onFailureImpl(caught);
					}
				}

				@Override
				protected void onSuccessImpl(Void result)
				{
					Notification.notify(Notification.Type.INFO, I18n.LANG.notificationUserAdded());

					History.newItem(Page.viewUsers.getUrl());
				}
			});
		}
		else
		{
			Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationFillAllFields());
		}
	}

	private boolean checkPasswords()
	{
		return StringUtils.areEqual(password.getValue(), passwordConfirm.getValue());
	}

	private boolean checkConditions()
	{
		return !StringUtils.isEmpty(username.getValue(), fullName.getValue(), email.getValue());
	}

	private Institution getInstitution()
	{
		if (existingInstTab.isActive())
			return existingInst.getInstitution();
		else
			return newInst.getInstitution();
	}

	private void tabSelected(Composite tab)
	{
		if (tab instanceof InstitutionExistingView)
		{
			existingInstTab.setActive(true);
			newInstTab.setActive(false);

			existingInst.setVisible(true);
			newInst.setVisible(false);
		}
		else if (tab instanceof InstitutionNewView)
		{
			newInstTab.setActive(true);
			existingInstTab.setActive(false);

			newInst.setVisible(true);
			existingInst.setVisible(false);
		}
	}

	interface CreateUserViewUiBinder extends UiBinder<Panel, CreateUserView>
	{
	}
}
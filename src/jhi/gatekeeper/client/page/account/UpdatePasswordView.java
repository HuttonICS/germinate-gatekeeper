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
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;

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
public class UpdatePasswordView extends Composite
{
	private static UpdatePasswordViewUiBinder ourUiBinder = GWT.create(UpdatePasswordViewUiBinder.class);
	@UiField
	BootstrapInputGroup oldPassword;

	@UiField
	BootstrapInputGroup newPassword;

	@UiField
	BootstrapInputGroup passwordConfirm;

	@UiField
	Button button;

	@UiField
	Tooltip tooltip;

	@UiField
	ProgressBar progressBar;

	private int passwordStrength = 0;

	private ValueChangeHandler<String> passwordStrengthHandler = event -> {
		if (StringUtils.isEmpty(newPassword.getValue()))
			passwordStrength = 0;
		else
			passwordStrength = getPasswordStrength(newPassword.getValue());

		switch (passwordStrength)
		{
			case 0:
				progressBar.setPercent(1);
				progressBar.setType(ProgressBarType.DANGER);
				tooltip.setTitle(I18n.LANG.passwordStrengthZero());
				break;
			case 1:
				progressBar.setPercent(25);
				progressBar.setType(ProgressBarType.DANGER);
				tooltip.setTitle(I18n.LANG.passwordStrengthOne());
				break;
			case 2:
				progressBar.setPercent(50);
				progressBar.setType(ProgressBarType.DANGER);
				tooltip.setTitle(I18n.LANG.passwordStrengthTwo());
				break;
			case 3:
				progressBar.setPercent(75);
				progressBar.setType(ProgressBarType.WARNING);
				tooltip.setTitle(I18n.LANG.passwordStrengthThree());
				break;
			case 4:
				progressBar.setPercent(100);
				progressBar.setType(ProgressBarType.SUCCESS);
				tooltip.setTitle(I18n.LANG.passwordStrengthFour());
				break;
		}
	};

	public UpdatePasswordView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		newPassword.getInput().addValueChangeHandler(passwordStrengthHandler);
		newPassword.getInput().addKeyUpHandler(event -> passwordStrengthHandler.onValueChange(null));

		button.addClickHandler(event ->
		{
			String op = oldPassword.getValue();
			String np = newPassword.getValue();
			String pc = passwordConfirm.getValue();

			if (StringUtils.isEmpty(op))
			{
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationFillAllFields());
				return;
			}
			else if (passwordStrength < 2)
			{
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationUserPasswordsTooWeak());
				return;
			}
			else if (StringUtils.isEmpty(np, pc) || !StringUtils.areEqual(np, pc))
			{
				Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationUserPasswordsDontMatch());
				return;
			}

			UserService.Instance.getInstance().updatePassword(Cookie.getRequestProperties(), new UserCredentials(null, op, np), new AsyncCallbackLogoutOnFailure<Void>()
			{
				@Override
				protected void onFailureImpl(Throwable caught)
				{
					if (caught instanceof InvalidCredentialsException)
						Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationLoginInvalidPassword());
					else
						super.onFailureImpl(caught);
				}

				@Override
				protected void onSuccessImpl(Void result)
				{
					oldPassword.setValue("");
					newPassword.setValue("");
					passwordConfirm.setValue("");
					Notification.notify(Notification.Type.SUCCESS, I18n.LANG.notificationUserPasswordChanged());
				}
			});
		});
	}

	private native int getPasswordStrength(String password)/*-{
		return $wnd.zxcvbn(password).score;
	}-*/;

	interface UpdatePasswordViewUiBinder extends UiBinder<Panel, UpdatePasswordView>
	{
	}
}
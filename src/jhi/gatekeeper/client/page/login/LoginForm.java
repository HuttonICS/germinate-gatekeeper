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

package jhi.gatekeeper.client.page.login;

import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.SubmitButton;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.util.*;

/**
 * @author Sebastian Raubach
 */
public class LoginForm extends Composite
{
	private static LoginFormNewUiBinder ourUiBinder = GWT.create(LoginFormNewUiBinder.class);
	private final Element      originalForm;
	@UiField
	FormPanel form;

	@UiField
	FlowPanel usernameDiv;

	@UiField
	FlowPanel passwordDiv;

	@UiField
	SubmitButton button;
	private       InputElement usernameBox;
	private       InputElement passwordBox;
	public LoginForm(final ClickHandler handler)
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		originalForm = Document.get().getElementById(Ids.DIV_LOGIN_FORM);

		usernameBox = (InputElement) Document.get().getElementById(Ids.INPUT_LOGIN_USERNAME);
		passwordBox = (InputElement) Document.get().getElementById(Ids.INPUT_LOGIN_PASSWORD);

		usernameBox.setPropertyString("placeholder", I18n.LANG.loginUsernameLabel());
		passwordBox.setPropertyString("placeholder", I18n.LANG.loginPasswordLabel());

		usernameDiv.getElement().appendChild(usernameBox);
		passwordDiv.getElement().appendChild(passwordBox);

		button.addClickHandler(event ->
		{
			event.preventDefault();
			form.submit();
		});

		form.addSubmitHandler(event ->
		{
			event.cancel();
			handler.onClick(null);
		});

		EventListener enterKeyListener = event ->
		{
			if (event.getKeyCode() == KeyCodes.KEY_ENTER)
			{
				form.submit();
			}
		};

		DOM.sinkEvents(button.getElement(), Event.ONCLICK);
		DOM.setEventListener(button.getElement(), event ->
		{
			event.preventDefault();
			form.submit();
		});

		DOM.sinkEvents(usernameBox, Event.KEYEVENTS);
		DOM.setEventListener(usernameBox, enterKeyListener);

		DOM.sinkEvents(passwordBox, Event.KEYEVENTS);
		DOM.setEventListener(passwordBox, enterKeyListener);
	}

	public void forceFocus()
	{
		usernameBox.focus();
	}

	public void highlightUsername()
	{
		usernameDiv.addStyleName(ValidationState.ERROR.getCssName());
		usernameBox.focus();
		usernameBox.select();
	}

	public void highlightPassword()
	{
		passwordDiv.addStyleName(ValidationState.ERROR.getCssName());
		passwordBox.focus();
		passwordBox.select();
	}

	public String getUsername()
	{
		return usernameBox.getValue();
	}

	public String getPassword()
	{
		return passwordBox.getValue();
	}

	public void clear()
	{
		originalForm.removeAllChildren();

		usernameBox.setInnerText(null);
		passwordBox.setInnerText(null);

		usernameBox.removeFromParent();
		passwordBox.removeFromParent();

		usernameBox.setInnerText(null);
		passwordBox.setInnerText(null);

		originalForm.appendChild(usernameBox);
		originalForm.appendChild(passwordBox);

        /* Unfortunately, we have to use this approach, as the browser will always try to fill the text fields again */
		Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand()
		{
			int counter = 1;

			@Override
			public boolean execute()
			{
				((InputElement) Document.get().getElementById(Ids.INPUT_LOGIN_USERNAME)).setValue(null);
				((InputElement) Document.get().getElementById(Ids.INPUT_LOGIN_PASSWORD)).setValue(null);

				return counter++ < 10;
			}
		}, 100);
	}

	interface LoginFormNewUiBinder extends UiBinder<Well, LoginForm>
	{
	}
}
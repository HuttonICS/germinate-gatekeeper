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

package jhi.gatekeeper.client.page.unapproveduser;


import com.google.gwt.core.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.TextBox;

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
public class UnapprovedUserDecisionView extends Composite
{
	private static UnapprovedUserDecisionViewUiBinder ourUiBinder = GWT.create(UnapprovedUserDecisionViewUiBinder.class);
	private final HandlerRegistration handlerRegistration;
	@UiField
	Button delete;

	@UiField
	Button approve;

	@UiField
	Button reject;

	private UnapprovedUser user;

	public UnapprovedUserDecisionView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		this.setVisible(false);

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(UnapprovedUserSelectionEvent.TYPE, event ->
		{
			UnapprovedUserDecisionView.this.user = event.getUnapprovedUser();
			UnapprovedUserDecisionView.this.setVisible(event.getUnapprovedUser() != null);
		});

		final AsyncCallbackLogoutOnFailure<Void> callback = new AsyncCallbackLogoutOnFailure<Void>()
		{
			@Override
			protected void onSuccessImpl(Void result)
			{
				GatekeeperEventBus.BUS.fireEvent(new UnapprovedUserSelectionEvent(null));
				GatekeeperEventBus.BUS.fireEvent(new UnapprovedUserListChangeEvent());
			}
		};

		delete.addClickHandler(event -> UnapprovedUserService.Instance.getInstance().delete(Cookie.getRequestProperties(), user, callback));
		approve.addClickHandler(event -> UnapprovedUserService.Instance.getInstance().approve(Cookie.getRequestProperties(), user, callback));
		reject.addClickHandler(event ->
		{
			FlowPanel panel = new FlowPanel();
			final Label text = new Label(I18n.LANG.dialogMessageRejectUser());
			final TextBox reason = new TextBox();

			panel.add(text);
			panel.add(reason);

			final ModalDialog dialog = new ModalDialog(I18n.LANG.dialogTitleRejectUser(), panel)
			{
				@Override
				protected void onButtonClicked(ModalDialogButton button)
				{
					if (button == ModalDialogButton.OK)
						UnapprovedUserService.Instance.getInstance().reject(Cookie.getRequestProperties(), user, reason.getText(), callback);
				}
			}
					.addShownHandler(evt -> reason.setFocus(true))
					.setType(ModalDialog.ModalDialogType.OK_CANCEL);

			dialog.show();
		});
	}

	@Override
	protected void onUnload()
	{
		if (handlerRegistration != null)
			handlerRegistration.removeHandler();

		super.onUnload();
	}

	interface UnapprovedUserDecisionViewUiBinder extends UiBinder<Panel, UnapprovedUserDecisionView>
	{

	}
}
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

package jhi.gatekeeper.client.page.accessrequest;


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
public class AccessRequestDecisionView extends Composite
{
	private static AccessRequestDecisionViewUiBinder ourUiBinder = GWT.create(AccessRequestDecisionViewUiBinder.class);
	@UiField
	Button delete;
	@UiField
	Button approve;
	@UiField
	Button reject;
	private AccessRequest request;
	private HandlerRegistration handlerRegistration;

	public AccessRequestDecisionView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		this.setVisible(false);

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(AccessRequestSelectionEvent.TYPE, event ->
		{
			AccessRequestDecisionView.this.request = event.getAccessRequest();
			AccessRequestDecisionView.this.setVisible(event.getAccessRequest() != null);
		});

		final AsyncCallbackLogoutOnFailure<Void> callback = new AsyncCallbackLogoutOnFailure<Void>()
		{
			@Override
			protected void onSuccessImpl(Void result)
			{
				GatekeeperEventBus.BUS.fireEvent(new AccessRequestSelectionEvent(null));
				GatekeeperEventBus.BUS.fireEvent(new AccessRequestListChangeEvent());
			}
		};

		delete.addClickHandler(event -> AccessRequestService.Instance.getInstance().delete(Cookie.getRequestProperties(), request, callback));
		approve.addClickHandler(event -> AccessRequestService.Instance.getInstance().approve(Cookie.getRequestProperties(), request, callback));
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
						AccessRequestService.Instance.getInstance().reject(Cookie.getRequestProperties(), request, reason.getText(), callback);
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

	interface AccessRequestDecisionViewUiBinder extends UiBinder<Panel, AccessRequestDecisionView>
	{

	}
}
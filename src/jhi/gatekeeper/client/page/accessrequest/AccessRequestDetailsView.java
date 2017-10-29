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

package jhi.gatekeeper.client.page.accessrequest;

import com.google.gwt.core.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.i18n.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Panel;

import java.util.*;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.bean.*;


/**
 * @author Sebastian Raubach
 */
public class AccessRequestDetailsView extends Composite
{
	private static final DateTimeFormat FORMAT_LOCALIZED_DATE_TIME = DateTimeFormat.getFormat(I18n.LANG.genericDateTimeFormat());
	private static AccessRequestDetailsViewUiBinder ourUiBinder = GWT.create(AccessRequestDetailsViewUiBinder.class);
	@UiField
	Label message;
	@UiField
	Description description;
	@UiField
	DescriptionData username;
	@UiField
	DescriptionData email;
	@UiField
	DescriptionData institution;
	@UiField
	DescriptionData createdOn;
	@UiField
	DescriptionData fullName;
	@UiField
	DescriptionData requestsAccessTo;
	private List<HandlerRegistration> handlerRegistrations = new ArrayList<>();

	public AccessRequestDetailsView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		handlerRegistrations.add(GatekeeperEventBus.BUS.addHandler(AccessRequestSelectionEvent.TYPE, event -> setAccessRequest(event.getAccessRequest())));
		handlerRegistrations.add(GatekeeperEventBus.BUS.addHandler(AccessRequestListEmptyEvent.TYPE, event -> AccessRequestDetailsView.this.setVisible(false)));
	}

	private void setAccessRequest(AccessRequest request)
	{
		message.setVisible(request == null);
		description.setVisible(request != null);

//		this.setVisible(request != null);

		if (request != null)
		{
			username.setText(request.getUser().getUsername());
			fullName.setText(request.getUser().getFullName());
			email.setText(request.getUser().getEmail());
			institution.setText(request.getUser().getInstitution().getName() + "(" + request.getUser().getInstitution().getAcronym() + ") - " + request.getUser().getInstitution().getAddress());
			createdOn.setText(FORMAT_LOCALIZED_DATE_TIME.format(request.getUser().getCreationDate()));
			requestsAccessTo.setText(request.getDatabaseSystem().getServerName() + " - " + request.getDatabaseSystem().getSystemName());
		}
	}

	@Override
	protected void onUnload()
	{
		handlerRegistrations.forEach(HandlerRegistration::removeHandler);

		super.onUnload();
	}

	interface AccessRequestDetailsViewUiBinder extends UiBinder<Panel, AccessRequestDetailsView>
	{
	}
}
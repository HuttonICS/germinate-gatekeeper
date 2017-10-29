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

package jhi.gatekeeper.client.page.unapproveduser;

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
public class UnapprovedUserDetailsView extends Composite
{
	private static final DateTimeFormat FORMAT_LOCALIZED_DATE_TIME = DateTimeFormat.getFormat(I18n.LANG.genericDateTimeFormat());
	private static ViewUserDetailsViewUiBinder ourUiBinder = GWT.create(ViewUserDetailsViewUiBinder.class);
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
	DescriptionData requestsAccessToData;
	private List<HandlerRegistration> handlerRegistrations = new ArrayList<>();

	public UnapprovedUserDetailsView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		handlerRegistrations.add(GatekeeperEventBus.BUS.addHandler(UnapprovedUserSelectionEvent.TYPE, event -> setUser(event.getUnapprovedUser())));
		handlerRegistrations.add(GatekeeperEventBus.BUS.addHandler(UnapprovedUserListEmptyEvent.TYPE, event -> UnapprovedUserDetailsView.this.setVisible(false)));
	}

	private void setUser(UnapprovedUser user)
	{
		message.setVisible(user == null);
		description.setVisible(user != null);

//		this.setVisible(user != null);

		if (user != null)
		{
			username.setText(user.getUsername());
			fullName.setText(user.getFullName());
			email.setText(user.getEmail());
			institution.setText(user.getInstitution().getName() + " (" + user.getInstitution().getAcronym() + ") - " + user.getInstitution().getAddress());
			if (user.getCreationDate() != null)
				createdOn.setText(FORMAT_LOCALIZED_DATE_TIME.format(user.getCreationDate()));
			else
				createdOn.setText("");
			requestsAccessToData.setText(user.getDatabaseSystem().getServerName() + " - " + user.getDatabaseSystem().getSystemName());
		}
	}

	@Override
	protected void onUnload()
	{
		handlerRegistrations.forEach(HandlerRegistration::removeHandler);

		super.onUnload();
	}

	interface ViewUserDetailsViewUiBinder extends UiBinder<Panel, UnapprovedUserDetailsView>
	{
	}
}
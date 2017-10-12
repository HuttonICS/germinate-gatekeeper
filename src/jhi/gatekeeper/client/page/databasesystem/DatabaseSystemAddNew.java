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

package jhi.gatekeeper.client.page.databasesystem;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import jhi.gatekeeper.client.widget.select.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class DatabaseSystemAddNew extends Composite
{
	private static UserAddViewUiBinder ourUiBinder = GWT.create(UserAddViewUiBinder.class);
	@UiField
	UserListBox     user;
	@UiField
	UserTypeListBox accessLevel;
	public DatabaseSystemAddNew()
	{
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public User getUser()
	{
		return user.getValue();
	}

	public UserType getUserType()
	{
		return accessLevel.getValue();
	}

	interface UserAddViewUiBinder extends UiBinder<FlowPanel, DatabaseSystemAddNew>
	{
	}
}
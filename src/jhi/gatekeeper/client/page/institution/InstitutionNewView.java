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

package jhi.gatekeeper.client.page.institution;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.TextArea;

import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class InstitutionNewView extends Composite
{
	private static InstitutionNewViewUiBinder ourUiBinder = GWT.create(InstitutionNewViewUiBinder.class);
	@UiField
	BootstrapInputGroup institutionName;

	@UiField
	BootstrapInputGroup institutionAcronym;

	@UiField
	TextArea institutionAddress;

	public InstitutionNewView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public Institution getInstitution()
	{
		if (StringUtils.isEmpty(institutionName.getValue(), institutionAcronym.getValue(), institutionAddress.getValue()))
		{
			return null;
		}
		else
		{
			Institution institution = new Institution();
			institution.setName(institutionName.getValue());
			institution.setAcronym(institutionAcronym.getValue());
			institution.setAddress(institutionAddress.getValue());
			return institution;
		}
	}

	interface InstitutionNewViewUiBinder extends UiBinder<FlowPanel, InstitutionNewView>
	{
	}
}
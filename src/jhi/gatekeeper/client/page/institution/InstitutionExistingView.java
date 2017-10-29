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

package jhi.gatekeeper.client.page.institution;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import jhi.gatekeeper.client.widget.select.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class InstitutionExistingView extends Composite
{
	private static InstitutionExistingViewUiBinder ourUiBinder = GWT.create(InstitutionExistingViewUiBinder.class);
	@UiField
	InstitutionListBox institutionName;

	public InstitutionExistingView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	public Institution getInstitution()
	{
		return institutionName.getValue();
	}

	interface InstitutionExistingViewUiBinder extends UiBinder<FlowPanel, InstitutionExistingView>
	{
	}
}
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

package jhi.gatekeeper.client.widget;


import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;

import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class BootstrapInputGroup extends Composite
{
	private static BootstrapInputGroupUiBinder ourUiBinder = GWT.create(BootstrapInputGroupUiBinder.class);
	@UiField
	FormLabel           label;
	@UiField
	InputGroupWithAddon addon;
	@UiField
	Input               input;
	private String message;
	private String placeholder;
	private String fa;

	@UiConstructor
	public BootstrapInputGroup(final InputType type)
	{
		this();
		input.setType(type);
	}

	public BootstrapInputGroup()
	{
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	@Override
	protected void onLoad()
	{
		super.onLoad();

		if (StringUtils.isEmpty(message))
			label.setVisible(false);
		else
			label.setText(message);

		input.setPlaceholder(placeholder);
		addon.setFa(fa);
	}

	public void setLabel(String message)
	{
		this.message = message;
	}

	public void setPlaceholder(String placeholder)
	{
		this.placeholder = placeholder;
	}

	public void setFa(String fa)
	{
		this.fa = fa;
	}

	public Input getInput()
	{
		return input;
	}

	public String getValue()
	{
		return input.getValue();
	}

	public void setValue(String value)
	{
		input.setValue(value);
	}

	interface BootstrapInputGroupUiBinder extends UiBinder<FlowPanel, BootstrapInputGroup>
	{
	}
}
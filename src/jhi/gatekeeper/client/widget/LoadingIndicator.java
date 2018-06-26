/*
 * Copyright 2018 Information and Computational Sciences,
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

/**
 * @author Sebastian Raubach
 */
public class LoadingIndicator extends Composite
{
	private static LoadingIndicatorUiBinder ourUiBinder = GWT.create(LoadingIndicatorUiBinder.class);
	@UiField
	Modal       modal;
	@UiField
	ProgressBar progress;
	public LoadingIndicator(String message)
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		modal.setTitle(message);
	}

	public void show()
	{
		modal.show();
	}

	public void hide()
	{
		modal.hide();
	}

	public void setProgress(float value)
	{
		progress.setPercent(value);
	}

	interface LoadingIndicatorUiBinder extends UiBinder<FlowPanel, LoadingIndicator>
	{
	}
}
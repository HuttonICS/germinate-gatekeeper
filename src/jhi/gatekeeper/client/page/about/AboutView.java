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

package jhi.gatekeeper.client.page.about;


import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.*;
import com.google.gwt.i18n.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import java.util.*;

import jhi.gatekeeper.client.i18n.*;

/**
 * @author Sebastian Raubach
 */
public class AboutView extends Composite
{
	private static AboutViewUiBinder ourUiBinder = GWT.create(AboutViewUiBinder.class);

	@UiField
	SimplePanel banner;
	@UiField
	HTML        message;

	public AboutView()
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		jsniBanner(banner.getElement());

		message.setHTML(I18n.LANG.aboutMessage(I18n.LANG.menuTopForgottenPassword(), DateTimeFormat.getFormat("yyyy").format(new Date())));
	}

	private native void jsniBanner(Element element)/*-{
		$wnd.$(element).huttonBanner();
	}-*/;

	interface AboutViewUiBinder extends UiBinder<FlowPanel, AboutView>
	{
	}
}
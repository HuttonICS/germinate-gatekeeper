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

package jhi.gatekeeper.client.page.template;

import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.*;
import com.google.gwt.uibinder.client.*;

/**
 * @author Sebastian Raubach
 */
public class CookiePolicyView
{
	private static CookiePolicyViewUiBinder ourUiBinder = GWT.create(CookiePolicyViewUiBinder.class);
	private final DivElement rootElement;

	public CookiePolicyView()
	{
		rootElement = ourUiBinder.createAndBindUi(this);
	}

	public Element getElement()
	{
		return rootElement;
	}

	interface CookiePolicyViewUiBinder extends UiBinder<DivElement, CookiePolicyView>
	{
	}
}
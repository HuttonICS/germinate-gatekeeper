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

package jhi.gatekeeper.client.widget;

import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.*;
import com.google.gwt.query.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.*;

import java.util.*;

import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class TopBarView
{
	private static TopBarViewUiBinder ourUiBinder = GWT.create(TopBarViewUiBinder.class);
	private final UListElement rootElement;
	@UiField
	LIElement languageDropdown;
	@UiField
	UListElement languageList;
	@UiField
	AnchorElement aboutAnchor;
	@UiField
	AnchorElement lostPasswordAnchor;
	@UiField
	AnchorElement loginAnchor;
	@UiField
	AnchorElement logoutAnchor;

	public TopBarView()
	{
		rootElement = ourUiBinder.createAndBindUi(this);

		aboutAnchor.setHref("#" + Page.about.getUrl());
		lostPasswordAnchor.setHref("#" + Page.lostPassword.getUrl());
		loginAnchor.setHref("#" + Page.login.getUrl());
		logoutAnchor.setHref("#" + Page.logout.getUrl());

		GQuery.$(logoutAnchor).hide();

		GatekeeperEventBus.BUS.addHandler(LoginEvent.TYPE, event ->
		{
			GQuery.$(logoutAnchor).show();
			GQuery.$(loginAnchor).hide();
			GQuery.$(lostPasswordAnchor).hide();
		});
		GatekeeperEventBus.BUS.addHandler(LogoutEvent.TYPE, event ->
		{
			GQuery.$(logoutAnchor).hide();
			GQuery.$(loginAnchor).show();
			GQuery.$(lostPasswordAnchor).show();
		});

		/* Get all the supported locales */
		final List<String> locales = new ArrayList<>(Arrays.asList(LocaleInfo.getAvailableLocaleNames()));

        /* Remove the default locale, since it's equal to en_GB */
		locales.remove("default");

        /* Sort the remaining locales */
		Collections.sort(locales);

        /* Get the currently active locale */
		String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();

		boolean additionalLanguages = false;

		for (final String locale : locales)
		{
			/* Try to get the native display name */
			/* Remember the currently active locale */
			LIElement languageItem = Document.get().createLIElement();

			AnchorElement anchor = Document.get().createAnchorElement();
			anchor.setInnerText(LocaleInfo.getLocaleNativeDisplayName(locale));

			Element icon = Document.get().createElement("i");
			icon.addClassName(Classes.combine(Classes.COUNTRY, locale));

			anchor.insertFirst(icon);

			Event.sinkEvents(anchor, Event.ONCLICK);
			Event.setEventListener(anchor, event -> Window.Location.replace(Window.Location.createUrlBuilder().setParameter(LocaleInfo.getLocaleQueryParam(), locale).buildString()));

			languageItem.appendChild(anchor);
			languageList.appendChild(languageItem);

			if (!locale.equals(currentLocale))
				additionalLanguages = true;
		}

		if (!additionalLanguages)
			languageDropdown.removeFromParent();
	}

	public Element getElement()
	{
		return rootElement;
	}

	interface TopBarViewUiBinder extends UiBinder<UListElement, TopBarView>
	{
	}
}
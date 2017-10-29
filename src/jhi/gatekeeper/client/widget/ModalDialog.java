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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import org.gwtbootstrap3.client.shared.event.*;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.*;

import jhi.gatekeeper.client.i18n.*;

/**
 * @author Sebastian Raubach
 */
public abstract class ModalDialog extends Composite
{
	private static ModalDialogUiBinder ourUiBinder = GWT.create(ModalDialogUiBinder.class);
	@UiField
	Modal modal;
	@UiField
	ModalBody body;
	@UiField
	Button positive;
	@UiField
	Button negative;
	private ModalDialogType type = ModalDialogType.YES_NO;

	public ModalDialog(String title, String message)
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		modal.setTitle(title);

		body.add(new Label(message));
	}

	public ModalDialog(String title, Panel content)
	{
		initWidget(ourUiBinder.createAndBindUi(this));

		modal.setTitle(title);

		body.add(content);
	}

	public void show()
	{
		if (type.positive != null)
		{
			positive.setText(type.positive.text);
			positive.addClickHandler(event -> onButtonClicked(type.positive));
		}
		else
		{
			positive.removeFromParent();
		}

		if (type.negative != null)
		{
			negative.setText(type.negative.text);
			negative.addClickHandler(event -> onButtonClicked(type.negative));
		}
		else
		{
			negative.removeFromParent();
		}

		modal.setRemoveOnHide(true);
		modal.show();
	}

	public ModalDialog addShownHandler(ModalShownHandler handler)
	{
		modal.addShownHandler(handler);
		return this;
	}

	public ModalDialog setType(ModalDialogType type)
	{
		this.type = type;
		return this;
	}

	protected abstract void onButtonClicked(ModalDialogButton button);

	public enum ModalDialogButton
	{
		YES(I18n.LANG.genericButtonYes()),
		NO(I18n.LANG.genericButtonNo()),
		OK(I18n.LANG.genericButtonOk()),
		CANCEL(I18n.LANG.genericButtonCancel());

		private String text;

		ModalDialogButton(String text)
		{
			this.text = text;
		}
	}

	public enum ModalDialogType
	{
		YES_NO(ModalDialogButton.YES, ModalDialogButton.NO),
		OK_CANCEL(ModalDialogButton.OK, ModalDialogButton.CANCEL),
		OK(ModalDialogButton.OK, null);

		private ModalDialogButton positive;
		private ModalDialogButton negative;

		ModalDialogType(ModalDialogButton positive, ModalDialogButton negative)
		{
			this.positive = positive;
			this.negative = negative;
		}
	}

	interface ModalDialogUiBinder extends UiBinder<HTMLPanel, ModalDialog>
	{
	}
}
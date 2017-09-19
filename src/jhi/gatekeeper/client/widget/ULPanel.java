/**
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

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * {@link ULPanel} is a basic implementation of an unordered list (ul)
 *
 * @author Sebastian Raubach
 */
public class ULPanel extends ComplexPanel
{
	private UListElement list;

	/**
	 * Creates a new unordered list instance
	 */
	public ULPanel()
	{
		list = Document.get().createULElement();
		setElement(list);
	}

	/**
	 * Adds the given widget as a new li to the ul
	 */
	@Override
	public void add(Widget child)
	{
		Element li = Document.get().createLIElement().cast();
		list.appendChild(li);
		super.add(child, li);
	}

	/**
	 * Adds the given widget as a new li to the ul with the given style class
	 *
	 * @param child     The widget to add
	 * @param className The class name to use
	 */
	public void add(Widget child, String className)
	{
		Element li = Document.get().createLIElement().cast();
		li.setAttribute("class", className);
		list.appendChild(li);
		super.add(child, li);
	}
}

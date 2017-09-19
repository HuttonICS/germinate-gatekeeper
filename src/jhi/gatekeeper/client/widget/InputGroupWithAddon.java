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

import org.gwtbootstrap3.client.ui.*;

import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class InputGroupWithAddon extends InputGroup
{
	private String id = Long.toString(RandomUtils.RANDOM.nextLong());

	private Element addonI = Document.get().createElement("i");

	private InputGroupAddon addon = new InputGroupAddon();

	private Widget widget;

	private String fa = "";

	@Override
	protected void onLoad()
	{
		super.onLoad();

		insert(addon, 0);

		addonI.setClassName(Classes.combine(Classes.FA, Classes.FA_FW, fa));
		addon.getElement().appendChild(addonI);
		addon.setId(id);
		widget.getElement().setAttribute("aria-describedby", id);
	}

	public void setFa(String fa)
	{
		this.fa = fa;
		addonI.setClassName(Classes.combine(Classes.FA, Classes.FA_FW, fa));
	}

	@Override
	public void add(Widget w)
	{
		super.add(w);
		widget = w;
	}
}

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

import com.google.gwt.cell.client.*;
import com.google.gwt.dom.client.*;

import java.util.*;

public class ClickableTableCell extends SafeHtmlCell
{
	/**
	 * Tell the cell which events to handle
	 */
	@Override
	public Set<String> getConsumedEvents()
	{
		HashSet<String> events = new HashSet<>();
		events.add(BrowserEvents.CLICK);
		return events;
	}
}
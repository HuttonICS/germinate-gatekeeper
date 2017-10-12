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

package jhi.gatekeeper.client.cell;

import com.google.gwt.safehtml.shared.*;

import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class UnapprovedUserListCell extends AbstractListCell<UnapprovedUser>
{
	@Override
	public void render(Context context, UnapprovedUser value, SafeHtmlBuilder sb)
	{
		if (value == null)
		{
			return;
		}

		SafeHtml safeValue = SafeHtmlUtils.fromString(value.getUsername());

		/* Check if the user is an admin */
		boolean isAdmin = value.isGatekeeperAdmin();

		if (isAdmin)
			sb.append(USER_TEMPLATES.admin(safeValue));
		else
			sb.append(USER_TEMPLATES.user(safeValue));
	}
}

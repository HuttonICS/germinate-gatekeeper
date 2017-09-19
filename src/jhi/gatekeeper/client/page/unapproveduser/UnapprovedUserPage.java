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

package jhi.gatekeeper.client.page.unapproveduser;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.page.*;
import jhi.gatekeeper.client.page.accessrequest.*;
import jhi.gatekeeper.client.util.*;

/**
 * @author Sebastian Raubach
 */
public class UnapprovedUserPage extends GatekeeperPage
{
	public UnapprovedUserPage()
	{

	}

	@Override
	protected void setUpContent()
	{
		panel.add(HTMLUtils.createHeading(4, I18n.LANG.approveUserTitle()));
		panel.add(new UnapprovedUserView());

		panel.add(HTMLUtils.createHeading(4, I18n.LANG.accessRequestTitle()));
		panel.add(new AccessRequestView());
	}
}

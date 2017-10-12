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

package jhi.gatekeeper.client.page.account;

import jhi.gatekeeper.client.page.*;
import jhi.gatekeeper.client.page.user.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class AccountSettingsPage extends GatekeeperPage
{
	public AccountSettingsPage()
	{

	}

	@Override
	protected void setUpContent()
	{
		panel.add(new UserDetailsView());
		panel.add(new UpdateEmailView());
		panel.add(new UpdatePasswordView());

		GatekeeperEventBus.BUS.addHandler(UserUpdatedEvent.TYPE, event ->
		{
			UserService.Instance.getInstance().getUser(Cookie.getRequestProperties(), new AsyncCallbackLogoutOnFailure<User>()
			{
				@Override
				protected void onSuccessImpl(User result)
				{
					GatekeeperEventBus.BUS.fireEvent(new UserSelectionEvent(result));
				}
			});
		});

		GatekeeperEventBus.BUS.fireEvent(new UserUpdatedEvent());
	}
}

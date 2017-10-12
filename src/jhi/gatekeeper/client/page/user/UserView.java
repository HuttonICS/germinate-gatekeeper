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

package jhi.gatekeeper.client.page.user;

import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.rpc.*;
import com.google.gwt.user.client.ui.*;

import java.util.*;

import jhi.database.shared.util.*;
import jhi.gatekeeper.client.cell.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.pagination.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;


/**
 * @author Sebastian Raubach
 */
public class UserView extends Composite
{

	private HandlerRegistration handlerRegistration;

	public UserView()
	{
		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		panel.add(getUserList());
		panel.add(new UserDetailsView());
		panel.add(new UserGatekeeperAccessView());
		panel.add(new UserPermissionsView());
		panel.add(new UserDeleteView());
		panel.add(new UserAddView());
	}

	private Widget getUserList()
	{
		final AbstractPaginatedSearchableCellList<User> list = new AbstractPaginatedSearchableCellList<User>(new UserListCell())
		{
			@Override
			public void getItemData(User searchBean, Pagination pagination, AsyncCallback<PaginatedResult<List<User>>> callback)
			{
				UserService.Instance.getInstance().getUserList(Cookie.getRequestProperties(), searchBean, pagination, callback);
			}

			@Override
			public User getSearchBean(String searchValue)
			{
				if (StringUtils.isEmpty(searchValue))
					return null;
				else
					return new User()
							.setUsername(searchValue);
			}

			@Override
			protected void onItemSelected(User item)
			{
				GatekeeperEventBus.BUS.fireEvent(new UserSelectionEvent(item));
			}

			@Override
			protected void onListEmpty()
			{
				GatekeeperEventBus.BUS.fireEvent(new UserListEmptyEvent());
			}

			@Override
			protected void onListChanged()
			{
				GatekeeperEventBus.BUS.fireEvent(new UserSelectionEvent(null));
			}
		};

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(UserListChangedEvent.TYPE, event -> list.update());

		return list;
	}

	@Override
	protected void onUnload()
	{
		if (handlerRegistration != null)
			handlerRegistration.removeHandler();

		super.onUnload();
	}
}
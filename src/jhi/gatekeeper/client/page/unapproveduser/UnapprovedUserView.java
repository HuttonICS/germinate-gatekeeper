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

package jhi.gatekeeper.client.page.unapproveduser;

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
public class UnapprovedUserView extends Composite
{

	private HandlerRegistration handlerRegistration;

	public UnapprovedUserView()
	{
		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		panel.add(getUserList());
		panel.add(new UnapprovedUserDetailsView());
		panel.add(new UnapprovedUserDecisionView());
	}

	private Widget getUserList()
	{
		final AbstractPaginatedCellList<UnapprovedUser> list = new AbstractPaginatedCellList<UnapprovedUser>(new UnapprovedUserListCell())
		{
			@Override
			protected void getItemData(Pagination pagination, AsyncCallback<PaginatedResult<List<UnapprovedUser>>> callback)
			{
				UnapprovedUserService.Instance.getInstance().getList(Cookie.getRequestProperties(), pagination, callback);
			}

			@Override
			protected void onItemSelected(UnapprovedUser item)
			{
				GatekeeperEventBus.BUS.fireEvent(new UnapprovedUserSelectionEvent(item));
			}

			@Override
			protected void onListEmpty()
			{
				GatekeeperEventBus.BUS.fireEvent(new UnapprovedUserListEmptyEvent());
			}

			@Override
			protected void onListChanged()
			{
				GatekeeperEventBus.BUS.fireEvent(new UnapprovedUserSelectionEvent(null));
			}
		};

		handlerRegistration = GatekeeperEventBus.BUS.addHandler(UnapprovedUserListChangeEvent.TYPE, event -> list.update());

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
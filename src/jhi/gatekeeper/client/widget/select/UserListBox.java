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

package jhi.gatekeeper.client.widget.select;

import com.google.gwt.text.shared.*;

import java.io.*;
import java.util.*;

import jhi.database.shared.util.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class UserListBox extends GatekeeperListBox<User>
{
	public UserListBox()
	{
		super(new Renderer<User>()
		{
			@Override
			public String render(User object)
			{
				return object.getUsername();
			}

			@Override
			public void render(User object, Appendable appendable) throws IOException
			{
				appendable.append(render(object));
			}
		});
	}

	public void update()
	{
		UserService.Instance.getInstance().getUserList(Cookie.getRequestProperties(), null, Pagination.DEFAULT, new AsyncCallbackLogoutOnFailure<PaginatedResult<List<User>>>()
		{
			@Override
			protected void onSuccessImpl(PaginatedResult<List<User>> result)
			{
				if (CollectionUtils.isEmpty())
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationNoDatabaseSystemFound());
				}
				else
				{
					UserListBox.this.setValue(result.getResult().get(0));
					UserListBox.this.setAcceptableValues(result.getResult());
				}
			}
		});
	}

	@Override
	protected void onLoad()
	{
		super.onLoad();

		update();
	}
}

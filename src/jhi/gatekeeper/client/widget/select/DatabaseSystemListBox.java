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
public class DatabaseSystemListBox extends GatekeeperListBox<DatabaseSystem>
{
	public DatabaseSystemListBox()
	{
		super(new Renderer<DatabaseSystem>()
		{
			@Override
			public String render(DatabaseSystem object)
			{
				return object.getServerName() + " - " + object.getSystemName();
			}

			@Override
			public void render(DatabaseSystem object, Appendable appendable) throws IOException
			{
				appendable.append(render(object));
			}
		});
	}

	public void update()
	{
		DatabaseSystemService.Instance.getInstance().getDatabaseSystemList(Cookie.getRequestProperties(), Pagination.DEFAULT, new AsyncCallbackLogoutOnFailure<PaginatedResult<List<DatabaseSystem>>>()
		{
			@Override
			protected void onSuccessImpl(PaginatedResult<List<DatabaseSystem>> result)
			{
				if (CollectionUtils.isEmpty())
				{
					Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationNoDatabaseSystemFound());
				}
				else
				{
					DatabaseSystemListBox.this.setValue(result.getResult().get(0));
					DatabaseSystemListBox.this.setAcceptableValues(result.getResult());
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

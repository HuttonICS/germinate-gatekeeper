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

package jhi.gatekeeper.client.widget.pagination;

import com.google.gwt.cell.client.*;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.rpc.*;

import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

import java.util.*;

import jhi.database.shared.util.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class DatabasePermissionsTable extends AbstractPaginatedCellTable<DatabasePermission>
{
	private Long id;
	private Type type;
	public DatabasePermissionsTable(Long id, Type type)
	{
		super();
		this.id = id;
		this.type = type;
	}

	@Override
	protected void createColumns()
	{
		/* Add the database system column */
		cellTable.addColumn(new TextColumn<DatabasePermission>()
		{
			@Override
			public String getValue(DatabasePermission object)
			{
				return object.getUser().getUsername();
			}
		}, I18n.LANG.viewUserDetailsTitleUsername());

		/* Add the database system column */
		cellTable.addColumn(new TextColumn<DatabasePermission>()
		{
			@Override
			public String getValue(DatabasePermission object)
			{
				return object.getDatabaseSystem().getSystemName();
			}
		}, I18n.LANG.tableHeaderDatabasePermissionsSystemName());

		/* Add the database server column */
		cellTable.addColumn(new TextColumn<DatabasePermission>()
		{
			@Override
			public String getValue(DatabasePermission object)
			{
				return object.getDatabaseSystem().getServerName();
			}
		}, I18n.LANG.tableHeaderDatabasePermissionsServerName());

		/* Get the user types */
		List<String> categories = new ArrayList<>();
		for (UserType type : UserType.values())
			categories.add(type.getName());

		/* Create a selection cell */
		SelectionCell selectionCell = new SelectionCell(categories);

		/* Add the user types column */
		Column<DatabasePermission, String> userTypeColumn = new Column<DatabasePermission, String>(selectionCell)
		{
			@Override
			public String getValue(DatabasePermission object)
			{
				return object.getUserType().getName();
			}
		};
		cellTable.addColumn(userTypeColumn, I18n.LANG.tableHeaderDatabasePermissionsUserType());

		userTypeColumn.setFieldUpdater((index, object, value) ->
		{
			object.setUserType(UserType.getByName(value));
			DatabasePermissionService.Instance.getInstance().grantPermissionForUser(Cookie.getRequestProperties(), object, new AsyncCallbackLogoutOnFailure<Void>()
			{
				@Override
				protected void onSuccessImpl(Void result)
				{
					Notification.notify(Notification.Type.SUCCESS, I18n.LANG.notificationDatabasePermissionsUpdated());
				}
			});
		});

		final ButtonCell buttonCell = new ButtonCell(IconType.REMOVE);

		Column<DatabasePermission, String> deleteColumn = new Column<DatabasePermission, String>(buttonCell)
		{
			@Override
			public String getValue(DatabasePermission object)
			{
				return null;
			}
		};
		cellTable.addColumn(deleteColumn, I18n.LANG.tableHeaderDatabasePermissionsDelete());

		deleteColumn.setFieldUpdater((index, object, value) -> DatabasePermissionService.Instance.getInstance().
				revokePermissionForUser(Cookie.getRequestProperties(), object, new AsyncCallbackLogoutOnFailure<Void>()

				{
					@Override
					protected void onSuccessImpl(Void result)
					{
						Notification.notify(Notification.Type.INFO, I18n.LANG.notificationDatabasePermissionsUpdated());
						GatekeeperEventBus.BUS.fireEvent(new DatabasePermissionListChangeEvent());
					}
				}));

		fixItemAlignment();

	}

	@Override
	protected void getItemData(Pagination pagination, AsyncCallback<PaginatedResult<List<DatabasePermission>>> callback)
	{
		switch (type)
		{
			case USER:
				DatabasePermissionService.Instance.getInstance().getPermissionListDataByUser(Cookie.getRequestProperties(), id, pagination, callback);
				break;
			case DATABASE_SYSTEM:
				DatabasePermissionService.Instance.getInstance().getPermissionsForSystem(Cookie.getRequestProperties(), id, pagination, callback);
				break;
		}

	}

	public enum Type
	{
		USER,
		DATABASE_SYSTEM
	}
}

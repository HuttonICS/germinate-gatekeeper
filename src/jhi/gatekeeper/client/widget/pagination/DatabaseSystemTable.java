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

package jhi.gatekeeper.client.widget.pagination;

import com.google.gwt.cell.client.*;
import com.google.gwt.dom.client.*;
import com.google.gwt.safehtml.shared.*;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.rpc.*;

import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

import java.util.*;

import jhi.database.shared.util.*;
import jhi.gatekeeper.client.cell.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.util.event.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class DatabaseSystemTable extends AbstractPaginatedCellTable<DatabaseSystem>
{
	@Override
	protected void createColumns()
	{
		/* Add the database system column */
		addColumn(new Column<DatabaseSystem, SafeHtml>(new ClickableTableCell())
		{
			@Override
			public void onBrowserEvent(Cell.Context context, Element elem, DatabaseSystem object, NativeEvent event)
			{
				if (BrowserEvents.CLICK.equals(event.getType()))
				{
					ParameterStore.put(Parameter.databaseSystemId, object.getId());

					if (!event.getCtrlKey() && !event.getShiftKey() && !event.getMetaKey())
						super.onBrowserEvent(context, elem, object, event);
				}
			}

			@Override
			public SafeHtml getValue(DatabaseSystem object)
			{
				SafeUri uri = UriUtils.fromString("#" + Page.systemsUserList.getUrl());
				return AbstractListCell.USER_TEMPLATES.anchor(uri, object.getSystemName());
			}
		}, I18n.LANG.tableHeaderDatabasePermissionsSystemName());

		/* Add the database server column */
		addColumn(new TextColumn<DatabaseSystem>()
		{
			@Override
			public String getValue(DatabaseSystem object)
			{
				return object.getServerName();
			}
		}, I18n.LANG.tableHeaderDatabasePermissionsServerName());

		/* Add the database system description */
		addColumn(new TextColumn<DatabaseSystem>()
		{
			@Override
			public String getValue(DatabaseSystem object)
			{
				return object.getDescription();
			}
		}, I18n.LANG.tableHeaderDatabasePermissionsSystemDescription());

		ButtonCell buttonCell = new ButtonCell(IconType.REMOVE);

		Column<DatabaseSystem, String> deleteColumn = new Column<DatabaseSystem, String>(buttonCell)
		{
			@Override
			public String getValue(DatabaseSystem object)
			{
				return null;
			}
		};
		addColumn(deleteColumn, I18n.LANG.tableHeaderDatabasePermissionsDelete());

		GatekeeperEventBus.BUS.addHandler(DatabaseSystemListChangeEvent.TYPE, event -> DatabaseSystemTable.this.update());

		deleteColumn.setFieldUpdater((index, object, value) ->
		{
			new ModalDialog(I18n.LANG.dialogTitleDeleteDatabase(), I18n.LANG.dialogMessageDeleteDatabase())
			{
				@Override
				protected void onButtonClicked(ModalDialogButton button)
				{
					if (button == ModalDialogButton.YES)
					{
						DatabaseSystemService.Instance.getInstance().delete(Cookie.getRequestProperties(), object, new AsyncCallbackLogoutOnFailure<Void>()
						{
							@Override
							protected void onSuccessImpl(Void result)
							{
								GatekeeperEventBus.BUS.fireEvent(new DatabaseSystemListChangeEvent());
							}
						});
					}
				}

			}
					.setType(ModalDialog.ModalDialogType.YES_NO)
					.show();
		});

		fixItemAlignment();
	}

	@Override
	protected void getItemData(Pagination pagination, AsyncCallback<PaginatedResult<List<DatabaseSystem>>> callback)
	{
		DatabaseSystemService.Instance.getInstance().getDatabaseSystemList(Cookie.getRequestProperties(), pagination, callback);
	}
}

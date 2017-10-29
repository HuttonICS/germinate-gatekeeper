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

package jhi.gatekeeper.client.widget.pagination;

import com.google.gwt.cell.client.*;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.rpc.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.*;

import org.gwtbootstrap3.client.ui.constants.*;

import java.util.*;

import jhi.database.shared.util.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.callback.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public abstract class AbstractPaginatedCellList<T> extends Composite
{
	private static final Integer NR_OF_ITEMS_PER_PAGE = 10;

	protected FlowPanel panel      = new FlowPanel();
	private   FlowPanel errorPanel = new FlowPanel();

	private CellList<T>                     cellList;
	private NoSelectionModel<T>             selectionModel;
	private BootstrapPager                  pager;
	private RefreshableAsyncDataProvider<T> dataProvider;
	private int rangeStart = 0;

	public AbstractPaginatedCellList(Cell<T> cell)
	{
		initWidget(panel);

		cellList = new CellList<>(cell);
		cellList.setStyleName(Styles.LIST_GROUP);

		selectionModel = new NoSelectionModel<>();
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(event ->
		{
			T currentSelection = selectionModel.getLastSelectedObject();
			for (int i = 0; i < cellList.getVisibleItemCount(); i++)
			{
				cellList.getRowElement(i).removeClassName(Styles.ACTIVE);
			}

			cellList.getRowElement(cellList.getKeyboardSelectedRow()).addClassName(Styles.ACTIVE);

			onItemSelected(currentSelection);
		});

		pager = new BootstrapPager();
		pager.setDisplay(cellList);
		pager.setVisible(false);
	}

	@Override
	protected void onLoad()
	{
		super.onLoad();

		panel.add(errorPanel);
		panel.add(cellList);
		panel.add(pager);

		update();
	}

	public void update()
	{
		pager.setPageStart(0);
		queryForData();
	}

	private void queryForData()
	{
		if (dataProvider == null)
		{
			dataProvider = new RefreshableAsyncDataProvider<T>()
			{
				@Override
				protected void onRangeChanged(HasData<T> display)
				{
					/* Get the start and length of the current page */
					final Range range = display.getVisibleRange();
					rangeStart = range.getStart();
					final int length = display.getVisibleRange().getLength();

					/* Set up the callback object */
					getItemData(new Pagination(rangeStart, length), new AsyncCallbackLogoutOnFailure<PaginatedResult<List<T>>>()
					{
						@Override
						public void onFailureImpl(Throwable caught)
						{
							updateRowData(0, new ArrayList<>());

							super.onFailureImpl(caught);
						}

						@Override
						public void onSuccessImpl(PaginatedResult<List<T>> result)
						{
							cellList.setPageSize(NR_OF_ITEMS_PER_PAGE);
							cellList.setRowCount((int) result.getCount(), true);

							cellList.setVisible(result.getCount() > 0);
							pager.setVisible(result.getCount() > NR_OF_ITEMS_PER_PAGE);

							errorPanel.clear();

							if (result.getCount() < 1)
							{
								errorPanel.add(HTMLUtils.createHeading(5, I18n.LANG.genericHeadingNoDataFound()));

								onListEmpty();
							}
							else
							{
								/* Update table */
								// dataTable.setRowData(start, data);
								updateRowData(rangeStart, result.getResult());

								for (int i = 0; i < cellList.getRowCount(); i++)
									cellList.getRowElement(i).setClassName(Styles.LIST_GROUP_ITEM);

								if (result.getCount() < rangeStart)
								{
									pager.setPageStart(0);
								}

								onListChanged();
							}
						}
					});
				}
			};

			/* Connect the table to the data provider */
			dataProvider.addDataDisplay(cellList);
		}
		else
		{
			dataProvider.refresh(cellList);
		}
	}

	protected abstract void getItemData(Pagination pagination, AsyncCallback<PaginatedResult<List<T>>> callback);

	protected abstract void onItemSelected(T item);

	protected abstract void onListEmpty();

	protected abstract void onListChanged();
}

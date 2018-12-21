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

import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.rpc.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.*;

import org.gwtbootstrap3.client.ui.gwt.CellTable;

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
public abstract class AbstractPaginatedCellTable<T> extends Composite
{
	private static final Integer NR_OF_ITEMS_PER_PAGE = 10;

	protected FlowPanel      panel      = new FlowPanel();
	protected CellTable<T>   cellTable;
	private   FlowPanel      errorPanel = new FlowPanel();
	private   BootstrapPager pager;

	public AbstractPaginatedCellTable()
	{
		initWidget(panel);

		cellTable = new CellTable<>();

		/* Add a custom loading indicator */
		SimplePanel anim = new SimplePanel();
		anim.setStyleName(Classes.PROGRESS_INDICATOR_INDETERMINATE);
		anim.getElement().getStyle().setZIndex(10000);
		anim.getElement().getStyle().setPosition(Style.Position.RELATIVE);
		anim.getElement().getStyle().setLeft(0, Style.Unit.PX);
		anim.getElement().getStyle().setTop(0, Style.Unit.PX);
		anim.getElement().getStyle().setMargin(0, Style.Unit.PX);
		cellTable.setLoadingIndicator(anim);

		pager = new BootstrapPager();
		pager.addStyleName(Classes.NO_MARGIN_TOP);
		pager.setDisplay(cellTable);
		pager.setVisible(false);
	}

	@Override
	protected void onLoad()
	{
		super.onLoad();

		panel.add(errorPanel);

		FlowPanel scrollPanel = new FlowPanel();
		scrollPanel.setStyleName(Classes.SCROLL_PANEL);
		scrollPanel.add(cellTable);
		panel.add(scrollPanel);

		panel.add(pager);

		createColumns();

		update();
	}

	public void update()
	{
		pager.setPageStart(0);
		queryForData();
	}

	private void queryForData()
	{
		AsyncDataProvider<T> dataProvider = new AsyncDataProvider<T>()
		{
			@Override
			protected void onRangeChanged(HasData<T> display)
			{
				/* Get the start and length of the current page */
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				final int length = display.getVisibleRange().getLength();

				/* Set up the callback object */
				getItemData(new Pagination(start, length), new AsyncCallbackLogoutOnFailure<PaginatedResult<List<T>>>()
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
						cellTable.setPageSize(NR_OF_ITEMS_PER_PAGE);
						cellTable.setRowCount((int) result.getCount(), true);

						cellTable.setVisible(result.getCount() > 0);
						pager.setVisible(result.getCount() > NR_OF_ITEMS_PER_PAGE);

						errorPanel.clear();

						if (result.getCount() < 1)
						{
							errorPanel.add(HTMLUtils.createHeading(5, I18n.LANG.genericHeadingNoDataFound()));
						}
						else
						{
							/* Update table */
							// dataTable.setRowData(start, data);
							updateRowData(start, result.getResult());

							if (result.getCount() < start)
							{
								pager.setPageStart(0);
							}
						}
					}
				});
			}
		};

		/* Connect the table to the data provider */
		dataProvider.addDataDisplay(cellTable);
	}

	void fixItemAlignment()
	{
		/* Dirty hack to get stuff vertically aligned and break words */
		cellTable.addLoadingStateChangeHandler(event ->
		{
			if (event.getLoadingState() == LoadingStateChangeEvent.LoadingState.LOADED)
			{
				Scheduler.get().scheduleDeferred(() ->
				{
					for (int i = 0; i < cellTable.getRowCount(); i++)
					{
						toggle(cellTable.getRowElement(i));
					}
				});
			}
		});
	}

	private native void toggle(Element element) /*-{
		$wnd.$(element)
			.find("td")
			.css("vertical-align", "middle")
			.addClass(@jhi.gatekeeper.client.util.Classes::BREAK_WORD);
	}-*/;

	protected void addColumn(Column<T, ?> column, String header)
	{
		cellTable.addColumn(column, header);
	}

	protected abstract void createColumns();

	protected abstract void getItemData(Pagination pagination, AsyncCallback<PaginatedResult<List<T>>> callback);
}

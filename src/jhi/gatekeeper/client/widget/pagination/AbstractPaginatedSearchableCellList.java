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
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.*;

import java.util.*;

import jhi.database.shared.util.*;
import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.widget.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public abstract class AbstractPaginatedSearchableCellList<T> extends AbstractPaginatedCellList<T> implements PaginatedSearchableCellWidget<T>
{
	private BootstrapInputGroup searchBox = new BootstrapInputGroup();

	public AbstractPaginatedSearchableCellList(Cell<T> cell)
	{
		super(cell);

		searchBox.setFa(Classes.FA_SEARCH);
		searchBox.setPlaceholder(I18n.LANG.genericTextSearch());
		searchBox.addStyleName(Classes.MARGIN_BOTTOM);

		/* Listen for key events, start a timer and only start the request after 500ms of nothing happening */
		searchBox.getInput().addKeyUpHandler(new KeyUpHandler()
		{
			private Timer timer = new Timer()
			{
				@Override
				public void run()
				{
					AbstractPaginatedSearchableCellList.super.update();
				}
			};

			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				/* Restart the timer after the event */
				timer.schedule(500);
			}
		});
	}

	@Override
	protected void onLoad()
	{
		panel.add(searchBox);

		super.onLoad();
	}

	@Override
	protected final void getItemData(Pagination pagination, AsyncCallback<PaginatedResult<List<T>>> callback)
	{
		getItemData(getSearchBean(searchBox.getValue()), pagination, callback);
	}
}

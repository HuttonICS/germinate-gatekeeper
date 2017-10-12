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

package jhi.gatekeeper.client.util.event;

import com.google.gwt.event.shared.*;

/**
 * A {@link DatabaseSystemListChangeEvent} indicates that the user navigated to a new page.
 *
 * @author Sebastian Raubach
 */
public class DatabaseSystemListChangeEvent extends GwtEvent<DatabaseSystemListChangeEvent.DatabaseSystemListChangeEventHandler>
{
	public static final Type<DatabaseSystemListChangeEventHandler> TYPE = new Type<>();

	/**
	 * Creates a new instance of {@link DatabaseSystemListChangeEvent}
	 */
	public DatabaseSystemListChangeEvent()
	{

	}

	@Override
	public Type<DatabaseSystemListChangeEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(DatabaseSystemListChangeEventHandler handler)
	{
		handler.onDatabaseSystemDeleted(this);
	}

	/**
	 * {@link DatabaseSystemListChangeEventHandler} is the {@link EventHandler} of {@link DatabaseSystemListChangeEvent}
	 */
	public interface DatabaseSystemListChangeEventHandler extends EventHandler
	{
		/**
		 * Called when a {@link DatabaseSystemListChangeEvent} has been fired
		 *
		 * @param event The {@link DatabaseSystemListChangeEvent}
		 */
		void onDatabaseSystemDeleted(DatabaseSystemListChangeEvent event);
	}
}

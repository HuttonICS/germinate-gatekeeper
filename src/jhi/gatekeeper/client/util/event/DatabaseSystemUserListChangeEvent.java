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
 * A {@link DatabaseSystemUserListChangeEvent} indicates that the user navigated to a new page.
 *
 * @author Sebastian Raubach
 */
public class DatabaseSystemUserListChangeEvent extends GwtEvent<DatabaseSystemUserListChangeEvent.DatabaseSystemUserListChangeEventHandler>
{
	public static final Type<DatabaseSystemUserListChangeEventHandler> TYPE = new Type<>();

	/**
	 * Creates a new instance of {@link DatabaseSystemUserListChangeEvent}
	 */
	public DatabaseSystemUserListChangeEvent()
	{

	}

	@Override
	public Type<DatabaseSystemUserListChangeEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(DatabaseSystemUserListChangeEventHandler handler)
	{
		handler.onDatabaseSystemUserDeleted(this);
	}

	/**
	 * {@link DatabaseSystemUserListChangeEventHandler} is the {@link EventHandler} of {@link DatabaseSystemUserListChangeEvent}
	 */
	public interface DatabaseSystemUserListChangeEventHandler extends EventHandler
	{
		/**
		 * Called when a {@link DatabaseSystemUserListChangeEvent} has been fired
		 *
		 * @param event The {@link DatabaseSystemUserListChangeEvent}
		 */
		void onDatabaseSystemUserDeleted(DatabaseSystemUserListChangeEvent event);
	}
}

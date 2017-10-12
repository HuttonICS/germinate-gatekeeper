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

import jhi.gatekeeper.shared.bean.*;

/**
 * A {@link UserSelectionEvent} indicates that the user navigated to a new page.
 *
 * @author Sebastian Raubach
 */
public class UserSelectionEvent extends GwtEvent<UserSelectionEvent.UserSelectionEventHandler>
{
	public static final Type<UserSelectionEventHandler> TYPE = new Type<>();
	private User user;

	/**
	 * Creates a new instance of {@link UserSelectionEvent}
	 *
	 * @param user The User
	 */
	public UserSelectionEvent(User user)
	{
		this.user = user;
	}

	public User getUser()
	{
		return user;
	}

	@Override
	public Type<UserSelectionEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(UserSelectionEventHandler handler)
	{
		handler.onUserSelected(this);
	}

	/**
	 * {@link UserSelectionEventHandler} is the {@link EventHandler} of {@link UserSelectionEvent}
	 */
	public interface UserSelectionEventHandler extends EventHandler
	{
		/**
		 * Called when a {@link UserSelectionEvent} has been fired
		 *
		 * @param event The {@link UserSelectionEvent}
		 */
		void onUserSelected(UserSelectionEvent event);
	}
}

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
 * A {@link UnapprovedUserSelectionEvent} indicates that the user navigated to a new page.
 *
 * @author Sebastian Raubach
 */
public class UnapprovedUserSelectionEvent extends GwtEvent<UnapprovedUserSelectionEvent.UnapprovedUserSelectionEventHandler>
{
	public static final Type<UnapprovedUserSelectionEventHandler> TYPE = new Type<>();
	private UnapprovedUser user;

	/**
	 * Creates a new instance of {@link UnapprovedUserSelectionEvent}
	 *
	 * @param user The User
	 */
	public UnapprovedUserSelectionEvent(UnapprovedUser user)
	{
		this.user = user;
	}

	public UnapprovedUser getUnapprovedUser()
	{
		return user;
	}

	@Override
	public Type<UnapprovedUserSelectionEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(UnapprovedUserSelectionEventHandler handler)
	{
		handler.onUnapprovedUserSelected(this);
	}

	/**
	 * {@link UnapprovedUserSelectionEventHandler} is the {@link EventHandler} of {@link UnapprovedUserSelectionEvent}
	 */
	public interface UnapprovedUserSelectionEventHandler extends EventHandler
	{
		/**
		 * Called when a {@link UnapprovedUserSelectionEvent} has been fired
		 *
		 * @param event The {@link UnapprovedUserSelectionEvent}
		 */
		void onUnapprovedUserSelected(UnapprovedUserSelectionEvent event);
	}
}

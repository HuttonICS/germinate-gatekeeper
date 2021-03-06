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

package jhi.gatekeeper.client.util.event;

import com.google.gwt.event.shared.*;

/**
 * A {@link UnapprovedUserListEmptyEvent} indicates that the user navigated to a new page.
 *
 * @author Sebastian Raubach
 */
public class UnapprovedUserListEmptyEvent extends GwtEvent<UnapprovedUserListEmptyEvent.UnapprovedUserListEmptyEventHandler>
{
	public static final Type<UnapprovedUserListEmptyEventHandler> TYPE = new Type<>();

	/**
	 * Creates a new instance of {@link UnapprovedUserListEmptyEvent}
	 */
	public UnapprovedUserListEmptyEvent()
	{
	}

	@Override
	public Type<UnapprovedUserListEmptyEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(UnapprovedUserListEmptyEventHandler handler)
	{
		handler.onUnapprovedUserListEmpty(this);
	}

	/**
	 * {@link UnapprovedUserListEmptyEventHandler} is the {@link EventHandler} of {@link UnapprovedUserListEmptyEvent}
	 */
	public interface UnapprovedUserListEmptyEventHandler extends EventHandler
	{
		/**
		 * Called when a {@link UnapprovedUserListEmptyEvent} has been fired
		 *
		 * @param event The {@link UnapprovedUserListEmptyEvent}
		 */
		void onUnapprovedUserListEmpty(UnapprovedUserListEmptyEvent event);
	}
}

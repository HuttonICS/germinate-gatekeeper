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
 * A {@link AccessRequestSelectionEvent} indicates that the user navigated to a new page.
 *
 * @author Sebastian Raubach
 */
public class AccessRequestSelectionEvent extends GwtEvent<AccessRequestSelectionEvent.AccessRequestSelectionEventHandler>
{
	public static final Type<AccessRequestSelectionEventHandler> TYPE = new Type<>();
	private AccessRequest accessRequest;

	/**
	 * Creates a new instance of {@link AccessRequestSelectionEvent}
	 *
	 * @param accessRequest The AccessRequest
	 */
	public AccessRequestSelectionEvent(AccessRequest accessRequest)
	{
		this.accessRequest = accessRequest;
	}

	public AccessRequest getAccessRequest()
	{
		return accessRequest;
	}

	@Override
	public Type<AccessRequestSelectionEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(AccessRequestSelectionEventHandler handler)
	{
		handler.onAccessRequestSelected(this);
	}

	/**
	 * {@link AccessRequestSelectionEventHandler} is the {@link EventHandler} of {@link AccessRequestSelectionEvent}
	 */
	public interface AccessRequestSelectionEventHandler extends EventHandler
	{
		/**
		 * Called when a {@link AccessRequestSelectionEvent} has been fired
		 *
		 * @param event The {@link AccessRequestSelectionEvent}
		 */
		void onAccessRequestSelected(AccessRequestSelectionEvent event);
	}
}

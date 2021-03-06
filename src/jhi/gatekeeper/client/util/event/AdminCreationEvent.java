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
 * A {@link AdminCreationEvent} indicates that the user navigated to a new page.
 *
 * @author Sebastian Raubach
 */
public class AdminCreationEvent extends GwtEvent<AdminCreationEvent.AdminCreationEventHandler>
{
	public static final Type<AdminCreationEventHandler> TYPE = new Type<>();

	/**
	 * Creates a new instance of {@link AdminCreationEvent}
	 */
	public AdminCreationEvent()
	{

	}

	@Override
	public Type<AdminCreationEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(AdminCreationEventHandler handler)
	{
		handler.onAdminCreated(this);
	}

	/**
	 * {@link AdminCreationEventHandler} is the {@link EventHandler} of {@link AdminCreationEvent}
	 */
	public interface AdminCreationEventHandler extends EventHandler
	{
		/**
		 * Called when a {@link AdminCreationEvent} has been fired
		 *
		 * @param event The {@link AdminCreationEvent}
		 */
		void onAdminCreated(AdminCreationEvent event);
	}
}

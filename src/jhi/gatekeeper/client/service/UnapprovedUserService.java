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

package jhi.gatekeeper.client.service;

import com.google.gwt.core.shared.*;
import com.google.gwt.user.client.rpc.*;

import java.util.*;

import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
@RemoteServiceRelativePath("unapproved-user")
public interface UnapprovedUserService extends RemoteService
{
	long getCount(RequestProperties properties) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException;

	PaginatedResult<List<UnapprovedUser>> getList(RequestProperties properties, Pagination pagination) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException;

	void delete(RequestProperties properties, UnapprovedUser user) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException;

	void approve(RequestProperties properties, UnapprovedUser user) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException, EmailException, SuspendedUserException, InvalidActivationKeyException, UserNotFoundException;

	void reject(RequestProperties properties, UnapprovedUser user, String rejectionReason) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException, EmailException;

	final class Instance
	{
		public static UnapprovedUserServiceAsync getInstance()
		{
			return InstanceHolder.INSTANCE;
		}

		/**
		 * {@link InstanceHolder} is loaded on the first execution of {@link Instance#getInstance()} or the first access to {@link
		 * InstanceHolder#INSTANCE}, not before.
		 * <p/>
		 * This solution (<a href= "http://en.wikipedia.org/wiki/Initialization_on_demand_holder_idiom" >Initialization-on-demand holder idiom</a>) is
		 * thread-safe without requiring special language constructs (i.e. <code>volatile</code> or <code>synchronized</code>).
		 *
		 * @author Sebastian Raubach
		 */
		private static final class InstanceHolder
		{
			private static final UnapprovedUserServiceAsync INSTANCE = GWT.create(UnapprovedUserService.class);
		}
	}
}

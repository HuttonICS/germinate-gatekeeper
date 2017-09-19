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
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService
{
	void createAdmin(RequestProperties properties, User user, UserCredentials credentials) throws UserExistsException, DatabaseException;

	void updateUserEmail(RequestProperties properties, String email) throws InvalidCredentialsException, InvalidSessionException, DatabaseException;

	void updatePassword(RequestProperties properties, UserCredentials credentials) throws InvalidCredentialsException, InvalidSessionException, DatabaseException;

	long getUserCount(RequestProperties properties, User searchBean) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException;

	PaginatedResult<List<User>> getUserList(RequestProperties properties, User searchBean, Pagination pagination) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException;

	void deleteUser(RequestProperties properties, long id) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException;

	void addUser(RequestProperties properties, User user, UserCredentials credentials) throws InvalidSessionException, UserExistsException, InsufficientPermissionsException, DatabaseException;

	void sendNewPassword(RequestProperties properties, String username, String email) throws InvalidCredentialsException, EmailException, DatabaseException;

	void activateUser(RequestProperties properties, String key) throws InvalidActivationKeyException, EmailException, DatabaseException, UserNotFoundException, SuspendedUserException;

	void setHasAccessToGatekeeper(RequestProperties properties, User user) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException;

	User getUser(RequestProperties properties) throws InvalidSessionException, InvalidCredentialsException, DatabaseException;

	final class Instance
	{
		public static UserServiceAsync getInstance()
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
			private static final UserServiceAsync INSTANCE = GWT.create(UserService.class);
		}
	}
}

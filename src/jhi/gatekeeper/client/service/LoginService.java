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

import jhi.database.shared.exception.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link LoginService} is a {@link RemoteService} providing methods to log in.
 *
 * @author Sebastian Raubach
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService
{
	/**
	 * Attempts to login the user
	 *
	 * @param properties  The {@link RequestProperties}
	 * @param credentials The {@link UserCredentials}
	 * @return The session id (either the old one if it's still valid, or a newly generated one if the login was successful) and the path of the
	 * application (used for the cookie) and the user id
	 * @throws InvalidCredentialsException      Will be thrown if the session is invalid and the user credentials are invalid
	 * @throws AdminAccountMissingException     Thrown if no admin account exists
	 * @throws SuspendedUserException           Thrown if the user has been suspended
	 * @throws InsufficientPermissionsException Thrown if the user doesn't have sufficient permissions to log in
	 * @throws DatabaseException                Thrown if the query fails on the server
	 */
	UserAuthentication login(RequestProperties properties, UserCredentials credentials) throws InvalidCredentialsException, AdminAccountMissingException, SuspendedUserException,
			InsufficientPermissionsException, DatabaseException;

	void logout(RequestProperties properties) throws InvalidSessionException, SuspendedUserException;

	final class Instance
	{
		public static LoginServiceAsync getInstance()
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
			private static final LoginServiceAsync INSTANCE = GWT.create(LoginService.class);
		}
	}
}

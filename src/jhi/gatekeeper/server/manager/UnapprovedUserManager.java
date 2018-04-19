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

package jhi.gatekeeper.server.manager;

import java.util.*;

import jhi.database.server.parser.*;
import jhi.database.server.query.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class UnapprovedUserManager extends AbstractManager
{
	private static final String SELECT_BY_ID = "SELECT * FROM unapproved_users WHERE id = ?";

	private static final String QUERY_COUNT             = "SELECT COUNT(1) AS count FROM unapproved_users WHERE has_been_rejected = 0";
	private static final String QUERY_PASSWORD          = "SELECT user_password FROM unapproved_users WHERE id = ?";
	private static final String QUERY_LIST_DATA         = "SELECT * FROM unapproved_users WHERE has_been_rejected = 0 ORDER BY created_on ASC LIMIT ?, ?";
	private static final String QUERY_BY_ACTIVATION_KEY = "SELECT * FROM unapproved_users WHERE activation_key = ?";

	private static final String UPDATE_ACTIVATION_KEY = "UPDATE unapproved_users SET activation_key = ? WHERE id = ?";
	private static final String UPDATE_REJECT         = "UPDATE unapproved_users SET has_been_rejected = 1 WHERE id = ?";

	private static final String DELETE_BY_ID = "DELETE FROM unapproved_users WHERE id = ?";

	public static UnapprovedUserManager get()
	{
		return UnapprovedUserManager.Inst.INSTANCE;
	}

	/**
	 * Returns the {@link UnapprovedUser} with the given id.
	 *
	 * @param id The id of the {@link UnapprovedUser}
	 * @return The {@link UnapprovedUser} with the given id
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static UnapprovedUser getById(Long id) throws DatabaseException
	{
		if (id == null)
			return null;

		return new DatabaseObjectQuery<UnapprovedUser>(SELECT_BY_ID)
				.setLong(id)
				.run()
				.getObject(UnapprovedUser.Parser.Instance.getInstance());
	}

	/**
	 * Deletes the given {@link UnapprovedUser} from the database.
	 *
	 * @param user The {@link UnapprovedUser} to delete
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void delete(UnapprovedUser user) throws DatabaseException
	{
		new ValueQuery(DELETE_BY_ID)
				.setLong(user.getId())
				.execute();
	}

	/**
	 * Approves the given {@link UnapprovedUser} request.
	 *
	 * @param user The {@link UnapprovedUser} request
	 * @throws DatabaseException             Thrown if the interaction with the database fails
	 * @throws SuspendedUserException        Thrown if the given user has been suspended
	 * @throws InvalidActivationKeyException Thrown if the activation failed
	 * @throws UserNotFoundException         Thrown if no user with the given details has been found
	 */
	public static void approve(UnapprovedUser user) throws DatabaseException, SuspendedUserException, InvalidActivationKeyException, UserNotFoundException
	{
		User u = UserManager.activateUser(user);

		DatabasePermission permission = new DatabasePermission(u, user.getDatabaseSystem(), UserType.REGULAR_USER);

		DatabasePermissionManager.grantPermission(permission);

		delete(user);
	}

	/**
	 * Rejects the given {@link UnapprovedUser} request.
	 *
	 * @param user The {@link UnapprovedUser} to reject
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void reject(UnapprovedUser user) throws DatabaseException
	{
		new ValueQuery(UPDATE_REJECT)
				.setLong(user.getId())
				.execute();
	}

	/**
	 * Returns the number of {@link UnapprovedUser}.
	 *
	 * @return The number of {@link UnapprovedUser}
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static long getCount() throws DatabaseException
	{
		return new ValueQuery(QUERY_COUNT)
				.run(COUNT)
				.getLong();
	}

	/**
	 * Returns a {@link PaginatedResult} of {@link UnapprovedUser}s.
	 *
	 * @param pagination The {@link Pagination} object specifying the chunk of data to get
	 * @return A {@link PaginatedResult} of {@link UnapprovedUser}s
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static PaginatedResult<List<UnapprovedUser>> getList(Pagination pagination) throws DatabaseException
	{
		return new DatabaseObjectQuery<UnapprovedUser>(QUERY_LIST_DATA)
				.setFetchesCount(pagination.getResultSize())
				.setInt(pagination.getStart())
				.setInt(pagination.getSize())
				.run()
				.getObjectsPaginated(UnapprovedUser.Parser.Instance.getInstance(), false);
	}

	/**
	 * Returns the {@link UnapprovedUser} request with the given activation key.
	 *
	 * @param activationKey The activation key of the {@link UnapprovedUser}
	 * @return The {@link UnapprovedUser} request with the given activation key
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static UnapprovedUser getByActivationKey(String activationKey) throws DatabaseException
	{
		return new DatabaseObjectQuery<UnapprovedUser>(QUERY_BY_ACTIVATION_KEY)
				.setString(activationKey)
				.run()
				.getObject(UnapprovedUser.Parser.Instance.getInstance());
	}

	/**
	 * Sets the activation key of the given {@link UnapprovedUser}.
	 *
	 * @param user The {@link UnapprovedUser} to update
	 * @param uuid The activation key to set
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void setActivationKey(UnapprovedUser user, String uuid) throws DatabaseException
	{
		new ValueQuery(UPDATE_ACTIVATION_KEY)
				.setString(uuid)
				.setLong(user.getId())
				.execute();
	}

	/**
	 * Returns the password (hashed+salted) of the {@link UnapprovedUser} request.
	 *
	 * @param user
	 * @return
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static String getPassword(UnapprovedUser user) throws DatabaseException
	{
		return new ValueQuery(QUERY_PASSWORD)
				.setLong(user.getId())
				.run(UnapprovedUser.PASSWORD)
				.getString();
	}

	@Override
	protected DatabaseObjectParser getParser()
	{
		return UnapprovedUser.Parser.Instance.getInstance();
	}

	private static class Inst
	{
		private static final UnapprovedUserManager INSTANCE = new UnapprovedUserManager();
	}
}

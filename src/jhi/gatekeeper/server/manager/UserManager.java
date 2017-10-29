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
import jhi.gatekeeper.server.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link UserManager} is a utility class that allows interactions with Gatekeeper.
 *
 * @author Sebastian Raubach
 */
public class UserManager extends AbstractManager
{
	private static final String USER_TYPE_ADMIN = "Administrator";

	private static final String QUERY_ID_FROM_USERNAME              = "SELECT users.* FROM users WHERE users.username = ?";
	private static final String QUERY_ID_FROM_USERNAME_AND_PASSWORD = "SELECT users.* FROM users WHERE users.username = ? AND users.email_address = ?";
	private static final String QUERY_USER_IS_ADMIN                 = "SELECT users.*, database_systems.*, user_types.* FROM users LEFT JOIN user_has_access_to_databases ON users.id = user_has_access_to_databases.user_id LEFT JOIN database_systems ON database_systems.id = user_has_access_to_databases.database_id LEFT JOIN user_types ON user_types.id = user_has_access_to_databases.user_type_id WHERE users.id = ? AND system_name = \"gatekeeper\" AND user_types.description = \"" + USER_TYPE_ADMIN + "\"";
	private static final String QUERY_BY_ID                         = "SELECT * FROM users WHERE id = ?";
	private static final String QUERY_COUNT_FOR_SEARCH              = "SELECT COUNT(1) AS count FROM users %s";
	private static final String QUERY_USER_LIST_DATA_FOR_SEARCH     = "SELECT * FROM users %s ORDER BY username LIMIT ?, ?";
	private static final String QUERY_USERNAME_EXISTS               = "SELECT COUNT(1) AS count FROM (SELECT users.id FROM users WHERE username = ? UNION SELECT unapproved_users.id FROM unapproved_users WHERE user_username = ? AND has_been_rejected = 0) x";

	private static final String QUERY_ADMIN_EXISTS = "SELECT COUNT(1) AS count FROM users LEFT JOIN user_has_access_to_databases ON users.id = user_has_access_to_databases.user_id LEFT JOIN user_types ON user_types.id = user_has_access_to_databases.user_type_id LEFT JOIN database_systems ON database_systems.id = user_has_access_to_databases.database_id WHERE database_systems.system_name = \"gatekeeper\" AND user_types.description = \"Administrator\"";

	private static final String UPDATE_PASSWORD                 = "UPDATE users SET password = ? WHERE id = ?";
	private static final String UPDATE_EMAIL                    = "UPDATE users SET email_address = ? WHERE id = ?";
	private static final String UPDATE_HAS_ACCESS_TO_GATEKEEPER = "UPDATE users SET has_access_to_gatekeeper = ? WHERE id = ?";

	private static final String DELETE_BY_ID = "DELETE FROM users WHERE id = ?";


	private static final String INSERT = "INSERT INTO users (username, password, full_name, email_address, created_on, institution_id, has_access_to_gatekeeper) VALUES (?, ?, ?, ?, NOW(), ?, 1)";

	public static UserManager get()
	{
		return UserManager.Inst.INSTANCE;
	}

	/**
	 * Creates the initial admin account with the given {@link User} and {@link UserCredentials}.
	 *
	 * @param user        The {@link User} to set as admin
	 * @param credentials The {@link UserCredentials} used to sign up
	 * @throws UserExistsException Thrown if the user already exists
	 * @throws DatabaseException   Thrown if the interaction with the database fails
	 */
	public static void createAdmin(User user, UserCredentials credentials) throws UserExistsException, DatabaseException
	{
		if (doesAdminAccountExist())
			throw new UserExistsException();

		add(user, credentials);

		PaginatedResult<List<DatabaseSystem>> systems = DatabaseSystemManager.getList(Pagination.DEFAULT);

		// Make sure that the initial Gatekeeper database exists.
		setGatekeeperPermission(user, UserType.ADMINISTRATOR);

		// Then give the admin user permissions to all the systems.
		if (!CollectionUtils.isEmpty(systems.getResult()))
		{
			for (DatabaseSystem system : systems.getResult())
			{
				DatabasePermission permission = new DatabasePermission(user, system, UserType.ADMINISTRATOR);
				DatabasePermissionManager.grantPermission(permission);
			}
		}
	}

	public static void setGatekeeperPermission(User user, UserType type) throws DatabaseException
	{
		DatabaseSystem gatekeeper = new DatabaseSystem(-1L)
				.setSystemName("gatekeeper")
				.setServerName("--")
				.setDescription("Gatekeeper Database");
		DatabaseSystemManager.ensureExists(gatekeeper);

		DatabasePermission permission = new DatabasePermission(user, gatekeeper, type);
		DatabasePermissionManager.grantPermission(permission);
	}

	/**
	 * Checks if the admin account already exists
	 *
	 * @return <code>true</code> if the admin account already exists, <code>false</code> otherwise
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static boolean doesAdminAccountExist() throws DatabaseException
	{
		return new ValueQuery(QUERY_ADMIN_EXISTS)
				.run(COUNT)
				.getLong() > 0;
	}

	/**
	 * Returns the {@link UserInternal} with the given username.
	 *
	 * @param username The username of the {@link UserInternal}
	 * @return The {@link UserInternal} with the given username.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static UserInternal getUserByUsername(String username) throws DatabaseException
	{
		return new DatabaseObjectQuery<UserInternal>(QUERY_ID_FROM_USERNAME)
				.setString(username)
				.run()
				.getObject(UserInternal.Parser.Instance.getInstance());
	}

	/**
	 * Returns the {@link UserInternal} based on username and password.
	 *
	 * @param username The username of the {@link UserInternal}
	 * @param email    The email address of the {@link UserInternal}
	 * @return The {@link UserInternal} based on username and password
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static UserInternal getUserByUsernameAndEmail(String username, String email) throws DatabaseException
	{
		return new DatabaseObjectQuery<UserInternal>(QUERY_ID_FROM_USERNAME_AND_PASSWORD)
				.setString(username)
				.setString(email)
				.run()
				.getObject(UserInternal.Parser.Instance.getInstance());
	}

	/**
	 * Checks of the given {@link User} with the given id is an admin.
	 *
	 * @param id The id of the {@link User}
	 * @return <code>true</code> if the {@link User} with the given id is an admin, <code>false</code> otherwise
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static boolean isUserAdmin(Long id) throws DatabaseException
	{
		List<Integer> ids = new ValueQuery(QUERY_USER_IS_ADMIN)
				.setLong(id)
				.run(User.ID)
				.getInts();

		return !CollectionUtils.isEmpty(ids);
	}

	/**
	 * Updates the password of the given {@link UserInternal} with the given password.
	 *
	 * @param userDetails The {@link UserInternal} to update
	 * @param password    The new password to set
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void updatePassword(UserInternal userDetails, String password) throws DatabaseException
	{
		Integer rounds = PropertyReader.getPropertyInteger(PropertyReader.GATEKEEPER_BCRYPT_ROUNDS, BCrypt.GENSALT_DEFAULT_LOG2_ROUNDS);
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt(rounds));

		new ValueQuery(UPDATE_PASSWORD)
				.setString(hashed)
				.setLong(userDetails.getId())
				.execute();
	}

	/**
	 * Updates the email address of the given {@link UserAuthentication} with the given email address.
	 *
	 * @param user  The {@link UserAuthentication} representing the user
	 * @param email The new email address to set
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void updateEmail(UserAuthentication user, String email) throws DatabaseException
	{
		new ValueQuery(UPDATE_EMAIL)
				.setString(email)
				.setLong(user.getId())
				.execute();

		user.setEmailAddress(email);
	}

	/**
	 * Returns the {@link User} with the given id.
	 *
	 * @param id The id of the {@link User}
	 * @return The {@link User} with the given id
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static User getById(Long id) throws DatabaseException
	{
		if (id == null)
			return null;

		return new DatabaseObjectQuery<User>(QUERY_BY_ID)
				.setLong(id)
				.run()
				.getObject(User.Parser.Instance.getInstance());
	}

	/**
	 * Returns the number of {@link User}.
	 *
	 * @return The number of {@link User}
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static long getCount(User searchBean) throws DatabaseException
	{
		return User.getCountRestrictions(QUERY_COUNT_FOR_SEARCH, searchBean)
				   .run(COUNT)
				   .getLong();
	}

	/**
	 * Deletes the {@link User} with the given id from the database.
	 *
	 * @param id The id of the {@link User} to delete
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void deleteById(long id) throws DatabaseException
	{
		new ValueQuery(DELETE_BY_ID)
				.setLong(id)
				.execute();
	}

	/**
	 * Returns a {@link PaginatedResult} of {@link User}s.
	 *
	 * @param pagination The {@link Pagination} object specifying the chunk of data to get
	 * @return A {@link PaginatedResult} of {@link User}s
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static PaginatedResult<List<User>> getList(User searchBean, Pagination pagination) throws DatabaseException
	{
		return User.getListRestrictions(QUERY_USER_LIST_DATA_FOR_SEARCH, searchBean, pagination)
				   .setInt(pagination.getStart())
				   .setInt(pagination.getSize())
				   .run()
				   .getObjectsPaginated(User.Parser.Instance.getInstance(), false);
	}

	/**
	 * Adds the given {@link User} with the given {@link UserCredentials} to the database.
	 *
	 * @param user        The {@link User} to add
	 * @param credentials The {@link UserCredentials} to use
	 * @return The ids of the newly inserted database items
	 * @throws DatabaseException   Thrown if the interaction with the database fails
	 * @throws UserExistsException Thrown if the user already exists
	 */
	public static void add(User user, UserCredentials credentials) throws DatabaseException, UserExistsException
	{
		long count = new ValueQuery(QUERY_USERNAME_EXISTS)
				.setString(user.getUsername())
				.setString(user.getUsername())
				.run(COUNT)
				.getLong();

		if (count > 0)
			throw new UserExistsException();

		InstitutionManager.ensureExists(user.getInstitution());

		String hashed = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt(PropertyReader.getPropertyInteger(PropertyReader.GATEKEEPER_BCRYPT_ROUNDS, BCrypt.GENSALT_DEFAULT_LOG2_ROUNDS)));

		List<Long> ids = new ValueQuery(INSERT)
				.setString(user.getUsername())
				.setString(hashed)
				.setString(user.getFullName())
				.setString(user.getEmail())
				.setLong(user.getInstitution().getId())
				.execute();

		if (!CollectionUtils.isEmpty(ids))
			user.setId(ids.get(0));
	}

	/**
	 * Activates the user account based on the given activation key
	 *
	 * @param key The activation key
	 * @return The newly created {@link User} object
	 * @throws DatabaseException             Thrown if the interaction with the database fails
	 * @throws InvalidActivationKeyException Thrown if the given activation key is invalid
	 * @throws SuspendedUserException        Thrown if the user has been suspended
	 * @throws UserNotFoundException         Thrown if the user cannot be found
	 */
	public static User activateUser(String key) throws DatabaseException, InvalidActivationKeyException, SuspendedUserException, UserNotFoundException
	{
		UnapprovedUser user = UnapprovedUserManager.getByActivationKey(key);

		User u = activateUser(user);

		DatabasePermission permission = new DatabasePermission(u, user.getDatabaseSystem(), UserType.REGULAR_USER);
		DatabasePermissionManager.grantPermission(permission);

		UnapprovedUserManager.delete(user);

		return u;
	}

	/**
	 * Activate user based on the given {@link UnapprovedUser}.
	 *
	 * @param user The {@link UnapprovedUser} to approve
	 * @return The newly created {@link User} object
	 * @throws DatabaseException             Thrown if the interaction with the database fails
	 * @throws InvalidActivationKeyException Thrown if the given activation key is invalid
	 * @throws SuspendedUserException        Thrown if the user has been suspended
	 * @throws UserNotFoundException         Thrown if the user cannot be found
	 */
	public static User activateUser(UnapprovedUser user) throws DatabaseException, InvalidActivationKeyException, SuspendedUserException, UserNotFoundException
	{
		if (user == null)
		{
			throw new InvalidActivationKeyException();
		}
		else if (user.hasBeenRejected())
		{
			throw new SuspendedUserException();
		}
		else
		{
			String password = UnapprovedUserManager.getPassword(user);

			InstitutionManager.ensureExists(user.getInstitution());

			List<Long> ids = new ValueQuery(INSERT)
					.setString(user.getUsername())
					.setString(password) // No need to hash here, it already is
					.setString(user.getFullName())
					.setString(user.getEmail())
					.setLong(user.getInstitution().getId())
					.execute();

			if (!CollectionUtils.isEmpty(ids))
			{
				UnapprovedUserManager.delete(user);

				/* After that, return the user with the inserted id */
				return getById(ids.get(0));
			}
			else
			{
				throw new UserNotFoundException();
			}
		}
	}

	/**
	 * Updates the gatekeeper access status for the given {@link User}.
	 *
	 * @param user The {@link User} to update
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void setHasAccessToGatekeeper(User user) throws DatabaseException
	{
		new ValueQuery(UPDATE_HAS_ACCESS_TO_GATEKEEPER)
				.setBoolean(user.isHasAccessToGatekeeper())
				.setLong(user.getId())
				.execute();
	}

	@Override
	protected DatabaseObjectParser getParser()
	{
		return User.Parser.Instance.getInstance();
	}

	private static class Inst
	{
		private static final UserManager INSTANCE = new UserManager();
	}
}

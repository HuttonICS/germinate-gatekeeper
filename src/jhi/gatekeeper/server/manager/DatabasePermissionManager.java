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

package jhi.gatekeeper.server.manager;

import java.util.*;

import jhi.database.server.parser.*;
import jhi.database.server.query.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class DatabasePermissionManager extends AbstractManager
{
	private static final String QUERY_BY_USER_ID = "SELECT * FROM user_has_access_to_databases WHERE user_id = ? LIMIT ?, ?";

	private static final String QUERY_BY_SYSTEM_ID = "SELECT * FROM user_has_access_to_databases WHERE database_id = ? LIMIT ?, ?";

	private static final String UPDATE_PERMISSION = "UPDATE user_has_access_to_databases SET user_type_id = ? WHERE user_id = ? AND database_id = ?";

	private static final String INSERT_PERMISSION = "INSERT INTO user_has_access_to_databases (user_id, database_id, user_type_id) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE user_type_id = VALUES(user_type_id)";

	private static final String DELETE_PERMISSION = "DELETE FROM user_has_access_to_databases WHERE user_id = ? AND database_id = ? and user_type_id = ?";

	public static DatabasePermissionManager get()
	{
		return DatabasePermissionManager.Inst.INSTANCE;
	}

	/**
	 * Returns a {@link PaginatedResult} of {@link DatabasePermission}s for the given {@link User} id.
	 *
	 * @param id         The id of the {@link User}
	 * @param pagination The {@link Pagination} object specifying the chunk of data to get
	 * @return A {@link PaginatedResult} of {@link DatabasePermission}s for the given {@link User} id.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static PaginatedResult<List<DatabasePermission>> getByUserId(Long id, Pagination pagination) throws DatabaseException
	{
		if (id == null)
			return null;

		return new DatabaseObjectQuery<DatabasePermission>(QUERY_BY_USER_ID)
				.setFetchesCount(pagination.getResultSize())
				.setLong(id)
				.setInt(pagination.getStart())
				.setInt(pagination.getSize())
				.run()
				.getObjectsPaginated(DatabasePermission.Parser.Instance.getInstance(), false);
	}

	/**
	 * Returns a {@link PaginatedResult} of {@link DatabasePermission}s for the given {@link DatabaseSystem} id.
	 *
	 * @param id         The id of the {@link DatabaseSystem}
	 * @param pagination The {@link Pagination} object specifying the chunk of data to get
	 * @return A {@link PaginatedResult} of {@link DatabasePermission}s for the given {@link DatabaseSystem} id.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static PaginatedResult<List<DatabasePermission>> getBySystemId(Long id, Pagination pagination) throws DatabaseException
	{
		if (id == null)
			return null;

		return new DatabaseObjectQuery<DatabasePermission>(QUERY_BY_SYSTEM_ID)
				.setFetchesCount(pagination.getResultSize())
				.setLong(id)
				.setInt(pagination.getStart())
				.setInt(pagination.getSize())
				.run()
				.getObjectsPaginated(DatabasePermission.Parser.Instance.getInstance(), false);
	}

	/**
	 * Grants the given permission. Will update it if exists.
	 *
	 * @param permission The {@link DatabasePermission} to grant.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void grantPermission(DatabasePermission permission) throws DatabaseException
	{
		DatabaseSystemManager.ensureExists(permission.getDatabaseSystem());

		String query = DatabaseObject.hasId(permission) ? UPDATE_PERMISSION : INSERT_PERMISSION;

		new ValueQuery(query)
				.setLong(permission.getUser().getId())
				.setLong(permission.getDatabaseSystem().getId())
				.setLong(permission.getUserType().getId())
				.execute();
	}

	/**
	 * Revokes the given {@link DatabasePermission}
	 *
	 * @param permission The {@link DatabasePermission} to revoke.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void revokePermission(DatabasePermission permission) throws DatabaseException
	{
		new ValueQuery(DELETE_PERMISSION)
				.setLong(permission.getUser().getId())
				.setLong(permission.getDatabaseSystem().getId())
				.setLong(permission.getUserType().getId())
				.execute();
	}

	@Override
	protected DatabaseObjectParser getParser()
	{
		return DatabasePermission.Parser.Instance.getInstance();
	}

	private static class Inst
	{
		private static final DatabasePermissionManager INSTANCE = new DatabasePermissionManager();
	}
}

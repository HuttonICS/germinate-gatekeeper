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
public class DatabaseSystemManager extends AbstractManager
{
	private static final String QUERY_ALL   = "SELECT * FROM database_systems LIMIT ?, ?";
	private static final String QUERY_BY_ID = "SELECT * FROM database_systems WHERE id = ?";
	private static final String QUERY_COUNT = "SELECT COUNT(1) AS count FROM database_systems";

	private static final String INSERT_WHERE_NOT_EXISTS = "INSERT INTO database_systems (system_name, server_name, description) SELECT %s, %s, %s FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM database_systems WHERE system_name = ? AND server_name = ?)";

	private static final String DELETE = "DELETE FROM database_systems WHERE id = ?";

	public static DatabaseSystemManager get()
	{
		return DatabaseSystemManager.Inst.INSTANCE;
	}

	/**
	 * Returns the {@link DatabaseSystem} with the given id.
	 *
	 * @param id The id of the {@link DatabaseSystem} to get
	 * @return The {@link DatabaseSystem} with the given id.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static DatabaseSystem getById(Long id) throws DatabaseException
	{
		if (id == null)
			return null;

		return new DatabaseObjectQuery<DatabaseSystem>(QUERY_BY_ID)
				.setLong(id)
				.run()
				.getObject(DatabaseSystem.Parser.Instance.getInstance());
	}

	/**
	 * Returns the number of {@link DatabaseSystem}.
	 *
	 * @return The number of {@link DatabaseSystem}
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static long getCount() throws DatabaseException
	{
		return new ValueQuery(QUERY_COUNT)
				.run(COUNT)
				.getLong();
	}

	/**
	 * Returns a {@link PaginatedResult} of {@link DatabaseSystem}s.
	 *
	 * @param pagination The {@link Pagination} object specifying the chunk of data to get
	 * @return A {@link PaginatedResult} of {@link DatabaseSystem}s
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static PaginatedResult<List<DatabaseSystem>> getList(Pagination pagination) throws DatabaseException
	{
		return new DatabaseObjectQuery<DatabaseSystem>(QUERY_ALL)
				.setFetchesCount(pagination.getResultSize())
				.setInt(pagination.getStart())
				.setInt(pagination.getSize())
				.run()
				.getObjectsPaginated(DatabaseSystem.Parser.Instance.getInstance(), false);
	}

	private static String getQuoted(String input)
	{
		if (StringUtils.isEmpty(input))
			return "NULL";
		else
			return "\"" + input + "\"";
	}

	/**
	 * Ensures the given {@link DatabaseSystem} exists.
	 *
	 * @param databaseSystem The {@link DatabaseSystem} to check
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void ensureExists(DatabaseSystem databaseSystem) throws DatabaseException
	{
		if(!DatabaseObject.hasId(databaseSystem))
		{
			String formatted = String.format(INSERT_WHERE_NOT_EXISTS, getQuoted(databaseSystem.getSystemName()), getQuoted(databaseSystem.getServerName()), getQuoted(databaseSystem.getDescription()));

			ValueQuery query = new ValueQuery(formatted)
					.setString(databaseSystem.getSystemName())
					.setString(databaseSystem.getServerName());

			List<Long> generatedKeys = query.execute();

			if (!CollectionUtils.isEmpty(generatedKeys))
				databaseSystem.setId(generatedKeys.get(0));
		}
	}

	/**
	 * Deletes the given {@link DatabaseSystem} from the database.
	 *
	 * @param databaseSystem The {@link DatabaseSystem} to delete.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void delete(DatabaseSystem databaseSystem) throws DatabaseException
	{
		new ValueQuery(DELETE)
				.setLong(databaseSystem.getId())
				.execute();
	}

	/**
	 * Adds a new {@link DatabaseSystem} to the database.
	 *
	 * @param databaseSystem The {@link DatabaseSystem} to add
	 * @return <code>true</code> if the item has been added.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static boolean add(DatabaseSystem databaseSystem) throws DatabaseException
	{
		ensureExists(databaseSystem);
		return databaseSystem.getId() != null;
	}

	@Override
	protected DatabaseObjectParser getParser()
	{
		return DatabaseSystem.Parser.Instance.getInstance();
	}

	private static class Inst
	{
		private static final DatabaseSystemManager INSTANCE = new DatabaseSystemManager();
	}
}

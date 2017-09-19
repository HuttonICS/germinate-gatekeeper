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
public class InstitutionManager extends AbstractManager
{
	public static final String ID      = "id";
	public static final String NAME    = "name";
	public static final String ACRONY  = "acronym";
	public static final String ADDRESS = "address";

	private static final String QUERY_BY_ID = "SELECT * FROM institutions WHERE id = ?";
	private static final String QUERY_ALL   = "SELECT * FROM institutions ORDER BY name LIMIT ?, ?";
	private static final String QUERY_COUNT = "SELECT COUNT(1) FROM institutions";

	private static final String INSERT = "INSERT INTO institutions (name, acronym, address) VALUES (?, ?, ?)";

	public static InstitutionManager get()
	{
		return InstitutionManager.Inst.INSTANCE;
	}

	/**
	 * Returns the {@link Institution} with the given id.
	 *
	 * @param id The id of the {@link Institution}
	 * @return The {@link Institution} with the given id
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static Institution getById(Long id) throws DatabaseException
	{
		if (id == null)
			return null;

		return new DatabaseObjectQuery<Institution>(QUERY_BY_ID)
				.setLong(id)
				.run()
				.getObject(Institution.Parser.Instance.getInstance());
	}

	/**
	 * Returns a {@link PaginatedResult} of {@link Institution}s.
	 *
	 * @param pagination The {@link Pagination} object specifying the chunk of data to get
	 * @return A {@link PaginatedResult} of {@link Institution}s
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static PaginatedResult<List<Institution>> getList(Pagination pagination) throws DatabaseException
	{
		return new DatabaseObjectQuery<Institution>(QUERY_ALL)
				.setInt(pagination.getStart())
				.setInt(pagination.getSize())
				.run()
				.getObjectsPaginated(Institution.Parser.Instance.getInstance(), true);
	}

	/**
	 * Returns the number of {@link Institution}.
	 *
	 * @return The number of {@link Institution}
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static long getCount() throws DatabaseException
	{
		return new ValueQuery(QUERY_COUNT)
				.run(COUNT)
				.getLong();
	}

	/**
	 * Ensures the given {@link Institution} exists.
	 *
	 * @param institution The {@link Institution} to check
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void ensureExists(Institution institution) throws DatabaseException
	{
		if (!DatabaseObject.hasId(institution))
		{
			List<Long> generatedKeys = new ValueQuery(INSERT)
					.setString(institution.getName())
					.setString(institution.getAcronym())
					.setString(institution.getAddress())
					.execute();

			if (!CollectionUtils.isEmpty(generatedKeys))
				institution.setId(generatedKeys.get(0));
		}
	}

	@Override
	protected DatabaseObjectParser getParser()
	{
		return Institution.Parser.Instance.getInstance();
	}

	private static class Inst
	{
		private static final InstitutionManager INSTANCE = new InstitutionManager();
	}
}

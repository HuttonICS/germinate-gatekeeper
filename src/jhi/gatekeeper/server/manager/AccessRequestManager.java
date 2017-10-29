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

/**
 * {@link AccessRequestManager} is a utility class that allows interactions with Gatekeeper <p> Every method in this class checks  before actually
 * doing anything. If the property is set to <code>false</code>, the method will either return <code>null</code> (if a return type is required) or
 * simply return (<code>void</code> methods).
 *
 * @author Sebastian Raubach
 */
public class AccessRequestManager extends AbstractManager
{
	private static final String QUERY_COUNT              = "SELECT COUNT(1) AS count FROM access_requests WHERE has_been_rejected = 0";
	private static final String QUERY_LIST_DATA          = "SELECT * FROM access_requests WHERE has_been_rejected = 0 ORDER BY user_id ASC LIMIT ?, ?";
	private static final String SELECT_BY_ACTIVATION_KEY = "SELECT * FROM access_requests WHERE activation_key = ?";
	private static final String SELECT_BY_ID             = "SELECT * FROM access_requests WHERE id = ?";

	private static final String DELETE = "DELETE FROM access_requests WHERE id = ?";

	private static final String UPDATE_REJECT = "UPDATE access_requests SET has_been_rejected = 1 WHERE id = ?";

	public static AccessRequestManager get()
	{
		return Inst.INSTANCE;
	}

	/**
	 * Returns the {@link AccessRequest} with the given id.
	 *
	 * @param id The id of the {@link AccessRequest}
	 * @return The {@link AccessRequest} with the given id
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static AccessRequest getById(Long id) throws DatabaseException
	{
		if (id == null)
			return null;

		return new DatabaseObjectQuery<AccessRequest>(SELECT_BY_ID)
				.setLong(id)
				.run()
				.getObject(AccessRequest.Parser.Instance.getInstance());
	}

	/**
	 * Returns the number of {@link AccessRequest}.
	 *
	 * @return The number of {@link AccessRequest}
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static long getCount() throws DatabaseException
	{
		return new ValueQuery(QUERY_COUNT)
				.run(COUNT)
				.getLong();
	}

	/**
	 * Returns a {@link PaginatedResult} of {@link AccessRequest}s.
	 *
	 * @param pagination The {@link Pagination} object specifying the chunk of data to get
	 * @return A {@link PaginatedResult} of {@link AccessRequest}s
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static PaginatedResult<List<AccessRequest>> getList(Pagination pagination) throws DatabaseException
	{
		return new DatabaseObjectQuery<AccessRequest>(QUERY_LIST_DATA)
				.setFetchesCount(pagination.getResultSize())
				.setInt(pagination.getStart())
				.setInt(pagination.getSize())
				.run()
				.getObjectsPaginated(AccessRequest.Parser.Instance.getInstance(), false);
	}

	/**
	 * Returns the {@link AccessRequest} by its activation key.
	 *
	 * @param activationKey The activation key for this {@link AccessRequest}
	 * @return The {@link AccessRequest} by its activation key
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static AccessRequest getByActivationKey(String activationKey) throws DatabaseException
	{
		return new DatabaseObjectQuery<AccessRequest>(SELECT_BY_ACTIVATION_KEY)
				.setString(activationKey)
				.run()
				.getObject(AccessRequest.Parser.Instance.getInstance());
	}

	/**
	 * Removes the given {@link AccessRequest} from the database.
	 *
	 * @param request The {@link AccessRequest} to remove
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void delete(AccessRequest request) throws DatabaseException
	{
		new ValueQuery(DELETE)
				.setLong(request.getId())
				.execute();
	}

	/**
	 * Approves the given {@link AccessRequest}. Removes it after successful approval.
	 *
	 * @param request The {@link AccessRequest} to approve
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void approve(AccessRequest request) throws DatabaseException
	{
		DatabasePermission permission = new DatabasePermission(request.getUser(), request.getDatabaseSystem(), UserType.REGULAR_USER);

		DatabasePermissionManager.grantPermission(permission);

		delete(request);
	}

	/**
	 * Rejects the given {@link AccessRequest}.
	 *
	 * @param request
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void reject(AccessRequest request) throws DatabaseException
	{
		new ValueQuery(UPDATE_REJECT)
				.setLong(request.getId())
				.execute();
	}

	@Override
	protected DatabaseObjectParser getParser()
	{
		return AccessRequest.Parser.Instance.getInstance();
	}

	private static class Inst
	{
		private static final AccessRequestManager INSTANCE = new AccessRequestManager();
	}
}

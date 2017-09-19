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

package jhi.gatekeeper.shared.bean;

import com.google.gwt.core.shared.*;

import java.util.*;

import jhi.database.server.*;
import jhi.database.server.parser.*;
import jhi.database.shared.exception.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.server.util.*;

/**
 * @author Sebastian Raubach
 */
public class UserInternal extends User
{
	private String password;

	public UserInternal()
	{
	}

	public UserInternal(Long id)
	{
		super(id);
	}

	public UserInternal(Long id, String username, String fullName, String email, Institution institution, Date creationDate, boolean hasAccessToGatekeeper, boolean isGatekeeperAdmin, String password)
	{
		super(id, username, fullName, email, institution, creationDate, hasAccessToGatekeeper, isGatekeeperAdmin);
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}

	public UserInternal setPassword(String password)
	{
		this.password = password;
		return this;
	}

	@GwtIncompatible
	public static class Parser extends DatabaseObjectParser<UserInternal>
	{
		private static final GatekeeperDatabaseObjectCache<Institution> INSTITUTION_CACHE = new GatekeeperDatabaseObjectCache<>(Institution.class);

		private Parser()
		{
			registerCache(INSTITUTION_CACHE);
		}

		@Override
		public UserInternal parse(DatabaseResult row, boolean foreignsFromResultSet) throws DatabaseException
		{
			Long id = row.getLong(ID);

			if (id == null)
				return null;
			else
			{
				UserInternal user = new UserInternal(id);

				user.setPassword(row.getString(PASSWORD))
					.setUsername(row.getString(USERNAME))
					.setFullName(row.getString(FULL_NAME))
					.setEmail(row.getString(EMAIL_ADDRESS))
					.setCreationDate(row.getTimestamp(CREATED_ON))
					.setInstitution(INSTITUTION_CACHE.get(row.getLong(INSTITUTION_ID), row, foreignsFromResultSet))
					.setHasAccessToGatekeeper(row.getBoolean(HAS_ACCESS_TO_GATEKEEPER))
					.setIsGatekeeperAdmin(UserManager.isUserAdmin(row.getLong(ID)));

				return user;
			}
		}

		public static final class Instance
		{
			public static Parser getInstance()
			{
				return InstanceHolder.INSTANCE;
			}

			/**
			 * {@link InstanceHolder} is loaded on the first execution of {@link Instance#getInstance()} or the first access to {@link
			 * InstanceHolder#INSTANCE}, not before.
			 * <p/>
			 * This solution (<a href= "http://en.wikipedia.org/wiki/Initialization_on_demand_holder_idiom" >Initialization-on-demand holder
			 * idiom</a>) is thread-safe without requiring special language constructs (i.e. <code>volatile</code> or <code>synchronized</code>).
			 *
			 * @author Sebastian Raubach
			 */
			private static final class InstanceHolder
			{
				private static final Parser INSTANCE = new Parser();
			}
		}
	}
}

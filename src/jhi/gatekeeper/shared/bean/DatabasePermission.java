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

package jhi.gatekeeper.shared.bean;

import com.google.gwt.core.shared.*;

import jhi.database.server.*;
import jhi.database.server.parser.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.server.util.*;

/**
 * @author Sebastian Raubach
 */
public class DatabasePermission extends DatabaseObject
{
	public static final String USER_ID      = "user_id";
	public static final String DATABASE_ID  = "database_id";
	public static final String USER_TYPE_ID = "user_type_id";

	private User           user;
	private DatabaseSystem databaseSystem;
	private UserType       userType;

	public DatabasePermission()
	{

	}

	public DatabasePermission(User user, DatabaseSystem databaseSystem, UserType userType)
	{
		this.user = user;
		this.databaseSystem = databaseSystem;
		this.userType = userType;
	}

	public User getUser()
	{
		return user;
	}

	public DatabasePermission setUser(User user)
	{
		this.user = user;
		return this;
	}

	public DatabaseSystem getDatabaseSystem()
	{
		return databaseSystem;
	}

	public DatabasePermission setDatabaseSystem(DatabaseSystem databaseSystem)
	{
		this.databaseSystem = databaseSystem;
		return this;
	}

	public UserType getUserType()
	{
		return userType;
	}

	public DatabasePermission setUserType(UserType userType)
	{
		this.userType = userType;
		return this;
	}

	@Override
	public String toString()
	{
		return "DatabasePermission{" +
				"user=" + user +
				", databaseSystem=" + databaseSystem +
				", userType=" + userType +
				'}';
	}

	@GwtIncompatible
	public static class Parser extends DatabaseObjectParser<DatabasePermission>
	{
		private static GatekeeperDatabaseObjectCache<User>           USER_CACHE            = new GatekeeperDatabaseObjectCache<>(User.class);
		private static GatekeeperDatabaseObjectCache<DatabaseSystem> DATABASE_SYSTEM_CACHE = new GatekeeperDatabaseObjectCache<>(DatabaseSystem.class);
		private Parser()
		{
			registerCache(USER_CACHE);
			registerCache(DATABASE_SYSTEM_CACHE);
		}

		@Override
		public DatabasePermission parse(DatabaseResult row, boolean foreignsFromResultSet) throws DatabaseException
		{
			return new DatabasePermission()
					.setUser(USER_CACHE.get(row.getLong(USER_ID), row, foreignsFromResultSet))
					.setUserType(UserType.getById(row.getLong(USER_TYPE_ID)))
					.setDatabaseSystem(DATABASE_SYSTEM_CACHE.get(row.getLong(DATABASE_ID), row, foreignsFromResultSet));
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

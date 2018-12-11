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

package jhi.gatekeeper.shared.bean;

import com.google.gwt.core.shared.*;

import java.util.*;

import jhi.database.server.*;
import jhi.database.server.parser.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.server.util.*;

/**
 * @author Sebastian Raubach
 */
public class AccessRequest extends DatabaseObject
{
	public static final String ID                 = "id";
	public static final String USER_ID            = "user_id";
	public static final String DATABASE_SYSTEM_ID = "database_system_id";
	public static final String HAS_BEEN_REJECTED  = "has_been_rejected";
	public static final String ACTIVATION_KEY     = "activation_key";
	public static final String NEEDS_APPROVAL     = "needs_approval";
	public static final String CREATED_ON         = "created_on";

	private User           user;
	private DatabaseSystem databaseSystem;
	private boolean        hasBeenRejected;
	private String         activationKey;
	private boolean        needsApproval;
	private Date           createdOn;

	public AccessRequest()
	{
		super();
	}

	public AccessRequest(Long id)
	{
		super(id);
	}

	public AccessRequest(Long id, User user, DatabaseSystem databaseSystem, boolean hasBeenRejected, String activationKey, boolean needsApproval, Date createdOn)
	{
		super(id);
		this.user = user;
		this.databaseSystem = databaseSystem;
		this.hasBeenRejected = hasBeenRejected;
		this.activationKey = activationKey;
		this.needsApproval = needsApproval;
		this.createdOn = createdOn;
	}

	public User getUser()
	{
		return user;
	}

	public AccessRequest setUser(User user)
	{
		this.user = user;
		return this;
	}

	public DatabaseSystem getDatabaseSystem()
	{
		return databaseSystem;
	}

	public AccessRequest setDatabaseSystem(DatabaseSystem databaseSystem)
	{
		this.databaseSystem = databaseSystem;
		return this;
	}

	public boolean isHasBeenRejected()
	{
		return hasBeenRejected;
	}

	public AccessRequest setHasBeenRejected(boolean hasBeenRejected)
	{
		this.hasBeenRejected = hasBeenRejected;
		return this;
	}

	public String getActivationKey()
	{
		return activationKey;
	}

	public AccessRequest setActivationKey(String activationKey)
	{
		this.activationKey = activationKey;
		return this;
	}

	public boolean isNeedsApproval()
	{
		return needsApproval;
	}

	public AccessRequest setNeedsApproval(boolean needsApproval)
	{
		this.needsApproval = needsApproval;
		return this;
	}

	public Date getCreatedOn()
	{
		return createdOn;
	}

	public AccessRequest setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
		return this;
	}

	@Override
	public String toString()
	{
		return "AccessRequest{" +
			"user=" + user +
			", databaseSystem=" + databaseSystem +
			", hasBeenRejected=" + hasBeenRejected +
			", activationKey='" + activationKey + '\'' +
			", needsApproval=" + needsApproval +
			", createdOn=" + createdOn +
			"} " + super.toString();
	}

	@GwtIncompatible
	public static class Parser extends DatabaseObjectParser<AccessRequest>
	{
		private static final GatekeeperDatabaseObjectCache<User>           USER_CACHE            = new GatekeeperDatabaseObjectCache<>(User.class);
		private static final GatekeeperDatabaseObjectCache<DatabaseSystem> DATABASE_SYSTEM_CACHE = new GatekeeperDatabaseObjectCache<>(DatabaseSystem.class);

		private Parser()
		{
			registerCache(USER_CACHE);
			registerCache(DATABASE_SYSTEM_CACHE);
		}

		@Override
		public AccessRequest parse(DatabaseResult row, boolean foreignsFromResultSet)
			throws DatabaseException
		{
			Long id = row.getLong(ID);

			if (id == null)
				return null;
			else
				return new AccessRequest(id)
					.setUser(USER_CACHE.get(row.getLong(USER_ID), row, foreignsFromResultSet))
					.setDatabaseSystem(DATABASE_SYSTEM_CACHE.get(row.getLong(DATABASE_SYSTEM_ID), row, foreignsFromResultSet))
					.setHasBeenRejected(row.getBoolean(HAS_BEEN_REJECTED))
					.setActivationKey(row.getString(ACTIVATION_KEY))
					.setNeedsApproval(row.getBoolean(NEEDS_APPROVAL))
					.setCreatedOn(row.getTimestamp(CREATED_ON));
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

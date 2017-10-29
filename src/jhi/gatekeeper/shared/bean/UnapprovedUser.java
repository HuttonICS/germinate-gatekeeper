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
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.server.util.*;

/**
 * @author Sebastian Raubach
 */
public class UnapprovedUser extends User
{
	public static final String ID                  = "id";
	public static final String USERNAME            = "user_username";
	public static final String PASSWORD            = "user_password";
	public static final String FULL_NAME           = "user_full_name";
	public static final String EMAIL_ADDRESS       = "user_email_address";
	public static final String INSTITUTION_ID      = "institution_id";
	public static final String INSTITUTION_NAME    = "institution_name";
	public static final String INSTITUTION_ACRONYM = "institution_acronym";
	public static final String INSTITUTION_ADDRESS = "institution_address";
	public static final String DATABASE_SYSTEM_ID  = "database_system_id";
	public static final String HAS_BEEN_REJECTED   = "has_been_rejected";
	public static final String ACTIVATION_KEY      = "activation_key";

	private DatabaseSystem databaseSystem;
	private boolean        hasBeenRejected;
	private String         activationKey;

	public UnapprovedUser()
	{
	}

	public UnapprovedUser(Long id)
	{
		super(id);
	}

	public UnapprovedUser(Long id, String username, String fullName, String email, DatabaseSystem databaseSystem, Institution institution, Date creationDate, boolean hasAccessToGatekeeper, boolean isGatekeeperAdmin, boolean hasBeenRejected, String activationKey)
	{
		super(id, username, fullName, email, institution, creationDate, hasAccessToGatekeeper, isGatekeeperAdmin);
		this.databaseSystem = databaseSystem;
		this.hasBeenRejected = hasBeenRejected;
		this.activationKey = activationKey;
	}

	public DatabaseSystem getDatabaseSystem()
	{
		return databaseSystem;
	}

	public UnapprovedUser setDatabaseSystem(DatabaseSystem databaseSystem)
	{
		this.databaseSystem = databaseSystem;
		return this;
	}

	public boolean hasBeenRejected()
	{
		return hasBeenRejected;
	}

	public UnapprovedUser setHasBeenRejected(boolean hasBeenRejected)
	{
		this.hasBeenRejected = hasBeenRejected;

		return this;
	}

	public String getActivationKey()
	{
		return activationKey;
	}

	public UnapprovedUser setActivationKey(String activationKey)
	{
		this.activationKey = activationKey;
		return this;
	}

	@GwtIncompatible
	public static class Parser extends DatabaseObjectParser<UnapprovedUser>
	{
		private static final GatekeeperDatabaseObjectCache<Institution>    INSTITUTION_CACHE     = new GatekeeperDatabaseObjectCache<>(Institution.class);
		private static final GatekeeperDatabaseObjectCache<DatabaseSystem> DATABASE_SYSTEM_CACHE = new GatekeeperDatabaseObjectCache<>(DatabaseSystem.class);
		private Parser()
		{
			registerCache(INSTITUTION_CACHE);
			registerCache(DATABASE_SYSTEM_CACHE);
		}

		@Override
		public UnapprovedUser parse(DatabaseResult row, boolean foreignsFromResultSet) throws DatabaseException
		{
			Long userId = row.getLong(InstitutionManager.ID);

			if (userId == null)
				return null;
			else
			{
				UnapprovedUser user = new UnapprovedUser(row.getLong(ID));

				Long institutionId = row.getLong(INSTITUTION_ID);

				Institution inst;
				if (institutionId != null)
				{
					inst = INSTITUTION_CACHE.get(institutionId, row, foreignsFromResultSet);
				}
				else
				{
					inst = new Institution()
							.setName(row.getString(INSTITUTION_NAME))
							.setAcronym(row.getString(INSTITUTION_ACRONYM))
							.setAddress(row.getString(INSTITUTION_ADDRESS));
				}

				user.setHasBeenRejected(row.getBoolean(HAS_BEEN_REJECTED))
					.setActivationKey(row.getString(ACTIVATION_KEY))
					.setDatabaseSystem(DATABASE_SYSTEM_CACHE.get(row.getLong(DATABASE_SYSTEM_ID), row, foreignsFromResultSet))
					.setUsername(row.getString(USERNAME))
					.setFullName(row.getString(FULL_NAME))
					.setEmail(row.getString(EMAIL_ADDRESS))
					.setCreationDate(row.getTimestamp(CREATED_ON))
					.setInstitution(inst)
					.setHasAccessToGatekeeper(false)
					.setInstitution(inst);

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

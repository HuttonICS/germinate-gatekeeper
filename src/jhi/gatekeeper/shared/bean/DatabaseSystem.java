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
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class DatabaseSystem extends DatabaseObject
{
	public static final String ID          = "id";
	public static final String SYSTEM_NAME = "system_name";
	public static final String SERVER_NAME = "server_name";
	public static final String DESCRIPTION = "description";

	private String systemName;
	private String serverName;
	private String description;

	public DatabaseSystem()
	{
	}

	public DatabaseSystem(Long id)
	{
		super(id);
	}

	public DatabaseSystem(Long id, String systemName, String serverName, String description)
	{
		super(id);
		this.systemName = systemName;
		this.serverName = serverName;
		this.description = description;
	}

	public String getSystemName()
	{
		return systemName;
	}

	public DatabaseSystem setSystemName(String systemName)
	{
		this.systemName = systemName;
		return this;
	}

	public String getServerName()
	{
		return serverName;
	}

	public DatabaseSystem setServerName(String serverName)
	{
		this.serverName = serverName;
		return this;
	}

	public String getMeaningfulDescription()
	{
		if (!StringUtils.isEmpty(description))
			return description;
		else if (!StringUtils.isEmpty(systemName))
			return systemName;
		else
			return "";
	}

	public String getDescription()
	{
		return description;
	}

	public DatabaseSystem setDescription(String description)
	{
		this.description = description;
		return this;
	}

	@Override
	public String toString()
	{
		return "DatabaseSystem{" +
				"id=" + id +
				", systemName='" + systemName + '\'' +
				", serverName='" + serverName + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	@GwtIncompatible
	public static class Parser extends DatabaseObjectParser<DatabaseSystem>
	{
		private Parser()
		{
		}

		@Override
		public DatabaseSystem parse(DatabaseResult row, boolean foreignsFromResultSet) throws DatabaseException
		{
			Long id = row.getLong(ID);

			if (id == null)
				return null;
			else
				return new DatabaseSystem(id)
						.setSystemName(row.getString(SYSTEM_NAME))
						.setServerName(row.getString(SERVER_NAME))
						.setDescription(row.getString(DESCRIPTION));
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

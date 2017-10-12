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
import jhi.gatekeeper.server.manager.*;

/**
 * @author Sebastian Raubach
 */
public class Institution extends DatabaseObject
{
	private String name;
	private String acronym;
	private String address;

	public Institution()
	{
	}

	public Institution(Long id)
	{
		super(id);
	}

	public Institution(Long id, String name, String acronym, String address)
	{
		super(id);
		this.name = name;
		this.acronym = acronym;
		this.address = address;
	}

	public String getName()
	{
		return name;
	}

	public Institution setName(String name)
	{
		this.name = name;
		return this;
	}

	public String getAcronym()
	{
		return acronym;
	}

	public Institution setAcronym(String acronym)
	{
		this.acronym = acronym;
		return this;
	}

	public String getAddress()
	{
		return address;
	}

	public Institution setAddress(String address)
	{
		this.address = address;
		return this;
	}

	@GwtIncompatible
	public static class Parser extends DatabaseObjectParser<Institution>
	{
		private Parser()
		{
		}

		@Override
		public Institution parse(DatabaseResult row, boolean foreignsFromResultSet) throws DatabaseException
		{
			Long id = row.getLong(InstitutionManager.ID);

			if (id == null)
				return null;
			else
				return new Institution(id)
						.setName(row.getString(InstitutionManager.NAME))
						.setAcronym(row.getString(InstitutionManager.ACRONY))
						.setAddress(row.getString(InstitutionManager.ADDRESS));
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

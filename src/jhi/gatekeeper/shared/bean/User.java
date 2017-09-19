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
import jhi.database.server.query.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.server.util.*;
import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class User extends DatabaseObject
{
	public static final String ID                       = "id";
	public static final String USERNAME                 = "username";
	public static final String PASSWORD                 = "password";
	public static final String FULL_NAME                = "full_name";
	public static final String EMAIL_ADDRESS            = "email_address";
	public static final String CREATED_ON               = "created_on";
	public static final String INSTITUTION_ID           = "institution_id";
	public static final String HAS_ACCESS_TO_GATEKEEPER = "has_access_to_gatekeeper";

	private String      username;
	private String      fullName;
	private String      email;
	private Institution institution;
	private Date        creationDate;
	private boolean hasAccessToGatekeeper = true;
	private boolean isGatekeeperAdmin     = false;

	public User()
	{
	}

	public User(Long id)
	{
		super(id);
	}

	public User(Long id, String username, String fullName, String email, Institution institution, Date creationDate, boolean hasAccessToGatekeeper, boolean isGatekeeperAdmin)
	{
		super(id);
		this.username = username;
		this.fullName = fullName;
		this.email = email;
		this.institution = institution;
		this.creationDate = creationDate;
		this.hasAccessToGatekeeper = hasAccessToGatekeeper;
		this.isGatekeeperAdmin = isGatekeeperAdmin;
	}

	@GwtIncompatible
	public static DatabaseObjectQuery<User> getListRestrictions(String query, User searchBean, Pagination pagination) throws DatabaseException
	{
		DatabaseObjectQuery<User> q = new DatabaseObjectQuery<>(String.format(query, getRestrictionQuery(searchBean)));
		q.setFetchesCount(pagination.getResultSize());

		setRestrictions(q, searchBean);

		return q;
	}

	@GwtIncompatible
	public static ValueQuery getCountRestrictions(String query, User searchBean) throws DatabaseException
	{
		ValueQuery q = new ValueQuery(String.format(query, getRestrictionQuery(searchBean)));

		setRestrictions(q, searchBean);

		return q;
	}

	@GwtIncompatible
	private static String getRestrictionQuery(User searchBean)
	{
		if (searchBean == null)
			return "";

		StringBuilder builder = new StringBuilder();

		if (!StringUtils.isEmpty(searchBean.getUsername()))
		{
			builder.append(USERNAME)
				   .append(" LIKE ?");
		}
		else if (!StringUtils.isEmpty(searchBean.getFullName()))
		{
			builder.append(FULL_NAME)
				   .append(" LIKE ?");
		}
		else if (!StringUtils.isEmpty(searchBean.getEmail()))
		{
			builder.append(EMAIL_ADDRESS)
				   .append(" LIKE ?");
		}
		else if (searchBean.getId() != null)
		{
			builder.append(ID)
				   .append(" = ?");
		}

		return builder.length() > 0 ? (" WHERE " + builder.toString()) : "";
	}

	@GwtIncompatible
	private static void setRestrictions(BaseQuery q, User searchBean) throws DatabaseException
	{
		if (searchBean == null)
			return;

		if (!StringUtils.isEmpty(searchBean.getUsername()))
		{
			q.setString("%" + searchBean.getUsername() + "%");
		}
		else if (!StringUtils.isEmpty(searchBean.getFullName()))
		{
			q.setString("%" + searchBean.getFullName() + "%");
		}
		else if (!StringUtils.isEmpty(searchBean.getEmail()))
		{
			q.setString("%" + searchBean.getEmail() + "%");
		}
		else if (searchBean.getId() != null)
		{
			q.setLong(searchBean.getId());
		}
	}

	public User setId(String id)
	{
		this.id = Long.parseLong(id);
		return this;
	}

	public String getUsername()
	{
		return username;
	}

	public User setUsername(String username)
	{
		this.username = username;
		return this;
	}

	public String getFullName()
	{
		return fullName;
	}

	public User setFullName(String fullName)
	{
		this.fullName = fullName;
		return this;
	}

	public String getEmail()
	{
		return email;
	}

	public User setEmail(String email)
	{
		this.email = email;
		return this;
	}

	public Institution getInstitution()
	{
		return institution;
	}

	public User setInstitution(Institution institution)
	{
		this.institution = institution;
		return this;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public User setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
		return this;
	}

	public boolean isHasAccessToGatekeeper()
	{
		return hasAccessToGatekeeper;
	}

	public User setHasAccessToGatekeeper(boolean hasAccessToGatekeeper)
	{
		this.hasAccessToGatekeeper = hasAccessToGatekeeper;
		return this;
	}

	public boolean isGatekeeperAdmin()
	{
		return isGatekeeperAdmin;
	}

	public User setIsGatekeeperAdmin(boolean isGatekeeperAdmin)
	{
		this.isGatekeeperAdmin = isGatekeeperAdmin;
		return this;
	}

	@Override
	public String toString()
	{
		return "User{" +
				"username='" + username + '\'' +
				", fullName='" + fullName + '\'' +
				", email='" + email + '\'' +
				", institution=" + institution +
				", creationDate=" + creationDate +
				", hasAccessToGatekeeper=" + hasAccessToGatekeeper +
				", isGatekeeperAdmin=" + isGatekeeperAdmin +
				"} " + super.toString();
	}

	@GwtIncompatible
	public static class Parser extends DatabaseObjectParser<User>
	{
		private static final GatekeeperDatabaseObjectCache<Institution> INSTITUTION_CACHE = new GatekeeperDatabaseObjectCache<>(Institution.class);

		private Parser()
		{
			registerCache(INSTITUTION_CACHE);
		}

		@Override
		public User parse(DatabaseResult row, boolean foreignsFromResultSet) throws DatabaseException
		{
			Long id = row.getLong(ID);

			if (id == null)
				return null;
			else
				return new User(id)
						.setUsername(row.getString(USERNAME))
						.setFullName(row.getString(FULL_NAME))
						.setEmail(row.getString(EMAIL_ADDRESS))
						.setCreationDate(row.getTimestamp(CREATED_ON))
						.setInstitution(INSTITUTION_CACHE.get(row.getLong(INSTITUTION_ID), row, foreignsFromResultSet))
						.setHasAccessToGatekeeper(row.getBoolean(HAS_ACCESS_TO_GATEKEEPER))
						.setIsGatekeeperAdmin(UserManager.isUserAdmin(row.getLong(ID)));
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

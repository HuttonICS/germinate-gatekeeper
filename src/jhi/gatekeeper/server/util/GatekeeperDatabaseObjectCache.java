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

package jhi.gatekeeper.server.util;

import jhi.database.server.*;
import jhi.database.server.util.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.shared.bean.*;


public class GatekeeperDatabaseObjectCache<T extends DatabaseObject> extends DatabaseObjectCache<T>
{
	public GatekeeperDatabaseObjectCache(Class<T> clazz)
	{
		super(clazz);
	}

	@SuppressWarnings("unchecked")
	protected T getFromManager(Long id) throws DatabaseException
	{
		if (AccessRequest.class.equals(clazz))
		{
			return (T) AccessRequestManager.getById(id);
		}
		else if (DatabaseSystem.class.equals(clazz))
		{
			return (T) DatabaseSystemManager.getById(id);
		}
		else if (Institution.class.equals(clazz))
		{
			return (T) InstitutionManager.getById(id);
		}
		else if (UnapprovedUser.class.equals(clazz))
		{
			return (T) UnapprovedUserManager.getById(id);
		}
		else if (User.class.equals(clazz))
		{
			return (T) UserManager.getById(id);
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T getFromManager(DatabaseResult res) throws DatabaseException
	{
		if (AccessRequest.class.equals(clazz))
		{
			return (T) AccessRequestManager.get().getFromResult(res);
		}
		else if (DatabaseSystem.class.equals(clazz))
		{
			return (T) DatabaseSystemManager.get().getFromResult(res);
		}
		else if (Institution.class.equals(clazz))
		{
			return (T) InstitutionManager.get().getFromResult(res);
		}
		else if (UnapprovedUser.class.equals(clazz))
		{
			return (T) UnapprovedUserManager.get().getFromResult(res);
		}
		else if (User.class.equals(clazz))
		{
			return (T) UserManager.get().getFromResult(res);
		}

		return null;
	}
}

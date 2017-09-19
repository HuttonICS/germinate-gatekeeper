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

package jhi.gatekeeper.server.service;

import java.util.*;

import javax.servlet.annotation.*;

import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;
import jhi.gatekeeper.client.service.*;
import jhi.gatekeeper.server.manager.*;
import jhi.gatekeeper.server.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
@WebServlet(urlPatterns = {"/gatekeeper/unapproved-user"})
public class UnapprovedUserServiceImpl extends AbstractServletImpl implements UnapprovedUserService
{
	@Override
	public long getCount(RequestProperties properties) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		return UnapprovedUserManager.getCount();
	}

	@Override
	public PaginatedResult<List<UnapprovedUser>> getList(RequestProperties properties, Pagination pagination) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		if (pagination == null)
			pagination = Pagination.DEFAULT;

		return UnapprovedUserManager.getList(pagination);
	}

	@Override
	public void delete(RequestProperties properties, UnapprovedUser user) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		UnapprovedUserManager.delete(user);
	}

	@Override
	public void approve(RequestProperties properties, UnapprovedUser user) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException, EmailException, SuspendedUserException, InvalidActivationKeyException, UserNotFoundException
	{
		checkSessionAndPermissions(properties);

		UnapprovedUserManager.approve(user);

		Email.sendUnapprovedUserApproved(getUserLocale(user), user);
	}

	@Override
	public void reject(RequestProperties properties, UnapprovedUser user, String rejectionReason) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException, EmailException
	{
		checkSessionAndPermissions(properties);

		UnapprovedUserManager.reject(user);

		Email.sendUnapprovedUserRejected(getUserLocale(user), user, rejectionReason);
	}

	private Locale getUserLocale(UnapprovedUser user)
	{
		return Locale.ENGLISH;
	}
}

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
@WebServlet(urlPatterns = {"/gatekeeper/user"})
public class UserServiceImpl extends AbstractServletImpl implements UserService
{
	@Override
	public void createAdmin(RequestProperties properties, User user, UserCredentials credentials)
		throws UserExistsException, DatabaseException
	{
		UserManager.createAdmin(user, credentials);
	}

	@Override
	public long getUserCount(RequestProperties properties, User searchBean)
		throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		return UserManager.getCount(searchBean);
	}

	@Override
	public void updateUserEmail(RequestProperties properties, String email)
		throws InvalidCredentialsException, InvalidSessionException, DatabaseException
	{
		checkSession(properties);

		UserAuthentication user = (UserAuthentication) getThreadLocalRequest().getSession().getAttribute(Session.USER);

		if (user == null)
			throw new InvalidCredentialsException();

		UserManager.updateEmail(user, email);
	}

	@Override
	public void updatePassword(RequestProperties properties, UserCredentials credentials)
		throws InvalidCredentialsException, InvalidSessionException, DatabaseException, EmailException
	{
		checkSession(properties);

		UserAuthentication user = (UserAuthentication) getThreadLocalRequest().getSession().getAttribute(Session.USER);

		if (user == null)
			throw new InvalidCredentialsException();

		UserInternal u = UserManager.getUserByUsernameAndEmail(user.getUsername(), user.getEmailAddress());

		// Check the old password to make sure it's correct!
		if (!BCrypt.checkpw(credentials.getOldPassword(), u.getPassword()))
			throw new InvalidCredentialsException("Invalid password");

		UserManager.updatePassword(UserManager.getUserByUsernameAndEmail(user.getUsername(), user.getEmailAddress()), credentials.getPassword());

		Email.sendPasswordChangeInfo(properties.getLocale(), u);

		Session.terminateSessions(user.getId());
	}

	@Override
	public PaginatedResult<List<User>> getUserList(RequestProperties properties, User searchBean, Pagination pagination)
		throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		if (pagination == null)
			pagination = Pagination.DEFAULT;

		return UserManager.getList(searchBean, pagination);
	}

	@Override
	public void deleteUser(RequestProperties properties, long id)
		throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		UserManager.deleteById(id);
	}

	@Override
	public void addUser(RequestProperties properties, User user, UserCredentials credentials)
		throws InvalidSessionException, UserExistsException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		UserManager.add(user, credentials);
		UserManager.setGatekeeperPermission(user, UserType.REGULAR_USER);
	}

	@Override
	public void sendNewPassword(RequestProperties properties, String username, String email)
		throws InvalidCredentialsException, EmailException, DatabaseException
	{
		UserInternal user = UserManager.getUserByUsernameAndEmail(username, email);

		if (user == null)
			throw new InvalidCredentialsException(InvalidCredentialsException.INVALID_USERNAME);

		String newPassword = UUID.randomUUID().toString();

		/* Try to send the email before updating the password. If this fails, we don't want to update the password */
		Email.sendNewPassword(properties.getLocale(), user, newPassword);

		PasswordResetEventManager.insert(new PasswordResetEvent(null, user, new Date(System.currentTimeMillis()), getThreadLocalRequest().getRemoteAddr()));

		UserManager.updatePassword(user, newPassword);
	}

	@Override
	public ActivationDecision activateUser(RequestProperties properties, String key)
		throws InvalidActivationKeyException, EmailException, DatabaseException, UserNotFoundException, SuspendedUserException
	{
		UnapprovedUser unapprovedUser = UnapprovedUserManager.getByActivationKey(key);

		if (unapprovedUser != null)
		{
			if (unapprovedUser.isNeedsApproval())
			{
				try
				{
					Email.sendAwaitingApproval(Locale.ENGLISH, unapprovedUser);
					Email.sendAdministratorNotification(Locale.ENGLISH, unapprovedUser);
					return ActivationDecision.AWAITS_APPROVAL;
				}
				catch (EmailException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				User user = UserManager.activateUser(key);

				Email.sendActivationConfirmation(properties.getLocale(), user);
				return ActivationDecision.GRANTED;
			}
		}
		else
		{
			throw new UserNotFoundException();
		}

		return ActivationDecision.ERROR;
	}

	@Override
	public void setHasAccessToGatekeeper(RequestProperties properties, User user)
		throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSessionAndPermissions(properties);

		UserManager.setHasAccessToGatekeeper(user);
	}

	@Override
	public User getUser(RequestProperties properties)
		throws InvalidSessionException, InvalidCredentialsException, DatabaseException
	{
		checkSession(properties);

		UserAuthentication user = (UserAuthentication) getThreadLocalRequest().getSession().getAttribute(Session.USER);

		if (user == null)
			throw new InvalidCredentialsException();

		return UserManager.getById(user.getId());
	}
}

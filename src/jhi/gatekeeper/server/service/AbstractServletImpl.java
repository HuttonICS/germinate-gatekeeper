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

import com.google.gwt.user.server.rpc.*;

import jhi.database.shared.exception.*;
import jhi.gatekeeper.server.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class AbstractServletImpl extends RemoteServiceServlet
{
	protected void checkSessionAndPermissions(RequestProperties properties) throws InvalidSessionException, InsufficientPermissionsException, DatabaseException
	{
		checkSession(properties);
		UserAuthentication.checkIfAdmin(getThreadLocalRequest());
	}

	protected void checkSession(RequestProperties properties) throws InvalidSessionException
	{
		Session.checkSession(properties, getThreadLocalRequest());
	}
}

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

package jhi.gatekeeper.server.manager;

import jhi.database.server.query.*;
import jhi.database.shared.exception.*;
import jhi.gatekeeper.shared.bean.*;

/**
 * {@link PasswordResetEventManager} is a utility class that allows interactions with Gatekeeper <p> Every method in this class checks  before
 * actually doing anything. If the property is set to <code>false</code>, the method will either return <code>null</code> (if a return type is
 * required) or simply return (<code>void</code> methods).
 *
 * @author Sebastian Raubach
 */
public class PasswordResetEventManager
{
	private static final String INSERT = "INSERT INTO password_reset_log (user_id, timestamp, ip_address) VALUE (?, ?, ?)";

	/**
	 * Inserts the given {@link PasswordResetEvent} to the database.
	 *
	 * @param event The {@link PasswordResetEvent} to add
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public static void insert(PasswordResetEvent event) throws DatabaseException
	{
		new ValueQuery(INSERT)
				.setLong(event.getUser().getId())
				.setTimestamp(event.getTimestamp())
				.setString(event.getIpAddress())
				.execute();
	}
}

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

import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class DatabaseUtils
{
	/**
	 * Returns a String of the form <SERVER>:<PORT>/<DATABASE>
	 *
	 * @return A String of the form <SERVER>:<PORT>/<DATABASE>
	 */
	public static String getServerString()
	{
		String server = PropertyReader.getProperty(PropertyReader.DATABASE_SERVER);
		String database = PropertyReader.getProperty(PropertyReader.DATABASE_NAME);
		String port = PropertyReader.getProperty(PropertyReader.DATABASE_PORT);
		boolean usePort = PropertyReader.getPropertyBoolean(PropertyReader.DATABASE_USE_PORT);

		return getServerString(server, database, usePort ? port : null);
	}

	/**
	 * Returns a String of the form <SERVER>:<PORT>/<DATABASE>
	 *
	 * @param server   The server string
	 * @param database The database string
	 * @param port     The port string
	 * @return A String of the form <SERVER>:<PORT>/<DATABASE>
	 */
	public static String getServerString(String server, String database, String port)
	{
		if (!StringUtils.isEmpty(port))
		{
			return server + ":" + port + "/" + database;
		}
		else
		{
			return server + "/" + database;
		}
	}
}

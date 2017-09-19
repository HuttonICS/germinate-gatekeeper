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

package jhi.gatekeeper.shared.exception;

import java.io.*;
import java.util.logging.*;

/**
 * @author Sebastian Raubach
 */
public abstract class GatekeeperException extends Exception implements Serializable
{
	private static final long serialVersionUID = -2710745421855737236L;

	private transient final static Logger LOGGER = Logger.getLogger(GatekeeperException.class.getName());

	public static boolean printExceptions = false;
	public static boolean isDebugging     = false;

	public GatekeeperException()
	{
		super();

		if (printExceptions)
			logException();
	}

	public GatekeeperException(String message)
	{
		super(message);

		if (printExceptions)
			logException();
	}

	public GatekeeperException(Exception e)
	{
		/*
		 * Since Exceptions are forwarded to the client, we only forward the
         * actual exception content (stack trace, original error message) if
         * we're running in debug mode
         */
		super(isDebugging ? e : null);

		if (printExceptions)
			/*
			 * HOWEVER, we allow subclasses to still handle the actual exception
             * on the server. This can be used for logging purposes
             */
			logException(e);
	}

	/**
	 * Log to the {@link Logger}
	 */
	protected void logException()
	{
		LOGGER.log(Level.SEVERE, this.toString(), this);
	}

	/**
	 * Subclasses can override this method to get access to the original exception details even if not in debug mode.
	 *
	 * @param e The original exception.
	 */
	protected void logException(Exception e)
	{
		LOGGER.log(Level.SEVERE, e.toString(), e);
	}
}

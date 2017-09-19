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

/**
 * {@link InvalidCredentialsException} extends {@link GatekeeperException} to indicate that the credentials used to log in do not match those in the
 * database.
 *
 * @author Sebastian Raubach
 */
public class InvalidCredentialsException extends GatekeeperException implements Serializable
{
	public static final int UNKNOWN_REASON   = -1;
	public static final int INVALID_PASSWORD = 0;
	public static final int INVALID_USERNAME = 1;
	private static final long serialVersionUID = 2654363400317432444L;
	private int errorType = -1;

	public InvalidCredentialsException()
	{
		this(UNKNOWN_REASON);
	}

	public InvalidCredentialsException(int errorType)
	{
		super();
		this.errorType = errorType;
	}

	public InvalidCredentialsException(String message)
	{
		this(message, UNKNOWN_REASON);
	}

	public InvalidCredentialsException(String message, int errorType)
	{
		super(message);
		this.errorType = errorType;
	}

	public InvalidCredentialsException(Exception e)
	{
		super(e);
	}

	public int getErrorType()
	{
		return errorType;
	}
}

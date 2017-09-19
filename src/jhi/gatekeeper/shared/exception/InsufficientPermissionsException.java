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
 * {@link InsufficientPermissionsException} extends {@link GatekeeperException} that indicates that there was an attempt to perform an operation for
 * which the user's permissions are not sufficient.
 *
 * @author Sebastian Raubach
 */
public class InsufficientPermissionsException extends GatekeeperException implements Serializable
{
	private static final long serialVersionUID = 8740407447954506899L;

	public InsufficientPermissionsException()
	{
		super();
	}

	public InsufficientPermissionsException(String message)
	{
		super(message);
	}

	public InsufficientPermissionsException(Exception e)
	{
		super(e);
	}
}

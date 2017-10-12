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

package jhi.gatekeeper.shared.exception;

import java.io.*;

/**
 * {@link AdminAccountMissingException} extends {@link GatekeeperException} used to indicate that no admin account exists for this Gatekeeper
 * instance.
 *
 * @author Sebastian Raubach
 */
public class AdminAccountMissingException extends GatekeeperException implements Serializable
{
	public AdminAccountMissingException()
	{
		super();
	}

	public AdminAccountMissingException(String message)
	{
		super(message);
	}

	public AdminAccountMissingException(Exception e)
	{
		super(e);
	}
}

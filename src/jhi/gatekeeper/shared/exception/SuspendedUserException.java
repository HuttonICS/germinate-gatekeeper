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

package jhi.gatekeeper.shared.exception;

import java.io.*;

/**
 * {@link SuspendedUserException} extends {@link GatekeeperException} used to indicate that the current user is suspended. This means that the user is
 * no longer allowed to access the information stored in this Germinate instance.
 *
 * @author Sebastian Raubach
 */
public class SuspendedUserException extends GatekeeperException implements Serializable
{
	private static final long serialVersionUID = 913644257571257806L;

	public SuspendedUserException()
	{
		super();
	}

	public SuspendedUserException(String message)
	{
		super(message);
	}

	public SuspendedUserException(Exception e)
	{
		super(e);
	}
}

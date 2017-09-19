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

package jhi.gatekeeper.shared.bean;

import java.io.*;

/**
 * @author Sebastian Raubach
 */
public enum UserType implements Serializable
{
	ADMINISTRATOR(1, "Administrator"),
	REGULAR_USER(2, "Regular User"),
	SUSPENDED_USER(3, "Suspended User");

	private long   id;
	private String name;

	UserType(long id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public static UserType getById(long id)
	{
		for (UserType type : values())
		{
			if (type.id == id)
				return type;
		}

		throw new RuntimeException("No UserType found for id: " + id);
	}

	public static UserType getByName(String name)
	{
		for (UserType type : values())
		{
			if (type.name.equals(name))
				return type;
		}

		throw new RuntimeException("No UserType found for name: " + name);
	}

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
}

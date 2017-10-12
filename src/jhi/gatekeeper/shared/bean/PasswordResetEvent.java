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

package jhi.gatekeeper.shared.bean;

import java.util.*;

import jhi.database.shared.util.*;

/**
 * @author Sebastian Raubach
 */
public class PasswordResetEvent extends DatabaseObject
{
	private User   user;
	private Date   timestamp;
	private String ipAddress;

	public PasswordResetEvent()
	{
		super();
	}

	public PasswordResetEvent(Long id, User user, Date timestamp, String ipAddress)
	{
		super(id);
		this.user = user;
		this.timestamp = timestamp;
		this.ipAddress = ipAddress;
	}

	public User getUser()
	{
		return user;
	}

	public PasswordResetEvent setUser(User user)
	{
		this.user = user;
		return this;
	}

	public Date getTimestamp()
	{
		return timestamp;
	}

	public PasswordResetEvent setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
		return this;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public PasswordResetEvent setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
		return this;
	}
}

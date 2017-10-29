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

package jhi.gatekeeper.shared;

/**
 * Valid {@link Parameter}s of the {@link jhi.gatekeeper.client.util.ParameterStore}
 *
 * @author Sebastian Raubach
 */
public enum Parameter
{
	databaseSystemId(Long.class, true, true);

	private Class<?> type;

	private boolean isStoredInLocalStorage;
	private boolean expires;

	Parameter(Class<?> type, boolean isStoredInLocalStorage, boolean expires)
	{
		this.type = type;
		this.isStoredInLocalStorage = isStoredInLocalStorage;
		this.expires = expires;
	}

	public Class<?> getType()
	{
		return type;
	}

	public boolean isStoredInLocalStorage()
	{
		return isStoredInLocalStorage;
	}

	public boolean doesExpire()
	{
		return expires;
	}
}

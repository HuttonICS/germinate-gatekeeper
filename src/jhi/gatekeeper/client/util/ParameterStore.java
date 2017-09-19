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

package jhi.gatekeeper.client.util;

import java.util.*;

import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link ParameterStore} is the central instance holding the {@link Parameter}s of the current state.
 *
 * @author Sebastian Raubach
 */
public class ParameterStore
{
	private static final Map<Parameter, Object> STATE_PARAMETERS = new HashMap<>();

	/**
	 * Get a specific {@link Parameter} from the {@link ParameterStore}
	 *
	 * @param key The {@link Parameter} identifier
	 * @return The {@link Parameter} or <code>null</code>
	 */
	public static Object get(Parameter key)
	{
		return get(key, true);
	}

	/**
	 * Get a specific {@link Parameter} from the {@link ParameterStore}
	 *
	 * @param key               The {@link Parameter} identifier
	 * @param useCookieFallback useCookieFallback If no key is present, use the cookie value as a fallback?
	 * @return The {@link Parameter} or <code>null</code>
	 */
	public static Object get(Parameter key, boolean useCookieFallback)
	{
		Object result = STATE_PARAMETERS.get(key);

		if (result == null && key.isStoredInLocalStorage() && useCookieFallback)
		{
			if (key.getType().equals(List.class))
			{
				return CollectionUtils.parseList(LocalStorage.get(key.name()), ",");
			}
			else if(key.getType().equals(Long.class))
			{
				return Long.parseLong(LocalStorage.get(key.name()));
			}
			else if(key.getType().equals(Boolean.class))
			{
				return Boolean.parseBoolean(LocalStorage.get(key.name()));
			}
			else
			{
				return LocalStorage.get(key.name());
			}
		}
		else
			return result;
	}

	/**
	 * Gets a specific {@link Parameter} from the {@link ParameterStore}. Makes sure that it is in {@link String} form. This will, e.g., convert a
	 * {@link List} to a csv String.
	 *
	 * @param key The {@link Parameter} identifier
	 * @return The {@link Parameter} or <code>null</code>
	 */
	public static String getAsString(Parameter key)
	{
		return getAsString(key, true);
	}

	/**
	 * Gets a specific {@link Parameter} from the {@link ParameterStore}. Makes sure that it is in {@link String} form. This will, e.g., convert a
	 * {@link List} to a csv String.
	 *
	 * @param key               The {@link Parameter} identifier
	 * @param useCookieFallback If no key is present, use the cookie value as a fallback?
	 * @return The {@link Parameter} or <code>null</code>
	 */
	public static String getAsString(Parameter key, boolean useCookieFallback)
	{
		if (ParameterStore.get(key, useCookieFallback) == null)
			return null;
		else if (key.getType().equals(List.class))
			return CollectionUtils.join((List<?>) ParameterStore.get(key), ",");
		else
			return ParameterStore.get(key).toString();
	}

	/**
	 * Set a specific {@link Parameter} to the {@link ParameterStore}
	 *
	 * @param key   The {@link Parameter} identifier
	 * @param value The actual {@link Parameter} value
	 * @return the previous value associated with key, or null if there was no mapping for the key. (A null return can also indicate that the map
	 * previously associated null with the key)
	 */
	public static Object put(Parameter key, Object value)
	{
		if (key.isStoredInLocalStorage())
		{
			if (value == null)
				LocalStorage.remove(key.name());
			else
			{
				boolean expires = key.doesExpire();
				if (key.getType().equals(List.class))
				{
					LocalStorage.set(key.name(), CollectionUtils.join((List<?>) value, ","), expires);
				}
				else
				{
					LocalStorage.set(key.name(), value.toString(), expires);
				}
			}
		}

		if (value == null)
			return STATE_PARAMETERS.remove(key);
		else
			return STATE_PARAMETERS.put(key, value);
	}

	/**
	 * Set a specific {@link Parameter} to the {@link ParameterStore}
	 *
	 * @param key   The {@link Parameter} identifier
	 * @param value The actual {@link Parameter} value
	 * @param type  The {@link Class} of this {@link Parameter}
	 * @return the previous value associated with key, or null if there was no mapping for the key. (A null return can also indicate that the map
	 * previously associated null with the key)
	 * @throws UnsupportedDataTypeException Thrown if the given type is not supported
	 */
	public static Object put(Parameter key, String value, Class<?> type) throws UnsupportedDataTypeException
	{
		if (!type.equals(List.class) && key.isStoredInLocalStorage())
			LocalStorage.set(key.name(), value, key.doesExpire());

		if (type.equals(Integer.class))
		{
			return STATE_PARAMETERS.put(key, Integer.parseInt(value));
		}
		else if (type.equals(Double.class))
		{
			return STATE_PARAMETERS.put(key, Double.parseDouble(value));
		}
		else if (type.equals(Float.class))
		{
			return STATE_PARAMETERS.put(key, Float.parseFloat(value));
		}
		else if (type.equals(Boolean.class))
		{
			return STATE_PARAMETERS.put(key, Boolean.parseBoolean(value));
		}
		else if (type.equals(Long.class))
		{
			return STATE_PARAMETERS.put(key, Long.parseLong(value));
		}
		else if (type.equals(String.class))
		{
			return STATE_PARAMETERS.put(key, value);
		}
		else
			throw new UnsupportedDataTypeException();
	}

	/**
	 * Returns true if the {@link Parameter} store contains a mapping for the specified key.
	 *
	 * @param key The {@link Parameter} identifier
	 * @return <code>true</code> if the {@link Parameter} store contains a mapping for the specified key.
	 */
	public static boolean containsKey(Parameter key)
	{
		String localStorageValue = (key.isStoredInLocalStorage()) ? LocalStorage.get(key.name()) : null;
		return STATE_PARAMETERS.containsKey(key) || !StringUtils.isEmpty(localStorageValue);
	}

	/**
	 * Removes the mapping for a key from this map if it is present (optional operation).
	 *
	 * @param key The {@link Parameter} identifier
	 * @return The previous value associated with key, or <code>null</code> if there was no mapping for the key.
	 */
	public static Object remove(Parameter key)
	{
		if (key.isStoredInLocalStorage())
			LocalStorage.remove(key.name());
		return STATE_PARAMETERS.remove(key);
	}

	/**
	 * Removes all of the mappings from the {@link ParameterStore}. The {@link ParameterStore} will be empty after this call returns.
	 */
	public static void clear()
	{
		for (Parameter param : Parameter.values())
		{
			if (param.isStoredInLocalStorage() && param.doesExpire())
				LocalStorage.remove(param.name());
		}
		STATE_PARAMETERS.clear();
	}
}

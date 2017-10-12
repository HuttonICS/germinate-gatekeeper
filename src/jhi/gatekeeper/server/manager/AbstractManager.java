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

package jhi.gatekeeper.server.manager;

import jhi.database.server.*;
import jhi.database.server.parser.*;
import jhi.database.shared.exception.*;
import jhi.database.shared.util.*;

/**
 * @author Sebastian Raubach
 */
public abstract class AbstractManager<T extends DatabaseObject>
{
	public static final String COUNT = "count";

	/**
	 * Returns the {@link DatabaseObjectParser} that can be used to parse the {@link DatabaseObject} from the {@link DatabaseResult}.
	 *
	 * @return The {@link DatabaseObjectParser} that can be used to parse the {@link DatabaseObject} from the {@link DatabaseResult}.
	 */
	protected abstract DatabaseObjectParser<T> getParser();

	/**
	 * Extracts and returns the {@link DatabaseObject} from the {@link DatabaseResult} without running additional queries. This means that all the
	 * information for the {@link DatabaseObject}s that are fields of this item needs to be contained in the {@link DatabaseResult} as well.
	 *
	 * @param res The {@link DatabaseResult} containing the data
	 * @return The {@link DatabaseObject} from the {@link DatabaseResult} without running additional queries. This means that all the information for
	 * the {@link DatabaseObject}s that are fields of this item needs to be contained in the {@link DatabaseResult} as well.
	 * @throws DatabaseException Thrown if the interaction with the database fails
	 */
	public T getFromResult(DatabaseResult res) throws DatabaseException
	{
		return getParser().parse(res, true);
	}
}

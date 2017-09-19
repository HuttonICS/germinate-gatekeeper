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

package jhi.gatekeeper.client.widget.select;

import com.google.gwt.text.shared.*;

import java.io.*;
import java.util.*;

import jhi.gatekeeper.shared.bean.*;

/**
 * @author Sebastian Raubach
 */
public class UserTypeListBox extends GatekeeperListBox<UserType>
{
	public UserTypeListBox()
	{
		super(new Renderer<UserType>()
		{
			@Override
			public String render(UserType object)
			{
				return object.getName();
			}

			@Override
			public void render(UserType object, Appendable appendable) throws IOException
			{
				appendable.append(render(object));
			}
		});
	}

	@Override
	protected void onLoad()
	{
		super.onLoad();

		this.setValue(UserType.values()[0]);
		this.setAcceptableValues(Arrays.asList(UserType.values()));
	}
}

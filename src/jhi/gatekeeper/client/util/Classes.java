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

import jhi.gatekeeper.shared.*;

/**
 * @author Sebastian Raubach
 */
public class Classes
{
	public static final String FA                = "fa";
	public static final String FA_CHECK_SQUARE   = "fa-check-square";
	public static final String FA_DATABASE       = "fa-database";
	public static final String FA_ENVELOPE       = "fa-envelope";
	public static final String FA_FW             = "fa-fw";
	public static final String FA_GEAR           = "fa-gear";
	public static final String FA_INFO           = "fa-info";
	public static final String FA_KEY            = "fa-key";
	public static final String FA_LANGUAGE       = "fa-language";
	public static final String FA_LIST           = "fa-list";
	public static final String FA_MAP_MARKER     = "fa-map-marker";
	public static final String FA_PENCIL         = "fa-pencil";
	public static final String FA_SIGN_IN        = "fa-sign-in";
	public static final String FA_SIGN_OUT       = "fa-sign-out";
	public static final String FA_SEARCH         = "fa-search";
	public static final String FA_SERVER         = "fa-server";
	public static final String FA_TAG            = "fa-tag";
	public static final String FA_UNIVERSITY     = "fa-university";
	public static final String FA_USER           = "fa-user";
	public static final String FA_USER_PLUS      = "fa-user-plus";
	public static final String FA_USER_SECRET    = "fa-user-secret";
	public static final String FA_USERS          = "fa-users";
	public static final String FA_TIMES_CIRCLE_O = "fa-times-circle-o";

	public static final String BREAK_WORD = "break-word";

	public static final String FORM_LOGO           = "form-logo";
	public static final String FORM_SIGNIN         = "form-signin";
	public static final String FORM_SIGNIN_HEADING = "form-signin-heading";

	public static final String BTN_LG      = "btn-lg";
	public static final String BTN_PRIMARY = "btn-primary";

	public static final String NO_MARGIN_TOP = "no-margin-top";

	public static final String MARGIN_BOTTOM = "margin-bottom";

	public static final String PROGRESS_INDICATOR_INDETERMINATE = "progress-indicator-indeterminate";

	public static final String SCROLL_PANEL = "scroll-panel";

	public static final String COUNTRY = "country";

	/**
	 * Returns the combined style string
	 *
	 * @param styles The styles to combine
	 * @return The combined string
	 */
	public static String combine(String... styles)
	{
		if (styles.length == 0)
			return "";
		else
		{
			StringBuilder builder = new StringBuilder();
			builder.append(styles[0]);

			for (int i = 1; i < styles.length; i++)
			{
				if (!StringUtils.isEmpty(styles[i]))
				{
					builder.append(" ")
						   .append(styles[i]);
				}
			}

			return builder.toString();
		}
	}
}

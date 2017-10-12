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

package jhi.gatekeeper.shared;

/**
 * Set of the available "pages"
 *
 * @author Sebastian Raubach
 */
public enum Page
{
	about("about", Category.main, true),
	activateUser("activate-user", Category.main, true),
	addNewUser("add-user", Category.main, false),
	accountSettings("account-settings", Category.settings, false),
	approveUsers("approve-user", Category.main, false),
	createAdmin("create-admin", Category.main, true),
	login("login", Category.main, true),
	logout("logout", Category.main, true),
	lostPassword("lost-password", Category.main, true),
	systemsList("system-list", Category.main, false),
	systemsUserList("system-user-list", Category.main, false),
	viewUsers("user-list", Category.main, false);

	private String   url;
	private Category category;
	private boolean  isPublic;

	Page(String url, Category category, boolean isPublic)
	{
		this.url = url;
		this.category = category;
		this.isPublic = isPublic;
	}

	/**
	 * Tries to create an instance from the constants
	 *
	 * @param name The possible name of one of the constants
	 * @return The {@link Page} instance
	 */
	public static Page getValueOf(String name)
	{
		for (Page page : values())
		{
			if (page.getUrl().equals(name))
				return page;
		}
		throw new IllegalArgumentException("Invalid Page value: " + name);
	}

	public String getUrl()
	{
		return url;
	}

	public Category getCategory()
	{
		return category;
	}

	public boolean isPublic()
	{
		return isPublic;
	}

	public enum Category
	{
		main,
		settings
	}
}

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

package jhi.gatekeeper.server.util;

import javax.servlet.*;

public class ApplicationListener implements ServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		PropertyReader.initialize();

//		try
//		{
//			Logger.getLogger("").log(Level.INFO, "RUNNING FLYWAY");
//			Flyway flyway = new Flyway();
//			flyway.setDataSource(Database.DatabaseType.MYSQL.getConnectionString() + DatabaseUtils.getServerString(), PropertyReader.getProperty(PropertyReader.DATABASE_USERNAME), PropertyReader.getProperty(PropertyReader.DATABASE_PASSWORD));
//			flyway.setLocations("classpath:jhi.gatekeeper.server.database.migration");
//			flyway.setBaselineOnMigrate(true);
//			flyway.migrate();
//		}
//		catch (FlywayException e)
//		{
//			e.printStackTrace();
//		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		/* Remember to stop the property and image file watcher */
		PropertyReader.stopFileWatcher();
	}
}

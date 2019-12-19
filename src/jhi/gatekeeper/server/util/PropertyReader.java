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

package jhi.gatekeeper.server.util;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;

import javax.servlet.http.*;

import jhi.database.server.*;
import jhi.database.shared.exception.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * {@link PropertyReader} is a wrapper around {@link Properties} to read properties.
 *
 * @author Sebastian Raubach
 */
public class PropertyReader
{
	public static final String EMAIL_SERVER   = "email.server";
	public static final String EMAIL_ADDRESS  = "email.address";
	public static final String EMAIL_USERNAME = "email.username";
	public static final String EMAIL_PASSWORD = "email.password";
	public static final String EMAIL_PORT     = "email.port";

	public static final String DATABASE_SERVER   = "database.server";
	public static final String DATABASE_NAME     = "database.name";
	public static final String DATABASE_PORT     = "database.port";
	public static final String DATABASE_USE_PORT = "database.useport";

	public static final String DATABASE_USERNAME = "database.username";
	public static final String DATABASE_PASSWORD = "database.password";

	public static final String GATEKEEPER_BCRYPT_ROUNDS           = "gatekeeper.bcrypt.rounds";
	public static final String GATEKEEPER_COOKIE_LIFESPAN_MINUTES = "gatekeeper.cookie.lifespan.minutes";
	public static final String GATEKEEPER_SERVER_LOGGING_ENABLED  = "gatekeeper.server.logging.enabled";

	/** The name of the properties file */
	private static final String PROPERTIES_FILE = "config.properties";

	private static Properties                   properties = new Properties();
	private static PropertyChangeListenerThread fileWatcher;

	private static Thread   fileWatcherThread;
	private static WatchKey watchKey;

	/**
	 * Attempts to reads the properties file and then checks the required properties.
	 */
	public static void initialize()
	{
		try
		{
			/* Start to listen for file changes */
			Path path = new File(PropertyReader.class.getClassLoader().getResource(PROPERTIES_FILE).toURI()).getParentFile().toPath();
			FileSystem fs = path.getFileSystem();

			WatchService service = fs.newWatchService();
			/* start the file watcher thread below */
			fileWatcher = new PropertyChangeListenerThread(service);
			fileWatcherThread = new Thread(fileWatcher, "PropertyFileWatcher");
			fileWatcherThread.start();

			/* Register events */
			watchKey = path.register(service, StandardWatchEventKinds.ENTRY_MODIFY);
		}
		catch (IOException | URISyntaxException | NullPointerException e)
		{
			throw new RuntimeException(e);
		}

		loadProperties();
	}

	private static void loadProperties()
	{
		InputStream stream = null;
		try
		{
			stream = PropertyReader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			properties.load(stream);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			if (stream != null)
			{
				try
				{
					stream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		checkRequiredProperties();

		GatekeeperException.printExceptions = PropertyReader.getPropertyBoolean(GATEKEEPER_SERVER_LOGGING_ENABLED, false);

		Database.init(DatabaseUtils.getServerString(), getProperty(DATABASE_USERNAME), getProperty(DATABASE_PASSWORD, ""));
		DatabaseException.setForwardExceptionContent(false);
		DatabaseException.setPrintExceptions(true);
	}

	public static void stopFileWatcher()
	{
		if (watchKey != null)
			watchKey.cancel();
		if (fileWatcher != null)
			fileWatcher.stop();
		if (fileWatcherThread != null)
			fileWatcherThread.interrupt();
	}

	/**
	 * Checks the required properties
	 */
	private static void checkRequiredProperties()
	{
		boolean databaseUsePort = getPropertyBoolean(DATABASE_USE_PORT);
		String databasePort = getProperty(DATABASE_PORT);

		if (StringUtils.isEmpty(getProperty(DATABASE_SERVER)))
			throwException(DATABASE_SERVER);
		if (StringUtils.isEmpty(getProperty(DATABASE_NAME)))
			throwException(DATABASE_NAME);
		if (StringUtils.isEmpty(getProperty(DATABASE_USERNAME)))
			throwException(DATABASE_USERNAME);
		if (databaseUsePort && StringUtils.isEmpty(databasePort))
			throwException(DATABASE_PORT);
	}

	/**
	 * Throws a {@link RuntimeException} for the given property
	 *
	 * @param property The name of the property.
	 */
	private static void throwException(String property)
	{
		throw new RuntimeException("Gatekeeper failed to start: Non-optional property not set: '" + property + "'");
	}

	/**
	 * Reads a property from the .properties file
	 *
	 * @param propertyName The property to read
	 * @return The property or <code>null</code> if the property is not found
	 */
	public static String getProperty(String propertyName)
	{
		return properties.getProperty(propertyName);
	}

	/**
	 * Reads a property from the .properties file
	 *
	 * @param propertyName The property to read
	 * @return The property or <code>null</code> if the property is not found
	 */
	public static String getProperty(String propertyName, String fallback)
	{
		String result = properties.getProperty(propertyName);

		if (result == null)
			result = fallback;

		return result;
	}

	/**
	 * Reads an {@link Integer} property from the .properties file. The fallback will be used if there is no such property.
	 *
	 * @param propertyName The property to read
	 * @param fallback     The value that is returned if the property isn't set
	 * @return The {@link Integer} property
	 */
	public static Integer getPropertyInteger(String propertyName, int fallback)
	{
		try
		{
			return Integer.parseInt(getProperty(propertyName));
		}
		catch (Exception e)
		{
			return fallback;
		}
	}

	/**
	 * Reads an {@link Boolean} property from the .properties file
	 *
	 * @param propertyName The property to read
	 * @return The {@link Boolean} property
	 */
	public static Boolean getPropertyBoolean(String propertyName)
	{
		return Boolean.parseBoolean(getProperty(propertyName));
	}

	/**
	 * Reads an {@link Boolean} property from the .properties file. The fallback will be used if there is no such property.
	 *
	 * @param propertyName The property to read
	 * @param fallback     The value that is returned if the property isn't set
	 * @return The {@link Boolean} property
	 */
	public static Boolean getPropertyBoolean(String propertyName, boolean fallback)
	{
		try
		{
			return Boolean.parseBoolean(getProperty(propertyName));
		}
		catch (Exception e)
		{
			return fallback;
		}
	}

	/**
	 * Returns the context path of the app i.e. given "http://ics.hutton.ac.uk:80/germinate-baz/genotype?dummyParam=3" it will return:
	 * "/germinate-baz"
	 *
	 * @param req The current request
	 * @return The context path of the app (see description) or <code>"null"</code> if req is <code>null</code>
	 */
	public static String getContextPath(HttpServletRequest req)
	{
		if (req == null)
			return "null";
		else
			return req.getContextPath();
	}

	/**
	 * Returns the server base of the given request, i.e. given "http://ics.hutton.ac.uk:80/germinate-baz/genotype?dummyParam=3" it will return:
	 * "http://ics.hutton.ac.uk:80/germinate-baz"
	 *
	 * @param req The current request
	 * @return The server base (see description)
	 */
	public static String getServerBase(HttpServletRequest req)
	{
		String scheme = req.getScheme(); // http
		String serverName = req.getServerName(); // ics.hutton.ac.uk
		int serverPort = req.getServerPort(); // 80
		String contextPath = req.getContextPath(); // /germinate-baz

		return scheme + "://" + serverName + ":" + serverPort + contextPath; // http://ics.hutton.ac.uk:80/germinate-baz
	}

	/**
	 * This Runnable is used to constantly attempt to take from the watch queue, and will receive all events that are registered with the fileWatcher
	 * it is associated.
	 */
	private static class PropertyChangeListenerThread implements Runnable
	{

		/** the watchService that is passed in from above */
		private WatchService watcher;

		private boolean stopped = false;

		public PropertyChangeListenerThread(WatchService watcher)
		{
			this.watcher = watcher;
		}

		public void stop()
		{
			stopped = true;
		}

		/**
		 * In order to implement a file watcher, we loop forever ensuring requesting to take the next item from the file watchers queue.
		 */
		@Override
		public void run()
		{
			while (!stopped)
			{
				/* Wait for key to be signaled */
				WatchKey key;
				try
				{
					key = watcher.take();
				}
				catch (InterruptedException x)
				{
					return;
				}

				/*
				 * We have a polled event, now we traverse it and receive all
				 * the states from it
				 */
				for (WatchEvent<?> event : key.pollEvents())
				{
					WatchEvent.Kind<?> kind = event.kind();

					if (kind == StandardWatchEventKinds.OVERFLOW)
					{
						continue;
					}

					/*
					 * The filename is the context of the event
					 */
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();

					if (!stopped && PROPERTIES_FILE.equals(filename.getFileName().toString()))
						loadProperties();
				}

				/*
				 * Reset the key -- this step is critical if you want to receive
				 * further watch events. If the key is no longer valid, the
				 * directory is inaccessible so exit the loop.
				 */
				boolean valid = key.reset();

				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}

				if (!valid)
				{
					break;
				}
			}
		}
	}
}

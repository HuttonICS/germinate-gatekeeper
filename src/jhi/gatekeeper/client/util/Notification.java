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

package jhi.gatekeeper.client.util;

import com.google.gwt.dom.client.*;

import java.util.*;

/**
 * {@link Notification} is a wrapper for <a href="http://toastrjs.com/">Toastr - Simple javascript toast notifications</a>.
 * <p/>
 * Notification will strip all html tags from the content before showing them to prevent execution of, e.g., javascript.
 *
 * @author Sebastian Raubach
 */
public class Notification
{
	public static final String POSITION_TOP_RIGHT         = "toast-top-right";
	public static final String POSITION_BOTTOM_RIGHT      = "toast-bottom-right";
	public static final String POSITION_BOTTOM_LEFT       = "toast-bottom-left";
	public static final String POSITION_TOP_LEFT          = "toast-top-left";
	public static final String POSITION_TOP_FULL_WIDTH    = "toast-top-full-width";
	public static final String POSITION_BOTTOM_FULL_WIDTH = "toast-bottom-full-width";

	public static final int DURATION_LONG  = 6000;
	public static final int DURATION_SHORT = 3500;

	private static final long IGNORE_LAST_MESSAGE_IF_LESS_THAN = 1000;

	private static Map<Type, Map.Entry<String, Long>> lastMessages = new HashMap<>();

	/**
	 * Initializes the notification settings
	 */
	public static void init()
	{
		setPositionClass(POSITION_TOP_RIGHT);
		setTimeOut(DURATION_SHORT);
		setShowDuration(200);
		setHideDuration(500);
		setCloseButtonEnabled(false);
		setPreventDuplicates(false);
		setShowProgressBar(true);
		setNewestOnTop(true);
	}

	/**
	 * Initializes the notification settings
	 *
	 * @param target The {@link Element}
	 */
	public static void init(String target)
	{
		init();

		setTarget(target);
	}

	/**
	 * Shows a notification of the given {@link Type} with the given message
	 *
	 * @param message The message to display
	 */
	public static void notify(Type type, boolean message)
	{
		notify(type, Boolean.toString(message));
	}

	/**
	 * Shows a notification of the given {@link Type} with the given message
	 *
	 * @param message The message to display
	 */
	public static void notify(Type type, double message)
	{
		notify(type, Double.toString(message));
	}

	/**
	 * Shows a notification of the given {@link Type} with the given message
	 *
	 * @param message The message to display
	 */
	public static void notify(Type type, float message)
	{
		notify(type, Float.toString(message));
	}

	/**
	 * Shows a notification of the given {@link Type} with the given message
	 *
	 * @param message The message to display
	 */
	public static void notify(Type type, int message)
	{
		notify(type, Integer.toString(message));
	}

	/**
	 * Shows a notification of the given {@link Type} with the given message
	 *
	 * @param type    The {@link Type} of notification
	 * @param message The message to display
	 */
	public static void notify(Type type, Object message)
	{
		if (message == null)
			notify(type, "null");
		else
		{
			if (message instanceof Exception)
				notify(type, ((Exception) message).getLocalizedMessage());
			else
				notify(type, message.toString());
		}
	}

	/**
	 * Shows a notification of the given {@link Type} with the given message
	 *
	 * @param type    The {@link Type} of notification
	 * @param message The message
	 */
	public static void notify(Type type, String message)
	{
		/* Strip potentially malicious content */
		message = HTMLUtils.stripHtmlTags(message);

        /*
		 * Check if the same notification has already been issued within a
         * certain amount of time. If so, ignore this one, if not, show it.
         */
		long now = System.currentTimeMillis();

		Map.Entry<String, Long> lastMessage = lastMessages.get(type);

		if (lastMessage != null)
		{
			/* If new equals old and the interval requirement is met, return */
			if (now - lastMessage.getValue() < IGNORE_LAST_MESSAGE_IF_LESS_THAN && message.equals(lastMessage.getKey()))
			{
				return;
			}
		}

        /* Save the new last message */
		lastMessages.put(type, new AbstractMap.SimpleEntry<>(message, now));

		switch (type)
		{
			case WARNING:
				showWarning(message);
				break;
			case INFO:
				showInfo(message);
				break;
			case ERROR:
				showError(message);
				break;
			case SUCCESS:
				showSuccess(message);
				break;
		}
	}

	private static native void showSuccess(String title, String message)/*-{
		$wnd.toastr.success(message,
			title);
	}-*/;

	private static native void showSuccess(String message)/*-{
		$wnd.toastr.success(message);
	}-*/;

	private static native void showError(String title, String message)/*-{
		$wnd.toastr.error(message,
			title);
	}-*/;

	private static native void showError(String message)/*-{
		$wnd.toastr.error(message);
	}-*/;

	private static native void showWarning(String title, String message)/*-{
		$wnd.toastr.warning(message,
			title);
	}-*/;

	private static native void showWarning(String message)/*-{
		$wnd.toastr.warning(message);
	}-*/;

	private static native void showInfo(String title, String message)/*-{
		$wnd.toastr.info(message,
			title);
	}-*/;

	private static native void showInfo(String message)/*-{
		$wnd.toastr.info(message);
	}-*/;

	/**
	 * Show the newest notification on top?
	 *
	 * @param onTop Show the newest notification on top?
	 */
	public static native void setNewestOnTop(boolean onTop)/*-{
		$wnd.toastr.options.newestOnTop = onTop;
	}-*/;

	/**
	 * Sets the fade in duration in milliseconds
	 *
	 * @param showDuration The fade in duration
	 */
	public static native void setShowDuration(int showDuration)/*-{
		$wnd.toastr.options.showDuration = showDuration;
	}-*/;

	/**
	 * Sets the fade out duration in milliseconds
	 *
	 * @param hideDuration The fade out duration
	 */
	public static native void setHideDuration(int hideDuration)/*-{
		$wnd.toastr.options.hideDuration = hideDuration;
	}-*/;

	/**
	 * Sets the actual notification duration in milliseconds
	 *
	 * @param timeOut The actual notification duration
	 */
	public static native void setTimeOut(int timeOut)/*-{
		$wnd.toastr.options.timeOut = timeOut;
	}-*/;

	/**
	 * Sets the position class (Any of the class position constants)
	 *
	 * @param positionClass The position class
	 */
	public static native void setPositionClass(String positionClass)/*-{
		$wnd.toastr.options.positionClass = positionClass;
	}-*/;

	/**
	 * Enable/disable the close button of the notification
	 *
	 * @param enabled Enable close button?
	 */
	public static native void setCloseButtonEnabled(boolean enabled)/*-{
		$wnd.toastr.options.closeButton = enabled;
	}-*/;

	/**
	 * Enable/disable the duplicate prevention
	 *
	 * @param preventDuplicates Enable duplicate prevention?
	 */
	public static native void setPreventDuplicates(boolean preventDuplicates)/*-{
		$wnd.toastr.options.preventDuplicates = preventDuplicates;
	}-*/;

	/**
	 * Enable/disable the progress bar
	 *
	 * @param show Enable the progress bar?
	 */
	public static native void setShowProgressBar(boolean show)/*-{
		$wnd.toastr.options.progressBar = show;
	}-*/;

	/**
	 * Positions the notification relative to the given target element
	 *
	 * @param target The target element id/class
	 */
	public static native void setTarget(String target)/*-{
		$wnd.toastr.options.target = target;
	}-*/;

	/**
	 * The available types of {@link Notification}s.
	 *
	 * @author Sebastian Raubach
	 */
	public enum Type
	{
		WARNING,
		INFO,
		ERROR,
		SUCCESS
	}

}

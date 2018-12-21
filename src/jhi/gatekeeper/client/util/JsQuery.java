/*
 * Copyright 2018 Information and Computational Sciences,
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

package jhi.gatekeeper.client.util;

import com.google.gwt.dom.client.*;

/**
 * @author Sebastian Raubach
 */
public class JsQuery
{
	public static native void hide(Element element) /*-{
		$wnd.$(element).hide();
	}-*/;

	public static native void show(Element element) /*-{
		$wnd.$(element).show();
	}-*/;

	public static native void hide(String element) /*-{
		$wnd.$(element).hide();
	}-*/;

	public static native void show(String element) /*-{
		$wnd.$(element).show();
	}-*/;

	public static native void removeClass(String selector, String clazz) /*-{
		$wnd.$(selector).removeClass(clazz);
	}-*/;

	public static native void addClass(String selector, String clazz) /*-{
		$wnd.$(selector).addClass(clazz);
	}-*/;
}

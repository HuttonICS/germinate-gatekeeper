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

package jhi.gatekeeper.client.util.callback;

import com.google.gwt.core.client.*;

import jhi.gatekeeper.client.i18n.*;
import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.event.*;

/**
 * {@link RunAsyncNotifyCallback} implements {@link RunAsyncCallback} and implements the {@link #onFailure(Throwable)} method. It will show a {@link
 * Notification} and fire a {@link LogoutEvent}.
 *
 * @author Sebastian Raubach
 */
public abstract class RunAsyncNotifyCallback implements RunAsyncCallback
{
	@Override
	public void onFailure(Throwable reason)
	{
		Notification.notify(Notification.Type.ERROR, I18n.LANG.notificationCodeDownloadFailed() + " " + I18n.LANG.notificationReloadPage());
		GatekeeperEventBus.BUS.fireEvent(new LogoutEvent());
	}
}

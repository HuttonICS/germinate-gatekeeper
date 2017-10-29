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

package jhi.gatekeeper.client.util.callback;

import com.google.gwt.user.client.rpc.*;

import jhi.gatekeeper.client.util.*;
import jhi.gatekeeper.client.util.event.*;

/**
 * {@link AsyncCallbackLogoutOnFailure} is an {@link AsyncCallback} that will take care of most failure cases. As an example: An
 * InvalidSessionException will result in the user being logged out.
 *
 * @param <T> The type of the return value that was declared in the synchronous version of the method. If the return type is a primitive, use the
 *            boxed version of that primitive (for example, an <code>int</code> return type becomes an {@link Integer} type argument, and a void
 *            return type becomes a {@link Void} type argument, which is always <code>null</code>).
 * @author Sebastian Raubach
 */
public class AsyncCallbackLogoutOnFailure<T> implements AsyncCallback<T>
{
	public AsyncCallbackLogoutOnFailure()
	{
		super();

		Cookie.extend();
	}

	@Override
	public final void onFailure(Throwable caught)
	{
		onFailureImpl(caught);
	}

	/**
	 * Called when an asynchronous call fails to complete normally. {@link IncompatibleRemoteServiceException}s, {@link InvocationException} s, or
	 * checked exceptions thrown by the service method are examples of the type of failures that can be passed to this method.
	 * <p/>
	 * If caught is an instance of an {@link IncompatibleRemoteServiceException} the application should try to get into a state where a browser
	 * refresh can be safely done.
	 *
	 * @param caught failure encountered while executing a remote procedure call
	 * @see #onFailure(Throwable)
	 */
	protected void onFailureImpl(Throwable caught)
	{
		GatekeeperEventBus.BUS.fireEvent(new ExceptionEvent(caught));
	}

	@Override
	public final void onSuccess(T result)
	{
		onSuccessImpl(result);
	}

	/**
	 * Called when an asynchronous call completes successfully.
	 *
	 * @param result the return value of the remote produced call
	 * @see #onSuccess(Object)
	 */
	protected void onSuccessImpl(T result)
	{
	}
}

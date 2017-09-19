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

package jhi.gatekeeper.client.service;

import com.google.gwt.user.client.rpc.*;

import java.util.*;

import jhi.database.shared.util.*;
import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;

public interface UserServiceAsync
{
	void getUserCount(RequestProperties properties, User searchBean, AsyncCallback<Long> async);

	void updateUserEmail(RequestProperties properties, String email, AsyncCallback<Void> async);

	void updatePassword(RequestProperties properties, UserCredentials credentials, AsyncCallback<Void> async);

	void getUserList(RequestProperties properties, User searchBean, Pagination pagination, AsyncCallback<PaginatedResult<List<User>>> async);

	void deleteUser(RequestProperties properties, long id, AsyncCallback<Void> async);

	void addUser(RequestProperties properties, User user, UserCredentials credentials, AsyncCallback<Void> async);

	void createAdmin(RequestProperties properties, User user, UserCredentials credentials, AsyncCallback<Void> async);

	void sendNewPassword(RequestProperties properties, String username, String email, AsyncCallback<Void> async);

	void activateUser(RequestProperties properties, String key, AsyncCallback<Void> async);

	void setHasAccessToGatekeeper(RequestProperties properties, User user, AsyncCallback<Void> async);

	void getUser(RequestProperties properties, AsyncCallback<User> async);
}

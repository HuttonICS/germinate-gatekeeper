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

package jhi.gatekeeper.client.i18n;

import com.google.gwt.core.shared.*;
import com.google.gwt.i18n.client.*;
import com.google.gwt.safehtml.shared.*;

/**
 * @author Sebastian Raubach
 */
public interface I18n extends Messages
{
	I18n LANG = GWT.create(I18n.class);

	@Key("generic.heading.no.data.found")
	@DefaultMessage("No data found")
	String genericHeadingNoDataFound();

	@Key("generic.date.time.format")
	@DefaultMessage("dd/MM/yyyy HH:mm:ss")
	String genericDateTimeFormat();

	@Key("generic.date.format")
	@DefaultMessage("dd/MM/yyyy")
	String genericDateFormat();

	@Key("generic.button.add")
	@DefaultMessage("Add")
	String genericButtonAdd();

	@Key("generic.button.create")
	@DefaultMessage("Create")
	String genericButtonCreate();

	@Key("generic.button.delete")
	@DefaultMessage("Delete")
	String genericButtonDelete();

	@Key("generic.button.reject")
	@DefaultMessage("Reject")
	String genericButtonReject();

	@Key("generic.button.approve")
	@DefaultMessage("Approve")
	String genericButtonApprove();

	@Key("generic.button.update")
	@DefaultMessage("Update")
	String genericButtonUpdate();

	@Key("generic.button.send")
	@DefaultMessage("Send")
	String genericButtonSend();

	@Key("generic.button.yes")
	@DefaultMessage("Yes")
	String genericButtonYes();

	@Key("generic.button.no")
	@DefaultMessage("No")
	String genericButtonNo();

	@Key("generic.button.ok")
	@DefaultMessage("OK")
	String genericButtonOk();

	@Key("generic.button.cancel")
	@DefaultMessage("Cancel")
	String genericButtonCancel();

	@Key("generic.button.close")
	@DefaultMessage("Close")
	String genericButtonClose();

	@Key("generic.text.search")
	@DefaultMessage("Search")
	String genericTextSearch();

	@Key("dialog.title.delete.user")
	@DefaultMessage("Delete user")
	String dialogTitleDeleteUser();

	@Key("dialog.title.reject.user")
	@DefaultMessage("Reject user")
	String dialogTitleRejectUser();

	@Key("dialog.message.reject.user")
	@DefaultMessage("Provide an optional rejection reason below:")
	String dialogMessageRejectUser();

	@Key("dialog.title.delete.database")
	@DefaultMessage("Delete database")
	String dialogTitleDeleteDatabase();

	@Key("dialog.message.delete.user")
	@DefaultMessage("Are you sure you want to delete this user?")
	String dialogMessageDeleteUser();

	@Key("dialog.message.delete.database")
	@DefaultMessage("Are you sure you want to delete this database system?")
	String dialogMessageDeleteDatabase();


	@Key("menu.item.user.view")
	@DefaultMessage("View user list")
	String menuItemViewUsers();

	@Key("menu.item.user.approve")
	@DefaultMessage("Approve users")
	String menuItemApproveUsers();

	@Key("menu.item.user.add")
	@DefaultMessage("Add new user")
	String menuItemAddUser();

	@Key("menu.item.databases.view")
	@DefaultMessage("View database system list")
	String menuItemViewDatabases();

	@Key("menu.item.admin.settings")
	@DefaultMessage("Account settings")
	String menuItemAdminSettings();

	@Key("menu.top.about")
	@DefaultMessage("About")
	String menuTopAbout();

	@Key("menu.top.password.forgotten")
	@DefaultMessage("Forgot your password?")
	String menuTopForgottenPassword();

	@Key("menu.top.language")
	@DefaultMessage("Select language")
	String menuTopLanguage();

	@Key("menu.top.login")
	@DefaultMessage("Login")
	String menuTopLogin();

	@Key("menu.top.logout")
	@DefaultMessage("Logout")
	String menuTopLogout();


	@Key("notification.login.successful")
	@DefaultMessage("Successfully logged in.")
	String notificationLoginSuccessful();

	@Key("notification.session.invalid")
	@DefaultMessage("Invalid session.")
	String notificationInvalidSession();

	@Key("notification.payload.invalid")
	@DefaultMessage("Invalid session payload.")
	String notificationInvalidPayload();

	@Key("notification.cookie.invalid")
	@DefaultMessage("Invalid session cookie.")
	String notificationInvalidCookie();

	@Key("notification.fill.all.fields")
	@DefaultMessage("Please fill in all fields.")
	String notificationFillAllFields();

	@Key("notification.login.password.invalid")
	@DefaultMessage("Invalid password.")
	String notificationLoginInvalidPassword();

	@Key("notification.login.username.invalid")
	@DefaultMessage("Invalid username.")
	String notificationLoginInvalidUsername();

	@Key("notification.login.unsuccessful")
	@DefaultMessage("Login unsuccessful.")
	String notificationLoginUnsuccessful();

	@Key("notification.login.permissions.insufficient")
	@DefaultMessage("Insufficient permissions.")
	String notificationInsufficientPermissions();

	@Key("notification.login.user.suspended")
	@DefaultMessage("User has been suspended.")
	String notificationLoginSuspendedUser();

	@Key("notification.code.download.failed")
	@DefaultMessage("The code download failed.")
	String notificationCodeDownloadFailed();

	@Key("notification.page.reload")
	@DefaultMessage("Please reload the page.")
	String notificationReloadPage();

	@Key("notification.page.unavailable")
	@DefaultMessage("The selected page is unavailable.")
	String notificationPageUnavailable();

	@Key("notification.user.email.changed")
	@DefaultMessage("Email address successfully changed.")
	String notificationUserEmailChanged();

	@Key("notification.user.passwords.dont.match")
	@DefaultMessage("Passwords don''t match.")
	String notificationUserPasswordsDontMatch();

	@Key("notification.user.password.changed")
	@DefaultMessage("Password successfully changed. Please log out and back in.")
	String notificationUserPasswordChanged();

	@Key("notification.no.data.found")
	@DefaultMessage("No data found.")
	String notificationNoDataFound();

	@Key("notification.error.database")
	@DefaultMessage("A database error occured.")
	String notificationDatabaseError();

	@Key("notification.error.server.unspecified")
	@DefaultMessage("An unspecified error occured on the server.")
	String notificationUnspecifiedServerError();

	@Key("notification.error.unknown")
	@DefaultMessage("An unknown error occured: {0}")
	String notificationUnknownError(String name);

	@Key("notification.database.permissions.updated")
	@DefaultMessage("Database permission successfully updated.")
	String notificationDatabasePermissionsUpdated();

	@Key("notification.no.database.system.found")
	@DefaultMessage("No existing database system found. Please create a new one.")
	String notificationNoDatabaseSystemFound();

	@Key("notification.database.system.added")
	@DefaultMessage("Database system successfully added.")
	String notificationDatabaseSystemAdded();

	@Key("notification.database.system.exists")
	@DefaultMessage("Database system already exists.")
	String notificationDatabaseSystemExists();

	@Key("notification.no.institution.found")
	@DefaultMessage("No existing institution found. Please create a new one.")
	String notificationNoInstitutionFound();

	@Key("notification.user.added")
	@DefaultMessage("User successfully added.")
	String notificationUserAdded();

	@Key("notification.user.exists")
	@DefaultMessage("A user with this name already exists.")
	String notificationUserExists();

	@Key("notification.user.not.exists")
	@DefaultMessage("No user with this username exists.")
	String notificationUserNotExist();

	@Key("notification.admin.account.exists")
	@DefaultMessage("An admin account already exists.")
	String notificationAdminAccountExists();

	@Key("notification.admin.account.created")
	@DefaultMessage("Admin account successfully created.")
	String notificationAdminAccountCreated();

	@Key("notification.email.error")
	@DefaultMessage("Error while sending email: {0}")
	String notificationEmailError(String message);

	@Key("notification.user.new.password.sent")
	@DefaultMessage("A new password has been sent to your email address.")
	String notificationUserNewPasswordSent();


	@Key("widget.pager.number.format")
	@DefaultMessage("#,###")
	String pagerNumberFormat();

	@Key("widget.pager.of")
	@DefaultMessage("\u0020of\u0020")
	String pagerOf();

	@Key("widget.pager.of.over")
	@DefaultMessage("\u0020of over\u0020")
	String pagerOfOver();


	@Key("page.login.heading")
	@DefaultMessage("Please log in")
	String loginHeading();

	@Key("page.login.button.login")
	@DefaultMessage("Login")
	String loginButton();

	@Key("page.login.label.username")
	@DefaultMessage("Username")
	String loginUsernameLabel();

	@Key("page.login.label.password")
	@DefaultMessage("Password")
	String loginPasswordLabel();

	@Key("page.settings.password.update.label")
	@DefaultMessage("Update password")
	String updatePasswordLabel();

	@Key("page.settings.email.update.label")
	@DefaultMessage("Update email address")
	String updateEmailLabel();

	@Key("page.settings.password.update.hint")
	@DefaultMessage("Password")
	String updatePasswordHint();

	@Key("page.settings.password.update.confirm.hint")
	@DefaultMessage("Confirm password")
	String updatePasswordConfirmHint();


	@Key("page.view.user.details.heading")
	@DefaultMessage("User details")
	String viewUserDetailsHeading();

	@Key("page.view.user.details.placeholder")
	@DefaultMessage("Please select a user from the list.")
	String viewUserDetailsPlaceholder();

	@Key("page.view.user.details.title.username")
	@DefaultMessage("Username")
	String viewUserDetailsTitleUsername();

	@Key("page.view.user.details.title.full.name")
	@DefaultMessage("Full name")
	String viewUserDetailsTitleFullName();

	@Key("page.view.user.details.title.email.address")
	@DefaultMessage("Email address")
	String viewUserDetailsTitleEmailAddress();

	@Key("page.view.user.details.title.institution")
	@DefaultMessage("Institution")
	String viewUserDetailsTitleInstitution();

	@Key("page.view.user.details.title.created.on")
	@DefaultMessage("Created on")
	String viewUserDetailsTitleCreatedOn();

	@Key("page.view.user.details.title.requests.access.to")
	@DefaultMessage("Requests access to")
	String viewUserDetailsTitleRequestsAccessTo();

	@Key("page.view.user.permissions.heading")
	@DefaultMessage("User permissions")
	String viewUserPermissionsHeading();

	@Key("page.view.user.delete.heading")
	@DefaultMessage("Delete user")
	String viewUserDeleteHeading();

	@Key("page.view.user.delete.text")
	@DefaultMessage("Click on the button below to delete this user account.")
	String viewUserDeleteText();

	@Key("page.view.user.grant.permission.heading")
	@DefaultMessage("Grant permission")
	String viewUserGrantPermissionHeading();

	@Key("page.view.user.grant.permission.existing")
	@DefaultMessage("Existing")
	String viewUserAddTabExisting();

	@Key("page.view.user.grant.permission.new")
	@DefaultMessage("New")
	String viewUserAddTabNew();

	@Key("page.view.user.grant.permission.new.database.placeholder")
	@DefaultMessage("Database")
	String viewUserNewDatabaseNamePlaceholder();

	@Key("page.view.user.grant.permission.new.user.type.placeholder")
	@DefaultMessage("User type")
	String viewUserNewUserTypePlaceholder();

	@Key("page.view.user.grant.permission.new.user.placeholder")
	@DefaultMessage("User")
	String viewUserNewUserPlaceholder();

	@Key("page.view.user.grant.permission.new.server.placeholder")
	@DefaultMessage("Server")
	String viewUserNewServerNamePlaceholder();

	@Key("page.view.user.grant.permission.new.description.placeholder")
	@DefaultMessage("Description")
	String viewUserNewDescriptionPlaceholder();

	@Key("page.view.user.gatekeeper.access.heading")
	@DefaultMessage("Access to Gatekeeper")
	String viewUserGatekeeperAccessHeading();

	@Key("page.view.user.gatekeeper.access.text")
	@DefaultMessage("Should this user be allowed to change their email address and password?")
	String viewUserGatekeeperAccessText();


	@Key("table.header.database.permissions.system.name")
	@DefaultMessage("System name")
	String tableHeaderDatabasePermissionsSystemName();

	@Key("table.header.database.permissions.server.name")
	@DefaultMessage("Server name")
	String tableHeaderDatabasePermissionsServerName();

	@Key("table.header.database.permissions.system.description")
	@DefaultMessage("Description")
	String tableHeaderDatabasePermissionsSystemDescription();

	@Key("table.header.database.permissions.user.type")
	@DefaultMessage("User type")
	String tableHeaderDatabasePermissionsUserType();

	@Key("table.header.database.permissions.delete")
	@DefaultMessage("Delete")
	String tableHeaderDatabasePermissionsDelete();


	@Key("page.database.systems.heading")
	@DefaultMessage("Database systems")
	String databaseSystemHeading();

	@Key("page.database.system.users.heading")
	@DefaultMessage("Database system users")
	String databaseSystemUserHeading();

	@Key("page.database.systems.add.new.heading")
	@DefaultMessage("Add database system")
	String addDatabaseSystemHeading();

	@Key("page.create.user.heading")
	@DefaultMessage("User details")
	String addUserHeading();

	@Key("page.create.user.username.placeholder")
	@DefaultMessage("Username")
	String addUserUsernamePlaceholder();

	@Key("page.create.user.email.placeholder")
	@DefaultMessage("Email")
	String addUserEmailPlaceholder();

	@Key("page.create.user.password.placeholder")
	@DefaultMessage("Password")
	String addUserPasswordPlaceholder();

	@Key("page.create.user.password.confirm.placeholder")
	@DefaultMessage("Confirm password")
	String addUserPasswordConfirmPlaceholder();

	@Key("page.create.user.full.name.placeholder")
	@DefaultMessage("Full name")
	String addUserFullNamePlaceholder();

	@Key("page.create.user.institution.heading")
	@DefaultMessage("Select an institution")
	String addUserInstitutionHeading();

	@Key("page.create.user.institution.name.placeholder")
	@DefaultMessage("Institution name")
	String addUserInstitutionNamePlaceholder();

	@Key("page.create.user.institution.acronym.placeholder")
	@DefaultMessage("Institution acronym")
	String addUserInstitutionAcronymPlaceholder();

	@Key("page.create.user.institution.address.placeholder")
	@DefaultMessage("Institution address")
	String addUserInstitutionAddressPlaceholder();


	@Key("page.approve.user.title")
	@DefaultMessage("New user requests")
	String approveUserTitle();

	@Key("page.access.request.title")
	@DefaultMessage("Existing user requests")
	String accessRequestTitle();

	@Key("page.approve.user.heading")
	@DefaultMessage("Approve user requests")
	String approveUserDecisionHeading();

	@Key("page.approve.user.text")
	@DefaultMessage("Please select one of the three options below.")
	String approveUserDecisionText();


	@Key("page.create.admin.heading")
	@DefaultMessage("Create admin account")
	String createAdminHeading();


	@Key("page.password.forgotten.heading")
	@DefaultMessage("Lost password")
	String forgottenPasswordHeading();

	@Key("page.password.forgotten.username.placeholder")
	@DefaultMessage("Username")
	String forgottenPasswordUsernamePlaceholder();

	@Key("page.password.forgotten.email.placeholder")
	@DefaultMessage("Email address")
	String forgottenPasswordEmailPlaceholder();


	@Key("page.activate.user.heading")
	@DefaultMessage("Activate user")
	String activateUserHeading();

	@Key("page.activate.user.label.processing")
	@DefaultMessage("We''re processing your request.")
	String activateUserProcessing();

	@Key("page.activate.user.label.failed")
	@DefaultMessage("User activation failed. Please contact an administrator: {0}")
	String activateUserActivationFailed(String error);

	@Key("page.activate.user.label.successful")
	@DefaultMessage("User activation successful. Enjoy your Germinate experience.")
	String activateUserActivationSuccessful();

	@Key("page.activate.user.label.invalid.request")
	@DefaultMessage("The given key is not valid. No user has been activated.")
	String activateUserInvalidRequest();


	@Key("page.about.heading")
	@DefaultMessage("About Version")
	String aboutHeading();

	@Key("page.about.message")
	@DefaultMessage("<div style=''text-align: center; padding-bottom: 15px;''><img src=''img/gatekeeper.svg'' style=''width: 300px; max-width: 100%;''></div><p><b>Copyright © 2013-{1} Information and Computational Sciences, The James Hutton Institute</b></p><p><b>Germinate Gatekeeper</b> is written and developed by <b>Sebastian Raubach</b>, <b>Toby Philp</b> and <b>Paul Shaw</b> from the Information and Computational Sciences Group at JHI Dundee.</p><p>While we firmly believe in collaborative science we appreciate there are times when data is private and needs to be kept that way. Because of this we have implemented Germinate Gatekeeper. Gatekeeper acts as an authentication mechanism for the Germinate 3 system and allows restricted access to a Germinate database. Trusted users are provided with usernames and they can log in as required. Users can also change their passwords without manual intervention.</p><p>Germinate Gatekeeper uses a key derivation function for passwords. Any password you enter into this system is not available to administrators of this site so we cannot tell you your password if you have forgotten it or mail this information to you. If you have lost or forgotten your password we can temporarily set this to a new password which you can use to gain entry to your account to allow you to set a new password. To use this feature click on the <mark>{0}</mark> link in the top menu when you''re on the login page.</p>")
	SafeHtml aboutMessage(String forgottenPasswordLinkText, String year);

	@Key("cookie.policy.title")
	@DefaultMessage("Cookie Policy")
	String cookiePolicyTitle();

	@Key("cookie.policy.text")
	@DefaultMessage("<p>This website''s sole use of cookies (local storage) is to remember user settings between visits.</p><p>You can reject these cookies if you wish (and it won''t affect your usage of the site) either by blocking them using the options in your browser, or by installing plugins such as the <a href=\"https://tools.google.com/dlpage/gaoptout?hl=en\" target=\"_self\">Google Analytics Opt-out Browser Add-on</a> or <a href=\"https://www.ghostery.com/\" target=\"_blank\">Ghostery</a>.</p><p>You may also find the following links useful:</p><ul><li><a href=\"http://www.ico.gov.uk/for_organisations/privacy_and_electronic_communications/the_guide/cookies.aspx\">ICO Cookie Regulations and the EU Cookie Law</a></li></ul>")
	SafeHtml cookiePolicyText();
}
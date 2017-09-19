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

package jhi.gatekeeper.server.util;

import java.util.*;

import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.*;

import jhi.gatekeeper.shared.*;
import jhi.gatekeeper.shared.bean.*;
import jhi.gatekeeper.shared.exception.*;

/**
 * @author Sebastian Raubach
 */
public class Email
{
	public static void sendAdministratorNotification(Locale locale, AccessRequest request, boolean needsReview) throws EmailException
	{
		final EmailProperties config = EmailProperties.get();

		if (config == null)
			throw new EmailException("Invalid email properties");

		if (needsReview)
			send(config.mailAddress, I18n.getString(Locale.ENGLISH, I18n.EMAIL_TITLE_USER_REGISTRATION_ADMIN_NOTIFICATION), I18n.getString(Locale.ENGLISH, I18n.EMAIL_MESSAGE_USER_REGISTRATION_ADMIN_NOTIFICATION, request.getDatabaseSystem().getMeaningfulDescription()));
		else
			send(config.mailAddress, I18n.getString(Locale.ENGLISH, I18n.EMAIL_TITLE_USER_ACTIVATED_AUTOMATICALLY_ADMIN_NOTIFICATION), I18n.getString(Locale.ENGLISH, I18n.EMAIL_MESSAGE_USER_ACTIVATED_AUTOMATICALLY_ADMIN_NOTIFICATION, request.getDatabaseSystem().getMeaningfulDescription()));
	}

	public static void sendAdministratorNotification(Locale locale, UnapprovedUser user) throws EmailException
	{
		final EmailProperties config = EmailProperties.get();

		if (config == null)
			throw new EmailException("Invalid email properties");

		send(config.mailAddress, I18n.getString(Locale.ENGLISH, I18n.EMAIL_TITLE_USER_REGISTRATION_ADMIN_NOTIFICATION), I18n.getString(Locale.ENGLISH, I18n.EMAIL_MESSAGE_USER_REGISTRATION_ADMIN_NOTIFICATION, user.getDatabaseSystem().getMeaningfulDescription()));
	}

	public static void sendNewPassword(Locale locale, UserInternal user, String password) throws EmailException
	{
		send(user.getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_NEW_PASSWORD), I18n.getString(locale, I18n.EMAIL_MESSAGE_NEW_PASSWORD, password));
	}

	public static void sendActivationConfirmation(Locale locale, User user) throws EmailException
	{
		send(user.getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_ACTIVATED), I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_ACTIVATED));
	}

	public static void sendAwaitingApproval(Locale locale, AccessRequest request) throws EmailException
	{
		send(request.getUser().getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_REQUEST_PENDING), I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_PENDING));
	}

	public static void sendAwaitingApproval(Locale locale, UnapprovedUser user) throws EmailException
	{
		send(user.getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_REQUEST_PENDING), I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_PENDING));
	}

	public static void sendActivationPrompt(Locale locale, UnapprovedUser user, String url) throws EmailException
	{
		send(user.getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_ACTIVATION_PROMPT), I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_ACTIVATION_PROMPT, url));
	}

	public static void sendAccessRequestApproved(Locale locale, AccessRequest request) throws EmailException
	{
		send(request.getUser().getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_REQUEST_APPROVED), I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_APPROVED, request.getDatabaseSystem().getMeaningfulDescription()));
	}

	public static void sendAccessRequestRejected(Locale locale, AccessRequest request, String rejectionReason) throws EmailException
	{
		String message;
		if (!StringUtils.isEmpty(rejectionReason))
			message = I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED, I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED_REASON, rejectionReason));
		else
			message = I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED, I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED_NO_REASON));

		send(request.getUser().getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_REQUEST_REJECTED), message);
	}

	public static void sendUnapprovedUserApproved(Locale locale, UnapprovedUser user) throws EmailException
	{
		send(user.getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_REQUEST_APPROVED), I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_APPROVED, user.getDatabaseSystem().getMeaningfulDescription()));
	}

	public static void sendUnapprovedUserRejected(Locale locale, UnapprovedUser user, String rejectionReason) throws EmailException
	{
		String message;
		if (!StringUtils.isEmpty(rejectionReason))
			message = I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED, I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED_REASON, rejectionReason));
		else
			message = I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED, I18n.getString(locale, I18n.EMAIL_MESSAGE_USER_REQUEST_REJECTED_NO_REASON));

		send(user.getEmail(), I18n.getString(locale, I18n.EMAIL_TITLE_USER_REQUEST_REJECTED), message);
	}

	/**
	 * Sends an email
	 *
	 * @param to          The recipient
	 * @param subject     The email subject
	 * @param htmlMessage The email message (HTML formatted)
	 * @throws EmailException Thrown if sending the email fails
	 */
	public static void send(String to, String subject, String htmlMessage) throws EmailException
	{
		try
		{
			final EmailProperties config = EmailProperties.get();

			if (config == null)
				throw new MessagingException("Invalid email properties");

			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", config.mailServer);
			props.put("mail.smtp.port", StringUtils.isEmpty(config.mailPort) ? "587" : config.mailPort);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			Session session;

			if (StringUtils.isEmpty(config.username, config.password))
			{
				props.put("mail.smtp.auth", "false");
				session = Session.getInstance(props);
			}
			else
			{
				props.put("mail.smtp.auth", "true");
				session = Session.getInstance(props, new javax.mail.Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(config.username, config.password);
					}
				});
			}

			Message message = new MimeMessage(session);
			message.setContent(htmlMessage, "text/html; charset=utf-8");
			message.setFrom(new InternetAddress(config.mailAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);

			/* Send the email */
			Transport.send(message);
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
			throw new EmailException(e);
		}
	}

	public static class EmailProperties
	{
		public String mailServer;
		public String mailAddress;
		public String username;
		public String password;
		public String mailPort;

		public EmailProperties()
		{
		}

		public EmailProperties(String mailServer, String mailAddress, String username, String password, String mailPort)
		{
			this.mailServer = mailServer;
			this.mailAddress = mailAddress;
			this.username = username;
			this.password = password;
			this.mailPort = mailPort;
		}

		public static EmailProperties get()
		{
			EmailProperties result = new EmailProperties();

			result.mailServer = PropertyReader.getProperty(PropertyReader.EMAIL_SERVER);
			result.mailAddress = PropertyReader.getProperty(PropertyReader.EMAIL_ADDRESS);
			result.username = PropertyReader.getProperty(PropertyReader.EMAIL_USERNAME);
			result.password = PropertyReader.getProperty(PropertyReader.EMAIL_PASSWORD);
			result.mailPort = PropertyReader.getProperty(PropertyReader.EMAIL_PORT);

			if (!result.isValid())
				return null;
			else
				return result;
		}

		public boolean isValid()
		{
			/* Check if any of the properties doesn't exist */
			if (mailServer == null || mailAddress == null || username == null || password == null)
				return false;

			/* Note the missing password. Sometimes an empty (but existing) password is valid */
			return !StringUtils.isEmpty(mailServer, mailAddress, username);
		}

		@Override
		public String toString()
		{
			return "EmailProperties{" +
					"mailServer='" + mailServer + '\'' +
					", mailAddress='" + mailAddress + '\'' +
					", username='" + username + '\'' +
					", password='" + password + '\'' +
					", mailPort='" + mailPort + '\'' +
					'}';
		}
	}
}

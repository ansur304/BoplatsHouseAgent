package com.job;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Component;

@Component
public class SendEmail {

	public SendEmail() {
		super();
	}

	static long startTime;
	static int hour, min;
	static List<String> appliedHouses;

	public void sendEmails(int housesApplied, String toEmail, String logName) {
		final String userNameY = "ansur304";
		final String passwordY = "@nsari985";

		String fromEmail = "ansur304@yahoo.com";
		// String toEmail1 ="ansur304@gmail.com";
		String toEmail2 = toEmail;

		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		// prop.setProperty("mail.smtp.host", "smtp.gmail.com"); // set
		// fromEmail, toEmail accordingly
		prop.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
		prop.setProperty("mail.smtp.port", "587");

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userNameY, passwordY);
			}
		});

		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(fromEmail));
			// msg.addRecipient(Message.RecipientType.TO, new
			// InternetAddress(toEmail1));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail2));
			msg.setSubject("Boplats Job - Total Houses Applied :: " + housesApplied);

			Multipart emailContent = new MimeMultipart();

			MimeBodyPart bodyText = new MimeBodyPart();
			bodyText.setText(
					"Please find the attached report of Boplats Job. Total houses applied are :: " + housesApplied);

			MimeBodyPart attachments = new MimeBodyPart();
			try {
				attachments.attachFile("./" + logName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			emailContent.addBodyPart(bodyText);
			emailContent.addBodyPart(attachments);

			msg.setContent(emailContent);
			Transport.send(msg);
			System.out.println("Sent Successfully.");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}

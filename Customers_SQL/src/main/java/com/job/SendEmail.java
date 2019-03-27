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

import com.model.Customer;


@Component
public class SendEmail {

    public SendEmail() {
		super();
		// TODO Auto-generated constructor stub
	}

	static long startTime;
    static int hour, min;
    static List<String> appliedHouses;
    
    public static void sendPlainTextEmail(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String message) throws AddressException,
            MessagingException {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
// *** BEGIN CHANGE
        properties.put("mail.smtp.user", userName);

        // creates a new session, no Authenticator (will connect() later)
        Session session = Session.getDefaultInstance(properties);
// *** END CHANGE

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        //msg.setSentDate(new Date());
        // set plain text message
        msg.setText(message);

// *** BEGIN CHANGE
        // sends the e-mail
        Transport t = session.getTransport("smtp");
        t.connect(host, userName, password);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();

// *** END CHANGE
    }

//        public static void main(String[] args) throws InterruptedException {
//            // TODO Auto-generated method stub
//        System.setProperty("webdriver.chrome.driver", "C:\\Dev_stuff\\Workspace\\HouseAgent\\chromedriver.exe");
//            WebDriver driver = new ChromeDriver();
//            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
//            driver.get("https://accounts.google.com/ServiceLogin?");
//            // gmail login
//        driver.findElement(By.id("Email")).sendKeys("ansur304@gmail.com");
//            driver.findElement(By.id("next")).click();
//            Thread.sleep(1000);
//        driver.findElement(By.id("Passwd")).sendKeys("@nsari985");
//            driver.findElement(By.id("signIn")).click();
//
//            // some optional actions for reaching gmail inbox
//            driver.findElement(By.xpath("//*[@title='Google apps']")).click();
//            driver.findElement(By.id("gb23")).click();
//            // clicks compose
//            driver.findElement(By.cssSelector(".T-I.J-J5-Ji.T-I-KE.L3")).click();
//            // types message in body without hampering signature
//            driver.findElement(By.id(":pg")).sendKeys("This is an auto-generated mail");;
//
//        }


    private static void sendEmail(int count) {

        String USER_NAME = "ansur304"; // GMail user name (just the part before "@gmail.com")
        String PASSWORD = "@nsari985"; // GMail password
        String RECIPIENT = "ansur304@gmail.com";
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = {RECIPIENT}; // list of recipient email addresses
        String subject = "Boplats Alert Houses";
        String body = "Applied Houses :: " + count + " Time :: " + hour + ":" + min;

        sendFromGMail(from, pass, to, subject, body);
    }

    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "173.194.202.109";//"173.194.202.108";//"smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            System.out.println("Successfully Connected");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            System.out.println("Connection Failed");
            me.printStackTrace();
        }
    }

    public void sendEmails(int housesApplied, String toEmail, String logName){
    	final String userNameY ="ansur304";
		final String passwordY="@nsari985";
		
		String fromEmail ="ansur304@yahoo.com";
//		String toEmail1 ="ansur304@gmail.com";
		String toEmail2 =toEmail;
		
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		//prop.setProperty("mail.smtp.host", "smtp.gmail.com"); // set fromEmail, toEmail accordingly
		prop.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
		prop.setProperty("mail.smtp.port", "587");
		
		Session session = Session.getInstance(prop, new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication (){
				return new PasswordAuthentication(userNameY, passwordY);
			}
		});
		
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(fromEmail));
//			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail1));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail2));
			msg.setSubject("Boplats Job - Total Houses Applied :: "+housesApplied);			
			
			Multipart emailContent = new MimeMultipart();
			
			MimeBodyPart bodyText = new MimeBodyPart();
			bodyText.setText("Please find the attached report of Boplats Job. Total houses applied are :: "+housesApplied);
			
			MimeBodyPart attachments = new MimeBodyPart();
			try {
				attachments.attachFile("./"+logName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			emailContent.addBodyPart(bodyText);
			emailContent.addBodyPart(attachments);
			
			msg.setContent(emailContent);
			Transport.send(msg);
			System.out.println("Sent Successfully.");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

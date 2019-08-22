package com.dachen.springweb.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class AliMailUtil {

    public static void main(String[] args) throws Exception {
        sendMail("zhongh08@163.com","测试主题","测试内容");
    }

    public static void sendMail(String toUser, String subject, String content) {
        EmailUtil util = new EmailUtil();
        EmailInfo info = new EmailInfo(toUser, subject, content);
        util.sendHtmlMail(info);
    }

    static class EmailInfo {
        private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        private String smtpServer = "smtp.hwcor.com";
        // SMTP服务器地址
        private String port = "465"; // 端口
        private String fromUserName = "hui.zhong@hwcor.com";
        // 登录SMTP服务器的用户名,发送人邮箱地址
        private String fromUserPassword = "Ali2012fupai";
        // 登录SMTP服务器的密码
        private String toUser;   // 收件人
        private String subject; // 邮件主题
        private String content; // 邮件正文

        public EmailInfo() {
        }

        public EmailInfo(String toUser, String subject, String content) {
            this.toUser = toUser;
            this.subject = subject;
            this.content = content;
            this.smtpServer = smtpServer;
            this.port = port;
            this.fromUserName = fromUserName;
            this.fromUserPassword = fromUserPassword;
        }

        public String getSSL_FACTORY() {
            return SSL_FACTORY;
        }

        public String getSmtpServer() {
            return smtpServer;
        }

        public void setSmtpServer(String smtpServer) {
            this.smtpServer = smtpServer;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getFromUserPassword() {
            return fromUserPassword;
        }

        public void setFromUserPassword(String fromUserPassword) {
            this.fromUserPassword = fromUserPassword;
        }

        public String getToUser() {
            return toUser;
        }

        public void setToUser(String toUser) {
            this.toUser = toUser;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    static class EmailUtil {
        /**
         * 进行base64加密，防止中文乱码
         */
        private static String changeEncode(String str) {
            try {
                str = MimeUtility.encodeText(new String(str.getBytes(), "UTF-8"), "UTF-8", "B"); // "B"代表Base64
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return str;
        }

        public static boolean sendHtmlMail(EmailInfo emailInfo) {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", emailInfo.getSmtpServer());
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // 使用JSSE的SSL
            properties.put("mail.smtp.socketFactory.fallback", "false"); // 只处理SSL的连接,对于非SSL的连接不做处理
            properties.put("mail.smtp.port", emailInfo.getPort());
            properties.put("mail.smtp.socketFactory.port",emailInfo.getPort());
            Session session = Session.getInstance(properties);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);

            try {
                // 发件人
                Address address = new InternetAddress(emailInfo.getFromUserName());
                message.setFrom(address);
                // 收件人
                Address toAddress = new InternetAddress(emailInfo.getToUser());
                message.setRecipient(MimeMessage.RecipientType.TO, toAddress); // 设置收件人,并设置其接收类型为TO

                // 主题message.setSubject(changeEncode(emailInfo.getSubject()));
                message.setSubject(emailInfo.getSubject());
                // 时间
                message.setSentDate(new Date());

                Multipart multipart = new MimeMultipart();

                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(emailInfo.getContent(), "text/html; charset=utf-8");
                multipart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                message.setContent(multipart);
                message.saveChanges();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            try {
                Transport transport = session.getTransport("smtp");
                transport.connect(emailInfo.getSmtpServer(), emailInfo.getFromUserName(), emailInfo.getFromUserPassword());
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

}






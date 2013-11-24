package com.leaderm.infra.mail;

import java.util.HashMap;
import java.util.Map;
 
/**
 * This program tests out the EmbeddedImageEmailUtil utility class.
 * @author www.codejava.net
 *
 */
public class InlineImageEmailTester {
 
    /**
     * main entry of the program
     */
    public static void main(String[] args) {
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "nir.armon33@gmail.com";
        String password = "Tammy1903";
 
        // message info
        String mailTo = "nir.armon33@gmail.com";
        String subject = "Test e-mail with inline images";
        StringBuffer body
            = new StringBuffer("<html>This message contains two inline images.<br>");
        body.append("The first image is a chart:<br>");
        body.append("<img src=\"cid:image1\" width=\"30%\" height=\"30%\" /><br>");
        body.append("The second one is a cube:<br>");
        body.append("<img src=\"cid:image2\" width=\"15%\" height=\"15%\" /><br>");
        body.append("End of message.");
        body.append("</html>");
 
        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
        inlineImages.put("image1", "C:/Users/nir_a/Documents/GitHub/leader-m-project/Google.png.jpg");
      
        try {
            EmbeddedImageEmailUtil.send(host, port, mailFrom, password, mailTo,
                subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }
}

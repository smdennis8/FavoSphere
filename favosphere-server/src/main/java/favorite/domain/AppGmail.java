package favorite.domain;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import favorite.models.Link;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.google.api.services.gmail.GmailScopes.MAIL_GOOGLE_COM;
import static javax.mail.Message.RecipientType.TO;

@Component
public class AppGmail {

    private final String APP_INBOX_EMAIL = "favosphere.app.inbox@gmail.com";
    private final Gmail service;

    public AppGmail() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        this.service = new Gmail.Builder(httpTransport, jsonFactory,
                getCredentials(httpTransport, jsonFactory))
                .setApplicationName("FavoSphere")
                .build();
    }

    private Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(GsonFactory.getDefaultInstance(),
                        new InputStreamReader(
                                AppGmail.class.getResourceAsStream(
                                        "/client_secret_193661643963-aais053pvf4toqf6m46tvl39t3sf2m74.apps.googleusercontent.com.json")));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(MAIL_GOOGLE_COM))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendGmailEmail(String subject, String message) throws Exception {

        // Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(APP_INBOX_EMAIL));
        email.addRecipient(TO, new InternetAddress(APP_INBOX_EMAIL));
        email.setSubject(subject);
        email.setText(message);

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message(); //gmail object
        msg.setRaw(encodedEmail);

        try {
            // Create send message
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }

    public List<Link> getAllInboxLinks(boolean deleteRead) throws IOException {

        ArrayList<Link> linkList = new ArrayList<>();
        List<String> msgIds = findAllInboxMessageIds();

        for (String msgId : msgIds) {

            Message msg = service.users().messages().get(APP_INBOX_EMAIL,msgId).execute();
            String snippet = msg.getSnippet().split(" ")[0];

            if(isValidURL(snippet)) {
                Link msgData = getEmailMessageData(msg);
                linkList.add(msgData);
            }
        }

        if(deleteRead) deleteGmailInboxMessages(msgIds);

        return linkList;
    }

    public List<Link> getUserInboxLinks(String userEmail, boolean deleteRead) throws IOException {

        ArrayList<Link> linkList = new ArrayList<>();
        List<String> msgIds = findAllInboxMessageIds();
        List<String> userMsgIds = new ArrayList<>();

        for (String msgId : msgIds) {

            Message msg = service.users().messages().get(APP_INBOX_EMAIL, msgId).execute();

            String emailSender = null;
            for (MessagePartHeader mph : msg.getPayload().getHeaders()) {
                if (mph.getName().equalsIgnoreCase("From")) {
                    emailSender = mph.getValue().split("<")[1].split(">")[0];
                    break;
                }
            }

            if (emailSender.equalsIgnoreCase(userEmail)) {
                userMsgIds.add(msgId);
                String snippet = msg.getSnippet().split(" ")[0];
                if (isValidURL(snippet)) {
                    Link msgData = getEmailMessageData(msg);
                    linkList.add(msgData);
                }
            }
        }

        if(deleteRead) deleteGmailInboxMessages(userMsgIds);

        return linkList;
    }

    private List<String> findAllInboxMessageIds() throws IOException {
        ListMessagesResponse response = service.users().messages().list(APP_INBOX_EMAIL).execute();
        ArrayList<String> msgIds = new ArrayList<>();
        for (Message msg : response.getMessages()) {
            msgIds.add(msg.getId());
        }
        return msgIds;
    }

    private void deleteGmailInboxMessages(List<String> msgIds) throws IOException {

        if(msgIds != null && msgIds.size() > 0) {
            for (String msgId : msgIds) {
                service.users().messages().trash(APP_INBOX_EMAIL, msgId).execute();
            }
        }
    }

    private static Link getEmailMessageData(Message msg) {
        // Returns: senderEmail, LocalDateTimeSent, urlMsgPayload
        String dateSent = ". . . .";
        String senderEmail = "";

        List<MessagePartHeader> msgHeaders = msg.getPayload().getHeaders();
        for (MessagePartHeader mph : msgHeaders) {
            if(mph.getName().equalsIgnoreCase("Date")) dateSent = mph.getValue();
            if(mph.getName().equalsIgnoreCase("From")) senderEmail = mph.getValue().split("<")[1].split(">")[0];
        }
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
        String[] months = dfs.getShortMonths();
        int monthNum = 0;
        String[] googleMsgSentDate = dateSent.split(" ");
        for (int i = 0; i < months.length; i++) {
            if (googleMsgSentDate[2].equalsIgnoreCase(months[i])) monthNum = i + 1;
        }

        LocalDate msgDate = LocalDate.of(
                Integer.parseInt(googleMsgSentDate[3]),monthNum,Integer.parseInt(googleMsgSentDate[1]));
        LocalTime msgTime = LocalTime.parse(dateSent.split(" ")[4]);
        LocalDateTime localDateTimeSent = LocalDateTime.of(msgDate,msgTime);

        String urlMsgPayload = msg.getSnippet().split(" ")[0];

        return new Link(senderEmail, urlMsgPayload, localDateTimeSent);
    }

    private boolean isValidURL(String url){
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }
}
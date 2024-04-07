package client.scenes;

import client.utils.ServerUtils;
import commons.Currency;
import commons.User;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class StartPageCtrl {

    private MainCtrl mainCtrl;
    @FXML
    private TextField firstname;
    private List<String> fnText = new ArrayList<>(List.of("First name",
            "Voornaam"));
    @FXML
    private Label title;
    private List<String> titleText = new ArrayList<>(List.of("Sign Up",
            "Begin"));
    @FXML
    private TextField lastname;
    private List<String> lnText = new ArrayList<>(List.of("Last name",
            "Achternaam"));
    @FXML
    private TextField email;
    private List<String> emText = new ArrayList<>(List.of("Email (optional)",
            "Email (optioneel)"));
    @FXML
    private ComboBox<Currency> currencyComboBox;
    private List<String> cText = new ArrayList<>(List.of("Currency",
            "Munteenheid"));
    @FXML
    private ComboBox<String> languageComboBox;
    private List<String> lText = new ArrayList<>(List.of("Language",
            "Taal"));
    private ServerUtils server;
    List<String> languages;

    @Inject
    public StartPageCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void createUser(ActionEvent event) throws IOException {
        try {
            if (!isValidInput())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("User Creation Warning");
                alert.setContentText("Please fill all necessary fields");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("User Creation Confirmation Alert");
            alert.setContentText("Do you want to create this user?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                String emailHolder = "-1";
                if (!(email.getText().isEmpty())) {
                    emailHolder = email.getText();
                    sendWelcomeMail(emailHolder, firstname.getText());
                }
                User u = new User(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData());

                u = server.addUser(firstname.getText(), lastname.getText(), emailHolder, getCurrencyData());
                File file = mainCtrl.getUserConfig();
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter pw = new PrintWriter(file);
                pw.println(mainCtrl.getLanguageIndex());
                pw.println(u.getId());
                System.out.println(u.getId());
                pw.close();
                System.out.println("User Created Successfully !!!");
                mainCtrl.getLastKnownInfo();
                mainCtrl.showHome();
            }
            else{
                firstname.setText(null);
                lastname.setText(null);
                email.setText(null);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void sendWelcomeMail(String mailAddress, String Name){
        final String username = "use.splitty";
        final String password = "sbfs akue pjrj oiqt";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });


        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("use.splitty@gmail.com", "Splitty App"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mailAddress)
            );
            message.setSubject("Welcome to Splitty " + Name + "!");
            message.setText(
                    "We're thrilled to have you here " + Name +",\n" +
                    "\n" +
                    "Welcome to Splitty, your go-to app for managing and processing expenses with ease! We're thrilled to have you on board as a new user, and we can't wait for you to experience the convenience and efficiency that Splitty offers.\n" +
                    "\n" +
                    "Here's what you can expect from Splitty:\n" +
                    "\n" +
                    "Simplified Expense Management: Say goodbye to the hassle of tracking and managing expenses manually. With Splitty, you can effortlessly track your expenses, split debts, and keep your finances organized in one place.\n" +
                    "\n" +
                    "Seamless Debt Splitting: Whether you're splitting bills with friends, family, or colleagues, Splitty makes it simple to divide expenses and settle debts fairly. No more awkward conversations or endless calculations – Splitty does the hard work for you.\n" +
                    "\n" +
                    "Secure and Private: We take the security and privacy of your financial data seriously. Rest assured that your information is safe and encrypted, so you can use Splitty with confidence.\n" +
                    "\n" +
                    "User-Friendly Interface: Splitty is designed with simplicity and ease of use in mind. Our intuitive interface makes it easy for you to navigate the app and access all its features effortlessly.\n" +
                    "\n" +
                    "If you have any questions or need assistance, our dedicated support team is here to help. Feel free to reach out to us at use.splitty@gmail.com – we're always happy to assist you.\n" +
                    "\n" +
                    "Once again, welcome to Splitty! We're excited to embark on this financial journey with you.\n" +
                    "\n" +
                    "Best regards,\n" +
                    "The Splitty Team\n" +
                    "\n" +
                    "Email: use.splitty@gmail.com");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isValidInput() {
        if(firstname.getText().isEmpty())
            return false;
        if(lastname.getText().isEmpty())
            return false;
        if(getCurrencyData() == null)
            return false;
        if(getLanguageData() == null)
            return false;
        return true;
    }

    public Currency getCurrencyData() {
        return currencyComboBox.getValue();
    }
    public String getLanguageData() {
        return languageComboBox.getValue();
    }

    public void setup()  {
        currencyComboBox.setItems(FXCollections.observableList(
                List.of(Currency.EUR, Currency.USD,
                        Currency.CHF, Currency.GBP)));
        currencyComboBox.getSelectionModel().selectFirst();
        languages = new ArrayList<>(List.of("English", "Nederlands"));
        languageComboBox.setItems(FXCollections.observableList(languages.stream().toList()));
        languageComboBox.getSelectionModel().select(mainCtrl.getLanguageIndex());
        languageComboBox.setOnAction(event -> {
            mainCtrl.setLanguageIndex(languageComboBox.getSelectionModel().getSelectedIndex());
            setTextLanguage();
        });

    }

    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        if(languageIndex < 0)
            languageIndex = 0;
        title.setText(titleText.get(languageIndex));
        lastname.setPromptText(lnText.get(languageIndex));
        firstname.setPromptText(fnText.get(languageIndex));
        email.setPromptText(emText.get(languageIndex));
        currencyComboBox.setPromptText(cText.get(languageIndex));
        languageComboBox.setPromptText(lText.get(languageIndex));
    }

}
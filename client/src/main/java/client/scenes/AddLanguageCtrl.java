package client.scenes;

import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class AddLanguageCtrl{

    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField nameField;
    @FXML
    private Label languageName;
    @FXML
    private Button goHomeButton;
    @FXML
    private Button addButton;
    @FXML
    private Label notFilledIn;
    @FXML
    private TableView<String> table;
    @FXML
    private TableColumn<String, String> languageColumn;
    @FXML
    private TableColumn<String, String> yourLanguageColumn;
    private HashMap<String, String> newLanguage;
    private HashMap<String, String> phrases;

    @Inject
    public AddLanguageCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.newLanguage = new HashMap<>();
    }

    @FXML
    void addLanguage(ActionEvent event) {
        if (nameField == null || nameField.getText().isEmpty() || nameField.getText().contains(" ")){
            notFilledIn.setText(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("FillInIdName"));
            return;
    }
        if (newLanguage.keySet().size()<phrases.keySet().size()){
            notFilledIn.setText("Not everything is filled in");
            notFilledIn.setText(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("NotFilledIn"));
            return;
        }
        System.out.println("Adding language... name: " + nameField.getText());
        String filePath = "client/src/main/resources/languages/language_" + nameField.getText() + ".properties";
        File f = new File(filePath);
        if(f.exists()){
            notFilledIn.setText(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("LanguageAlreadyExists"));
            return;
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            for (String i: newLanguage.keySet()) {
                writer.println(i + " = " + newLanguage.get(i));
            }
            writer.close();
            System.out.println("Lines have been written to the file successfully.");
            notFilledIn.setText(ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath()).getString("RestartLanguage"));
            mainCtrl.setRestart(true);
            notFilledIn.setTextFill(Color.rgb(10, 170, 32));
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
        mainCtrl.getLanguages().add(nameField.getText());
        mainCtrl.save(new Pair<>(mainCtrl.getLanguageIndex(), mainCtrl.getLanguages()));
    }
    public void setUp(){
        setTextLanguage();
        this.phrases = getPhrases();
        languageColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue()));
        table.setItems(FXCollections.observableList(phrases.keySet().stream().toList()));
        table.setEditable(true);
        yourLanguageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yourLanguageColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<String, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<String, String> event) {
                System.out.println(event.getRowValue() + " = " + event.getNewValue());
                newLanguage.put(phrases.get(event.getRowValue()), event.getNewValue());
            }
        });
    }
    public void setTextLanguage() {
        String language = mainCtrl.getLanguage();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.language_" + mainCtrl.getLanguageWithoutImagePath());
        languageName.setText(resourceBundle.getString("LanguageName"));
        goHomeButton.setText(resourceBundle.getString("Home"));
        addButton.setText(resourceBundle.getString("Add"));
        languageColumn.setText(mainCtrl.getLanguage().split(";")[0]);
        yourLanguageColumn.setText(resourceBundle.getString("YourLanguage"));
    }
    public HashMap<String, String> getPhrases() {
        HashMap<String, String> result = new HashMap<>();
        try {
            File file = new File("client/src/main/resources/languages/language_" + mainCtrl.getLanguageWithoutImagePath() + ".properties");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String wordBefore = parts[0].trim();
                    String textAfter = parts[1].trim();
                    result.put(textAfter, wordBefore);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return result;
    }


    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }
}

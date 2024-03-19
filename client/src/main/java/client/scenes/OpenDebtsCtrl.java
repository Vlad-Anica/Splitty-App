package client.scenes;

import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * Unused imports:
 * import javafx.scene.control.Label;
 * import javafx.scene.control.TextField;
 * import javafx.scene.paint.Color;
 */




public class OpenDebtsCtrl {
    @FXML
    private Button goHomeButton;
    List<String> goHomeButtonText = new ArrayList<>(List.of("Home", "Home (in Dutch)"));
    @FXML
    private Button goBackButton;
    List<String> goBackButtonText = new ArrayList<>(List.of("Back", "Back (in Dutch)"));
    @FXML
    private Text openDebtsTitle;
    List<String> openDebtsTitleText = new ArrayList<>(List.of("Open Debts", "Open Debts (in Dutch)"));
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    private MainCtrl mainCtrl;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setup() {
        setTextLanguage();
    }
    public void setTextLanguage() {
        int languageIndex = mainCtrl.getLanguageIndex();
        goHomeButton.setText(goBackButtonText.get(languageIndex));
        goBackButton.setText(goBackButtonText.get(languageIndex));
        openDebtsTitle.setText(openDebtsTitleText.get(languageIndex));
    }

    //need a way to show open debts from the database


    //For Now, the back button is the same as the home button, this needs changing in the future!
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

    public void goHome(ActionEvent event) throws IOException {
        mainCtrl.showHome();
    }

}

package datastruct.le;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConfirmationUI {
    @FXML
    private AnchorPane AnchorP;

    @FXML
    private Label TaskPromp;

    @FXML
    private Label AYS;

    @FXML
    private Button ConYes;

    @FXML
    private Button ConNo;

    private boolean result = false;

    @FXML
    public void initialize() {
        // Set up button click handlers
        ConYes.setOnAction(event -> {
            result = true;
            closeDialog();
        });

        ConNo.setOnAction(event -> {
            result = false;
            closeDialog();
        });
    }

    public void setPrompt(String prompt) {
        TaskPromp.setText(prompt);
    }


    public void setQuestion(String question) {
        AYS.setText(question);
    }

    public boolean getResult() {
        return result;
    }

    private void closeDialog() {
        Stage stage = (Stage) AnchorP.getScene().getWindow();
        stage.close();
    }
}
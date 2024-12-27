package datastruct.le;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TaskAddEdit {
    @FXML
    private AnchorPane AnchorP;

    @FXML
    private Label TaskPromp;

    @FXML
    private Label cLabel;

    @FXML
    private Label sLabel;

    @FXML
    private Label aLabel;

    @FXML
    private ComboBox<String> codeComboBox;

    @FXML
    private Spinner<Integer> severitySpinner;

    @FXML
    private TextField addressField;

    @FXML
    private Button dBut;

    @FXML
    private Button cBut;

    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        // Initialize code ComboBox with sample values
        ObservableList<String> codes = FXCollections.observableArrayList(
                "Fire", "Theft", "Murder", "Hostage", "Shooting", "Building Collapse", "Rescue Situation", "Animal Emergencies"
        );
        codeComboBox.setItems(codes);

        // Initialize severity Spinner (1-5 scale)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        severitySpinner.setValueFactory(valueFactory);

        // Set up button handlers
        dBut.setOnAction(event -> handleDone());
        cBut.setOnAction(event -> handleCancel());

        // Add validation listener to address field
        addressField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
    }

    private void validateInput() {
        boolean isValid =
                codeComboBox.getValue() != null &&
                        !addressField.getText().trim().isEmpty();

        dBut.setDisable(!isValid);
    }

    private void handleDone() {
        if (isInputValid()) {
            saveClicked = true;
            closeDialog();
        }
    }

    private void handleCancel() {
        saveClicked = false;
        closeDialog();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (codeComboBox.getValue() == null) {
            errorMessage += "Please select a code!\n";
        }

        if (addressField.getText() == null || addressField.getText().trim().isEmpty()) {
            errorMessage += "Please enter an address!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }


    // Getters for form data

    public String getCode() {
        return codeComboBox.getValue();
    }

    public int getSeverity() {
        return severitySpinner.getValue();
    }

    public String getAddress() {
        return addressField.getText();
    }

    // Setters for editing existing tasks
    public void setCode(String code) {
        codeComboBox.setValue(code);
    }

    public void setSeverity(int severity) {
        severitySpinner.getValueFactory().setValue(severity);
    }

    public void setAddress(String address) {
        addressField.setText(address);
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    private void closeDialog() {
        Stage stage = (Stage) AnchorP.getScene().getWindow();
        stage.close();
    }
}
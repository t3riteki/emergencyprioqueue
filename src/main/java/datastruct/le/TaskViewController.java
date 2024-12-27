package datastruct.le;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import java.io.*;
import java.util.ArrayList;

public class TaskViewController {
    @FXML private TableView<prioTask> tableView;
    @FXML private TableColumn<prioTask, String> codeCol;
    @FXML private TableColumn<prioTask, String> addressCol;
    @FXML private TableColumn<prioTask, Integer> timeCol;
    @FXML private TableColumn<prioTask, Integer> severityCol;
    @FXML private TableColumn<prioTask, Integer> prioCol;

    @FXML private ListView<String> Listview;

    @FXML private Label tvMain;
    @FXML private Label codeLab;
    @FXML private Label CodOpLa;

    @FXML private Button SaveB;
    @FXML private Button LoadB;
    @FXML private Button tAdd;
    @FXML private Button tDel;
    @FXML private Button tEdit;
    @FXML private Button CodeAdd;
    @FXML private Button CodeEdit;
    @FXML private Button CodeDel;

    private MaxHeap taskHeap = new MaxHeap();
    private hashingTable codeTable = new hashingTable(20);
    

    @FXML
    public void initialize() {
        // Initialize TableView columns with proper prioTask properties
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));
        prioCol.setCellValueFactory(new PropertyValueFactory<>("prioLevel"));


        // Set up button handlers
        setupButtonHandlers();

        // Add table selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends prioTask> observable, prioTask oldValue, prioTask newValue) ->
                        updateButtonStates());

        // Add list selection listener
        Listview.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) ->
                        updateCodeButtonStates());
    }


    private void setupButtonHandlers() {
        SaveB.setOnAction(event -> handleSave());
        LoadB.setOnAction(event -> handleLoad());
        tAdd.setOnAction(event -> handleTaskAdd());
        tDel.setOnAction(event -> handleTaskDelete());
        tEdit.setOnAction(event -> handleTaskEdit());
        CodeAdd.setOnAction(event -> handleCodeAdd());
        CodeEdit.setOnAction(event -> handleCodeEdit());
        CodeDel.setOnAction(event -> handleCodeDelete());
    }

    private void handleTaskAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskAddEdit.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Task");
            dialogStage.setScene(new Scene(page));

            TaskAddEdit controller = loader.getController();

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                // Create new prioTask with current time
                int currentTime = (int) (System.currentTimeMillis() / 1000); // Current time in seconds
                prioTask newTask = new prioTask(
                        controller.getCode(),
                        controller.getAddress(),
                        currentTime,
                        controller.getSeverity()
                );

                // Add to both table and heap
                tasks.add(newTask);
                taskHeap.insert(newTask);
                updateTableView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
        }
    }

    private void handleTaskEdit() {
        prioTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskAddEdit.fxml"));
                AnchorPane page = loader.load();
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Task");
                dialogStage.setScene(new Scene(page));

                TaskAddEdit controller = loader.getController();
                // Set existing values
                controller.setCode(selectedTask.getCode());
                controller.setAddress(selectedTask.getAddress());
                controller.setSeverity(selectedTask.getSeverity());

                dialogStage.showAndWait();

                if (controller.isSaveClicked()) {
                    // Update task with new values
                    selectedTask.setCode(controller.getCode());
                    selectedTask.setAddress(controller.getAddress());
                    selectedTask.setSeverity(controller.getSeverity());

                    // Rebuild heap with updated values
                    rebuildHeap();
                    updateTableView();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
            }
        }
    }

    private void handleTaskDelete() {
        prioTask selectedTask = tableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Task");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this task?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                tasks.remove(selectedTask);
                rebuildHeap();
                updateTableView();
            }
        }
    }

    private void handleCodeAdd() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Code");
        dialog.setHeaderText("Add New Code");
        dialog.setContentText("Enter new code:");

        dialog.showAndWait().ifPresent(code -> {
            if (!code.trim().isEmpty() && !codes.contains(code)) {
                codes.add(code);
            }
        });
    }

    private void handleCodeEdit() {
        String selectedCode = Listview.getSelectionModel().getSelectedItem();
        if (selectedCode != null) {
            TextInputDialog dialog = new TextInputDialog(selectedCode);
            dialog.setTitle("Edit Code");
            dialog.setHeaderText("Edit Code");
            dialog.setContentText("Enter new code value:");

            dialog.showAndWait().ifPresent(newCode -> {
                if (!newCode.trim().isEmpty() && !codes.contains(newCode)) {
                    int index = codes.indexOf(selectedCode);
                    codes.set(index, newCode);
                }
            });
        }
    }

    private void handleCodeDelete() {
        String selectedCode = Listview.getSelectionModel().getSelectedItem();
        if (selectedCode != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Code");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this code?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                codes.remove(selectedCode);
            }
        }
    }

    private void handleSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("tasks.dat"))) {
            oos.writeObject(new ArrayList<>(tasks));
            oos.writeObject(new ArrayList<>(codes));
            showAlert(Alert.AlertType.INFORMATION, "Save Successful",
                    "Data has been saved successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Error",
                    "Error saving data: " + e.getMessage());
        }
    }

    private void handleLoad() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("tasks.dat"))) {
            ArrayList<prioTask> loadedTasks = (ArrayList<prioTask>) ois.readObject();
            ArrayList<String> loadedCodes = (ArrayList<String>) ois.readObject();

            tasks.setAll(loadedTasks);
            codes.setAll(loadedCodes);

            // Rebuild heap with loaded tasks
            rebuildHeap();
            updateTableView();

            showAlert(Alert.AlertType.INFORMATION, "Load Successful",
                    "Data has been loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error",
                    "Error loading data: " + e.getMessage());
        }
    }

    private void rebuildHeap() {
        taskHeap = new MaxHeap();
        for (prioTask task : tasks) {
            taskHeap.insert(task);
        }
    }

    private void updateTableView() {
        // Sort tasks by priority level
        tasks.sort((t1, t2) -> Integer.compare(t2.getPrioLevel(), t1.getPrioLevel()));
        tableView.refresh();
    }

    private void updateButtonStates() {
        boolean taskSelected = tableView.getSelectionModel().getSelectedItem() != null;
        tEdit.setDisable(!taskSelected);
        tDel.setDisable(!taskSelected);
    }

    private void updateCodeButtonStates() {
        boolean codeSelected = Listview.getSelectionModel().getSelectedItem() != null;
        CodeEdit.setDisable(!codeSelected);
        CodeDel.setDisable(!codeSelected);
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
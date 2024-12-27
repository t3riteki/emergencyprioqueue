package datastruct.le;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TaskViewController {
    @FXML private TableView<Task> tableView; // Important

    @FXML private TableColumn<Task, Integer> taskCol;
    @FXML private TableColumn<Task, String> codeCol;
    @FXML private TableColumn<Task, String> resourceCol;
    @FXML private TableColumn<Task, String> timeCol;
    @FXML private TableColumn<Task, String> addCol;
    @FXML private TableColumn<Task, Integer> prioCol;

    @FXML private ListView<String> Listview; // Important

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
    
    private ObservableList<LinkedList> codes;
    private ObservableList<Task> tasks;

    @FXML
    public void initialize() {
        // Initialize Observable Lists with App's Data Structures
        // codes = FXCollections.observableArrayList(App.codeTable.getHTable()); Discontinued feature: #future project addition, user identified codes

        tasks = FXCollections.observableArrayList(App.taskHeap.getTaskHeap());
        tableView.setItems(tasks);

        // Initialize TableView columns with proper Task properties
        taskCol.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        resourceCol.setCellValueFactory(cellData ->{
            Task task = cellData.getValue();
            String code = task.getCode();
            
            String resources = App.rm.localResourceLookUp(code);

            return new SimpleStringProperty(resources);
        });

        addCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        timeCol.setCellValueFactory(cellData -> {
        Task task = cellData.getValue();
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(task.getPubtime(), 0, ZoneId.systemDefault().getRules().getOffset(Instant.now()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new SimpleStringProperty(dateTime.format(formatter));
});
        prioCol.setCellValueFactory(new PropertyValueFactory<>("prioLevel"));

        // Set up button handlers
        setupButtonHandlers();

        // // Add table selection listener
        // tableView.getSelectionModel().selectedItemProperty().addListener(
        //         (ObservableValue<? extends Task> observable, Task oldValue, Task newValue) -> updateButtonStates());

        // // Add list selection listener
        // Listview.getSelectionModel().selectedItemProperty().addListener(
        //         (ObservableValue<? extends String> observable, String oldValue, String newValue) -> updateCodeButtonStates());
    }


    private void setupButtonHandlers() {
        // SaveB.setOnAction(event -> handleSave());
        // LoadB.setOnAction(event -> handleLoad());
        tAdd.setOnAction(event -> handleTaskAdd());
        tDel.setOnAction(event -> handleTaskDelete());
        tEdit.setOnAction(event -> handleTaskEdit());
        // CodeAdd.setOnAction(event -> handleCodeAdd());
        // CodeEdit.setOnAction(event -> handleCodeEdit());
        // CodeDel.setOnAction(event -> handleCodeDelete());
    }

    public void updateTasks(){
        tasks.setAll(App.taskHeap.getTaskHeap());
    }

    private void handleTaskAdd() {
        System.out.println("Using Add Task");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Task_Add_Edit.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Task");
            dialogStage.setScene(new Scene(page));

            TaskAddEdit controller = loader.getController();

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                // Create new Task with current time
                int currentTime = (int) (System.currentTimeMillis() / 1000); // Current time in seconds
                System.out.println("Current Time: " + currentTime);
                Task newTask = new Task(
                        App.taskHeap.getCurrentSize() + 1,
                        controller.getCode(),
                        controller.getAddress(),
                        currentTime,
                        controller.getSeverity());

                // Add to both table and heap
                tasks.add(newTask);
                App.taskHeap.insert(newTask);
                App.taskHeap.display();
                updateTableView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
        }
    }

    private void handleTaskEdit() {
        System.out.println("Using Edit Task");

        Task selectedTask = tableView.getSelectionModel().getSelectedItem();



    //     if (selectedTask != null) {
    //         try {
    //             FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskAddEdit.fxml"));
    //             AnchorPane page = loader.load();
    //             Stage dialogStage = new Stage();
    //             dialogStage.setTitle("Edit Task");
    //             dialogStage.setScene(new Scene(page));

    //             TaskAddEdit controller = loader.getController();
    //             // Set existing values
    //             controller.setCode(selectedTask.getCode());
    //             controller.setAddress(selectedTask.getAddress());
    //             controller.setSeverity(selectedTask.getSeverity());

    //             dialogStage.showAndWait();

    //             if (controller.isSaveClicked()) {
    //                 // Update task with new values
    //                 selectedTask.setCode(controller.getCode());
    //                 selectedTask.setAddress(controller.getAddress());
    //                 selectedTask.setSeverity(controller.getSeverity());

    //                 // Rebuild heap with updated values
    //                 rebuildHeap();
    //                 updateTableView();
    //             }
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //             showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
    //         }
    //     }
    }

    private void handleTaskDelete() {
        System.out.println("Using Remove Task");
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();



    //     if (selectedTask != null) {
    //         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    //         alert.setTitle("Delete Task");
    //         alert.setHeaderText("Delete Confirmation");
    //         alert.setContentText("Are you sure you want to delete this task?");

    //         if (alert.showAndWait().get() == ButtonType.OK) {
    //             tasks.remove(selectedTask);
    //             rebuildHeap();
    //             updateTableView();
    //         }
    //     }
    }

    // private void handleCodeAdd() {
    //     TextInputDialog dialog = new TextInputDialog();



    //     dialog.setTitle("Add Code");
    //     dialog.setHeaderText("Add New Code");
    //     dialog.setContentText("Enter new code:");

    //     dialog.showAndWait().ifPresent(code -> {
    //         if (!code.trim().isEmpty() && !codes.contains(code)) {
    //             codes.add(code);
    //         }
    //     });
    // }

    // private void handleCodeEdit() {
    //     String selectedCode = Listview.getSelectionModel().getSelectedItem();
    //     if (selectedCode != null) {
    //         TextInputDialog dialog = new TextInputDialog(selectedCode);
    //         dialog.setTitle("Edit Code");
    //         dialog.setHeaderText("Edit Code");
    //         dialog.setContentText("Enter new code value:");

    //         dialog.showAndWait().ifPresent(newCode -> {
    //             if (!newCode.trim().isEmpty() && !codes.contains(newCode)) {
    //                 int index = codes.indexOf(selectedCode);
    //                 codes.set(index, newCode);
    //             }
    //         });
    //     }
    // }

    // private void handleCodeDelete() {
    //     String selectedCode = Listview.getSelectionModel().getSelectedItem();
    //     if (selectedCode != null) {
    //         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    //         alert.setTitle("Delete Code");
    //         alert.setHeaderText("Delete Confirmation");
    //         alert.setContentText("Are you sure you want to delete this code?");

    //         if (alert.showAndWait().get() == ButtonType.OK) {
    //             codes.remove(selectedCode);
    //         }
    //     }
    // }

    // private void handleSave() {
    //     try (ObjectOutputStream oos = new ObjectOutputStream(
    //             new FileOutputStream("tasks.dat"))) {
    //         oos.writeObject(new ArrayList<>(tasks));
    //         oos.writeObject(new ArrayList<>(codes));
    //         showAlert(Alert.AlertType.INFORMATION, "Save Successful",
    //                 "Data has been saved successfully.");
    //     } catch (IOException e) {
    //         showAlert(Alert.AlertType.ERROR, "Save Error",
    //                 "Error saving data: " + e.getMessage());
    //     }
    // }

    // private void handleLoad() {
    //     try (ObjectInputStream ois = new ObjectInputStream(
    //             new FileInputStream("tasks.dat"))) {
    //         ArrayList<Task> loadedTasks = (ArrayList<Task>) ois.readObject();
    //         ArrayList<String> loadedCodes = (ArrayList<String>) ois.readObject();

    //         tasks.setAll(loadedTasks);
    //         codes.setAll(loadedCodes);

    //         // Rebuild heap with loaded tasks
    //         rebuildHeap();
    //         updateTableView();

    //         showAlert(Alert.AlertType.INFORMATION, "Load Successful",
    //                 "Data has been loaded successfully.");
    //     } catch (IOException | ClassNotFoundException e) {
    //         showAlert(Alert.AlertType.ERROR, "Load Error",
    //                 "Error loading data: " + e.getMessage());
    //     }
    // }

    // private void rebuildHeap() {
    //     taskHeap = new MaxHeap();
    //     for (Task task : tasks) {
    //         taskHeap.insert(task);
    //     }
    // }

    private void updateTableView() {
        System.out.println(tasks.size());
        updateTasks();
        System.out.println(tasks.toString());
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
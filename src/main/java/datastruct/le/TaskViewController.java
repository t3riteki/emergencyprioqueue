package datastruct.le;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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

    @FXML private Button SaveB;
    @FXML private Button LoadB;
    @FXML private Button tAdd;
    @FXML private Button tDel;
    @FXML private Button tEdit;
    
    private ObservableList<String> codes;
    private ObservableList<Task> tasks;

    @FXML
    public void initialize() {
        tasks = FXCollections.observableArrayList(App.taskHeap.getTaskHeap());
        tableView.setItems(tasks);

        codes = FXCollections.observableArrayList(App.rm.hTable.toObservableList());
        System.out.println(codes.toString());
        Listview.setItems(codes);
       
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

        tableView.setRowFactory(tv -> new TableRow<Task>() {
        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setPrefHeight(Control.USE_COMPUTED_SIZE);
            } else {
                // Calculate the height based on content
                String text = item.getResources(); // This assumes 'resources' is a column in your Task class
                Text helper = new Text(text);
                helper.setWrappingWidth(resourceCol.getWidth() - 10); // Account for padding
                helper.setFont(getFont());
                double requiredHeight = helper.getLayoutBounds().getHeight();
                setPrefHeight(requiredHeight + 20); // Add some padding
            }
        }
    });

        setupButtonHandlers();
    }

    private void setupButtonHandlers() {
        SaveB.setOnAction(event -> handleSave());
        LoadB.setOnAction(event -> handleLoad());
        tAdd.setOnAction(event -> handleTaskAdd());
        tDel.setOnAction(event -> handleTaskDelete());
        tEdit.setOnAction(event -> handleTaskEdit());
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
                int currentTime = (int) (System.currentTimeMillis() / 1000); // Current time in seconds
                System.out.println("Current Time: " + currentTime);
                Task newTask = new Task(
                        App.taskHeap.getCurrentSize() + 1,
                        controller.getCode(),
                        controller.getAddress(),
                        currentTime,
                        controller.getSeverity());

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
        System.out.println("Selected Task"+selectedTask.toString());
        try {
            // Load the TaskAddEdit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Task_Add_Edit.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Task");
            dialogStage.setScene(new Scene(page));

            
            TaskAddEdit controller = loader.getController();

            controller.setCode(selectedTask.getCode());
            controller.setAddress(selectedTask.getAddress());
            controller.setSeverity(selectedTask.getSeverity());

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Task copy = new Task(selectedTask);
                copy.setCode(controller.getCode());
                copy.setAddress(controller.getAddress());
                copy.setSeverity(controller.getSeverity());
                copy.setPrioLevel(copy.calculatePriority(copy.getSeverity(), copy.getPubtime()));
                
                App.taskHeap.remove(selectedTask);
                App.taskHeap.insert(copy);

                updateTableView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
        }
    }

    private void handleTaskDelete() {
        System.out.println("Using Remove Task");
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationUI.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Delete Item?");
            dialogStage.setScene(new Scene(page));

            ConfirmationUI controller = loader.getController();

            dialogStage.showAndWait();

            if (controller.getResult()) {
                App.taskHeap.remove(selectedTask);
                updateTableView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
        }
    }

    private void handleSave() {
        System.out.println("Using Save Task Heap");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationUI.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Save File");
            dialogStage.setScene(new Scene(page));

            ConfirmationUI controller = loader.getController();

            dialogStage.showAndWait();

            if (controller.getResult()) {
                App.fileHandler.saveHeap(App.taskHeap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
        }
    }

    private void handleLoad() {
        System.out.println("Using Load Task Heap");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationUI.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Save File");
            dialogStage.setScene(new Scene(page));

            ConfirmationUI controller = loader.getController();

            dialogStage.showAndWait();

            if (controller.getResult()) {
                App.taskHeap = App.fileHandler.loadHeap();
                System.out.println("Loaded Tasks: " + App.taskHeap.toString());
                updateTableView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dialog window.");
        }

    }

    private void refreshResourceColumn(){
        for(Task t: tasks){ 
            System.out.println(t.getCode());
            String resource = App.rm.localResourceLookUp(t.getCode());
            t.setResources(resource);
        }
    }

    private void updateTableView() {
        // System.out.println("Updating Table View");
        // System.out.println("Pre Update Tasks: " + tasks.toString());
        updateTasks();
        refreshResourceColumn();
        // System.out.println("Post Update Tasks: " + tasks.toString());
        tableView.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
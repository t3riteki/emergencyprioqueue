package datastruct.le;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class TaskManagementSystem extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Task Management System");

        // Load the main task view
        showMainView();
    }

    /**
     * Shows the main task view window.
     */
    private void showMainView() {
        try {
            // Load TaskviewUI.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TaskManagementSystem.class.getResource("TaskviewUI.fxml"));
            AnchorPane mainLayout = (AnchorPane) loader.load();

            // Create the scene with the specified dimensions from your FXML
            Scene scene = new Scene(mainLayout, 1900, 1080);

            // Add stylesheets
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true); // Since it's a large window size
            primaryStage.show();

            // Get the controller
            TaskViewController controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the add/edit task dialog.
     * @param task The task to edit, or null for adding a new task
     * @return true if changes were saved, false otherwise
     */
    public static boolean showTaskAddEditDialog(Stage parentStage, prioTask task) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TaskManagementSystem.class.getResource("Task_Add_Edit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(task == null ? "Add New Task" : "Edit Task");
            dialogStage.initOwner(parentStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(TaskManagementSystem.class.getResource("/styles.css").toExternalForm());
            dialogStage.setScene(scene);

            TaskAddEdit controller = loader.getController();
            
            controller.setDialogStage(dialogStage);
            if (task != null) {
                controller.setTask(task);
            }

            dialogStage.showAndWait();
            return controller.isSaveClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows the confirmation dialog.
     * @param title The dialog title
     * @param prompt The confirmation prompt
     * @return true if confirmed, false otherwise
     */
    public static boolean showConfirmationDialog(Stage parentStage, String title, String prompt) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TaskManagementSystem.class.getResource("ConfirmationUI.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initOwner(parentStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(TaskManagementSystem.class.getResource("/styles.css").toExternalForm());
            dialogStage.setScene(scene);

            ConfirmationUI controller = loader.getController();
            controller.setPrompt(prompt);

            dialogStage.showAndWait();
            return controller.getResult();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
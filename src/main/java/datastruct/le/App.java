package datastruct.le;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    // Main Data structures
    public static hashingTable codeTable;
    public static MaxHeap taskHeap;
    public static resourceManager rm;

    public static FileIO fileHandler; // Handler

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("TaskviewUI"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {

        codeTable = new hashingTable();
        taskHeap = new MaxHeap();
        rm = new resourceManager();

        launch();
    }



}
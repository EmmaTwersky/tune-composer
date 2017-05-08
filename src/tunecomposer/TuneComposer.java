package tunecomposer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class creates and launches the Tune Composer window and application.
 */
public class TuneComposer extends Application {

    public static FileManager fileManager;
    
    public ApplicationController appController;
    
    /**
     * Construct the scene and start the application.
     * 
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {  
        Parent root = FXMLLoader.load(getClass().getResource("Application.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Tune Composer");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            boolean exit = fileManager.promptToExit();
            if (exit){
                System.exit(0);
            } else{
                we.consume();
            }
        });        
        primaryStage.show();                
    }

    /**
     * Launch the application.
     * 
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void setFileManager(FileManager fManager){
        fileManager = fManager;
    }
}
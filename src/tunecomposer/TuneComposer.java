package tunecomposer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * TuneComposer allows the user to create simple midi melodies and play them.
 */
public class TuneComposer extends Application {

    /**
     * Construct the scene and start the application.
     * 
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {  
        Parent root = FXMLLoader.load(getClass().getResource("TuneComposer.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Tune Composer");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
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
}

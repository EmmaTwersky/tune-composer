package tunecomposer;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * This class controls the menu bar and menu bar actions.
 *
 * @author Emma Twersky
 */
public class MenuBarController {  
    /**
     * Handles the play button from the Actions menu.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePlayScaleButtonAction(ActionEvent event) {
        CompositionPaneController.tunePlayerObj.play();
    }
    
    /**
     * Handles the Stop menu item.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        CompositionPaneController.tunePlayerObj.stop();
    }     
    
    /**
     * Handles the Select All menu item and selects all notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        CompositionPaneController.soundObject_array.forEach((note) -> {
            note.select();
        });
        CompositionPaneController.updateSelectedSoundObjectArray();
    }

    /**
     * Handles the Delete menu item and deletes all selected notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        for (SoundObject soundItem: CompositionPaneController.selected_soundobject_array) {
            soundItem.delete();
            CompositionPaneController.soundObject_array.remove(soundItem);
        }
        if (CompositionPaneController.selected_soundobject_array.size() == 0) {
        }
        CompositionPaneController.updateSelectedSoundObjectArray();
    }
    
    @FXML
    protected void handleGrouping(ActionEvent event) throws IOException{        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompositionPane.fxml"));
        Parent par = loader.load();
        CompositionPaneController controller = loader.getController();

        controller.makeGroup();
    }
    
    @FXML
    protected void handleUngrouping(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompositionPane.fxml"));
        Parent par = loader.load();
        CompositionPaneController controller = loader.getController();
        
        controller.unGroup();
    }

    /**
     * Handles the Exit menu item and exits the scene.
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }
}

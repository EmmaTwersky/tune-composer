package tunecomposer;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.*;

/**
 * This class controls the menu bar and menu bar actions.
 *
 * @author Emma Twersky
 */
public class MenuBarController {  
    @FXML
    public CompositionPaneController compositionPaneController;
        
    /**
     * Handles the play button from the Actions menu.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePlayScaleButtonAction(ActionEvent event) {
        compositionPaneController.play();
    }
    
    /**
     * Handles the Stop menu item.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        compositionPaneController.stop();
    }     
    
    /**
     * Handles the Select All menu item and selects all notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        SoundObjectPaneController.SOUNDOBJECT_ARRAY.forEach((sObj) -> {
            sObj.select();
        });
        SoundObjectPaneController.updateSelectedSoundObjectArray();
    }

    /**
     * Handles the Delete menu item and deletes all selected notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        for (SoundObject sObj: SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
            sObj.delete();
            SoundObjectPaneController.SOUNDOBJECT_ARRAY.remove(sObj);
        }
        SoundObjectPaneController.updateSelectedSoundObjectArray();
    }
    
    @FXML
    protected void handleGrouping(ActionEvent event) throws IOException{        
        compositionPaneController.group();
    }
    
    @FXML
    protected void handleUngrouping(ActionEvent event) throws IOException{
        compositionPaneController.ungroup();
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
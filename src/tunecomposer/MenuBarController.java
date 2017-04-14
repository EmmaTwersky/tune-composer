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
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
    
    
    /**
     * Initialize FXML. Creates ActionManager to control undo and redos. Sets
     * CompPaneController to have the same reference.
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    public void initialize(java.net.URL location, 
                                        java.util.ResourceBundle resources) {
        System.out.println("menubar");
        actionManager = new ActionManager();
        try {
            compositionPaneController.setActionManager(actionManager);
        } catch (NullPointerException ex) {
            System.out.println("In initialize MenuBarController, passed null"
                   + " to CompPaneController.setActionManager(manager)");
            System.exit(1);
        }
    }   

    
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
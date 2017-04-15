package tunecomposer;

import javafx.event.ActionEvent;
import javafx.fxml.*;

/**
 * This controller creates the application and handles the menu item selections.
 *
 * @author Emma Twersky
 */
public class ApplicationController {  
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
     
    @FXML
    public CompositionPaneController compositionPaneController;
        
    /**
     * Initialize FXML. 
     * Creates ActionManager to control undo and redo. 
     * Sets CompPaneController to have the same reference.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        actionManager = new ActionManager();
        try {
            compositionPaneController.setActionManager(actionManager);
        } catch (NullPointerException ex) {
            System.out.println("In initialize MenuBarController, passed null"
                   + " to CompPaneController.setActionManager(manager)");
            System.exit(1);
        }
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
    
    /**
     * Handles the Undo menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handleUndoMenuItemAction(ActionEvent event) {
    }
    
    /**
     * Handles the Undo menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handleRedoMenuItemAction(ActionEvent event) {
    }
    
    /**
     * Handles the Select All menu item selection. Selects all SoundObjects.
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
     * Handles the Delete menu item selection. Deletes all selected SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        for (SoundObject sObj: SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
            sObj.removeFromPane(compositionPaneController.soundObjectPane);
            SoundObjectPaneController.SOUNDOBJECT_ARRAY.remove(sObj);
        }
        SoundObjectPaneController.updateSelectedSoundObjectArray();
    }
    
    /**
     * Handles the Group menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleGroupMenuItemAction(ActionEvent event) {        
        compositionPaneController.group();
    }
    
    /**
     * Handles the Ungroup menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleUngroupMenuItemAction(ActionEvent event) {
        compositionPaneController.ungroup();
    }
    
    
    /**
     * Handles the Play menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePlayMenuItemAction(ActionEvent event) {
        compositionPaneController.play();
    }
    
    /**
     * Handles the Stop menu item selection.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
    }     
}
package tunecomposer;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
//import java.util.Observable;

/**
 * Controls the application and handles the menu item selections.
 */
public class ApplicationController implements Initializable {  
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
//    private Observer actionManagerObserver;
     
    /**
     * Reference to the compositionPaneController. 
     */
    @FXML
    public CompositionPaneController compositionPaneController;
        
    /**
     * Initialize FXML Application. 
     * Creates ActionManager to control actions. 
     * Sets CompPaneController to hold ActionManagerReference.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        actionManager = new ActionManager();
//        actionManager.addObserver(this);
        compositionPaneController.setActionManager(actionManager);
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
        compositionPaneController.stop();
        
        actionManager.undo();        
    }
    
    /**
     * Handles the Undo menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handleRedoMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
        
        actionManager.redo();
    }
    
    /**
     * Handles the Undo menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handleCutMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
        
        compositionPaneController.cut();
    }

    /**
     * Handles the Undo menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handleCopyMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
        
        compositionPaneController.copy();
    }

    /**
     * Handles the Undo menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePasteMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
        
        compositionPaneController.paste();
    }
    
    /**
     * Handles the Select All menu item selection. 
     * Selects all SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
        
        compositionPaneController.selectAll();
    }

    /**
     * Handles the Delete menu item selection. Deletes all selected SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();

        compositionPaneController.delete();
    }
    
    /**
     * Handles the Group menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleGroupMenuItemAction(ActionEvent event) {  
        compositionPaneController.stop();
        
        compositionPaneController.group();
    }
    
    /**
     * Handles the Ungroup menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleUngroupMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();

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
//        stopMenuItem.setDisable(false);
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
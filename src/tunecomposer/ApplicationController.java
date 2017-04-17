package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.DeleteAction;
import tunecomposer.actionclasses.SelectAction;

/**
 * Controls the application and handles the menu item selections.
 */
public class ApplicationController implements Initializable {  
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
     
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compositionPaneController.soundObjectPane);
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compositionPaneController.soundObjectPane);
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
        
        SelectAction selectAction;
        ArrayList<SoundObject> allObjs = new ArrayList();
                        
        for (Node sObj : compositionPaneController.soundObjectPane.getChildren()) {
            Rectangle r = (Rectangle) sObj;
            SoundObject s = (SoundObject) r.getUserData();
            if (!s.isSelected()) {
                allObjs.add(s);
            }
        }
        
        if (!allObjs.isEmpty()) {
            selectAction = new SelectAction(allObjs);
            actionManager.execute(selectAction);
            actionManager.putInUndoStack(selectAction);
        }
        
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compositionPaneController.soundObjectPane);
    }

    /**
     * Handles the Delete menu item selection. Deletes all selected SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();

        if (!SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.isEmpty()) {
        DeleteAction deleter;
        deleter = new DeleteAction(SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                                  compositionPaneController.soundObjectPane);
        
        deleter.execute();
        actionManager.putInUndoStack(deleter);
        }
        
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compositionPaneController.soundObjectPane);
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compositionPaneController.soundObjectPane);

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
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compositionPaneController.soundObjectPane);
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
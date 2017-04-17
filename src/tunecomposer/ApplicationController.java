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
 * Creates the application and handles the menu item selections.
 */
public class ApplicationController implements Initializable {  
    
    /**
     * Object that contains the actions program. 
     */
    private ActionManager actionManager;
     
    @FXML
    public CompositionPaneController compPaneController;
        
    /**
     * Initialize FXML. 
     * Creates ActionManager to handle actions. 
     * Sets CompPaneController to have the same reference.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        actionManager = new ActionManager();
        try {
            compPaneController.setActionManager(actionManager);
        } catch (NullPointerException ex) {
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
        compPaneController.stop();
        actionManager.undo();
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compPaneController.soundObjectPane);
    }
    
    /**
     * Handles the Undo menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handleRedoMenuItemAction(ActionEvent event) {
        compPaneController.stop();
        actionManager.redo();
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compPaneController.soundObjectPane);
    }
    
    /**
     * Handles the Select All menu item selection. 
     * Selects all SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        compPaneController.stop();
        SelectAction selectAction;
        ArrayList<SoundObject> allObjs = new ArrayList();
                        
        for (Node sObj : compPaneController.soundObjectPane.getChildren()) {
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
                compPaneController.soundObjectPane);
    }

    /**
     * Handles the Delete menu item selection. 
     * Deletes all selected SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        compPaneController.stop();
        if (!SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.isEmpty()) {
        DeleteAction deleter;
        deleter = new DeleteAction(SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                                  compPaneController.soundObjectPane);
        
        deleter.execute();
        actionManager.putInUndoStack(deleter);
        }
        
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compPaneController.soundObjectPane);
    }
    
    /**
     * Handles the Group menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleGroupMenuItemAction(ActionEvent event) {  
        compPaneController.stop();
        compPaneController.group();
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compPaneController.soundObjectPane);

    }
    
    /**
     * Handles the Ungroup menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleUngroupMenuItemAction(ActionEvent event) {
        compPaneController.stop();
        compPaneController.ungroup();
        SoundObjectPaneController.updateSelectedSoundObjectArray(
                compPaneController.soundObjectPane);
    }
    
    
    /**
     * Handles the Play menu item selection.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePlayMenuItemAction(ActionEvent event) {
        compPaneController.play();
    }
    
    /**
     * Handles the Stop menu item selection.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopMenuItemAction(ActionEvent event) {
        compPaneController.stop();
    }     
}

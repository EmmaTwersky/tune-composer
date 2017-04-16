package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.DeleteAction;
import tunecomposer.actionclasses.SelectAction;

/**
 * This controller creates the application and handles the menu item selections.
 *
 * @author Emma Twersky
 */
public class ApplicationController implements Initializable {  
    
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
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        actionManager = new ActionManager();
        try {
            compositionPaneController.setActionManager(actionManager);
        } catch (NullPointerException ex) {
            System.out.println("Null");
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
        compositionPaneController.stop();
        actionManager.undo();
        SoundObjectPaneController.updateSelectedSoundObjectArray(compositionPaneController.soundObjectPane);
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(compositionPaneController.soundObjectPane);
    }
    
    /**
     * Handles the Select All menu item selection. Selects all SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        SelectAction selectAction;
        ArrayList<SoundObject> allObjs = new ArrayList();
                        
        for (Node sObj : compositionPaneController.soundObjectPane.getChildren()) {
            Rectangle r = (Rectangle) sObj;
            SoundObject s = (SoundObject) r.getUserData();
            if (!s.isSelected()) {
                allObjs.add(s);
            }
        }
        
        selectAction = new SelectAction(allObjs);
        ArrayList<Action> selectActionArray = new ArrayList<>();
        selectActionArray.add(selectAction);
        actionManager.execute(selectActionArray);
        actionManager.putInUndoStack(selectActionArray);
        
        SoundObjectPaneController.updateSelectedSoundObjectArray(compositionPaneController.soundObjectPane);
    }

    /**
     * Handles the Delete menu item selection. Deletes all selected SoundObjects.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        
        DeleteAction deleter;
        deleter = new DeleteAction(SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                                  compositionPaneController.soundObjectPane);
        
        deleter.execute();
        ArrayList<Action> deleteActionList = new ArrayList();
        deleteActionList.add(deleter);
        actionManager.putInUndoStack(deleteActionList);
        
//        for (SoundObject sObj: SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
//            sObj.removeFromPane(compositionPaneController.soundObjectPane);
//        }
        SoundObjectPaneController.updateSelectedSoundObjectArray(compositionPaneController.soundObjectPane);
    }
    
    /**
     * Handles the Group menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleGroupMenuItemAction(ActionEvent event) {        
        compositionPaneController.group();
        SoundObjectPaneController.updateSelectedSoundObjectArray(compositionPaneController.soundObjectPane);

    }
    
    /**
     * Handles the Ungroup menu item selection.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleUngroupMenuItemAction(ActionEvent event) {
        compositionPaneController.ungroup();
        SoundObjectPaneController.updateSelectedSoundObjectArray(compositionPaneController.soundObjectPane);
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

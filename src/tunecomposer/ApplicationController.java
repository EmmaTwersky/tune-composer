package tunecomposer;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.CopyAction;
import tunecomposer.actionclasses.CutAction;

/**
 * Controls the application and handles the menu item selections.
 */
public class ApplicationController implements Initializable {  
    
    
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private MenuItem redoMenuItem;
    @FXML
    private MenuItem cutMenuItem;
    @FXML
    private MenuItem copyMenuItem;
    @FXML
    private MenuItem pasteMenuItem;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private MenuItem selAllMenuItem;
    @FXML
    private MenuItem groupMenuItem;
    @FXML
    private MenuItem ungroupMenuItem;
    @FXML
    private MenuItem playMenuItem;
    @FXML
    private MenuItem stopMenuItem;
    
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
//    private Observer actionManagerObserver;
    
    private ActionManagerObserver actionObserver;
    
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
        
        toggleMenuItemDisable(true);
        
        actionObserver = new ActionManagerObserver();
        actionManager = new ActionManager();
        actionManager.addObserver(actionObserver);
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
        playMenuItem.setDisable(true);
        stopMenuItem.setDisable(false);
    }
    
    /**
     * Handles the Stop menu item selection.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
        playMenuItem.setDisable(false);
        stopMenuItem.setDisable(true);
    }
    
    private void toggleMenuItemDisable(boolean on){
        undoMenuItem.setDisable(on);
        redoMenuItem.setDisable(on);
        selAllMenuItem.setDisable(on);
        playMenuItem.setDisable(on);
        copyMenuItem.setDisable(on);
        cutMenuItem.setDisable(on);
        deleteMenuItem.setDisable(on);
        groupMenuItem.setDisable(on);
        ungroupMenuItem.setDisable(on);
        stopMenuItem.setDisable(on);
        pasteMenuItem.setDisable(on);
    }
    
    /**
     *
     */
    class ActionManagerObserver implements Observer {
        
        @Override
        public void update(Observable manager, Object x){
            ActionManager observable = (ActionManager) manager;
            setDisable(observable);
        }
        
        private void setDisable(ActionManager observable){
            
            boolean isRedoEmpty = observable.isRedoStackEmpty();
            boolean isUndoEmpty = observable.isUndoStackEmpty();
            
            toggleMenuItemDisable(false);
            ungroupMenuItem.setDisable(true);
            stopMenuItem.setDisable(true);
            pasteMenuItem.setDisable(true);
            
            ArrayList<SoundObject> selItems = 
                    SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY;
            
            ObservableList<Node> allSoundObjects = compositionPaneController.
                    soundObjectPaneController.soundObjectPane.getChildren();
            
            if (isUndoEmpty){
                undoMenuItem.setDisable(true);
            }
            if (isRedoEmpty){
                redoMenuItem.setDisable(true);
            }

            if (allSoundObjects.isEmpty()){
                selAllMenuItem.setDisable(true);
                playMenuItem.setDisable(true);
            }
            
            else{
                selAllMenuItem.setDisable(true);
                for (Node node : allSoundObjects){
                    SoundObject sObj = (SoundObject) node.getUserData();
                    if (!sObj.isSelected()){
                        selAllMenuItem.setDisable(false);
                    }
                }
            }
            
            if(selItems.isEmpty()){
                copyMenuItem.setDisable(true);
                cutMenuItem.setDisable(true);
                deleteMenuItem.setDisable(true);
                groupMenuItem.setDisable(true);
            }
            else if (selItems.size()==1){
                groupMenuItem.setDisable(true);
            }
            
            for (SoundObject sObj: selItems){
                if (sObj instanceof Gesture){
                    ungroupMenuItem.setDisable(false);
                }
            }
            
            for (ArrayList<Action> aList: observable.undoStack){
                for (Action aObj: aList){
                    if ((aObj instanceof CutAction) || (aObj instanceof CopyAction)){
                        pasteMenuItem.setDisable(false);
                    }
                }
            }
            
        }
        
    }
}

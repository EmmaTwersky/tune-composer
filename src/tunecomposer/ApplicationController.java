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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
//import java.util.Observable;

/**
 * Controls the application and handles the menu item selections.
 */
public class ApplicationController implements Initializable {  
    
    
    @FXML
    private MenuItem UndoMenuItem;
    @FXML
    private MenuItem RedoMenuItem;
    @FXML
    private MenuItem CutMenuItem;
    @FXML
    private MenuItem CopyMenuItem;
    @FXML
    private MenuItem PasteMenuItem;
    @FXML
    private MenuItem DeleteMenuItem;
    @FXML
    private MenuItem SelAllMenuItem;
    @FXML
    private MenuItem GroupMenuItem;
    @FXML
    private MenuItem UngroupMenuItem;
    @FXML
    private MenuItem PlayMenuItem;
    @FXML
    private MenuItem StopMenuItem;
    
    
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
//        SoundObjectParser p = new SoundObjectParser(" x:1234 y:2344 width:324 instrument:42455523 </notebar>");
//        p.getNoteData();
        
//        p = new SoundObjectParser("  <tag>");
//        p.getNextTag();
//        p = new SoundObjectParser("   <tag> ");
//        p.getNextTag();
//        p = new SoundObjectParser("  <tag>  <a> ");
//        p.getNextTag();
//        p = new SoundObjectParser("tag");
//        p.getNextTag();        
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
        PlayMenuItem.setDisable(true);
        StopMenuItem.setDisable(false);
    }
    
    /**
     * Handles the Stop menu item selection.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopMenuItemAction(ActionEvent event) {
        compositionPaneController.stop();
        PlayMenuItem.setDisable(false);
        StopMenuItem.setDisable(true);
    }
    
    private void toggleMenuItemDisable(boolean on){
        UndoMenuItem.setDisable(on);
        RedoMenuItem.setDisable(on);
        SelAllMenuItem.setDisable(on);
        PlayMenuItem.setDisable(on);
        CopyMenuItem.setDisable(on);
        CutMenuItem.setDisable(on);
        DeleteMenuItem.setDisable(on);
        GroupMenuItem.setDisable(on);
        UngroupMenuItem.setDisable(on);
        StopMenuItem.setDisable(on);
        PasteMenuItem.setDisable(on);
    }
    
    /**
     *
     */
    class ActionManagerObserver implements Observer {
        
        @Override
        public void update(Observable manager, Object x){
            ActionManager observable = (ActionManager) manager;
            setDisable(observable.isRedoStackEmpty(),observable.isUndoStackEmpty());
        }
        
        private void setDisable(boolean isRedoEmpty,boolean isUndoEmpty){
            
            toggleMenuItemDisable(false);
            UngroupMenuItem.setDisable(true);
            StopMenuItem.setDisable(true);
            
            ArrayList<SoundObject> selItems = 
                    SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY;
            
            ObservableList<Node> allSoundObjects = compositionPaneController.
                    soundObjectPaneController.soundObjectPane.getChildren();
            
            if (isUndoEmpty){
                UndoMenuItem.setDisable(true);
            }
            if (isRedoEmpty){
                RedoMenuItem.setDisable(true);
            }

            if (allSoundObjects.isEmpty()){
                SelAllMenuItem.setDisable(true);
                PlayMenuItem.setDisable(true);
            }
            
            else{
                SelAllMenuItem.setDisable(true);
                for (Node node : allSoundObjects){
                    SoundObject sObj = (SoundObject) node.getUserData();
                    if (!sObj.isSelected()){
                        SelAllMenuItem.setDisable(false);
                    }
                }
            }
            
            if(selItems.isEmpty()){
                CopyMenuItem.setDisable(true);
                CutMenuItem.setDisable(true);
                DeleteMenuItem.setDisable(true);
                GroupMenuItem.setDisable(true);
            }
            
            for (SoundObject sObj: selItems){
                if (sObj instanceof Gesture){
                    UngroupMenuItem.setDisable(false);
                }
            }
            
            // IMPLEMENTATION FOR PASTE DISABLE DOESNT WORK YET
//            Clipboard clipboard;
//            clipboard = Clipboard.getSystemClipboard();
//            ClipboardContent content = new ClipboardContent();
//            if(content.getString() != null){
//                if (!content.getString().contains("<notebar>") &&
//                        !content.getString().contains("<gesture>")){
//                    PasteMenuItem.setDisable(true);
//                }
//            }
        }
        
    }
}

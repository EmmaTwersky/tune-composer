package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.DeleteAction;
import tunecomposer.actionclasses.GroupAction;
import tunecomposer.actionclasses.SelectAction;
import tunecomposer.actionclasses.UngroupAction;

/**
 * This is a controller class for the Pane that holds the sound objects such as
 * NoteBars and Gestures.
 *
 */
public class SoundObjectPaneController {

    @FXML
    public Pane soundObjectPane;
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
    
    /**
     * Create array of selected NoteBar objects.      
     */
    public static ArrayList<SoundObject> SELECTED_SOUNDOBJECT_ARRAY = new ArrayList();
         
    /**
     * Create array to temporarily store selected NoteBar objects.      
     */
    public static ArrayList<SoundObject> TEMP_SELECTED_SOUNDOBJ_ARRAY = new ArrayList();
    
    /**
     * Fills the SELECTED_SOUNDOBJECT_ARRAY with the currently selected notes.
     * @param pane
     */
    public static void updateSelectedSoundObjectArray(Pane pane){
        SELECTED_SOUNDOBJECT_ARRAY.clear();
        for (Node n: pane.getChildren()) {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) (r).getUserData();
            if (sObj.isSelected()) {
                if (sObj.getTopGesture() == null) {
                    SELECTED_SOUNDOBJECT_ARRAY.add(sObj);
                }
            }
        }
    }
    
    public void cut() {
    }
    
    public void copy() {
    }
    
    public void paste() {
    }

    public void selectAll() {
        SelectAction selectAction;
        ArrayList<SoundObject> allObjs = new ArrayList();
                        
        for (Node sObj : soundObjectPane.getChildren()) {
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
    }
    
    public void delete() {
        if (!SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.isEmpty()) {
        DeleteAction deleter;
        deleter = new DeleteAction(SELECTED_SOUNDOBJECT_ARRAY, soundObjectPane);
        
        deleter.execute();
        actionManager.putInUndoStack(deleter);
        }
    }
    
    /**
     * Groups the selected notes or gestures.
     */
    public void group() {
        GroupAction groupAction = new GroupAction(SELECTED_SOUNDOBJECT_ARRAY, 
        actionManager, soundObjectPane);
        actionManager.execute(groupAction);
        actionManager.putInUndoStack(groupAction);
        
        updateSelectedSoundObjectArray(soundObjectPane);
    }
    
    /**
     * Ungroups the selected group.
     */
    public void ungroup() {
        for (SoundObject sObj : SELECTED_SOUNDOBJECT_ARRAY) {
            if ((sObj instanceof Gesture) && (sObj.visualRectangle.getUserData() == sObj)) {
                UngroupAction ungroupAction = new UngroupAction(
                                            (Gesture) sObj, soundObjectPane);
                actionManager.undoStack.forEach((actionList) -> {
                    for (Action action : actionList) {
                        if (!action.affectedObjs.equals(SELECTED_SOUNDOBJECT_ARRAY)) {
                            actionManager.execute(ungroupAction);
                            actionManager.putInUndoStack(ungroupAction);
                        }
                    }
                });
            }
        }
        updateSelectedSoundObjectArray(soundObjectPane);
    }
    
    /**
     * Set the GroupAction to the instance that contains the undo and 
     * redo stacks. If given manager is null, then throws NullPointerException.
     * @param manager
     * @throws NullPointerException
     */
    public void setActionManager(ActionManager manager) 
                                     throws NullPointerException{
        if (manager == null) {
            throw new NullPointerException();
        }
        actionManager = manager;
    }
}

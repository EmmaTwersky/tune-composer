package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.GroupAction;
import tunecomposer.actionclasses.UngroupAction;

/**
 * FXML Controller class
 *
 * @author EmmaTwersky
 */
public class SoundObjectPaneController {

    @FXML
    public Pane soundObjectPane;
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
    
    /**
     * Create array of NoteBar objects and selected NoteBar objects.      
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
    
    /**
     * Empties the SELECTED_SOUNDOBJECT_ARRAY and un-selects all notes.
     * @param pane
     */
    public static void unselectAllSoundObjects(Pane pane){
        for (Node n: pane.getChildren()) {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) (r).getUserData();
            if (sObj.isSelected()) {
                sObj.unselect();
            }
        }
        SELECTED_SOUNDOBJECT_ARRAY.clear();
    }
        
    public void group() {
        GroupAction groupAction = new GroupAction(SELECTED_SOUNDOBJECT_ARRAY, 
                actionManager, soundObjectPane);
        ArrayList<Action> actionArray = new ArrayList();
        actionArray.add(groupAction);
        actionManager.execute(actionArray);
        actionManager.putInUndoStack(actionArray);
        
        updateSelectedSoundObjectArray(soundObjectPane);
    }
    
    public void ungroup() {
        ArrayList<Action> actionList = new ArrayList();
        for (SoundObject sObj : SELECTED_SOUNDOBJECT_ARRAY) {
            if (sObj instanceof Gesture && sObj.visualRectangle.getUserData() == sObj) {
                UngroupAction ungroupAction = new UngroupAction(
                                            (Gesture) sObj, soundObjectPane);
                actionList.add(ungroupAction);
                System.out.println("in ungroup(), added gesture to actionList");
//                ((Gesture) sObj).ungroup(soundObjectPane);
//                sObj.containedSoundObjects.forEach((innerSObj) -> {
//                    innerSObj.select();
//                    innerSObj.visualRectangle.setUserData(innerSObj);
//                });
            }
        }
        System.out.println("executing actionList in ungroup:" + actionList);
        actionManager.execute(actionList);
        actionManager.putInUndoStack(actionList);
        updateSelectedSoundObjectArray(soundObjectPane);
    }
    
    
    /**
     * Set the GroupAction to the instance that contains the undo and 
 redo stacks. If given manager is null, then throws NullPointerException.
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

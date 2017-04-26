package tunecomposer;

import java.util.ArrayList;
import java.util.EmptyStackException;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.CopyAction;
import tunecomposer.actionclasses.CutAction;
import tunecomposer.actionclasses.DeleteAction;
import tunecomposer.actionclasses.GroupAction;
import tunecomposer.actionclasses.PasteAction;
import tunecomposer.actionclasses.SelectAction;
import tunecomposer.actionclasses.UngroupAction;

/**
 * This is a controller class for the Pane that holds the sound objects such as
 * NoteBars and Gestures.
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
     */
    public void updateSelectedSoundObjectArray(){
        SELECTED_SOUNDOBJECT_ARRAY.clear();
        for (Node n: soundObjectPane.getChildren()) {
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
     * Fills the SELECTED_SOUNDOBJECT_ARRAY with the currently selected notes.
     * @param pane the current pane
     */
    public static void staticUpdateSelectedArray(Pane pane){
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
     * Adds selected SoundObjects to Clipboard and removes them from the
     * CompositionPane.
     */
    public void cut() {
        CutAction cutAction;

        cutAction = new CutAction(SELECTED_SOUNDOBJECT_ARRAY, soundObjectPane);
        actionManager.execute(cutAction);
        actionManager.putInUndoStack(cutAction);
        
        updateSelectedSoundObjectArray();
    }
    
    /**
     * Adds selected SoundObjects to Clipboard.
     */
    public void copy() {
        CopyAction copyAction;

        copyAction = new CopyAction(SELECTED_SOUNDOBJECT_ARRAY);
        actionManager.execute(copyAction);
        actionManager.putInUndoStack(copyAction);
        
        updateSelectedSoundObjectArray();
    }
    
    /**
     * Adds most recently copied or cut SoundObjects onto the CompositionPane
     * ten pixels to the right and down.
     */
    public void paste() {
        PasteAction pasteAction;

        pasteAction = new PasteAction(soundObjectPane, actionManager);
        ArrayList<Action> lastActions;
        try {
            lastActions = actionManager.peekUndoStack();
        } catch (EmptyStackException ex) {
            actionManager.execute(pasteAction);
            actionManager.putInUndoStack(pasteAction);
            return;
        }
        Action a = lastActions.get(0);
        if (a instanceof PasteAction) {
            PasteAction oldPaste = (PasteAction) a;
            int lastOffset = oldPaste.getOffset();
            lastOffset += 10;
            pasteAction.setOffset(lastOffset);
        }
        else if (a instanceof CopyAction) {
            int OFFSET = 10;
            pasteAction.setOffset(OFFSET);
        }
        actionManager.execute(pasteAction);
        actionManager.putInUndoStack(pasteAction);

        updateSelectedSoundObjectArray();
    }
    
    /**
     * Selects all the SoundObjects on the CompositionPane.
     */
    public void selectAll() {
        ArrayList<SoundObject> allSObjs = new ArrayList();
        
        for (Node n: soundObjectPane.getChildren()) {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) (r).getUserData();
            if (!sObj.isSelected()) {
                allSObjs.add(sObj);
            }
        }

        SelectAction selectAction;
        selectAction = new SelectAction(allSObjs);
        
        actionManager.execute(selectAction);
        updateSelectedSoundObjectArray();
        
        actionManager.putInUndoStack(selectAction);
    }
        
    /**
     * Deletes the selected SoundObjects from the CompositionPane.
     */
    public void delete() {
        
        ArrayList<Action> deletions = new ArrayList();
        
        DeleteAction deleteAction;
        deleteAction = new DeleteAction(SELECTED_SOUNDOBJECT_ARRAY, soundObjectPane);

        deleteAction.execute();
        deletions.add(deleteAction);
        
        updateSelectedSoundObjectArray();
        actionManager.putInUndoStack(deletions);
    }
    
    /**
     * Groups the selected NoteBars or Gestures.
     */
    public void group() {
        GroupAction groupAction = new GroupAction(SELECTED_SOUNDOBJECT_ARRAY, 
        actionManager, soundObjectPane);
        actionManager.execute(groupAction);
        updateSelectedSoundObjectArray();
        actionManager.putInUndoStack(groupAction);
    }
    
    /**
     * Ungroups the selected group.
     */
    public void ungroup() {
        ArrayList<Action> actions = new ArrayList();
        for (SoundObject sObj : SELECTED_SOUNDOBJECT_ARRAY) {
            if ((sObj instanceof Gesture) && (sObj.visualRectangle.getUserData() == sObj)) {
                UngroupAction ungroupAction = new UngroupAction(
                                            (Gesture) sObj, soundObjectPane);
                actions.add(ungroupAction);
            }
        }
        
        actionManager.execute(actions);
        updateSelectedSoundObjectArray();
        actionManager.putInUndoStack(actions);
    }
    
    /**
     * Set the GroupAction to the instance that contains the undo and 
     * redo stacks. If given manager is null, then throws NullPointerException.
     * @param manager
     * @throws NullPointerException
     */
    public void setActionManager(ActionManager manager) throws NullPointerException{
        if (manager == null) {
            throw new NullPointerException();
        }
        actionManager = manager;
    }
}

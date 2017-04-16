package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 * This class creates the SelectAction for the ActionManager.
 * 
 * @author EmmaTwersky
 */
public class SelectAction extends Action {
    
    /**
     * Array of SoundObjects to be affected by the action.
     */
    ArrayList<SoundObject> affectedObjs;
    
    /**
     * Sets up Action to select a SoundObject. 
     * Does not yet change the selection state of notes. 
     * 
     * @param selList selList must contain all SoundObjects to be affected
     */
    public SelectAction(ArrayList<SoundObject> selList) {
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
    }
        
    /**
     * Selects all of the rectangles contained within affectedObjs.
     */
    @Override
    public void execute() {
        for (SoundObject sObj : affectedObjs) {
            sObj.select();
        }
    }
    
    /**
     * Undoes selection on the affected notes.
     */
    @Override
    public void undo() {
        for (SoundObject sObj : affectedObjs) {
            sObj.unselect();
        }
    }
    
    /**
     * Redoes the selection on the affected notes.
     */
    @Override
    public void redo() {
        execute();
    }
    
    public void changeAffectedObjs(ArrayList<SoundObject> selList) {
        affectedObjs = selList;
    }
}

//    SelectAction selectAction;
//    ArrayList<SoundObject> allObjs = new ArrayList();
//    selectAction = new SelectAction(allObjs);
//    ArrayList<Action> selectActionArray = new ArrayList<>();
//    selectActionArray.add(selectAction);
//    actionManager.execute(selectActionArray);
//    actionManager.putInUndoStack(selectActionArray);

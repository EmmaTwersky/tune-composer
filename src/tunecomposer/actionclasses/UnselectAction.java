package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 * This class creates the UnselectAction for the ActionManager.
 * 
 * @author EmmaTwersky
 */
public class UnselectAction extends Action {
    
    /**
     * Array of SoundObjects to be affected by the action.
     */
    ArrayList<SoundObject> affectedObjs;

    /**
     * Sets up Action to unselect a SoundObject. 
     * Does not yet change the selection state of notes. 
     * 
     * @param selList selList must contain all SoundObjects to be affected
     */
    public UnselectAction(ArrayList<SoundObject> selList) {
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
    }
        
    /**
     * Unselects all of the rectangles contained within affectedObjs.
     */
    @Override
    public void execute() {
        affectedObjs.forEach((sObj) -> {
            sObj.unselect();
        });
    }
    
    /**
     * Undoes unselection on the affected notes.
     */
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.select();
        });
    }
    
    /**
     * Redoes the unselection on the affected notes.
     */
    @Override
    public void redo() {
        execute();
    }
    
    public void changeAffectedObjs(ArrayList<SoundObject> selList) {
        affectedObjs = selList;
    }
}
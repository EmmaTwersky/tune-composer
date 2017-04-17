package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 * An action which selects a given array of SoundObjects.
 */
public class SelectAction extends Action {
    
    /**
     * Sets up Action by setting affectedObjs to given SoundObjects.
     * 
     * @param selectedObjs all SoundObjects to be affected
     */
    public SelectAction(ArrayList<SoundObject> selectedObjs) {
        this.affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
    }
        
    /**
     * Selects all affectedObjs.
     */
    @Override
    public void execute() {
        for (SoundObject sObj : affectedObjs) {
            sObj.select();
            sObj.selectTopGesture();
        }
    }
    
    /**
     * Unselects all affectedObjs.
     */
    @Override
    public void undo() {
        for (SoundObject sObj : affectedObjs) {
            sObj.unselect();
            sObj.unselectTopGesture();
        }
    }
    
    /**
     * Re-selects all affectedObjs.
     */
    @Override
    public void redo() {
        execute();
    }
    
    /**
     * Updates the array of affectedObjs.
     * Used in dragging events on the composition pane. Useful for changing
     * SoundObjects to affect without creating another SelectAction object.
     * 
     * @param selectedObjs all SoundObjects to be affected
     */
    public void changeAffectedObjs(ArrayList<SoundObject> selectedObjs) {
        this.affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
    }
}
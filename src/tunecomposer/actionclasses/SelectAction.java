package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 * An action which stores selecting SoundObjects in the application.
 */
public class SelectAction extends Action {
    
    /**
     * Constructs an action event to select SoundObjects.
     * Clones the selectedObjs to be affectedObjs.
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
     * Used in dragging events on the composition pane.
     * 
     * @param selectedObjs all SoundObjects to be affected
     */
    public void changeAffectedObjs(ArrayList<SoundObject> selectedObjs) {
        this.affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
    }
}
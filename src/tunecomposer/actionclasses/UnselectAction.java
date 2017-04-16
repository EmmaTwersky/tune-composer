package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 * An action which stores unselecting SoundObjects in the application.
 */
public class UnselectAction extends Action {
    
    /**
     * Constructs an action event to unselect SoundObjects.
     * Clones the selectedObjs to be affectedObjs.
     * 
     * @param selectedObjs all SoundObjects to be affected
     */
    public UnselectAction(ArrayList<SoundObject> selectedObjs) {
        affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
    }
        
    /**
     * Unselects all affectedObjs.
     */
    @Override
    public void execute() {
        affectedObjs.forEach((sObj) -> {
            sObj.unselect();
            sObj.unselectTopGesture();
        });
    }
    
    /**
     * Selects all affectedObjs.
     */
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.select();
            sObj.selectTopGesture();
        });
    }
    
    /**
     * Re-executes the action.
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
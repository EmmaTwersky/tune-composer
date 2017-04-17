package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 * An action which can be repeatedly called to change all given SoundObject's length.
 */
public class LengthChangeAction extends Action{
    
    /**
     * Mouse x position when first pressed. Used to undo and find increment.
     */
    int initialX;
    
    /**
     * Last x coordinate of the length change.
     * Set after the mouse is released by setFinalX().
     */
    int finalX;
    
    /**
     * Constructs an action event to change SoundObject's length on the screen.
     * Sets initialLength and affectedObjs.
     * 
     * @param selectedObjs selList all SoundObjects to be affected
     * @param initialX initial x location of mouse event    
     */
    public LengthChangeAction(ArrayList<SoundObject> selectedObjs, int initialX){
        this.initialX = initialX;
        affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
    }
    
    /**
     * Changes the length of all SoundObjects in affectedObjs.
     */
    @Override
    public void execute() {
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(finalX - initialX);
        });
    }

    /**
     * Changes the length of affectedObjs back to their original length.
     */      
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(initialX - finalX);
        });
    }
    
    /**
     * Re-executes action.
     */
    @Override
    public void redo(){
        execute();
    }
    
    /**
     * Changes the length of all affectedObjs. 
     * 
     * @param lengthChangeIncrement increment to change the affectedObj's length by
     */
    public void stretch(int lengthChangeIncrement){
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(lengthChangeIncrement);
        });
    }
    
    /**
     * Sets finalX to the last x location of the mouse.
     * Used by undo to determine the total change in length.
     * 
     * @param x the final x location of the mouse
     */
    public void setFinalX(int x){
        finalX = x;
    }
}
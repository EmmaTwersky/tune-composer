/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 *
 * @author lonbern
 */
public class StretchAction extends Action{
    
    /**
     * Last known x coord to determine stretch distance in stretch(...) function.
     * Updated after every stretch() call. 
     * Initially set by the construction of this.
     */
    int finalLength;
    
    /**
     * Mouse x position when first pressed. Used to undo.
     */
    int initialLength;
    
    boolean executed;
    ArrayList<SoundObject> affectedObjs;

    
        /**
     * Sets up object to allow sound objects to be stretched. selList must contain all
     * SoundObjects to be infected, including NoteBars and GestureBoxes.
     * @param selList
     *          selList must contain all SoundObjects to be affected, including 
     *          NoteBars and GestureBoxes. Action will not look for SoundObjects 
     *          that should be selected by relation to a selected Gesture.
     * @param initLen initial x location of mouse click.     
     */
    public StretchAction(ArrayList<SoundObject> selList, int initLen){
        
        initialLength = initLen;
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
        
    }
    
    /**
     * Stretches all notes and gestures associated with this action. The input
     * is not where the items should be moved to. Instead, it is the position
     * of the mouse. The increment will be deduced from past and present mouse
     * positions.
     * @param lengthInc x coord of mouse.
     */
    public void stretch(int lengthInc){
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(lengthInc);
        });
        finalLength = lengthInc;
    }
    
    /**
     * Sets executed to true to set this as a completed action. After calling,
     * undo and redo will be possible. Called by redo, and 
     * can also be used as redo.
     */
    @Override
    public void execute() {
        executed = true;
    }

    /**
     * Sets all rectangles and gestureBoxes to the length they were before 
     * the StretchAction began. Can only undo if executed is true.
    */        
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(finalLength-initialLength);
        });
    }
    
    
    
}

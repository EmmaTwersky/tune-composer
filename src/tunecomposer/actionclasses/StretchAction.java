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
     * Set after the mouse is released by setFinalX().
     */
    int finalLength;
    
    /**
     * Mouse x position when first pressed. Used to undo.
     */
    int initialLength;
    
    /**
     * Listed of sound objects that were selected when the action began.
     */
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
     * @param lengthInc stretch increment.
     */
    public void stretch(int lengthInc){
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(lengthInc);
        });
    }
    
    /**
     * Sets finalLength to the last x location of the mouse.
     * Used by undo to determine the total change in length.
     * @param x the final x location of the mouse.
     */
    public void setFinalX(int x){
        finalLength = x;
    }
    
    /**
     * Changes the length of of the all affected sound objects.
     * execute is the opposite of undo is called by redo.
     */
    @Override
    public void execute() {
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(finalLength-initialLength);
        });
    }

    /**
     * Sets all rectangles and gestureBoxes to the length they were before 
     * the StretchAction began. Can only undo if executed is true.
    */        
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.changeLength(initialLength-finalLength);
        });
    }
    
    /**
     * Sets all rectangles and gestureBoxes to the length they were before 
     * the StretchAction was undone. calls execute.
    */
    @Override
    public void redo(){
        execute();
    }
    
    
}

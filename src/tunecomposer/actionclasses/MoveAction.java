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
 * @author lazarcl
 */
public class MoveAction extends Action {

    /**
     * Last known mouse coords to determine move distance in move(...) function.
     * Updated after every move() call. 
     * Initially set by the construction of this.
     */
    private int lastX;
    private int lastY;
    
    /**
     * Mouse position when first pressed. Used to undo.
     */
    private int startX;
    private int startY;
    
    ArrayList<SoundObject> affectedObjs;
    boolean executed;

    /**
     * Sets up object to allow sounds to be moved. selList must contain all
     * SoundObjects to be infected, including NoteBars and GestureBoxes. Action
     * will not look for SoundObjects that should be selected by relation to 
     * a selected Gesture.
     * @param selList
     *          selList must contain all SoundObjects to be affected, including 
     *          NoteBars and GestureBoxes. Action will not look for SoundObjects 
     *          that should be selected by relation to a selected Gesture.
     * @param x initial x location of mouse click.     
     * @param y initial y location of mouse click.
     */
    public MoveAction(ArrayList<SoundObject> selList, int x, int y) {
        
        startX = x;
        startY = y;
        lastX = x;
        lastY = y;
        
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
    }
    
    
    /**
     * Moves all rectangles and gestures associated with this action. The input
     * is not where the items should be moved to. Instead, it is the position
     * of the mouse. The increment will be deduced from past and present mouse
     * positions.
     * @param mouseX x coord of mouse
     * @param mouseY y coord of mouse
     */
    public void move(int mouseX, int mouseY) {
        
        affectedObjs.forEach((sObj) -> {
            sObj.move(mouseX, mouseY);
        });
    }
    
    
    /**
     * Sets all rectangles and gestureBoxes to where they were before 
     * the MoveAction began. Can only undo if executed is true.
    */
    @Override
    public void undo() {
        affectedObjs.forEach((sObj)->{
            sObj.move(startX-lastX, startY-lastY);
        });
    }
    
    public void setLastCoords(int x, int y){
        lastX = x;
        lastY = y;
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
    
}


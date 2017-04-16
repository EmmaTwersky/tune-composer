/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.NoteBar;
import tunecomposer.SoundObject;

/**
 *
 * @author lazarcl
 */
public class MoveAction extends Action {

    /**
     * The final mouse coordinates of the movement.
     * Set after the mouse is released.
     * 
     */
    private double lastX;
    private double lastY;
    
    /**
     * Mouse position when first pressed. Used to undo.
     */
    private final double startX;
    private final double startY;
    
    /**
     * Listed of sound objects that were selected when the action began.
     */
    ArrayList<SoundObject> affectedObjs;
    

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
    public MoveAction(ArrayList<SoundObject> selList, double x, double y) {
        
        startX = x;
        startY = y;
        
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
    public void move(double mouseX, double mouseY) {
        
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
            sObj.move(startX-lastX, startY-lastY+sObj.HEIGHT);
            sObj.snapInPlace();
        });
    }
    
    
    /**
     * Sets lastX and lastY to the latest drag location.
     * This allows "undo" to calculate the total move distance.
     * @param x
     * @param y
    */
    public void setLastCoords(double x, double y){
        lastX = x;
        lastY = y;
    }

    /**
     * Moves and sets into place all sound objects.
     * execute is the opposite of undo is called by redo.
     */
    @Override
    public void execute() {
        affectedObjs.forEach((sObj)->{
            sObj.move(lastX-startX, lastY-startY);
            sObj.snapInPlace();
        });
    }
    
        /**
     * Sets all rectangles and gestureBoxes to where they were before 
     * the MoveAction was undone. calls execute.
    */
    @Override
    public void redo() {
        execute();
    }
    
}


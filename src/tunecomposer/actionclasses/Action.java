/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author lazarcl
 */
public abstract class Action {
    /**
     * Array of rectangles to be affected by the action.
     */
    ArrayList<Rectangle> affectedObjs;
    /**
     * Whether the action was executed. False if not, true if it has been.
     */
    boolean executed = false;
    

    
    /**
     * Performs the action based on the given paramaters in the constructor.
     * This action determines whether an action can be undone.
     */
    public abstract void execute(); 
    
    /**
     * Undoes the action that was executed if executed is true. Does nothing
     * if false.
     */
    public abstract void undo();
    
    /**
     * Calls execute to perform the action again.
     */
    public void redo() {
        execute();
    }
    
    /**
     * From affectedObjs, adds any note or gesture relative into affectedObjs.
     * Ensures that entire gestures will be affected by selection.
     */
    public void addGestureSiblings() {
        //TODO
    }
    
}

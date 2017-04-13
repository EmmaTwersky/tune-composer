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
public class SelectAction extends Action {
    
    
    /**
     * Boolean flag to set this action to select or unselect given rectangles.
     * If true, then select all the objects, if false, then unselect all objs.
     */
    private final boolean selectObjs;
    
    
    
    /**
     * Sets up Action object as select or unselect. 
     * Does not yet change the selection state of notes. 
     * That is for the execute() method.
     * @param selList
     *          selList must contain all Rectangles to be affected, including 
     *          NoteBars and GestureBoxes. Action will not look for Rectangles 
     *          that should be selected by relation to a selected Gesture.
     * @param select 
     *          If select is true, then action will select all given Rectangles.
     *          If false, action will unselect all given Rectangles.
     */
    public SelectAction(ArrayList<Rectangle> selList, boolean select) {
        affectedObjs = (ArrayList<Rectangle>) selList.clone();
        selectObjs = select;
    }

    
    @Override
    public void undo() {
        //gesture siblings already added, so no need to call again.
        if (executed != true) {
            return;
        }
        for (Rectangle r : affectedObjs) {
            if (selectObjs == true) {
                unselect(r);
            }
            else {
                select(r);
            }
        }
    }
        
    /**
     * Selects or unselects all of the rectangles contained within affectedObjs.
     */
    @Override
    public void execute() {
        addGestureSiblings();
        for (Rectangle r : affectedObjs) {
            if (selectObjs == true) {
                select(r);
            }
            else {
                unselect(r);
            }
        }
    }
    
    /**
     * Sets the styling of the given rectangle as selected.
     * @param r 
     */
    private void select(Rectangle r) {

    }
    
    /**
     * Sets the styling of the given rectangle as selected.
     * @param r 
     */
    private void unselect(Rectangle r) {
        
    }
}

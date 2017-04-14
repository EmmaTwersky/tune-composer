/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import tunecomposer.Gesture;

/**
 *
 * @author lazarcl
 */
public class UngroupAction extends AbstractGroupAction {
    
    /**
     * @param selList
     *          All selected rectangles must have their top-most gesture be 
     * @param _gestPane reference to the gesture pane.
     */
    public UngroupAction(ArrayList<Rectangle> selList, Pane _gestPane) {
        theGesture = findTopGesture(selList);

        
        gestureBox = findGestureBox(theGesture);

       
        gesturePane = _gestPane;
        
        
        affectedObjs = (ArrayList<Rectangle>) selList.clone();
    }
    
    
    /**
     * Confirms that all rectangles in given array are related by at least one
     * Gesture. If parameter null then throws exception.
     * @return 
     *    Returns reference to top gesture if satisfied, null if not satisfied.
     * @throws NullPointerException
     */
    private static Gesture findTopGesture(ArrayList<Rectangle> list) 
                                                throws NullPointerException {
        //TODO
        return null;
    }
    
    /**
     * Finds the gestureBox that is related to the given gesture.
     * @param gesturePane
     * @return the gestureBox in the gesture pane that the gesture belongs to
     *      returns null if not found.
     */
    private static Rectangle findGestureBox(Gesture gesture) {
        //TODO
        return null;
    }
    
    /**
     * Ungroups the selected nodes
     */
    public void execute() {
        //TODO
        // call ungroup() from super
    }

            
    public void undo() {
        // call group from super.
    }
            
}

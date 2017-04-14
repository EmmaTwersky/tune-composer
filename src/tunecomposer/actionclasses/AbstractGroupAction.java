package tunecomposer.actionclasses;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author lazarcl
 */
public abstract class AbstractGroupAction extends Action{

    /**
     * Gesture box rectangle
     */
    Rectangle gestureBox;
    
    
    /**
     * The gesture object that was created in this action.
     */
    tunecomposer.Gesture theGesture;
    
    
    /**
     * The gesture pane to add gestureBox to while grouping.
     */
    Pane gesturePane;
    
    
    
    /**
     * Groups the rectangles in affectedObjs together. Assumes gesture and 
     * gesture box already exist. Assumes gesture and gestureBox are 
     * already linked. 
     */
    public void group() {
        //add gesture box to gesturePane
        //create newGesture object
        // for rect in affectedObjs
            // if rect's parentGesture is null
                // set newGesture as parent
            // else, do nothing(it already has a parent gest)
        //execute = true
    }

    /**
     * Ungroups the rectangles in affectedObjs together. Assumes gesture and 
     * gesture box already exist. Assumes gesture and gestureBox are 
     * already linked. 
     */
    public void ungroup() {
        //if execute == false, return;
        //for rect in affectedObjs
            //if rect.parentGesture is newGesture
                //set to null
        //remove gestureBox from pane, but keep for redo()
    }
    
}

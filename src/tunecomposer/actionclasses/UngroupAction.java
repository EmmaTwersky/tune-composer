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
import tunecomposer.SoundObject;

/**
 *
 * @author lazarcl
 */
public class UngroupAction extends AbstractGroupAction {
    
    
    /**
     * The gesture object that was created in this action.
     */
    tunecomposer.Gesture theGesture;
    

    /**
     * Array of SoundObjects to be affected by the action.
     */
    ArrayList<SoundObject> affectedObjs;
    
    /**
     * Pane that all SoundObject visuals live within.
     */
    private final Pane soundObjectPane;

    /**
     * @param selList
     *          All selected rectangles must have their top-most gesture be 
     * @param _pane reference to the gesture pane.
     */
    public UngroupAction(ArrayList<SoundObject> selList, Pane _pane) {
        theGesture = findTopGesture(selList);
       
        soundObjectPane = _pane;
        
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
    }
    
    
    /**
     * Confirms that all rectangles in given array are related by at least one
     * Gesture. If parameter null then throws exception.
     * @return 
     *    Returns reference to top gesture if satisfied, null if not satisfied.
     * @throws NullPointerException
     */
    private static Gesture findTopGesture(ArrayList<SoundObject> list) 
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

    @Override
    public void redo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
            
}

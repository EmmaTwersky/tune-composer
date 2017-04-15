/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.SoundObject;

/**
 *
 * @author lazarcl
 */
public class GroupAction extends AbstractGroupAction {
    
    
    
    /**
     * @param selList
     *          must contain all SoundObjects to be affected, can include
     *          rectangles that already have a parent gesture.
     * @param _gestPane reference to the gesture pane.
     */
    public GroupAction(ArrayList<SoundObject> selList, Pane _gestPane) {
        gesturePane = _gestPane;
        
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
    }
    
    
    public void execute() {
        //addGestureSiblings()
        //find top, bot, left, right extremes
        //make gestureBox
        //set gestureBox userData to newGesture
        //add gesture box to gesturePane
        //call group() from super.
    }

            
    public void undo() {
        // call ungroup from super.
    }
            
}

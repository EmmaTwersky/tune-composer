/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import tunecomposer.SoundObject;
import tunecomposer.Gesture;

/**
 *
 * @author lazarcl
 */
public class GroupAction extends AbstractGroupAction {
    

    /**
     * The gesture object that was created in this action.
     */
    Gesture gesture;
    

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
     *          must contain all SoundObjects to be affected.
     * @param _pane reference to the soundObjectPane.
     */
    public GroupAction(ArrayList<SoundObject> selList, Pane _pane) {
        gesture = new Gesture(selList);
        
        soundObjectPane = _pane;
        
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
    }
    
    
    @Override
    public void execute() {
        gesture.addToPane(soundObjectPane);
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
    
    public void redo() {
        
    }
}

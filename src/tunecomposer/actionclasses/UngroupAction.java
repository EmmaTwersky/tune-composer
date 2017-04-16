/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import javafx.scene.layout.Pane;
import tunecomposer.Gesture;

/**
 *
 * @author lazarcl
 */
public class UngroupAction extends Action {
    
    
    /**
     * The gesture object that was created in this action.
     */
    Gesture gesture;

    
    /**
     * Pane that all SoundObject visuals live within.
     */
    private final Pane soundObjectPane;

    /**
     * Sets fields to perform all methods in this instance. Must call execute()
     * to actually ungroup the given gesture.
     * @param givenGest
     *          Gesture to execute Ungroup on
     * @param _pane reference to the soundObjectPane.
     */
    public UngroupAction(Gesture givenGest, Pane _pane) {
        soundObjectPane = _pane;
                
        gesture = givenGest;
    }
    
    /**
     * Ungroups the selected nodes. Reset contained item's handlers to their 
     * previous state and remove gestueBox from soundObjectPane.
     */
    @Override
    public void execute() {
        gesture.ungroup(soundObjectPane);
        gesture.containedSoundObjects.forEach((innerSObj) -> {
            innerSObj.select();
            innerSObj.visualRectangle.setUserData(innerSObj);
        });
    }

    /**
     * Groups all the items associated by setting their handlers to this.gesture,
     * and adding gestureBox to soundObjectPane.
     */
    @Override
    public void undo() {
        gesture.group(soundObjectPane);
    }

    /**
     * Regroups the gesture to the state it was initially in when passed to 
     * this UngroupAction instance.
     */
    @Override
    public void redo() {
        execute();
    }
            
}

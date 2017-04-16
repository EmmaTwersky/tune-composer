/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.ActionManager;
import tunecomposer.SoundObject;
import tunecomposer.Gesture;

/**
 *
 * @author lazarcl
 */
public class GroupAction extends Action{
    

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
     * to group given items.
     * @param selList
     *          must contain all SoundObjects to be affected.
     * @param _actionManager
     * @param _pane reference to the soundObjectPane.
     */
    public GroupAction(ArrayList<SoundObject> selList, ActionManager _actionManager, Pane _pane) {             
        soundObjectPane = _pane;
        gesture = new Gesture(selList, _actionManager, soundObjectPane);
        gesture.visualRectangle.setUserData(gesture);
    }
    
    
    /**
     * Groups all the given items by setting their handlers to this.gesture,
     * and adding gestureBox to soundObjectPane.
     */
    @Override
    public void execute() {
        gesture.addToPane(soundObjectPane);
    }

    
    /**
     * Ungroups the gesture. This keeps a reference of the gesture for redo.
     */
    @Override
    public void undo() {
        gesture.ungroup(soundObjectPane);
        gesture.containedSoundObjects.forEach((innerSObj) -> {
            innerSObj.select();
            innerSObj.visualRectangle.setUserData(innerSObj);
        });
    }
    
    
    /**
     * Regroup the gesture to the same Gesture instance that the group was
     * first set to. All handlers are set to this gesture, and gesture box is
     * put into soundObjectPane with correct sizing.
     */
    @Override
    public void redo() {
        execute();
    }
}

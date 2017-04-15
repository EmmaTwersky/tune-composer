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
public class DeleteAction extends Action {

    /**
     * Array of SoundObjects to be affected by the action.
     */
    ArrayList<SoundObject> affectedObjs = new ArrayList<>();
    
    /**
     * Pane that all SoundObject visuals live within.
     */
    private final Pane soundObjectPane;

    
    /**
     * Sets up object for delete. Action will not be executed, execute() must be
     * called later to perform the action. Passed List of rectangles can be a 
     * combination of gesture boxes and note rectangles.
     * @param selList
     *          selList must contain all SoundObjects to be affected, including 
     *          NoteBars and GestureBoxes. Action will not look for SoundObjects 
     *          that should be selected by relation to a selected Gesture.
     * @param _pane Reference to the SoundObject pane where sound visuals live.
     */
    public DeleteAction(ArrayList<SoundObject> selList, Pane _pane) {
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
        soundObjectPane = _pane;
    }
    
    
    /**
     * Adds all rectangles back to their original panes. Holds a reference of 
     * all rectangles for redo(). Cannot be 
     */
    @Override
    public void undo() {
        for (SoundObject sObj : affectedObjs) {
            sObj.addToPane(soundObjectPane);
        }
    }

    /**
     * Removes all rectangles from their respective notePane or gestPane. Sets
     * execute to true so undo() can be performed. 
     */
    @Override
    public void execute() { 
        for (SoundObject sObj: affectedObjs) {
            sObj.removeFromPane(soundObjectPane);
        }
    }
    
}

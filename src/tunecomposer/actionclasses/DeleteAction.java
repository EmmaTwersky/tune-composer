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
     * References to the NoteBarPane and gesturePane that notes and gestures
     * should be removed from.
     */
    private Pane notePane;
    private Pane gestPane;

    
    /**
     * Sets up object for delete. Action will not be executed, execute() must be
     * called later to perform the action. Passed List of rectangles can be a 
     * combination of gesture boxes and note rectangles.
     * @param selList
     *          selList must contain all SoundObjects to be affected, including 
     *          NoteBars and GestureBoxes. Action will not look for SoundObjects 
     *          that should be selected by relation to a selected Gesture.
     * @param _notePane Reference to the notePane.
     * @param _gestPane Reference to the gesturePane.
     */
    public DeleteAction(ArrayList<SoundObject> selList, Pane _notePane, 
                        Pane _gestPane) {
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
        notePane = _notePane;
        gestPane = _gestPane;
    }
    
    
    /**
     * Adds all rectangles back to their original panes. Holds a reference of 
     * all rectangles for redo(). Cannot be 
     */
    @Override
    public void undo() {
        if (executed != true) {
            return;
        }
        for (SoundObject r : affectedObjs) {
            //TODO
            //need to seperate gesture boxes from notes to add to correct pane
        }
    }

    /**
     * Removes all rectangles from their respective notePane or gestPane. Sets
     * execute to true so undo() can be performed. 
     */
    @Override
    public void execute() { 
        addGestureSiblings();
        for (SoundObject r : affectedObjs) {
            //TODO
            //need to sort rectangles by type to remove from correct pane.
//            notePane.getChildren().add(rectangle);
//            executed = true;
        }
    }
    
}

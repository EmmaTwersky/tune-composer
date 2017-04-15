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
public class SelectAction extends Action {
    
    
    /**
     * Boolean flag to set this action to select or unselect given rectangles.
     * If true, then select all the objects, if false, then unselect all objs.
     */
    private final boolean selectObjs;
    

    /**
     * Array of SoundObjects to be affected by the action.
     */
    ArrayList<SoundObject> affectedObjs = new ArrayList<>();
    
    /**
     * Pane that all SoundObject visuals live within.
     */
    private final Pane soundObjectPane;

    
    
    /**
     * Sets up Action object as select or unselect. 
     * Does not yet change the selection state of notes. 
     * That is for the execute() method.
     * @param selList
     *          selList must contain all Rectangles to be affected, including 
     *          NoteBars and GestureBoxes. Action will not look for Rectangles 
     *          that should be selected by relation to a selected Gesture.
     * @param soundObjPane
     *          Pane that holds all SoundObject visuals. 
     * @param select 
     *          If select is true, then action will select all given Rectangles.
     *          If false, action will unselect all given Rectangles.
     */
    public SelectAction(ArrayList<SoundObject> selList, Pane soundObjPane,
                                                            boolean select) {
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
        selectObjs = select;
        soundObjectPane = soundObjPane;
    }

    
    @Override
    public void undo() {
        for (SoundObject obj : affectedObjs) {
            if (selectObjs == true) {
                obj.unselect();
            }
            else {
                obj.select();
            }
        }
    }
        
    /**
     * Selects or unselects all of the rectangles contained within affectedObjs.
     */
    @Override
    public void execute() {
        for (SoundObject obj : affectedObjs) {
            if (selectObjs == true) {
                obj.select();
            }
            else {
                obj.unselect();
            }
        }
    }
    
//    /**
//     * Sets the styling of the given rectangle as selected.
//     * @param r 
//     */
//    private void select(Rectangle r) {
//
//    }
//    
//    /**
//     * Sets the styling of the given rectangle as selected.
//     * @param r 
//     */
//    private void unselect(Rectangle r) {
//        
//    }
}

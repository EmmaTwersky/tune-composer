/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import tunecomposer.ActionManager;
import tunecomposer.SoundObject;

/**
 *  An action which adds a new chord to the soundObjectPane.
 */
public class AddSoundAction extends Action{
    
    /**
     * An instance of the ScrollPane that the composition pane is set on.
     */
    public ScrollPane scrollPane;
    
    /**
     * Constructor for AddSoundAction.
     * 
     * @param chordNotes
     * @param soundObjPane
     * @param actionManager
     * @param scrollP 
     */
    public AddSoundAction(ArrayList<SoundObject> chordNotes, Pane soundObjPane, ActionManager actionManager, ScrollPane scrollP) {
        affectedObjs = chordNotes;
        soundObjectPane = soundObjPane;
        scrollPane = scrollP;
    }
     
    /**
     * Adds the chord to the soundObjectPane.
     */
    @Override
    public void execute(){        
        affectedObjs.forEach((sObj) -> {
            sObj.addToPane(soundObjectPane);
                    });
    }
    
    /**
     * Removes the chord from the soundObjectPane.
     */
    @Override
    public void undo(){
        affectedObjs.forEach((sObj) -> {
            sObj.removeFromPane(soundObjectPane);
        });
    }
    
    /**
     * Re-executes action.
     */
    @Override
    public void redo(){
        execute();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import javafx.scene.layout.Pane;
import tunecomposer.ActionManager;
import tunecomposer.SoundObjectParser;
import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 *  An action which adds a new chord to the soundObjectPane.
 */
public class AddSoundAction extends Action{
    
    public final SoundObjectParser parser;
    
    public AddSoundAction(String soundXML, Pane soundObjectPane, ActionManager actionManager) {
        
        this.soundObjectPane = soundObjectPane;
        
        parser = new SoundObjectParser(soundXML, soundObjectPane, actionManager);
       
    }
    
    
    /**
     * Adds the chord to the soundObjectPane.
     */
    @Override
    public void execute(){
        ArrayList<SoundObject> objToAdd = parser.parseString();
        objToAdd.forEach((sObj) -> {
            sObj.addToPane(soundObjectPane);
                    });
    }
    
    /**
     * Removes the chord from the soundObjectPane.
     */
    @Override
    public void undo(){
        
    }
    
    /**
     * Re-executes action.
     */
    @Override
    public void redo(){
        execute();
    }
}

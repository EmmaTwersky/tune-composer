/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import tunecomposer.ActionManager;
import tunecomposer.CompositionPaneController;
import tunecomposer.SoundObjectParser;

/**
 *  An action which adds a new chord to the soundObjectPane.
 */
public class AddSoundAction extends Action{
    
    public final SoundObjectParser parser;
    
    public ScrollPane scrollPane;
    
    public AddSoundAction(String soundXML, Pane soundObjectPane, ActionManager actionManager, ScrollPane scrollP) {
        
        this.soundObjectPane = soundObjectPane;
        
        parser = new SoundObjectParser(soundXML, soundObjectPane, actionManager);
        affectedObjs = parser.parseString();
        
        scrollPane = scrollP;

    }
    
    
    /**
     * Adds the chord to the soundObjectPane.
     */
    @Override
    public void execute(){
        int topLeftX = (int) ((18000)*  scrollPane.getHvalue());
        int topLeftY = (int) ((1280)* scrollPane.getVvalue());
        
        int newX = topLeftX;
        int newY = topLeftY;
        
        System.out.println(newX);
        System.out.println(newX);

        affectedObjs.forEach((sObj) -> {
            sObj.move(newX, newY);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.Pane;
import tunecomposer.SoundObject;

/**
 *
 * @author EmmaTwersky
 */
public class PasteAction extends Action {
/**
     * Constructs an action event to copy SoundObjects.
     * Sets affectObjs and soundObjectPane.
     * @param soundObjectPane
     */
    public PasteAction(Pane soundObjectPane) {
        this.soundObjectPane = soundObjectPane;
        Clipboard clipboard;
        clipboard = Clipboard.getSystemClipboard();
        
        clipboard.getContent(DataFormat.HTML);
        
        String sObjs = clipboard.getString();
//        TODO: TURN STRING INTO OBJECTS
//          compare to most recent in undo stack (mayb happens in sound objectpane controller)
//        set affectedObjs to clipboard's string
    }
    
    /**
     * Copies all affectedObjs from the soundObjectPane.
     */
    @Override
    public void execute() {        
        affectedObjs.forEach((sObj) -> {
            sObj.addToPane(soundObjectPane);
        });
    }
    
    /**
     * Not possible.
     */
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.removeFromPane(soundObjectPane);
        });
    }

    /**
     * Not possible. 
     */
    @Override
    public void redo() {
    }
}

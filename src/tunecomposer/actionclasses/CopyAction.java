/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import tunecomposer.SoundObject;

/**
 *
 * @author EmmaTwersky
 */
public class CopyAction extends Action {
    
    /**
     * Constructs an action event to copy SoundObjects.
     * Sets affectObjs and soundObjectPane.
     * @param selectedObjs selList all SoundObjects to be affected
     */
    public CopyAction(ArrayList<SoundObject> selectedObjs) {
        affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
    }
    
    /**
     * Copies all affectedObjs from the soundObjectPane.
     */
    @Override
    public void execute() {
        String sObjs = "";
        affectedObjs.forEach((sObj) -> {
//            sObjs = sObjs + sObj.objectToXML();
//              TODO: make this happen
        });
        
        Clipboard clipboard;
        clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(sObjs);
//        content.putHtml("<b>Some</b> text");
        clipboard.setContent(content);
    }
    
    /**
     * Not possible.
     */
    @Override
    public void undo() {
    }

    /**
     * Not possible. 
     */
    @Override
    public void redo() {
    }
}

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
public abstract class Action {

    /**
     * Performs the action based on the given paramaters in the constructor.
     * This action determines whether an action can be undone.
     */
    public abstract void execute(); 
    
    /**
     * Undoes the action that was executed if executed is true. Does nothing
     * if false.
     */
    public abstract void undo();
    
    /**
     * Calls execute to perform the action again.
     */
    public void redo() {
        execute();
    }

}

package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.SoundObject;

/**
 * Abstract action class creates the skeleton of an action item, which stores
 * the changes in state of the Tune Composer.
 */
public abstract class Action {
    
    /**
     * Array of SoundObjects affected by the action.
     * Not always used in actions.
     */
    public ArrayList<SoundObject> affectedObjs;
    
    /**
     * Pane reference to the pane that affectedObjs are on.
     */
    public Pane soundObjectPane;
    
    /**
     * Performs the action based on the current state of the Action's fields.
     */
    public abstract void execute(); 
    
    /**
     * Undoes the changes performed during execute().
     */
    public abstract void undo();
    
    /**
     * Performs the action again.
     * Similar to execute, but in complex cases is not identical.
     */
    public abstract void redo();
}

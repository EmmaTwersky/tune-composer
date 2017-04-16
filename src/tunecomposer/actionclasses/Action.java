package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.SoundObject;

/**
 * Abstract action class creates the skeleton of an action item, which stores
 * the changed state of the Tune Composer.
 */
public abstract class Action {
    
    /**
     * Array of SoundObjects affected by the action.
     */
    public ArrayList<SoundObject> affectedObjs;
    
    /**
     * Pane reference to the pane that affectedObjs are on.
     */
    public Pane soundObjectPane;
    
    /**
     * Performs the action based on the given parameters in the constructor.
     */
    public abstract void execute(); 
    
    /**
     * Undoes the performed action.
     */
    public abstract void undo();
    
    /**
     * Performs the action again.
     * Similar to redo, but in complex cases is not identical.
     */
    public abstract void redo();
}

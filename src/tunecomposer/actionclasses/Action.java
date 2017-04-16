package tunecomposer.actionclasses;

/**
 * Abstract action class creates the skeleton of an action item, which stores
 * the changed state of the Tune Composer.
 */
public abstract class Action {

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

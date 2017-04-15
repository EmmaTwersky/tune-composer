package tunecomposer;

import java.util.Stack;
import tunecomposer.actionclasses.Action;

public class ActionManager {
    
    Stack<Action> undoStack;
    Stack<Action> redoStack;
    
    /**
     * Constructs the undo and redo stacks. They take any type Action object.
     */
    public ActionManager(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }
    
    /**
     * Perform the given action. Then push this item into the undoStack.
     * @param action Action type object to be performed in program. 
     */
    public void execute(Action action) {
        action.execute();
        undoStack.push(action);
        System.out.println("EXECUTE: undoStack Print: \t" + undoStack.toString());
    }
    
    /**
     * Pop top item on undoStack. Undo the action, then push it onto the
     * redoStack.
     */
    public void undo(){
        if (undoStack.isEmpty()) {
            return;
        }
        Action undoAction;
        undoAction = undoStack.pop();
        undoAction.undo();
        redoStack.push(undoAction);
        
        System.out.println("UNDO: undoStack Print: \t" + undoStack.toString() );

    }
    
    /**
     * Pop top item from redoStack. Redo the action's effects, and push it back
     * onto the undoStack.
     */
    public void redo(){
        if (redoStack.isEmpty()) {
            return;
        }
        Action redoAction;
        redoAction = redoStack.pop();
        redoAction.redo();
        undoStack.push(redoAction);
    }
    
    
    /**
     * Check if undo stack is empty. Returns false if it contains Actions, true
     * if it is empty.
     * @return boolean: true==empty, false==not empty
     */
    public boolean isUndoStackEmpty() {
        if (undoStack.isEmpty()) {
            return true;
        }
        return false;
    }
    
        /**
     * Check if redo stack is empty. Returns false if it contains Actions, true
     * if it is empty.
     * @return boolean: true==empty, false==not empty
     */
    public boolean isRedoStackEmpty() {
        if (redoStack.isEmpty()) {
            return true;
        }
        return false;
    }
}
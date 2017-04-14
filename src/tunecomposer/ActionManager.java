/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Stack;
import tunecomposer.actionclasses.Action;

/**
 *
 * @author vivancr
 */
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
    }
    
    /**
     * Pop top item on undoStack. Undo the action, then push it onto the
     * redoStack.
     */
    public void undo(){
        Action undoAction;
        undoAction = undoStack.pop();
        undoAction.undo();
        undoStack.push(undoAction);
    }
    
    /**
     * Pop top item from redoStack. Redo the action's effects, and push it back
     * onto the undoStack.
     */
    public void redo(){
        Action redoAction;
        redoAction = redoStack.pop();
        redoAction.redo();
        redoStack.push(redoAction);
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
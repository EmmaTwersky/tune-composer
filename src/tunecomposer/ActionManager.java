package tunecomposer;

import java.util.ArrayList;
import java.util.Stack;
import tunecomposer.actionclasses.Action;

/**
 * Manages the undo and redo stacks that all "undo-able" actions are pushed onto.
 */
public class ActionManager {
    
    /**
     * References to the stacks which hold actions to manage.
     */
    Stack<ArrayList<Action>> undoStack;
    Stack<ArrayList<Action>> redoStack;
    
    /**
     * Constructs the undo and redo stacks. 
     * These stacks can contain any object of type Action.
     */
    public ActionManager(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }
    
    /**
     * Perform the given action. 
     * 
     * @param action Action type object to be performed in program
     */
    public void execute(Action action) throws IllegalArgumentException{
        action.execute();
    }
    
    /**
     * Executes all given actions in the provided ArrayList of Actions.
     * 
     * @param actionList ArrayList of Action type objects to be performed in program
     */
    public void execute(ArrayList<Action> actionList) throws IllegalArgumentException{
        for (Action a : actionList) {
            this.execute(a);
        }
    }
    
    /**
     * Puts given Action onto the stack. If actionArray is null,
     * then does nothing. Clears redoStack to avoid redoing actions when it 
     * doesn't make sense to.
     * 
     * @param action action to put onto the stack
     */
    public void putInUndoStack(Action action) {
        ArrayList<Action> actionArray = new ArrayList();
        actionArray.add(action);
        
        redoStack.clear();
        undoStack.push(actionArray);
    }
    
    /**
     * Puts given ArrayList of actions onto the stack. If actionArray is null,
     * then does nothing. Clears redoStack to avoid redoing actions when it 
     * doesn't make sense to.
     * 
     * @param actionArray Actions to put onto the stack
     */
    public void putInUndoStack(ArrayList<Action> actionArray) {
        if (actionArray.isEmpty()) {
            return;
        }
        
        redoStack.clear();
        undoStack.push(actionArray);
    }
    
    
    /**
     * Pop top item on undoStack. 
     * Undo the action, then push it onto the redoStack.
     */
    public void undo(){

        if (undoStack.isEmpty()) {
            return;
        }
        ArrayList<Action> undoActions;
        undoActions = undoStack.pop();
        
        for (Action a : undoActions) {
            a.undo();
        }
        redoStack.push(undoActions);
    }
    
    /**
     * Pop top item from redoStack. 
     * Redo the action's effects, and push it back onto the undoStack.
     */
    public void redo(){
        if (redoStack.isEmpty()) {
            return;
        }
        ArrayList<Action> redoActions;
        redoActions = redoStack.pop();
        
        for (Action a : redoActions) {
            a.redo();   
        }
        undoStack.push(redoActions);
    }
    
    /**
     * Check if undo stack is empty. 
     * Returns false if it contains Actions, true if it is empty.
     * 
     * @return boolean if undoStack is empty
     */
    public boolean isUndoStackEmpty() {
        if (undoStack.isEmpty()) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if redo stack is empty. 
     * Returns false if it contains Actions, true if it is empty.
     * 
     * @return boolean if redoStack is empty
     */
    public boolean isRedoStackEmpty() {
        if (redoStack.isEmpty()) {
            return true;
        }
        return false;
    }
}

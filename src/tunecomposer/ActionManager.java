package tunecomposer;

import java.util.ArrayList;
import java.util.Stack;
import tunecomposer.actionclasses.Action;

/**
 * Object that manages the action stacks that all "mutable" actions are held in.
 */
public class ActionManager {
    
    /**
     * References to the stacks which hold actions to manage.
     */
    Stack<ArrayList<Action>> undoStack;
    Stack<ArrayList<Action>> redoStack;
    
    /**
     * Constructs the undo and redo stacks. 
     * They take any type Action object.
     */
    public ActionManager(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }
    
    /**
     * Perform the given action. 
     * 
     * @param action Action type object to be performed in program. 
     */
    public void execute(Action action) throws IllegalArgumentException{
        if (action == null) {
            throw new IllegalArgumentException();
        }
        action.execute();
    }
    
    /**
     * Executes all given actions in the provided ArrayList of Actions.
     * 
     * @param actionList 
     */
    public void execute(ArrayList<Action> actionList) throws IllegalArgumentException{
        if (actionList == null) {
            throw new IllegalArgumentException();
        }
        for (Action a : actionList) {
            this.execute(a);
        }
    }
    
    
    public void putInUndoStack(Action action) {
        if (action == null) {
            System.out.println("given actionList in ActionManager.putInUndoStack(ArrayList<Action>) is null");
            Thread.dumpStack();
        }
        
        ArrayList<Action> actionArray = new ArrayList();
        actionArray.add(action);
        
        redoStack.clear();
        undoStack.push(actionArray);
    }
    
    /**
     * Puts given ArrayList of actions into the stack. If actionArray is null,
     * then does nothing. Clears redoStack to avoid redoing actions when it 
     * doesn't make sense to.
     * @param actionArray 
     */
    public void putInUndoStack(ArrayList<Action> actionArray) {
        if (actionArray == null) {
            System.out.println("given actionList in ActionManager.putInUndoStack(ArrayList<Action>) is null");
            Thread.dumpStack();
        }
        
        if (actionArray.isEmpty()) {
            return;
        }
        
        redoStack.clear();
        undoStack.push(actionArray);
    }
    
    /**
     * Pop top item on undoStack. Undo the action, then push it onto the
     * redoStack.
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
     * Pop top item from redoStack. Redo the action's effects, and push it back
     * onto the undoStack.
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
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
    
    public ActionManager(){
        
    }
    
    public void undo(){
        Action undoAction;
        undoAction = undoStack.pop();
        undoAction.undo();
        undoStack.push(undoAction);
    }
    
    public void redo(){
        Action redoAction;
        redoAction = redoStack.pop();
        redoAction.redo();
        redoStack.push(redoAction);
    }
    
}
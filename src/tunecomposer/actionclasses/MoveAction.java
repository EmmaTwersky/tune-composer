package tunecomposer.actionclasses;

import java.util.ArrayList;
import tunecomposer.SoundObject;

/**
 * An action which can be called repeatedly to move an Array of SoundObjects.
 */
public class MoveAction extends Action {
    
    /**
     * Mouse position when first pressed. 
     * Used to undo.
     */
    private final double startX;
    private final double startY;
    
    /**
     * Final mouse coordinates of the movement.
     * Set after the mouse is released.
     */
    private double lastX;
    private double lastY;
        
    /**
     * Boolean to hold if movement should be locked.
     * The boolean is true if the move threatens to place affectedObjs
     * past the pane's borders.
     */
    private boolean lockMove;

    /**
     * Constructs an action event to move SoundObjects on the screen.
     * Sets affectedObjs and soundObjectPane.
     * 
     * @param selectedObjs selList all SoundObjects to be affected
     * @param x initial x location of mouse click  
     * @param y initial y location of mouse click
     */
    public MoveAction(ArrayList<SoundObject> selectedObjs, double x, double y) {
        affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
        startX = x;
        startY = y;
    }
    
    /**
     * Moves and snaps into place all affectedObjs.
     */
    @Override
    public void execute() {
        affectedObjs.forEach((sObj)->{
            sObj.move(lastX-startX, lastY-startY);
            sObj.snapInPlace();
        });
    }
    
    /**
     * Un-moves all affectedObjs. 
    */
    @Override
    public void undo() {
        affectedObjs.forEach((sObj)->{
            sObj.move(startX-lastX, startY-lastY+sObj.HEIGHT);
            sObj.snapInPlace();
        });
    }
    
    /**
     * Re-moves all affectedObjs.
    */
    @Override
    public void redo() {
        execute();
    }
    
    /**
     * Moves all affectedObjs. 
     * The increment of the move is calculated as the difference between mouse
     * positions, then implemented to each selectObj in the array. Call
     * this method to move affectedObjs without creating a new action; dragging.
     * 
     * @param mouseX x location of mouse click
     * @param mouseY y location of mouse click
     */
    public void move(double mouseX, double mouseY) {
        lockMove = false;
        
        affectedObjs.forEach((sObj) -> {
            if (sObj.isOnEdge(mouseX,mouseY)){
                lockMove = true;
            }
        });
        
        if (!lockMove){
            affectedObjs.forEach((sObj) -> {
                sObj.move(mouseX, mouseY);
            });
        }
    }
    
    /**
     * Returns if the move is failed.
     * Reason for fail could be trying to move notes outside of the pane's borders.
     * 
     * @return Boolean if move is locked
     */
    public boolean isMoveFailed(){
        return lockMove;
    }
    
    /**
     * Sets lastX and lastY to the latest drag location.
     * This allows undo to calculate the total move distance.
     * 
     * @param x final x coordinate of mouse event
     * @param y final y coordinate of mouse event
    */
    public void setLastCoords(double x, double y){
        lastX = x;
        lastY = y;
    }    
}
package tunecomposer;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.LengthChangeAction;
import tunecomposer.actionclasses.MoveAction;
import tunecomposer.actionclasses.SelectAction;
import tunecomposer.actionclasses.UnselectAction;

public abstract class SoundObject {
    
    /**
    * A rectangle to display on the screen to represent the object visually.
    */
    public Rectangle visualRectangle;
    
    
    /**
     * Reference the top-most gesture that this SoundObject belongs to.
     * If null, then this is the top, otherwise this is nested in a gesture 
     * belonging to the referenced at some depth.
     */
    protected Gesture topGesture = null;
    
    /**
     * Pane that holds all the soundObject rectangles.
     */
    public Pane soundObjectPane;
    
    /**
     * Height of the NoteBar rectangles.
     */
    public static final int HEIGHT = 10;
    
    /**
    * Sets given values for SoundObject dragging when clicked.
    */
    // 10 shows better selection, though 5 is the indicated value.
    public final int clickToEditLength = 10; 
    
    /**
    * Boolean value representing the SoundObjects selection state.
    * If true, then object is selected, if false, then unselected.
    */
    public boolean selected = true;
    
    /**
     * Most recently polled mouse coordinates.
     * Useful during mouse handlers to keep track of incremental changes in mouse
     * location.
     */
    public double latestX;
    public double latestY;

    /**
     * Boolean representing whether the object's length is currently being changed.
     * If true, then the end of the object was grabbed, if false, then it is not
     * grabbed in current mouse press.
     */
    public boolean draggingLength;
    
    /**
     * List of actions to be pushed onto undo stack. Used for compound actions.
     */
    ArrayList<Action> actionList;
    MoveAction sObjMove;
    
    /**
     * Instances of potential actions that could be execute/pushed onto undo stack.
     */
    LengthChangeAction sObjStretch;
    SelectAction selectAction;
    UnselectAction unselectAction;
    
    /**
    * Creates abstract set of SoundObject selection methods.
    */
    public abstract void select();
    
    /**
    * Sets objects state to unselect and reflects unselection by changing 
    * rectangle styling.
     */
    public abstract void unselect();
    
    /**
    * Toggles objects state to opposite of what it currently is and reflects 
    * change by changing rectangle styling.
     */
    public abstract void toggleSelection();
    
    /**
     * Returns true if this object is selected. False if unselected.
     * @return true if selected, false if not
     */
    public abstract boolean isSelected();
    
    /**
    *  Move the SoundObject by given increments.
     * @param xInc increment to shift visualRectangle's x coordinate
     * @param yInc increment to shift visualRectangle's y coordinate
    */
    public abstract void move(double xInc, double yInc);
    
    /**
    *  Change the SoundObject's length by given increment.
    * @param length amount to increment Sound Object's length.
    */    
    public abstract void changeLength(int length);
    
    /**
    * Snap object to y-coordinate of nearest note.
    */
    public abstract void snapInPlace();
    
    /**
    * Set the mouse event handlers of the this object.
    */
    public abstract void setHandlers();
    
        /**
     * Creates a Move or Stretch Action depending on the placement of the
     * initial click.
     * Helper method to the handleNotePressed event handler.
     */
    abstract void prepareMoveOrStretchAction();
    
    /**
    * Adds the SoundObject's visual representation to the pane.
    * 
    * @param soundObjectPane pane visualRectangle is on
    */
    public abstract void addToPane(Pane soundObjectPane);
    
    /**
    * Removes SoundObject's visual representation from the pane.
    * 
    * @param soundObjectPane pane visualRectangle is on
    */
    public abstract void removeFromPane(Pane soundObjectPane);

    /**
     * Checks if moving the soundObject will push it past the pane's borders.
     * 
     * @param x the "proposed" x move increment.
     * @param y the "proposed" y move increment.
     * @return onEdge is true if the move is illegal and false if its legal.
     */
    public abstract boolean isOnEdge(double x, double y);
    
    
    public abstract void setHandlers(EventHandler press, EventHandler drag, EventHandler release);
    
    /**
    * Adds the SoundObject's MidiEvent to the player.
    * 
     * @param player given instance of TunePlayer
    */
    public abstract void addToMidiPlayer(MidiPlayer player);
    
    
    /**
     * Method for recursively grabbing all children of a SoundObject. 
     * Returns an ArrayList of all children below the object not including self.
     * @return list of all, as in every level below, the current SoundObject.
     */
    public abstract ArrayList<SoundObject> getAllChildren();
    

    /**
     * Sets this.topGesture to the given Gesture object. 
     * Give null if this gesture is no longer a child of another parent.
     * Update topGesture when the old topGesture is grouped.
     * @param topGest The top-most Gesture that this gesture is a child of.
     */
    public void setTopGesture(Gesture topGest) {
        topGesture = topGest;
    }
    
    /**
     * Gives the topGesture to this object. 
     * Reference the most senior Gesture object that this is within.
     * @return the top gesture that this object is in.
     */
    public Gesture getTopGesture() {
        return topGesture;
    }

    
    /**
     * Selects the entire gesture that the method is called from.
     * If SoundObject not in a gesture, then does nothing.
     */
    public void selectTopGesture() {
        Gesture gest = this.getTopGesture();
        if (gest != null) {
            gest.select();
        }        
    }
    
    /**
     * Unselects the entire gesture that the method is called from.
     * If SoundObject not in a gesture, then does nothing.
     */
    public void unselectTopGesture() {
        Gesture gest = this.getTopGesture();
        if (gest != null) {
            gest.unselect();
        }        
    }    
       
    /**
     * Returns ArrayList of all SoundObjects that share a gesture with caller.
     * If no related SoundObjects, then returns empty ArrayList.
     * @return ArrayList of all related SoundObjects
     */
    public ArrayList<SoundObject> getAllRelated() {
        ArrayList<SoundObject> relatedList = new ArrayList();
        Gesture topGest = (Gesture) this.getTopGesture();
        
        if (topGest == null) {
            return relatedList;
        }
        
        return topGest.getAllChildren();
    }
    
    /**
     * Helper method for prepareSelectionAction.
     * Retrieves all other sound objects that are selected
     * (excluding the current note).
     * 
     * @return allSelected is an ArrayList of those selected sound objects.
     */
    private ArrayList<SoundObject> getOtherSelectedItems(){
        ArrayList<SoundObject> allSelected = new ArrayList();
            for (Node n : soundObjectPane.getChildren()) {
                Rectangle r = (Rectangle) n;
                SoundObject sObj = (SoundObject) r.getUserData();
                if (sObj.isSelected()) {
                    allSelected.add(sObj);
                }
            }
        return allSelected;
    }
    
    
    /**
     * Creates a Selection and Unselection Action and adds it the actionList
     * which will later be pushed onto the undo stack.
     * Helper method to the handleNotePressed and handleGesturePressed event handlers.
     *
     * @param isCtrlDown boolean -> reads "Is Control Down?"
     */
    void prepareSelectionAction(boolean isCtrlDown){
        ArrayList<SoundObject> thisSoundObject = new ArrayList();
        thisSoundObject.add((SoundObject) visualRectangle.getUserData());
        if (!selected) {
                if(!isCtrlDown){
                    ArrayList<SoundObject> allSelected;
                    allSelected = getOtherSelectedItems();
                    unselectAction = new UnselectAction(allSelected);
                    unselectAction.execute();
                    actionList.add(unselectAction);
                }
                selectAction = new SelectAction(thisSoundObject);
                selectAction.execute();
                actionList.add(selectAction);
            }
            else if (isCtrlDown){
                unselectAction = new UnselectAction(thisSoundObject);
                unselectAction.execute();
                actionList.add(unselectAction);
            }
    }
}

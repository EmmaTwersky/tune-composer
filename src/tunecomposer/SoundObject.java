package tunecomposer;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

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
    * Sets objects state to select and reflects selection by changing rectangle
    * styling.
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
    * Creates abstract method to move the SoundObject.
     * @param x visualRectangle's new x coordinate
     * @param y visualRectangle's new y coordinate
    */
    public abstract void move(double x, double y);
    
    /**
    * Creates abstract method to change the SoundObject's length.
    * @param length visualRectangle's new length
    */    
    public abstract void changeLength(int length);
    
    /**
    * Creates abstract set of SoundObject altering methods.
    */
    public abstract void snapInPlace();
    
    /**
    * Gives the object's visualRectangle mouse event handlers.
    */
    public abstract void setHandlers();
    
    /**
    * Adds the visualRectangle on the pane.
    * 
    * @param soundObjectPane pane visualRectangle is on
    */
    public abstract void addToPane(Pane soundObjectPane);
    
    /**
    * Removes the visualRectangle from the pane, also handles deletion.
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
    
    /**
    * Adds the SoundObject's MidiEvent to the player.
    * 
     * @param player given instance of TunePlayer
    */
    public abstract void addToMidiPlayer(MidiPlayer player);
    
    /**
     * Sets the mouse handlers of the called object to the given parameters.
     * For SoundObjects that contain items, should also set all items handlers 
     * to given.
     * @param press 
     *          Handler of the object that mouse press events will consume 
     * @param drag
     *          Handler of the object that mouse drag events will consume 
     * @param release 
     *          Handler of the object that mouse release events will consume 
     */
    public abstract void setHandlers(EventHandler press, EventHandler drag, EventHandler release);
    
    
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
}

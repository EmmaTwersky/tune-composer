package tunecomposer;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public abstract class SoundObject {
    
    /**
    * A rectangle to display on the screen.
    */
    public Rectangle visualRectangle;
    
    /**
    * An ArrayList of the SoundObjects contained within the object.
    */
    public ArrayList<SoundObject> containedSoundObjects;
    
    /**
    * Sets given values for SoundObject dragging when clicked.
    */
    public final int clickToEditLength = 10; // 10 shows better selection, though 5 is the indicated value.
    public final int minLength = 5;
    
    /**
    * Creates boolean value of if the SoundObject is currently selected.
    */
    public boolean selected = true;
    
    /**
     * Creates instances for the initial pressed values of the mouse for events.
     */
    public int initialX;
    public int initialY;

    /**
     * Creates boolean to ensure dragging to change duration is a separate instance.
     */
    public boolean draggingLength;
    
    /**
    * Creates abstract set of SoundObject selection methods.
    */
    public abstract void select();
    public abstract void unselect();
    public abstract void toggleSelection();
    public abstract boolean isSelected();
    
    /**
    * Creates abstract method to move the SoundObject.
     * @param x visualRectangle's new x coordinate
     * @param y visualRectangle's new y coordinate
    */
    public abstract void move(int x, int y);
    
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
    * Adds the SoundObject's MidiEvent to the player.
    * 
     * @param player given instance of TunePlayer
    */
    public abstract void addToMidiPlayer(MidiPlayer player);
}

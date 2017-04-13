package tunecomposer;

import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public abstract class SoundObject {
    
     /**
     * A rectangle to display on the screen.
     */
    public Rectangle visualRectangle;
    public ArrayList<SoundObject> containedSoundObjects;
    
    public final int clickToEditLength = 10; // 10 worked better, but 5 was indicated
    public final int minNoteLength = 5;
    
    /**
    * Creates boolean value of if the note is currently selected.
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
    
    public abstract void select();
    public abstract void unselect();
    public abstract void toggleSelection();
    public abstract boolean isSelected();
    
    public abstract void move(int x, int y);
    public abstract void changeLength(int lengthInc);
    public abstract void snapInPlace();
    public abstract void delete();
    
    public abstract void addToMidiPlayer(MidiPlayer player);
}

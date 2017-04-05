// Consider seperating model and view? (note from Janet but unclear what this means)
// Shorten handlers with stategy pattern or abstraction of methods?
// Fix bug in which some note bars are no longer able to display that they are selected. 
//      However, they are edited with the other note bars.
package tunecomposer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.ShortMessage;

/**
 * This class creates and edits NoteBar objects to display notes in the tune 
 * and be played in MidiPLayer.
 * 
 * @author Emma Twersky
 */
public class NoteBar extends SoundObject{
    /**
     * Create variables for the note name, instrument number, channel number,
     * pitch, starting value, and length. 
     */
    public final String name;
    public final int instrument;
    public final int channel;
    public int pitch;
    public int startTick;
    public int length;
    public static final int HEIGHT = 10;
    
    /**
     * Gesture this note is grouped under. Does not reference most outward 
     * gesture. Null if this is not in any gesture.
     */
    private Gesture parentGesture;
    
    /**
    * Creates boolean value of if the note is currently selected.
    */
    public boolean selected = true;
    
    /**
    gestureBox* Creates pane the note is on and the visual rectangle the note is displayed as.
    */
    public Pane pane;

    /**
    * Creates fixed height and set ranges for pitch. 
    */
    private final int pitchRange = 128;
    private final int noteHeight = 10;
    
    /**
    * Sets default length, minimum length and default click range to edit length. 
    */
    private final int defaultLength = 100;
    private final int clickToEditLength = 10; // 10 worked better, but 5 was indicated
    private final int minNoteLength = 5;
    
    /**
     * Load InstrumentInfo HashMap to look up instrument key values.
     */
    private final InstrumentInfo instrumentInfo = new InstrumentInfo();
    
    /**
     * Creates instances for the initial pressed values of the mouse for events.
     */
    private int initialX;
    private int initialY;
    
    /**
     * Creates boolean to ensure dragging to change length is a separate instance.
     */
    private boolean draggingLength;
        
    /**
     * Initialize the note bar object and variables, then constructs the display
     * and adds the note to the pane.
     * 
     * @param instrumName the string name of the instrument
     * @param x the top left corner x value of the note clicked
     * @param y the top left corner y value of the note clicked
     * @param compositionPane the pane the note is created on
     */
    NoteBar(double x, double y, Pane compositionPane){
        name = InstrumentToolBarController.selectedInstrument;
        instrument = instrumentInfo.getInstrumentValue(name);
        channel = instrumentInfo.getInstrumentChannel(name);
        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        length = defaultLength;
        
        parentGesture = null;
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        rectangleVisual = new Rectangle(xLocation, yLocation, length, noteHeight);
        rectangleVisual.getStyleClass().add(name);

        rectangleVisual.setOnMousePressed(handleNotePressed);
        rectangleVisual.setOnMouseDragged(handleNoteDragged);
        rectangleVisual.setOnMouseReleased(handleNoteReleased);
                
        selected = true;
        rectangleVisual.getStyleClass().add("selectedNote");
        pane = compositionPane;
        pane.getChildren().add(rectangleVisual);
    }
    
    /**
     * Returns if note is selected.
     * 
     * @return boolean, if note is selected
     */
    @Override
    public boolean isSelected(){
        return selected;
    }

    /**
     * Moves note freely on pane.
     * 
     * @param x the increment to change current x value by
     * @param y the increment to change current y value by
     */
    @Override
    public void move(int x, int y){ 
        int translateX = x + (int) rectangleVisual.getX();
        int translateY = y + (int) rectangleVisual.getY();
        rectangleVisual.setX(translateX);
        rectangleVisual.setY(translateY);
    }
    
    /**
     * Fixes note to sit in-between staff lines.
     * 
     * @param x the top left corner x value of the noteDisplay
     * @param y the top left corner y value of the noteDisplay
     */
    @Override
    public void snapInPlace(double x, double y){        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        rectangleVisual.setX(xLocation);
        rectangleVisual.setY(yLocation);
        rectangleVisual.getStyleClass().add(name);
    }
    
    public void snapInPlace() {
        int x = (int) rectangleVisual.getX();
        int y = (int) rectangleVisual.getY();
        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        rectangleVisual.setX(xLocation);
        rectangleVisual.setY(yLocation);
        rectangleVisual.getStyleClass().add(name);
    }
    
    
    /**
     * Changes note length.
     * 
     * @param lengthChange the amount to increment the length
     */
    @Override
    public void changeLength(int lengthChange){ 
        int newLength = length + lengthChange;
        if (newLength > minNoteLength) {
            length = newLength;
            rectangleVisual.setWidth(length);
        }
    }
    
    /**
     * Deletes note from pane.
     */
    @Override
    public void delete(){
        pane.getChildren().remove(rectangleVisual);
    }
    
    /**
     * Selects note and displays selection box around note.
     */
    @Override
    public void select(){
        selected = true;
        rectangleVisual.getStyleClass().removeAll("unselectedNote");
        rectangleVisual.getStyleClass().add("selectedNote");
        CompositionPaneController.updateSelectedSoundObjectArray(); 
    }
    
    /**
     * Un-selects note and removes selection box around note.
     */
    @Override
    public void unselect(){
        selected = false;
        rectangleVisual.getStyleClass().removeAll("selectedNote");
        rectangleVisual.getStyleClass().add("unselectedNote");
        CompositionPaneController.updateSelectedSoundObjectArray(); 
    }
    
    /**
     * Switches value of selected.
     */
    @Override
    public void toggleSelection(){
        if (selected) {unselect();}
        else {select();}
    }
    
    /**
     * Returns parent gesture that this note was originally grouped with;
     * ie. will not return a gesture's parent gesture. Null if it has no parent.
     * @return 
     */
    @Override
    public Gesture getParentGesture() {
        return parentGesture;
    }
    
    /**
     * Set parent gesture for this note. Set to null if ungrouping. Do not 
     * overwrite parentGesture if not null.
     * @param parent
     */
    @Override
    public void setParentGesture(Gesture parent) {
        parentGesture = parent;
    }
    
    
    /**
     * returns x coordinate of right side of rectangle.
     * @return 
     */
    @Override
    public int findRightMostCord() {
        return (int) rectangleVisual.getX() + length;
    }
    
    public int findLeftMostCord() {
        return (int) rectangleVisual.getX();
    }
    
    public int findTopMostCord() {
        return (int) rectangleVisual.getY();
    }

    public int findBottomMostCord() {
        return (int) rectangleVisual.getY() + HEIGHT;
    }

    
    /**
     * Adds note to MidiPlayer.
     * @param vol
     * @param player 
     */
    @Override
    public void addToMidi(int vol, MidiPlayer player){
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + channel, 
                            instrument, 0, 0, channel);
        player.addNote(pitch, vol, startTick, 
                            length, channel, 0);
    }
    
  /**
     * Handles note pressed event. 
     * Sets initial pressed values of the mouse and consumes the event.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleNotePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            CompositionPaneController.tunePlayerObj.stop();
            initialX = (int) event.getX();
            initialY = (int) event.getY();
            
            int editLengthMax = (int) rectangleVisual.getX() + length;
            int editLengthMin = editLengthMax - clickToEditLength;
            if (editLengthMin <= initialX && initialX <= editLengthMax) {
                draggingLength = true;
            }
                
            event.consume();
        }
    };
    
    /**
     * Handles note dragged event.
     * Selects and drags to move the note or drags to change length, 
     * based on note click location conventions. Translates note and consumes 
     * event. Also updates note lists.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleNoteDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            
            SoundObject topSoundObject = getTopParentGesture();            
            
            if (!selected) {
                CompositionPaneController.resetSelectedNotesArray(); 
                topSoundObject.select();
            }
            
            if (draggingLength) {
                CompositionPaneController.selected_soundobject_array.forEach((soundItem) -> {
                    soundItem.changeLength(x - initialX);
                });
            }
            else {
                int translateX = (x - initialX);
                int translateY = (y - initialY);
                CompositionPaneController.selected_soundobject_array.forEach((soundItem) -> {
                    soundItem.move(translateX, translateY);
                });
            }
            
            initialX = x;
            initialY = y;
            event.consume();
        }
    };
    
    /**
     * Handles mouse released.
     * Snaps note into place on lines, handles click note selections
     * based on the control button and consumes event.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleNoteReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            draggingLength = false;
            
            SoundObject topSoundObject = getTopParentGesture();
            
            if (event.isStillSincePress()) {
                if (event.isControlDown()) {
                    topSoundObject.toggleSelection();
                }
                else {
                    CompositionPaneController.resetSelectedNotesArray();
                    topSoundObject.select();
                }
            }
            
            for (SoundObject soundItem : 
                        CompositionPaneController.selected_soundobject_array) {
                
                soundItem.snapInPlace();
                
            }
            
//            CompositionPaneController.selected_soundobject_array.forEach((soundItem) -> {
//                soundItem.snapInPlace(soundItem.rectangleVisual.getX(), 
//                                      soundItem.rectangleVisual.getY());
//            });     
            
            event.consume();
        }
    };
    
    
    /**
     * Find and return the most encapsulating gesture to this note.
     * @return 
     */
    public SoundObject getTopParentGesture() {
        SoundObject tmp = this;
        while (tmp.getParentGesture() != null) {
            tmp = tmp.getParentGesture();
        }
        return tmp;
    }
}
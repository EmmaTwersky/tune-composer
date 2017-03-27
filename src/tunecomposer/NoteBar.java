// Consider seperating model and view? (note from Janet but unclear what this means)
// Shorten handlers with stategy pattern or abstraction of methods?
// Fix bug in which some note bars are no longer able to display that they are selected. 
//      However, they are edited with the other note bars.
package tunecomposer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This class creates and edits NoteBar objects to display notes in the tune 
 * and be played in MidiPLayer.
 * 
 * @author Emma Twersky
 */
public class NoteBar {
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
    
    /**
    * Creates boolean value of if the note is currently selected.
    */
    public boolean selected = true;
    
    /**
    * Creates pane the note is on and the visual rectangle the note is displayed as.
    */
    public Pane pane;
    public Rectangle noteDisplay;

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
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        noteDisplay = new Rectangle(xLocation, yLocation, length, noteHeight);
        noteDisplay.getStyleClass().add(name);

        noteDisplay.setOnMousePressed(handleNotePressed);
        noteDisplay.setOnMouseDragged(handleNoteDragged);
        noteDisplay.setOnMouseReleased(handleNoteReleased);
                
        selected = true;
        noteDisplay.getStyleClass().add("selectedNote");
        pane = compositionPane;
        pane.getChildren().add(noteDisplay);
    }
    
    /**
     * Returns if note is selected.
     * 
     * @return boolean, if note is selected
     */
    public boolean isSelected(){
        return selected;
    }

    /**
     * Moves note freely on pane.
     * 
     * @param x the new top left corner x value of the noteDisplay
     * @param y the new top left corner y value of the noteDisplay
     */
    public void move(int x, int y){ 
        int translateX = x + (int) noteDisplay.getX();
        int translateY = y + (int) noteDisplay.getY();
        noteDisplay.setX(translateX);
        noteDisplay.setY(translateY);
    }
    
    /**
     * Fixes note to sit in-between staff lines.
     * 
     * @param x the top left corner x value of the noteDisplay
     * @param y the top left corner y value of the noteDisplay
     */
    public void snapInPlace(double x, double y){        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        noteDisplay.setX(xLocation);
        noteDisplay.setY(yLocation);
        noteDisplay.getStyleClass().add(name);
    }
    
    /**
     * Changes note length.
     * 
     * @param lengthChange the amount to increment the length
     */
    public void changeLength(int lengthChange){ 
        int newLength = length + lengthChange;
        if (newLength > minNoteLength) {
            length = newLength;
            noteDisplay.setWidth(length);
        }
    }
    
    /**
     * Deletes note from pane.
     */
    public void delete(){
        pane.getChildren().remove(noteDisplay);
    }
    
    /**
     * Selects note and displays selection box around note.
     */
    public void select(){
        selected = true;
        noteDisplay.getStyleClass().remove("unselectedNote");
        noteDisplay.getStyleClass().add("selectedNote");
        CompositionPaneController.updateSelectedNotesArray(); 
    }
    
    /**
     * Un-selects note and removes selection box around note.
     */
    public void unselect(){
        selected = false;
        noteDisplay.getStyleClass().remove("selectedNote");
        noteDisplay.getStyleClass().add("unselectedNote");
        CompositionPaneController.updateSelectedNotesArray(); 
    }
    
    /**
     * Switches value of selected.
     */
    public void toggleSelection(){
        if (selected) {unselect();}
        else {select();}
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
            
            int editLengthMax = (int) noteDisplay.getX() + length;
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
            
            if (!selected) {
                CompositionPaneController.resetSelectedNotesArray(); 
                select();
            }
            
            if (draggingLength) {
                CompositionPaneController.SELECTED_NOTES_ARRAY.forEach((note) -> {
                    note.changeLength(x - initialX);
                });
            }
            else {
                int translateX = (x - initialX);
                int translateY = (y - initialY);
                CompositionPaneController.SELECTED_NOTES_ARRAY.forEach((note) -> {
                    note.move(translateX, translateY);
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

            if (event.isStillSincePress()) {
                if (event.isControlDown()) {
                    toggleSelection();
                }
                else {
                    CompositionPaneController.resetSelectedNotesArray(); 
                    select();
                }
            }
            
            CompositionPaneController.SELECTED_NOTES_ARRAY.forEach((note) -> {
                note.snapInPlace(note.noteDisplay.getX(), note.noteDisplay.getY());
            });     
            
            event.consume();
        }
    };
}
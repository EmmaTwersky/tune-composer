package tunecomposer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This class creates and edits NoteBar objects to display notes in the tune 
 * and be played in MidiPLayer.
 * 
 * @author Emma
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
     * Load InstrumentSelection HashMap to look up instrument key values.
     */
    private final InstrumentSelection instrumentInfo = new InstrumentSelection();
    
    /**
     * Initialize the note bar object and variables, then constructs the display
     * and adds the note to the pane.
     * 
     * @param instrumName the string name of the instrument
     * @param x the top left corner x value of the note clicked
     * @param y the top left corner y value of the note clicked
     * @param compositionPane the pane the note is created on
     */
    NoteBar(String instrumName, double x, double y, Pane compositionPane){
        name = instrumName;
        instrument = instrumentInfo.getInstrumentValue(instrumName);
        channel = instrumentInfo.getInstrumentChannel(instrumName);
        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        length = defaultLength;
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        pane = compositionPane;
        noteDisplay = new Rectangle(xLocation, yLocation, length, noteHeight);
        noteDisplay.getStyleClass().add(name);

        noteDisplay.setOnMousePressed(handleNotePressed);
        noteDisplay.setOnMouseDragged(handleNoteDragged);
        noteDisplay.setOnMouseReleased(handleNoteReleased);
                
        selected = true;
        noteDisplay.getStyleClass().add("selectedNote");
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
    public void moveNote(int x, int y){ 
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
    public void snapNoteInPlace(double x, double y){        
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
     * @param noteLength the new note length
     */
    public void changeNoteLength(int noteLength){ 
        if (noteLength > minNoteLength) {
            length = noteLength;
            noteDisplay.setWidth(length);
        }
    }
    
    /**
     * Deletes note from pane.
     */
    public void deleteNote(){
        pane.getChildren().remove(noteDisplay);
    }
    
    /**
     * Selects note and displays selection box around note.
     */
    public void selectNote(){
        selected = true;
        noteDisplay.getStyleClass().remove("unselectedNote");
        noteDisplay.getStyleClass().add("selectedNote");
    }
    
    /**
     * Un-selects note and removes selection box around note.
     */
    public void unselectNote(){
        selected = false;
        noteDisplay.getStyleClass().remove("selectedNote");
        noteDisplay.getStyleClass().add("unselectedNote");
    }
    
    /**
     * Switches value of selected.
     */
    public void toggleNoteSelection(){
        if (selected) {unselectNote();}
        else {selectNote();}
    }
    
    /**
     * Initialize pane controller for static use in event handling.
     */
    FXMLController FXMLController;
    
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
     * Handles note pressed event. 
     * Sets initial pressed values of the mouse, sets dragLength to false and
     * follows given conventions on control button selection. Relies on 
     * FXMLController to update selection array.
     * 
     * @param event the mouse click event
     * @see FXMLController
     */
    EventHandler<MouseEvent> handleNotePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {            
            initialX = (int) event.getX();
            initialY = (int) event.getY();

            draggingLength = false;
                        
            FXMLController.updateSelectedNotesArray();
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
            int editLengthMax = (int) noteDisplay.getX() + length;
            int editLengthMin = editLengthMax - clickToEditLength;
            
            if (!selected) {
                FXMLController.resetSelectedNotesArray(); 
                selectNote();
            }
            
            if (editLengthMin <= x && x <= editLengthMax) {
                draggingLength = true;
                for (NoteBar note: FXMLController.SELECTED_NOTES_ARRAY) {
                    note.changeNoteLength((x - initialX) + length);
                }
            }
            if (!draggingLength) {
                int translateX = (x - initialX);
                int translateY = (y - initialY);
                for (NoteBar note: FXMLController.SELECTED_NOTES_ARRAY) {
                    note.moveNote(translateX, translateY);
                }
            }
            
            initialX = x;
            initialY = y;
            FXMLController.updateSelectedNotesArray();
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
            if (event.isStillSincePress()) {
                if (event.isControlDown()) {
                    toggleNoteSelection();
                }
                else {
                    FXMLController.resetSelectedNotesArray(); 
                    selectNote();
                }
            }
            
            for (NoteBar note: FXMLController.SELECTED_NOTES_ARRAY) {
                note.snapNoteInPlace(note.noteDisplay.getX(), note.noteDisplay.getY());
            }     
            
            event.consume();
        }
    };
}

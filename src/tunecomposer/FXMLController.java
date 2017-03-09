package tunecomposer;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.ShortMessage;

/**
 * This class controls the <TuneComposer.fxml> which creates a MidiPlayer, 
 * then handles events on the pane and in the menu bar to play the MidiPlayer.
 * 
 * @author Emma
 */
public class FXMLController implements Initializable{
    
    /**
     * Create array of NoteBar objects and selected NoteBar objects.      
     */
    protected static final ArrayList<NoteBar> MUSIC_NOTES_ARRAY = new ArrayList(); 
    protected static final ArrayList<NoteBar> SELECTED_NOTES_ARRAY = new ArrayList(); 
    
    /**
     * Set volume to maximum.      
     */
    private static final int VOLUME = 127;
    
    /**
     * Initialize default note to "Piano".      
     */
    protected String selectedInstrument = "Piano";

    /** 
     * Set note height to 10 pixels and note length to 100 pixels. 
     */
    private final int noteHeight = 10; 
    private final int initialNoteLength = 100;
    
    /**
     * Set pitch range from 1 to 127 and bar range, the number of measures on 
     * the screen, to 20.
     */
    private final int pitchRange = 128;
    private final int barRange = 20;
    
    /**
     * One midi player is used throughout, so that it can be stopped.
     * Set resolution to 100 and beats per minute to 60.
     */
    private final int resolution = 100;
    private final int beatsPerMinute = 60;
    private final MidiPlayer player = new MidiPlayer(resolution, beatsPerMinute);
    
    /**
     * Initialize a RedBar to track the progression of time.
     */
    private RedBar redBarObj; // = new RedBar(compositionPane);
    
    /**
     * Initialize a Rectangle window to select notes on dragged.
     */
    private Rectangle window;
    
    /**
     * Create the pane which the note events take place on.      
     */
    @FXML
    public Pane compositionPane; 
    
    /**
     * Initialize FXML, draws initial setup of composition pane and 
     * initialized the RedBar.
     * 
     * @param location
     * @param resources
     * @see RedBar
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        for (int i = 0; i < pitchRange; i++) {
            Line staffLine = new Line(0, i*noteHeight, 
                    barRange*initialNoteLength, i*noteHeight);
            staffLine.setId("staffLine");
            compositionPane.getChildren().add(staffLine);
        }
        for (int i = 0; i < barRange; i++) {
            Line measureLine = new Line(i*initialNoteLength, 0, 
                    i*initialNoteLength, pitchRange*noteHeight);
            measureLine.setId("measureLine");
            compositionPane.getChildren().add(measureLine);
        }
        redBarObj = new RedBar(compositionPane);
    }
    
    /**
     * Fills the selectedNotesArray with the currently selected notes.
     */
    public static void updateSelectedNotesArray(){
        SELECTED_NOTES_ARRAY.clear();
        for (NoteBar note: MUSIC_NOTES_ARRAY) {            
            if (note.isSelected()) {
                note.selectNote();
                SELECTED_NOTES_ARRAY.add(note);
            }
        }
    }
    
    /**
     * Empties the selectedNotesArray and un-selects all notes.
     */
    public static void resetSelectedNotesArray(){
        for (NoteBar note: SELECTED_NOTES_ARRAY) {            
            note.unselectNote();
        }
        SELECTED_NOTES_ARRAY.clear();
    }
    
    /**
     * Handles the play button from the Actions menu.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePlayScaleButtonAction(ActionEvent event) {
        play();
    }  
    
    /**
     * Plays the sequence and begins the RedBar animation.
     */
    protected void play() {
        playSequence();
        redBarObj.playAnimation(MUSIC_NOTES_ARRAY);
    }
    
    /**
     * Plays the MidiPLayer.
     * Each time playSequence is called the sequence clears, adds all notes 
     * to the player, and starts from the beginning.
     */
    protected void playSequence() {
        player.stop();
        player.clear();
        addNotesArrayToMidiPlayer();
        player.play();
    }
    
    /**
     * Adds all NoteBar objects in musicNotesArray to MidiPlayer.
     */
    private void addNotesArrayToMidiPlayer() {
        for (NoteBar note: MUSIC_NOTES_ARRAY) {            
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + note.channel, 
                    note.instrument, 0, 0, note.channel);
            player.addNote(note.pitch, VOLUME, note.startTick, 
                    note.length, note.channel, 0);
        }
    }
    
    /**
     * Constructs ToggleGroup from <TuneComposer.fxml>.
     */
    @FXML
    ToggleGroup instrumentSelection;
    
    /**
     * Handles changes to the instrument selection menu.
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleInstrumentMenuAction(ActionEvent event) {
        Node toggle = (Node) instrumentSelection.getSelectedToggle();
        selectedInstrument = toggle.getId();
    }
    
    /**
     * Handles the Stop menu item.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        stop();
    }    
    
    /**
     * Stops the current MidiPlayer, clears MidiPlayer and stops RedBar.
     */
    public void stop() {
        player.stop();
        player.clear();
        redBarObj.stopAnimation();
    }  
    
    /**
     * Handles the Select All menu item and selects all notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        for (NoteBar note: MUSIC_NOTES_ARRAY) {
            note.selectNote();
        }
        updateSelectedNotesArray();
    }

    /**
     * Handles the Delete menu item and deletes all selected notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        for (NoteBar note: SELECTED_NOTES_ARRAY) {
            note.deleteNote();
            MUSIC_NOTES_ARRAY.remove(note);
        }
        updateSelectedNotesArray();
    }

    /**
     * Handles the Exit menu item and exits the scene.
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }
    
    /**
     * Handles mouse press on the compositionPane, stops current MidiPlayer,
     * gets initial value of mouse, and initializes selection window.
     * Updates selected notes for NoteBar handleOnPressed updates.
     * 
     * @param event the mouse click event
     * @see <NoteBar.java>
     */
    @FXML
    protected void handlePanePressed(MouseEvent event) {
        stop();
        
        int x = (int) event.getX();
        int y = (int) event.getY();
        
        window = new Rectangle(x, y, 0, 0);
        window.setId("selectionWindow");
        window.setVisible(false);
        compositionPane.getChildren().add(window);  
        
        updateSelectedNotesArray();
    };
    
    /**
     * Handles mouse dragged on the compositionPane, drags selection window,
     * highlights notes intersecting the window and updates selected notes.
     * 
     * @param event the mouse click event
     */
    @FXML
    protected void handlePaneDragged(MouseEvent event) {        
        window.setVisible(true);
        
        int width = (int) (event.getX() - window.getX());
        int height = (int) (event.getY() - window.getY());
        window.setWidth(width);
        window.setHeight(height);
        
        for (NoteBar note: MUSIC_NOTES_ARRAY) {
            Rectangle r = note.noteDisplay;
            if (window.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())){
                note.selectNote();
            }
            else {
                note.unselectNote();
            }
        }
        
        updateSelectedNotesArray();
    };
    
    /**
     * Handles mouse released event. 
     * Removes the selection window from the pane, then creates a new note and 
     * adds note to pane. If control is clicked notes remain selected, otherwise 
     * selected notes are cleared. Also updates selected notes.
     * 
     * @param event the mouse click event
     */
    @FXML
    protected void handlePaneReleased(MouseEvent event) {
        compositionPane.getChildren().remove(window);  
        
        if (event.isStillSincePress()) {
            if (!event.isControlDown()) {
                resetSelectedNotesArray(); 
            }
            NoteBar newNote = new NoteBar(selectedInstrument, 
                    event.getX(), event.getY(), compositionPane);       
            MUSIC_NOTES_ARRAY.add(newNote);
        }
        
        updateSelectedNotesArray();
    };
    
    /**
     * Moves all of the selected notes.
     * 
     * @param x new X coordinate of the note
     * @param y new Y coordinate of the note
     */
    /*
    public void moveNotes(int newX, int newY){
        updateSelectedNotesArray();
        System.out.println(selectedNotesArray);
        for (NoteBar note: selectedNotesArray) {
            note.moveNote(newX, newY);
        }
    }
    */
    
    /**
     * Changes all of the selected notes lengths.
     * 
     * @param noteLength
     */
    /*
    public void changeNoteLengths(int noteLength){ 
        updateSelectedNotesArray();
        for (NoteBar note: selectedNotesArray) {
            note.changeNoteLength(noteLength);
        }
    }
    */
}

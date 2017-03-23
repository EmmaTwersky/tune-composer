// Create the selection window in the FXML? let it move all directions.
package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * This controller creates the music pane and handles mouse events on the pane.
 *
 * @author Emma Twersky
 */
public class CompositionPaneController implements Initializable {
    /**
     * Create array of NoteBar objects and selected NoteBar objects.      
     */
    protected static final ArrayList<NoteBar> MUSIC_NOTES_ARRAY = new ArrayList(); 
    protected static final ArrayList<NoteBar> SELECTED_NOTES_ARRAY = new ArrayList();
    
    /**
     * Create array to temporarily store selected NoteBar objects.      
     */
    private final ArrayList<NoteBar> temp_selected_notes_array = new ArrayList(); 
    
    /** 
     * Set note height to 10 pixels and note length to 100 pixels. 
     */
    private static final int NOTE_HEIGHT = 10; 
    private static final int INITIAL_NOTE_LENGTH = 100;
    
    /**
     * Set pitch range from 1 to 127 and bar range, the number of measures on 
     * the screen, to 20.
     */
    private static final int PITCH_RANGE = 128;
    private static final int BAR_RANGE = 20; 
    
    /**
     * Create the tune player to compose and play notes on the composition pane.
     */
    public static TunePlayer tunePlayerObj;
    
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
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     * @see RedBar
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        for (int i = 0; i < PITCH_RANGE; i++) {
            Line staffLine = new Line(0, i * NOTE_HEIGHT, 
                    BAR_RANGE * INITIAL_NOTE_LENGTH, i * NOTE_HEIGHT);
            staffLine.setId("staffLine");
            compositionPane.getChildren().add(staffLine);
        }
        for (int i = 0; i < BAR_RANGE; i++) {
            Line measureLine = new Line(i * INITIAL_NOTE_LENGTH, 0, 
                    i * INITIAL_NOTE_LENGTH, PITCH_RANGE * NOTE_HEIGHT);
            measureLine.setId("measureLine");
            compositionPane.getChildren().add(measureLine);
        }
        tunePlayerObj = new TunePlayer(compositionPane);
    }   
    
    /**
     * Fills the selectedNotesArray with the currently selected notes.
     */
    public static void updateSelectedNotesArray(){
        SELECTED_NOTES_ARRAY.clear();
        for (NoteBar note: MUSIC_NOTES_ARRAY) {            
            if (note.isSelected()) {
                SELECTED_NOTES_ARRAY.add(note);
            }
        }
    }
    
    /**
     * Empties the selectedNotesArray and un-selects all notes.
     */
    public static void resetSelectedNotesArray(){
        for (NoteBar note: MUSIC_NOTES_ARRAY) {            
            if (note.isSelected()) {
                note.unselect();
            }
        }
        SELECTED_NOTES_ARRAY.clear();
    }
    
    /**
     * Handles mouse press on the compositionPane.
     * Stops current MidiPlayer, gets initial value of mouse, and initializes 
     * selection window. Holds currently selected notes for drag window.
     * 
     * @param event the mouse click event
     * @see <NoteBar.java>
     */
    @FXML
    protected void handlePanePressed(MouseEvent event) {
        tunePlayerObj.stop();
        
        int x = (int) event.getX();
        int y = (int) event.getY();
        
        window = new Rectangle(x, y, 0, 0);
        window.setId("selectionWindow");
        window.setVisible(false);
        compositionPane.getChildren().add(window); 
        
        if (event.isControlDown()) {
            SELECTED_NOTES_ARRAY.forEach((note) -> {
                temp_selected_notes_array.add(note);
            });
        }
    };
    
    /**
     * Handles mouse dragged on the compositionPane. 
     * Drags selection window, highlights notes intersecting the window and 
     * updates selected notes based on control button.
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
            if (!temp_selected_notes_array.contains(note)) {
                Rectangle r = note.noteDisplay;
                if (window.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())){
                    note.select();
                }
                else {
                    note.unselect();
                }
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
        temp_selected_notes_array.clear();
        
        if (event.isStillSincePress()) {
            if (!event.isControlDown()) {
                resetSelectedNotesArray(); 
            }
            NoteBar newNote = new NoteBar(event.getX(), event.getY(), compositionPane);       
            MUSIC_NOTES_ARRAY.add(newNote);
        }
        
        updateSelectedNotesArray();
    };
}
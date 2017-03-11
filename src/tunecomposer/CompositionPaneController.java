package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * This controller creates the music pane and handles ouse events on the pane.
 *
 * @author Emma Twersky
 */
public class CompositionPaneController implements Initializable {

    /**
     * Create array to temporarily store selected NoteBar objects.      
     */
    private final ArrayList<NoteBar> STORE_TEMPSELECTED_NOTES_ARRAY = new ArrayList(); 
    
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
        tunePlayerObj = new TunePlayer(compositionPane);
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
            TunePlayer.SELECTED_NOTES_ARRAY.forEach((note) -> {
                STORE_TEMPSELECTED_NOTES_ARRAY.add(note);
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
        
        for (NoteBar note: TunePlayer.MUSIC_NOTES_ARRAY) {
            if (!STORE_TEMPSELECTED_NOTES_ARRAY.contains(note)) {
                Rectangle r = note.noteDisplay;
                if (window.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())){
                    note.selectNote();
                }
                else {
                    note.unselectNote();
                }
            }
        }
        
        TunePlayer.updateSelectedNotesArray();
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
        STORE_TEMPSELECTED_NOTES_ARRAY.clear();
        
        if (event.isStillSincePress()) {
            if (!event.isControlDown()) {
                TunePlayer.resetSelectedNotesArray(); 
            }
            NoteBar newNote = new NoteBar(InstrumentToolBarController.selectedInstrument, 
                    event.getX(), event.getY(), compositionPane);       
            TunePlayer.MUSIC_NOTES_ARRAY.add(newNote);
        }
        
        TunePlayer.updateSelectedNotesArray();
    };
}
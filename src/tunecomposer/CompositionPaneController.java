package tunecomposer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This controller creates the composition pane which contains the TunePlayer 
 * for the composition of the current SoundObjects and handles mouse events on the pane.
 *
 * @author Emma Twersky
 */
public class CompositionPaneController implements Initializable { 
    /**
     * Create the tune player to compose and play notes on the composition pane.
     */
    public static TunePlayer tunePlayerObj;
    
    /**
     * Create the pane which the note events take place on.      
     */
    @FXML
    public Pane soundObjectPane;
    
    @FXML
    public SelectionWindowPaneController selectionWindowPaneController;
    
    @FXML
    public RedBarPaneController redBarPaneController;
    
    /**
     * Initialize FXML, draws initial setup of composition pane and 
     * initialized the RedBar.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        tunePlayerObj = new TunePlayer();
    }   
    
    public void play() {
        tunePlayerObj.play();
        redBarPaneController.playAnimation();
    }
    
    public void stop() {
        tunePlayerObj.stop();
        redBarPaneController.stopAnimation();
    }
    
    /**
     * Handles mouse press on the musicPane.
     * Stops current MidiPlayer, gets initial value of mouse, and initializes 
     * selection window. Holds currently selected notes for drag window.
     * 
     * @param event the mouse click event
     * @see <NoteBar.java>
     */
    @FXML
    protected void handlePanePressed(MouseEvent event) {
        tunePlayerObj.stop();
        redBarPaneController.stopAnimation();
        
        selectionWindowPaneController.dragStartX = event.getX();
        selectionWindowPaneController.dragStartY = event.getY();
        
        if (event.isControlDown()) {
            SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((note) -> {
                SoundObjectPaneController.TEMP_SELECTED_SOUNDOBJ_ARRAY.add(note);
            });
        }
    };
    
    /**
     * Handles mouse dragged on the musicPane. 
     * Drags selection window, highlights notes intersecting the window and 
     * updates selected notes based on control button.
     * 
     * @param event the mouse click event
     */
    @FXML
    protected void handlePaneDragged(MouseEvent event) {        
        selectionWindowPaneController.SELECTION_WINDOW.setVisible(true);
        
        selectionWindowPaneController.translateWindow(event.getX(), event.getY());
        
        for (SoundObject sObj: SoundObjectPaneController.SOUNDOBJECT_ARRAY) {
            if (!SoundObjectPaneController.TEMP_SELECTED_SOUNDOBJ_ARRAY.contains(sObj)) {
                Rectangle r = sObj.visualRectangle;
                if (selectionWindowPaneController.SELECTION_WINDOW.
                        intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())){
                    sObj.select();
                }
                else {
                    sObj.unselect();
                }
            }
        }
        
        SoundObjectPaneController.updateSelectedSoundObjectArray();
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
        selectionWindowPaneController.SELECTION_WINDOW.setVisible(false);
        SoundObjectPaneController.TEMP_SELECTED_SOUNDOBJ_ARRAY.clear();
        
        if (event.isStillSincePress()) {
            if (!event.isControlDown()) {
                SoundObjectPaneController.unselectAllSoundObjects(); 
            }
            NoteBar newNote = new NoteBar(event.getX(), event.getY(), soundObjectPane);       
            SoundObjectPaneController.SOUNDOBJECT_ARRAY.add(newNote);
        }
        
        SoundObjectPaneController.updateSelectedSoundObjectArray();
    };
}
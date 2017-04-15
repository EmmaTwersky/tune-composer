package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.AddNote;

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
    public SoundObjectPaneController soundObjectPaneController;
    
    @FXML
    public SelectionWindowPaneController selectionWindowPaneController;
    
    @FXML
    public RedBarPaneController redBarPaneController;
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    public ActionManager actionManager;
    
    /**
     * Initialize FXML and sets SoundObjectPaneController to reference
     * the same ActionManager object.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        tunePlayerObj = new TunePlayer();
        actionManager = new ActionManager();
        soundObjectPaneController.setActionManager(actionManager);
    }   
    
    /**
     * Plays the current composition on the CompositionPane.
     */
    public void play() {
        tunePlayerObj.play(soundObjectPane);
        redBarPaneController.playAnimation(soundObjectPane);
    }
    
    /**
     * Stops the current composition on the CompositionPane from playing.
     */
    public void stop() {
        tunePlayerObj.stop();
        redBarPaneController.stopAnimation();
    }
    
    /**
     * Groups the currently selected SoundObjects on the CompositionPane.
     */
    public void group() {        
        soundObjectPaneController.group();
    }
    
    /**
     * Ungroups the currently selected SoundObjects on the CompositionPane.
     */
    public void ungroup() {
        soundObjectPaneController.ungroup();
    }
    
    /**
     * Handles mouse press on the SoundObjectPane.
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
     * Handles mouse dragged on the SoundObjectPane. 
     * Drags selection window, highlights notes intersecting the window and 
     * updates selected notes based on control button.
     * 
     * @param event the mouse click event
     */
    @FXML
    protected void handlePaneDragged(MouseEvent event) {        
        selectionWindowPaneController.SELECTION_WINDOW.setVisible(true);
        
        selectionWindowPaneController.translateWindow(event.getX(), event.getY());
        
        for (Node n: soundObjectPane.getChildren()) {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) (r).getUserData();
            if (!SoundObjectPaneController.TEMP_SELECTED_SOUNDOBJ_ARRAY.contains(sObj)) {
                if (selectionWindowPaneController.SELECTION_WINDOW.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())){
                    sObj.select();
                }
                else {
                    sObj.unselect();
                }
            }
        }
                
        soundObjectPaneController.updateSelectedSoundObjectArray(soundObjectPane);
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
                SoundObjectPaneController.unselectAllSoundObjects(soundObjectPane); 
            }
            
            AddNote addAction;
            addAction = new AddNote(event.getX(), event.getY(), actionManager, soundObjectPane);
            
            ArrayList<Action> addActionArray = new ArrayList<>();
            addActionArray.add(addAction);
            
            actionManager.execute(addActionArray);
            actionManager.putInUndoStack(addActionArray);
        }
        
        soundObjectPaneController.updateSelectedSoundObjectArray(soundObjectPane);
    };
    
    
    /**
     * Set the actionManager to the Application's passed in ActionManager instance.
     * If given manager is null, then throws NullPointerException.
     * 
     * @param manager the ActionManager for the current TuneComposer application
     * @throws NullPointerException
     */
    public void setActionManager(ActionManager manager) throws NullPointerException{
        if (manager == null) {
            throw new NullPointerException();
        }
        actionManager = manager;
        try {
            soundObjectPaneController.setActionManager(actionManager);
        } catch (NullPointerException ex) {
            System.out.println("In CompPaneController.setActionManager(), "
                    + "passed null to "
                    + "soundObjectPaneController.setActionManager(manager)");
            System.exit(1);
        }
    }
}
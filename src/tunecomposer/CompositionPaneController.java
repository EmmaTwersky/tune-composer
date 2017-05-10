package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.AddNoteAction;
import tunecomposer.actionclasses.SelectAction;
import tunecomposer.actionclasses.UnselectAction;

/**
 * Controller for coordinating all the controllers in the composition StackPane.
 */
public class CompositionPaneController implements Initializable { 
    
    /**
     * Dimensions of the Composition StackPane.
     */
    public static final int PANE_X_MAX = 18000;
    public static final int PANE_Y_MAX = 1280;
    
    /**
     * Used by play and playSelected to determine where to begin play.
     */
    long startTick;
    
    /**
     * Create the tune player to compose and play notes on the CompositionPane.
     */
    public static TunePlayer tunePlayerObj;
    
    /**
     * The pane which holds all the SoundObject visuals.      
     */
    @FXML
    public Pane soundObjectPane;
    
    /**
     * Controller for pane that holds all SoundObject visuals.
     */
    @FXML
    public SoundObjectPaneController soundObjectPaneController;
    
    /**
     * Controller for pane that holds the selection window rectangle.
     */
    @FXML
    public SelectionWindowPaneController selectionWindowPaneController;
    
    /**
     * Controller for pane that holds the RedBar play animation.
     */
    @FXML
    public RedBarPaneController redBarPaneController;
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    public ActionManager actionManager;
    
    /**
     * Selection Actions used in tandem to record which notes states have been
     * changed during the current click.
     * Used in mouse handlers.
     */
    private SelectAction selectAction;
    private UnselectAction unselectAction;
    
    /**
     * All the selection actions that have been created during the current click.
     * Used in mouse handlers.
     */
    private ArrayList<SoundObject> selectObjs;
    private ArrayList<SoundObject> unselectObjs; 
    
    /**
     * Keeps track of the SoundObjects that were selected before the mouse was pressed.
     * Used to restore selection state correctly when undoing.
     */
    private ArrayList<SoundObject> wasSelected;
    
    /**
     * Creates TunePlayer and ActionManger objects, then sets 
     * SoundObjectPaneController to reference the same ActionManager object.
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
        startTick = 0;
        tunePlayerObj.play(soundObjectPane.getChildren(), startTick);
        redBarPaneController.playAnimation(soundObjectPane, startTick);
    }
    
    /**
     * Plays the selected notes on the CompositionPane.
     */
    public void playSelected() {
        ArrayList<Node> selectedNotes = new ArrayList();
        startTick = PANE_X_MAX;
        soundObjectPane.getChildren().forEach((n) -> {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) r.getUserData();
            if (sObj.isSelected()) {
                selectedNotes.add(n);
                if (startTick > sObj.getStartTick()){
                    startTick = sObj.getStartTick();
                }
            }
        });
        
        tunePlayerObj.play(selectedNotes, startTick);
        redBarPaneController.playAnimation(soundObjectPane, startTick);
    }
    
    /**
     * Stops the current composition on the CompositionPane from playing.
     */
    public void stop() {
        tunePlayerObj.stop();
        redBarPaneController.stopAnimation();
    }
    
    /**
     * Adds selected SoundObjects to Clipboard and removes them from the
     * CompositionPane.
     */
    public void cut() {
        stop();
        soundObjectPaneController.cut();
    }
    
    /**
     * Adds selected SoundObjects to Clipboard.
     */
    public void copy() {
        stop();
        soundObjectPaneController.copy();
    }
    
    /**
     * Adds most recently copied or cut SoundObjects onto the CompositionPane.
     */
    public void paste() {
        stop();
        soundObjectPaneController.paste();
    }
    
    /**
     * Selects all the SoundObjects on the CompositionPane.
     */
    public void selectAll() {
        stop();
        soundObjectPaneController.selectAll();        
    }
    
    /**
     * Deletes the selected SoundObjects from the CompositionPane.
     */
    public void delete() {
        stop();
        soundObjectPaneController.delete();
    }
    
    /**
     * Groups the currently selected SoundObjects on the CompositionPane.
     */
    public void group() {  
        stop();
        soundObjectPaneController.group();
    }
    
    /**
     * Ungroups the top gesture of any selected Gestures on the CompositionPane.
     */
    public void ungroup() {
        stop();
        soundObjectPaneController.ungroup();
    } 
    
    /**
     * Changes instrument of sound objects.
     * @param instrumentName
     */
    public void changeInstrument(String instrumentName) {
        stop();
        soundObjectPaneController.changeInstrument(instrumentName);
    }
       
    /**
     * Overwrites selectedObjs and unselectedObjs into their respective actions
     * and executes both actions.
     */
    private void updateDragActions() {
        selectAction.changeAffectedObjs(selectObjs);
        unselectAction.changeAffectedObjs(unselectObjs);
        unselectAction.execute();
        selectAction.execute();
    }
    
    /**
     * If given rectangle intersects with SELECTION_WINDOW, move it from 
     * unselectedObj to selectedObj. 
     * If not, then do the opposite.
     * 
     * @param rect Rectangle to check for intersection with SELECTION_WINDOW
     * @param sObj 
     */
    private void updateSelectedObjArrays(Rectangle rect) {
        SoundObject sObj = (SoundObject) rect.getUserData();
        if (selectionWindowPaneController.SELECTION_WINDOW.intersects(
                rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight())){
            if (!sObj.isSelected()) {
                if (!selectObjs.contains(sObj)) {
                    selectObjs.add(sObj);
                    unselectObjs.remove(sObj);
                }
            }
        }
        else {
            //move selected SoundObjects to unselectedObj from selectedObj
            if (sObj.isSelected()) {
                if (!unselectObjs.contains(sObj)) {
                    unselectObjs.add(sObj);
                    selectObjs.remove(sObj);
                }
            }
        }
    }

    /**
     * Handles the logic if the pane was clicked in place. 
     * Cannot pass null as parameter. This method may change arrayOfMouseActions, 
     * unselectedObjs, or unselectedObjs.
     * be changed after call.
     * @param event the MouseEvent to grab event info from
     * @param arrayOfMouseActions 
     *          Action array of all actions to push onto the undoStack
     * @throws IllegalArgumentException if null was passed
     */
    private void paneClickedInPlace(MouseEvent event, ArrayList<Action> arrayOfMouseActions) 
                                      throws IllegalArgumentException {
        if (event == null) {
            throw new IllegalArgumentException();
        }
        if (arrayOfMouseActions == null) {
            throw new IllegalArgumentException();
        }
        
        
        if (!event.isControlDown()) {
            for (Node n : soundObjectPane.getChildren()) {
                Rectangle r = (Rectangle) n;
                SoundObject s = (SoundObject) r.getUserData();
                if (s.isSelected()) {
                    unselectObjs.add(s);
                }
            }
        }
        else {
            unselectObjs.clear();
        }
        
        AddNoteAction addAction;
        addAction = new AddNoteAction(event.getX(), event.getY(), actionManager, soundObjectPane);
        
        arrayOfMouseActions.add(addAction);
        selectObjs.add(addAction.getNote());
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
        resetClickHandlerFields();
        
        tunePlayerObj.stop();
        redBarPaneController.stopAnimation();
        
        selectionWindowPaneController.resetWindowCoords(event.getX(), event.getY());
        
        SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((sObj) -> {
            if (event.isControlDown()) {
                SoundObjectPaneController.TEMP_SELECTED_SOUNDOBJ_ARRAY.add(sObj);
            }
            wasSelected.add(sObj);
            //add encapsulating gesture
            Gesture topGest = sObj.getTopGesture(); 
            if (topGest != null) {
                wasSelected.add(topGest);
            }
        });
    };
    
    /**
     * Creates new instances of actions and arrays used in the click handlers.
     */
    private void resetClickHandlerFields() {
        selectObjs = new ArrayList();
        unselectObjs = new ArrayList();
        selectAction = new SelectAction(selectObjs);
        unselectAction = new UnselectAction(unselectObjs);
        wasSelected = new ArrayList();
    } 

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
            SoundObject sObj = (SoundObject) r.getUserData();
            if (!SoundObjectPaneController.TEMP_SELECTED_SOUNDOBJ_ARRAY.contains(sObj)) {
                updateSelectedObjArrays(r);
            }
        }
        
        updateDragActions();
        SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane);
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
        ArrayList<Action> arrayOfMouseActions = new ArrayList<>();
        selectionWindowPaneController.SELECTION_WINDOW.setVisible(false);
        SoundObjectPaneController.TEMP_SELECTED_SOUNDOBJ_ARRAY.clear();
        
        if (event.isStillSincePress()) {
            paneClickedInPlace(event, arrayOfMouseActions);
        }
        
        //if was initially selected, don't keep in selectAction 
        for (SoundObject sObj : wasSelected) {
            selectObjs.remove(sObj);
        } 
        
       selectAction.changeAffectedObjs(selectObjs);
        unselectAction.changeAffectedObjs(unselectObjs);
        
        //if no visual change, then don't keep action
        if (!unselectAction.affectedObjs.isEmpty()) {
            arrayOfMouseActions.add(unselectAction);
        }
        
        if (!selectAction.affectedObjs.isEmpty()) {
        arrayOfMouseActions.add(selectAction);
        }
        
        actionManager.execute(arrayOfMouseActions);
        actionManager.putInUndoStack(arrayOfMouseActions);
        
        SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane);
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
            System.out.println("Action Manager null");
        }
    }
}

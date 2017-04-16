package tunecomposer;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.ShortMessage;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.MoveAction;
import tunecomposer.actionclasses.SelectAction;
import tunecomposer.actionclasses.StretchAction;
import tunecomposer.actionclasses.UnselectAction;

/**
 * This class creates and edits NoteBar objects to display notes in the tune 
 * and be played in MidiPLayer.
 * @extends SoundObject
 * 
 * @author Emma Twersky
 */
public class NoteBar extends SoundObject{
    /**
     * Create variables for the note name, instrument number, channel number,
     * pitch, starting value, and duration. 
     */
    public final String name;
    public final int instrument;
    public final int channel;
    public int pitch;
    public int startTick;
    public int duration;
    
    MoveAction sObjMove;
    StretchAction sObjStretch;
    SelectAction selectAction;
    UnselectAction unselectAction;

    /**
     * actionManager instance that holds the undo and redo stacks this note 
     * lives within. 
     */
    private ActionManager actionManager;
    ArrayList<Action> actionList;
    
    /**
    * Creates fixed height and set ranges for pitch. 
    */
    private final int PITCH_RANGE = 128;
    private final int NOTE_HEIGHT = 10;
    private final int VOLUME = 127;
    
    /**
    * Sets default duration, minimum duration and default click range to edit duration. 
    */
    private final int DEFAULT_LENGTH = 100;
    
    /**
     * Load InstrumentInfo HashMap to look up instrument key values.
     */
    private final InstrumentInfo instrumentInfo = new InstrumentInfo();
    
     /**
     * Initialize the NoteBar object and variables, then constructs the 
     * display and does not add it to a pane.
     * 
     * @param x the top left corner x value of the note clicked
     * @param y the top left corner y value of the note clicked
     * @param _actionManager
     * @param soundObjectPane
     */
    public NoteBar(double x, double y, ActionManager _actionManager, Pane soundObjectPane){
        name = InstrumentToolBarController.selectedInstrument;
        instrument = instrumentInfo.getInstrumentValue(name);
        channel = instrumentInfo.getInstrumentChannel(name);
        actionManager = _actionManager;
        pane = soundObjectPane;
        
        pitch = PITCH_RANGE - (int) y / NOTE_HEIGHT;
        startTick = (int) x;
        duration = DEFAULT_LENGTH;
                
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / NOTE_HEIGHT) * NOTE_HEIGHT;
        visualRectangle = new Rectangle(xLocation, yLocation, duration, NOTE_HEIGHT);
        visualRectangle.setId(name);

        setHandlers();

        select();
    }
    
    /**
     * Selects note and displays visualRectangle selection.
     */
    @Override
    public final void select(){
        selected = true;
        visualRectangle.getStyleClass().removeAll("unselectedNote");
        visualRectangle.getStyleClass().add("selectedNote");
        SoundObjectPaneController.updateSelectedSoundObjectArray(pane); 
    }
    
    /**
     * Un-selects note and removes visualRectangle selection.
     */
    @Override
    public void unselect(){
        selected = false;
        visualRectangle.getStyleClass().removeAll("selectedNote");
        visualRectangle.getStyleClass().add("unselectedNote");
        SoundObjectPaneController.updateSelectedSoundObjectArray(pane); 
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
        int translateX = x + (int) visualRectangle.getX();
        int translateY = y + (int) visualRectangle.getY();
        visualRectangle.setX(translateX);
        visualRectangle.setY(translateY);
    }
    
    /**
     * Changes note duration.
     * 
     * @param lengthChange the amount to increment the duration
     */
    @Override
    public void changeLength(int lengthChange){ 
        int newLength = duration + lengthChange;
        if (newLength > minLength) {
            duration = newLength;
            visualRectangle.setWidth(duration);
        }
    }

    /**
     * Fixes note to sit in-between staff lines.
     */
    @Override
    public void snapInPlace() {
        int x = (int) visualRectangle.getX();
        int y = (int) visualRectangle.getY();
        
        pitch = PITCH_RANGE - (int) y / NOTE_HEIGHT;
        startTick = (int) x;
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / NOTE_HEIGHT) * NOTE_HEIGHT;
        visualRectangle.setX(xLocation);
        visualRectangle.setY(yLocation);
        visualRectangle.getStyleClass().add(name);
    }
    
    /**
     * Sets the visualRectangle's mouse event handlers. 
     */
    @Override
    public final void setHandlers() {
        visualRectangle.setOnMousePressed(handleNotePressed);
        visualRectangle.setOnMouseDragged(handleNoteDragged);
        visualRectangle.setOnMouseReleased(handleNoteReleased);
    }
    
    /**
     * Adds the note to the given pane. 
     * Does not manage selection. Does not handle if given pane is null. 
     * 
     * @param soundObjectPane
     */
    @Override
    public void addToPane(Pane soundObjectPane) {
        soundObjectPane.getChildren().add(visualRectangle);
    }
    
    /**
     * Removes the note from the given pane. 
     * Does not manage selection. Does not handle if given pane is null. 
     * 
     * @param soundObjectPane
     */
    @Override
    public void removeFromPane(Pane soundObjectPane){
        soundObjectPane.getChildren().remove(visualRectangle);
    }
    
    /**
     * Adds the note to the MidiPlayer. 
     * 
     * @param player given TunePlayer object
     */
    @Override
    public void addToMidiPlayer(MidiPlayer player) {
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + this.channel, 
                this.instrument, 0, 0, this.channel);
        player.addNote(this.pitch, VOLUME, this.startTick, 
                this.duration, this.channel, 0);
    }
    
    /**
     * Handles note pressed event. 
     * Sets initial pressed values of the mouse and consumes the event.
     * Also checks if dragging the note to change length.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleNotePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            CompositionPaneController.tunePlayerObj.stop();
            actionList = new ArrayList();
            
            initialX = (int) event.getX();
            initialY = (int) event.getY();
            ArrayList<SoundObject> thisNote = new ArrayList();
            thisNote.add((SoundObject) visualRectangle.getUserData());
            
            if (!selected) {
                if(!event.isControlDown()){
                    SoundObjectPaneController.unselectAllSoundObjects(pane);
                }
                selectAction = new SelectAction(thisNote);
                selectAction.execute();
                actionList.add(selectAction);
            }
            else if (event.isControlDown()){
                unselectAction = new UnselectAction(thisNote);
                unselectAction.execute();
                actionList.add(unselectAction);
            }
            
            int editLengthMax = (int) visualRectangle.getX() + duration;
            int editLengthMin = editLengthMax - clickToEditLength;
            if ((editLengthMin <= initialX) && (initialX <= editLengthMax)) {
                draggingLength = true;
                sObjStretch = new StretchAction(
                        SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                        initialX);
            }
            else{
                sObjMove = new MoveAction(
                        SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                        initialX, initialY);
            }
                
            event.consume();
        }
    };
    
    /**
     * Handles note dragged event.
     * Selects and drags to move the note or drags to change duration, 
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
                        
            
            if (draggingLength) {
                sObjStretch.stretch(x - initialX);
            }
            else {
                int translateX = (x - initialX);
                int translateY = (y - initialY);
                sObjMove.move(translateX, translateY);
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
            if (draggingLength){
                sObjStretch.setFinalX(initialX);
                sObjStretch.execute();
                actionList.add(sObjStretch);
            }
            
            else{
                sObjMove.setLastCoords(initialX, initialY);
                sObjMove.execute();
                actionList.add(sObjMove);
            }
            
            for (SoundObject soundItem : SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
                soundItem.snapInPlace();
            }
            
            draggingLength = false;
            
            actionManager.putInUndoStack(actionList);
            event.consume();
        }
    };    
}

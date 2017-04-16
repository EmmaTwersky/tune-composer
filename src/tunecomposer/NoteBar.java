package tunecomposer;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Node;
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

    /**
     * ActionManager instance that holds the undo and redo stacks.
     */
    private ActionManager actionManager;
    ArrayList<Action> actionList;
    MoveAction sObjMove;
    StretchAction sObjStretch;
    SelectAction selectAction;
    UnselectAction unselectAction;
    
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
    public void move(double x, double y){
        double newXLoc = x + visualRectangle.getX();
        double newYLoc = y + visualRectangle.getY();
        
        visualRectangle.setX(newXLoc);
        visualRectangle.setY(newYLoc);
    }
    
    /**
     * Checks if moving note will push it past the pane's borders.
     * 
     * @param xInc the "proposed" x move increment.
     * @param yInc the "proposed" y move increment.
     * @return onEdge is true if the move is illegal and false if its legal.
     */
    @Override
    public boolean isOnEdge(double xInc, double yInc){
        
        boolean onEdge = false;
        
        double x = visualRectangle.getX()+xInc;
        double y = visualRectangle.getY()+yInc;
        if ((x < 0 || x+duration > 2000) ||
            (y < 0 || y+HEIGHT > 1280)){
            onEdge = true;
        }
        return onEdge;
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
     * Method for recursively grabbing all children of a SoundObject. 
     * Returns an ArrayList containing only this note.
     * @return an ArrayList containing only this note.
     */
    @Override
    public ArrayList<SoundObject> getAllChildren() {
        ArrayList<SoundObject> array = new ArrayList();
        array.add(this);
        return array;
    }
    
    
    /**
     * 
     * @param press
     * @param drag
     * @param release 
     */
    @Override
    public void setHandlers(EventHandler press, EventHandler drag, EventHandler release) {
        this.visualRectangle.setOnMousePressed(press);
        this.visualRectangle.setOnMouseDragged(drag);
        this.visualRectangle.setOnMouseReleased(release);
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
            
            latestX = event.getX();
            latestY = event.getY();
            ArrayList<SoundObject> thisNote = new ArrayList();
            thisNote.add((SoundObject) visualRectangle.getUserData());
            
            if (!selected) {
                if(!event.isControlDown()){
                    ArrayList<SoundObject> allSelected = new ArrayList();
                    for (Node n : pane.getChildren()) {
                        Rectangle r = (Rectangle) n;
                        SoundObject sObj = (SoundObject) r.getUserData();
                        if (sObj.isSelected()) {
                            allSelected.add(sObj);
                        }
                    }
                    unselectAction = new UnselectAction(allSelected);
                    unselectAction.execute();
                    actionList.add(unselectAction);
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
            
            double editLengthMax = visualRectangle.getX() + duration;
            double editLengthMin = editLengthMax - clickToEditLength;
            if ((editLengthMin <= latestX) && (latestX <= editLengthMax)) {
                draggingLength = true;
                sObjStretch = new StretchAction(
                        SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                        (int)latestX);
            }
            else {
                sObjMove = new MoveAction(
                        SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                        latestX, latestY);
            }
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
            double x = event.getX();
            double y = event.getY();
            
            if (draggingLength) {
                sObjStretch.stretch((int)(x - latestX));
            }
            else {
                double translateX = (x - latestX);
                double translateY = (y - latestY);
                sObjMove.move(translateX, translateY);
            }
            
            latestX = x;
            latestY = y;
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
            if (!event.isStillSincePress()) {
                if (draggingLength){
                    sObjStretch.setFinalX((int)latestX);
                    actionList.add(sObjStretch);
                }
                else {
                    sObjMove.setLastCoords(latestX, latestY);
                    actionList.add(sObjMove);
                }
                for (SoundObject soundItem : SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
                    soundItem.snapInPlace();
                }
                draggingLength = false;
            }
             
            actionManager.putInUndoStack(actionList);
            event.consume();
        }
    };    
}

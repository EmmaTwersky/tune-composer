package tunecomposer;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.ShortMessage;
import tunecomposer.actionclasses.MoveAction;
import tunecomposer.actionclasses.LengthChangeAction;

/**
 * This class creates and edits NoteBar objects to display notes in the tune 
 * and be played in MidiPLayer.
 * @extends SoundObject
 */
public final class NoteBar extends SoundObject {
    /**
     * Create variables for the note name, instrument number, channel number,
     * pitch, starting value, and duration. 
     */
    public String name;
    private int instrument;
    public int channel;
    private int pitch;
    private int duration;

    /**
     * ActionManager instance that holds the undo and redo stacks.
     */
    private ActionManager actionManager;
    
    /**
    * Creates fixed height and set ranges for pitch. 
    */
    private final int PITCH_RANGE = 128;
    private final int NOTE_HEIGHT = 10;
    private final int VOLUME = 127;
    
    /**
    * Sets default duration, minimum duration and default click range to edit duration. 
    */
    public static int noteLength = 40;
    
    /**
     * Default for note sound to be set to if instrument not recognized.
     */
    private static final String DEFAULT_NAME = "Piano";
    
    /**
     * Minimum length that a NoteBar rectangle can become. 
     * Consequently, also limits the minimum duration of the note.
     */
    public final int minLength = 5;
    
    /**
     * X value that the rectangle would be at if the note didn't snap in place.
     * Used to prevent loosing positional information when rounding x-coordinate
     * of rectangle.
     */
    private double unsnappedX;

    
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
        this.soundObjectPane = soundObjectPane;
        
        pitch = PITCH_RANGE - ((int) Math.round(y / NOTE_HEIGHT));
        startTick = (int) x;
        duration = noteLength;
                
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / NOTE_HEIGHT) * NOTE_HEIGHT;
        visualRectangle = new Rectangle(xLocation, yLocation, getDuration(), NOTE_HEIGHT);
        visualRectangle.setId(name);

        unsnappedX = x;
        snapXInPlace();
        unsnappedX = visualRectangle.getX();
        
        setHandlers();

        select();
    }
    
    /**
     * Useful to create a NoteBar from XML. Given x, y, and length must be 
     * positive coordinates. 
     * 
     * @param x the top left corner x value of the note clicked
     * @param y the top left corner y value of the note clicked
     * @param length length the note 
     * @param instrument must be a valid MIDI instrument number. If value not
     *      found in INSTRUMENT_VALUE map, then note is set to "Piano"
     * @param am must be the actionManager being used for undo/redo.
     * @param soundObjPane must be pane all SoundObjects are put in
     */
    public NoteBar(int x, int y, int length, int instrument, 
                                          ActionManager am, Pane soundObjPane) {
        if ("NOT FOUND".equals(instrumentInfo.getInstName(instrument))) {
            name = DEFAULT_NAME;
        }
        else { name = instrumentInfo.getInstName(instrument); }
        this.instrument = instrumentInfo.getInstrumentValue(name);
        channel = instrumentInfo.getInstrumentChannel(name);
        actionManager = am;
        soundObjectPane = soundObjPane;
        
        pitch = PITCH_RANGE - ((int) Math.round(y / NOTE_HEIGHT));
        startTick = x;
        duration = length;
        
        
        int xLocation = x;
        int yLocation = (int) Math.round((double) y / NOTE_HEIGHT) * NOTE_HEIGHT;
        visualRectangle = new Rectangle(xLocation, yLocation, duration, NOTE_HEIGHT);
        visualRectangle.setId(name);

        unsnappedX = x;
        snapXInPlace();
        unsnappedX = visualRectangle.getX();
        
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
        SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane); 
    }
    
    /**
     * Un-selects note and removes visualRectangle selection.
     */
    @Override
    public void unselect(){
        selected = false;
        visualRectangle.getStyleClass().removeAll("selectedNote");
        visualRectangle.getStyleClass().add("unselectedNote");
        SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane); 
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
     * Returns instrument.
     * 
     * @return the instrument
     */
    public int getInstrument() {
        return instrument;
    }
    
    /**
     * Changes instrument.
     */
    @Override
    public void changeInstrument(String instrument) {
        name = instrument;
        this.instrument = instrumentInfo.getInstrumentValue(name);
        channel = instrumentInfo.getInstrumentChannel(name);
        visualRectangle.setId(name);
    }

    /**
     * Returns pitch.
     * @return the pitch
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Returns duration.
     * 
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }
    
    /**
     * Parser that converts a NoteBar to a String to be used in the  Clipboard.
     * 
     * @return String in an XML-like format
     */
    @Override
    public String objectToXML(){
	String startTag = "<notebar>";
	String endTag = " </notebar>";
	String xstr = " x:";
	String ystr = " y:";
	String widthstr = " width:";
	String inststr = " instrument:";
	
	String x = String.valueOf((int) this.visualRectangle.getX());
	String y = String.valueOf((int) this.visualRectangle.getY());
	String instrument = String.valueOf(this.instrument);
	String width = String.valueOf(this.duration);
	
	String result = startTag + xstr + x + ystr + y + widthstr + width + inststr + instrument + endTag;
	        
	return result;
    }
    
    
    /**
     * Moves note on pane by given increments.
     * Keeps note's pitch and startTick up to date with current position.
     * @param xInc the increment to change current x value by
     * @param yInc the increment to change current y value by
     */
    @Override
    public void move(double xInc, double yInc){
        double newYLoc = yInc + visualRectangle.getY();
        double newXLoc = xInc + visualRectangle.getX();
        
        visualRectangle.setX(newXLoc);
        visualRectangle.setY(newYLoc);
        
        //update pitch and startTick
        startTick = (int) visualRectangle.getX();
        pitch = PITCH_RANGE - ((int) Math.round((int)visualRectangle.getY() / NOTE_HEIGHT));

//        snapYInPlace();
//        snapXInPlace();
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
        
        int minX = 0;
        int minY = 0;
        int maxX = CompositionPaneController.PANE_X_MAX;
        int maxY = CompositionPaneController.PANE_Y_MAX;
        
        if ((x < minX || x+getDuration() > maxX) ||
            (y < minY || y+NOTE_HEIGHT > maxY)){
            onEdge = true;
        }
        return onEdge;
    }
    
    /**
     * Changes note duration and visualRectangle's length accordingly
     * 
     * @param lengthChange the amount to increment the duration and length.
     */
    @Override
    public void changeLength(int lengthChange){ 
        int newLength = getDuration() + lengthChange;
        if (newLength > minLength) {
            duration = newLength;
            visualRectangle.setWidth(getDuration());
        }
    }

    /**
     * Fixes note to sit in-between staff lines.
     * Also updates the note's pitch to match the movement.
     * Precondition: the note has just been moved.
     * Postcondition: the notes location is fixed to sit in between staff lines.
     */
    @Override
    public void snapYInPlace() {
        //Get raw values of rectangle location.
        double yRaw = visualRectangle.getY();
        
        pitch = PITCH_RANGE - ((int) Math.round((int)yRaw / NOTE_HEIGHT));
        
        //Fix raw values.
        int yFixed = (int) Math.round(yRaw / (double)NOTE_HEIGHT) * NOTE_HEIGHT;
        
        //Reset rectangle to fixed values.
//        visualRectangle.setX(xFixed);
        visualRectangle.setY(yFixed);
    }
    
    /**
     * Snaps the note to the nearest x-coordinate using snapXDistance as the
     * distance to snap to.
     * Updates the startTick of the note.
     * 
     */
    @Override
    public void snapXInPlace() {
        int xFixed = (int) Math.round(unsnappedX / (double) snapXDistance) * snapXDistance;
        startTick = (int) visualRectangle.getX();
        
        //Reset rectangle to fixed values.
        visualRectangle.setX(xFixed);
    }
    
    /**
     * Sets the visualRectangle's mouse event handlers.
     * 
     * Precondition: the note does not belong to a gesture.
     */
    @Override
    public final void setHandlers() {
        visualRectangle.setOnMousePressed(handleNotePressed);
        visualRectangle.setOnMouseDragged(handleNoteDragged);
        visualRectangle.setOnMouseReleased(handleNoteReleased);
    }
    
    /**
     * Sets the mouse handlers of the called object to the given parameters.
     * Makes all child SoundObject's handlers the given. Useful to make all
     * SoundObjects in a group use the topGesture's EventHandlers.
     * @param press 
     *          Handler of the object that mouse press events will consume 
     * @param drag
     *          Handler of the object that mouse drag events will consume 
     * @param release 
     *          Handler of the object that mouse release events will consume 
     */
    @Override
    public void setHandlers(EventHandler press, EventHandler drag, EventHandler release) {
        this.visualRectangle.setOnMousePressed(press);
        this.visualRectangle.setOnMouseDragged(drag);
        this.visualRectangle.setOnMouseReleased(release);
    }
    
    /**
     * Adds the note to the given pane. 
     * Does not manage selection. Does not handle if given pane is null. 
     * Precondition: the note is not on the pane and the pane is not null.
     * 
     * @param soundObjectPane pane the note is on
     */
    @Override
    public void addToPane(Pane soundObjectPane) {
        soundObjectPane.getChildren().add(visualRectangle);
    }
    
    /**
     * Removes the note from the given pane. 
     * Does not manage selection. Does not handle if given pane is null.
     * Precondition: the note is on the pane and the pane is not null.
     * 
     * @param soundObjectPane pane the note is on
     */
    @Override
    public void removeFromPane(Pane soundObjectPane){
        soundObjectPane.getChildren().remove(visualRectangle);
    }
    
    /**
     * Method for recursively grabbing all children of a SoundObject. 
     * 
     * @return an ArrayList containing only this note.
     */
    @Override
    public ArrayList<SoundObject> getAllChildren() {
        ArrayList<SoundObject> array = new ArrayList();
        array.add(this);
        return array;
    }
    
    /**
     * Adds the note to the MidiPlayer. 
     * 
     * @param player given TunePlayer object
     */
    @Override
    public void addToMidiPlayer(MidiPlayer player) {
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + this.channel, this.getInstrument(), 0, 0, this.channel);
        player.addNote(this.getPitch(), VOLUME, this.getStartTick(), this.getDuration(), this.channel, 0);
    }
    
    /**
     * Handles note pressed event. 
     * Prepares the proper Selection,Unselection, Move, and Stretch Actions
     * depending on the placement of the click, if control is down, and which
     * note(s) were already selected.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleNotePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            CompositionPaneController.tunePlayerObj.stop();
            
            //used for dragging note length
            latestX = event.getX();
            latestY = event.getY();
            //used for dragging note
            
            lastXShiftMouseLoc = event.getX();
            lastYShiftMouseLoc = event.getY();

            actionList = new ArrayList();
            
            prepareSelectionAction(event.isControlDown());
            
            prepareMoveOrStretchAction();
            SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane);
        }
    };

    /**
     * Handles note dragged event.
     * Continuously updates the execution of Move/Stretch Actions using their
     * move/stretch methods.
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
                latestX = x;
                latestY = y;
            }
            else { 
                shiftNotePosition(x, y);
            }
            
            if (draggingLength || !sObjMoveAction.isMoveFailed()){
                latestX = x;
                latestY = y;
            }
            
            SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane);
            
            event.consume();
        }
    };
    
    /**
     * Handles mouse released.
     * Prepares states of stretch and move for potential undoing (using the
     * setFinalX and setLastCoords methods.  Pushes all actions onto undo stack.
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
                    sObjMoveAction.setLastCoords(lastXShiftMouseLoc, lastYShiftMouseLoc);
                    actionList.add(sObjMoveAction);
                }
                for (SoundObject soundItem : SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
                    soundItem.snapYInPlace();
                }
                draggingLength = false;
            }
            
            SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane);
            
            actionManager.putInUndoStack(actionList);
            
            event.consume();
        }
    };
    
    
    /**
     * Creates a Move or Stretch Action depending on the placement of the
     * initial click.
     * Helper method to the handleNotePressed event handler.
     */
    @Override
    void prepareMoveOrStretchAction(){
        double editLengthMax = visualRectangle.getX() + getDuration();
        double editLengthMin = editLengthMax - clickToEditLength;
        if ((editLengthMin <= latestX) && (latestX <= editLengthMax)) {
            draggingLength = true;
            sObjStretch = new LengthChangeAction(
                    SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                    (int)latestX);
        }
        else {
            sObjMoveAction = new MoveAction(
            SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                    lastXShiftMouseLoc, lastYShiftMouseLoc);
        }
    }
}

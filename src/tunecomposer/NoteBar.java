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
public class NoteBar extends SoundObject {
    /**
     * Create variables for the note name, instrument number, channel number,
     * pitch, starting value, and duration. 
     */
    public final String name;
    private final int instrument;
    public final int channel;
    private int pitch;
    private int startTick;
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
    private final int DEFAULT_LENGTH = 100;
    
    /**
     * Minimum length that a NoteBar rectangle can become. 
     * Consequently, also limits the minimum duration of the note.
     */
    public final int minLength = 5;
    
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
        
        pitch = PITCH_RANGE - (int) y / NOTE_HEIGHT;
        startTick = (int) x;
        duration = DEFAULT_LENGTH;
                
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / NOTE_HEIGHT) * NOTE_HEIGHT;
        visualRectangle = new Rectangle(xLocation, yLocation, getDuration(), NOTE_HEIGHT);
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
     * Moves note freely on pane.

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
        
        int minX = 0;
        int minY = 0;
        int maxX = CompositionPaneController.PANE_X_MAX;
        int maxY = CompositionPaneController.PANE_Y_MAX;
        
        if ((x < minX || x+getDuration() > maxX) ||
            (y < minY || y+HEIGHT > maxY)){
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
     * 
     * Precondition: the note has just been moved.
     * Postcondition: the notes location is fixed to sit in between staff lines.
     */
    @Override
    public void snapInPlace() {
        //Get raw values of rectangle location.
        int xRaw = (int) visualRectangle.getX();
        int yRaw = (int) visualRectangle.getY();
        
        pitch = PITCH_RANGE - (int) yRaw / NOTE_HEIGHT;
        startTick = (int) xRaw;
        
        //Fix raw values.
        int xFixed = (int) xRaw;
        int yFixed = (int) Math.round(yRaw / NOTE_HEIGHT) * NOTE_HEIGHT;
        
        //Reset rectangle to fixed values.
        visualRectangle.setX(xFixed);
        visualRectangle.setY(yFixed);
        visualRectangle.getStyleClass().add(name);
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
            
            latestX = event.getX();
            latestY = event.getY();
            

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
                double translateX = (x - latestX);
                double translateY = (y - latestY);
                sObjMove.move(translateX, translateY);
                latestX = x;
                latestY = y;
            }
            
            if (draggingLength || !sObjMove.isMoveFailed()){
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
                    sObjMove.setLastCoords(latestX, latestY);
                    actionList.add(sObjMove);
                }
                for (SoundObject soundItem : SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
                    soundItem.snapInPlace();
                }
                draggingLength = false;
            }
            
            actionManager.putInUndoStack(actionList);
            
            SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane);
            
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
            sObjMove = new MoveAction(
            SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                    latestX, latestY);
        }
    }

    /**
     * @return the instrument
     */
    public int getInstrument() {
        return instrument;
    }

    /**
     * @return the pitch
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * @return the startTick
     */
    public int getStartTick() {
        return startTick;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }
    
    @Override
    public String objectToXML(){
	String startTag = "<notebar>";
	String endTag = " </notebar>";
	String xstr = " x:";
	String ystr = " y:";
	String widthstr = " width:";
	String inststr = " instrument:";
	
	String x = String.valueOf(this.startTick);
	String y = String.valueOf(this.pitch);
	String instrument = String.valueOf(this.instrument);
	String width = String.valueOf(this.duration);
	
	String result = startTag + xstr + x + ystr + y + widthstr + width + inststr + instrument + endTag;
	
        System.out.println(result);
        
	return result;
    }
}

// Shorten handlers with stategy pattern or abstraction of methods?
package tunecomposer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.ShortMessage;

/**
 * This class creates and edits NoteBar objects to display notes in the tune 
 * and be played in MidiPLayer.
 * 
 * @author Emma Twersky
 */
public class NoteBar extends SoundObject{
    /**
     * Create variables for the note name, instrument number, channel number,
 pitch, starting value, and duration. 
     */
    public final String name;
    public final int instrument;
    public final int channel;
    public int pitch;
    public int startTick;
    public int duration;
    public static final int HEIGHT = 10;
    
    /**
    Creates pane the note is on and the visual rectangle the note is displayed as.
    */
    public Pane pane;

    /**
    * Creates fixed height and set ranges for pitch. 
    */
    private final int pitchRange = 128;
    private final int noteHeight = 10;
    private static final int VOLUME = 127;
    
    /**
    * Sets default duration, minimum duration and default click range to edit duration. 
    */
    private final int defaultLength = 100;
    
    /**
     * Load InstrumentInfo HashMap to look up instrument key values.
     */
    private final InstrumentInfo instrumentInfo = new InstrumentInfo();
        
    /**
     * Initialize the note bar object and variables, then constructs the display
     * and adds the note to the pane.
     * 
     * @param x the top left corner x value of the note clicked
     * @param y the top left corner y value of the note clicked
     */
    public NoteBar(double x, double y, Pane musicPane){
        name = InstrumentToolBarController.selectedInstrument;
        instrument = instrumentInfo.getInstrumentValue(name);
        channel = instrumentInfo.getInstrumentChannel(name);
        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        duration = defaultLength;
                
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        visualRectangle = new Rectangle(xLocation, yLocation, duration, noteHeight);
        visualRectangle.setId(name);

        setHandlers();
                        
        pane = musicPane;
        pane.getChildren().add(visualRectangle);
        select();

//        containedSoundObjects.add(this);
    }
    
     /**
     * Initialize the note bar object and variables, then constructs the 
     * display and doesn't add it to any pane.
     * 
     * @param x the top left corner x value of the note clicked
     * @param y the top left corner y value of the note clicked
     */
    public NoteBar(double x, double y){
        name = InstrumentToolBarController.selectedInstrument;
        instrument = instrumentInfo.getInstrumentValue(name);
        channel = instrumentInfo.getInstrumentChannel(name);
        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        duration = defaultLength;
                
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        visualRectangle = new Rectangle(xLocation, yLocation, duration, noteHeight);
        visualRectangle.setId(name);

        setHandlers();

        select();

//        containedSoundObjects.add(this);
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
     * Fixes note to sit in-between staff lines.
     */
    @Override
    public void snapInPlace() {
        int x = (int) visualRectangle.getX();
        int y = (int) visualRectangle.getY();
        
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        
        int xLocation = (int) x;
        int yLocation = (int) Math.round(y / noteHeight) * noteHeight;
        visualRectangle.setX(xLocation);
        visualRectangle.setY(yLocation);
        visualRectangle.getStyleClass().add(name);
    }
    
    /**
     * Changes note duration.
     * 
     * @param lengthChange the amount to increment the duration
     */
    @Override
    public void changeLength(int lengthChange){ 
        int newLength = duration + lengthChange;
        if (newLength > minNoteLength) {
            duration = newLength;
            visualRectangle.setWidth(duration);
        }
    }
    
    /**
     * Removes note from pane.
     */
    @Override
    public void delete(){
        pane.getChildren().remove(visualRectangle);
    }
    
    /**
     * Adds the note to the given pane. Sets this.pane equal to given pane.
     * Does not manage selection. Does not handle if given pane is null.
     * @param noteBarPane 
     */
    public void addToPane(Pane noteBarPane) {
        pane = noteBarPane;
        pane.getChildren().add(visualRectangle);
    }
    
    
    /**
     * Selects note and displays selection box around note.
     */
    @Override
    public void select(){
        selected = true;
        visualRectangle.getStyleClass().removeAll("unselectedNote");
        visualRectangle.getStyleClass().add("selectedNote");
        SoundObjectPaneController.updateSelectedSoundObjectArray(); 
    }
    
    /**
     * Un-selects note and removes selection box around note.
     */
    @Override
    public void unselect(){
        selected = false;
        visualRectangle.getStyleClass().removeAll("selectedNote");
        visualRectangle.getStyleClass().add("unselectedNote");
        SoundObjectPaneController.updateSelectedSoundObjectArray(); 
    }
    
    /**
     * Switches value of selected.
     */
    @Override
    public void toggleSelection(){
        if (selected) {unselect();}
        else {select();}
    }
    
    @Override
    public void setHandlers() {
        visualRectangle.setOnMousePressed(handleNotePressed);
        visualRectangle.setOnMouseDragged(handleNoteDragged);
        visualRectangle.setOnMouseReleased(handleNoteReleased);
    }
        
  /**
     * Handles note pressed event. 
     * Sets initial pressed values of the mouse and consumes the event.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleNotePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            CompositionPaneController.tunePlayerObj.stop();
            initialX = (int) event.getX();
            initialY = (int) event.getY();
            
            int editLengthMax = (int) visualRectangle.getX() + duration;
            int editLengthMin = editLengthMax - clickToEditLength;
            if ((editLengthMin <= initialX) && (initialX <= editLengthMax)) {
                draggingLength = true;
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
                        
            if (!selected) {
                SoundObjectPaneController.unselectAllSoundObjects(); 
                select();
            }
            
            if (draggingLength) {
                SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((soundItem) -> {
                    soundItem.changeLength(x - initialX);
                });
            }
            else {
                int translateX = (x - initialX);
                int translateY = (y - initialY);
                SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((soundItem) -> {
                    soundItem.move(translateX, translateY);
                });
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
            draggingLength = false;
                        
            if (event.isStillSincePress()) {
                if (event.isControlDown()) {
                    toggleSelection();
                }
                else {
                    SoundObjectPaneController.unselectAllSoundObjects();
                    select();
                }
            }
            
            for (SoundObject soundItem : SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY) {
                soundItem.snapInPlace();
            }
              
            event.consume();
        }
    };

    @Override
    public void addToMidiPlayer(MidiPlayer player) {
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + this.channel, 
                this.instrument, 0, 0, this.channel);
        player.addNote(this.pitch, VOLUME, this.startTick, 
                this.duration, this.channel, 0);
    }
    
}
package tunecomposer;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.MoveAction;
import tunecomposer.actionclasses.LengthChangeAction;

/**
 * This class creates and edits Gesture objects to display notes in the tune 
 * and be played in MidiPLayer.
 * @extends SoundObject
 * 
 */
public final class Gesture extends SoundObject{

    /**
     * Creates the coordinates of the visualRectangle. 
     */
    public double topX;
    public double topY;
    public double bottomX;
    public double bottomY;

    /**
    * An ArrayList of the SoundObjects contained within the Gesture.
    */
    public ArrayList<SoundObject> containedSoundObjects;

    /**
     * ActionManager instance that holds the undo and redo stacks.
     */
    private ActionManager actionManager;
    
    /**
     * Initializes the Gesture object and variables, then constructs the 
     * display.
     */
    public Gesture(){
        visualRectangle = new Rectangle();
        visualRectangle.setMouseTransparent(true);
        containedSoundObjects = new ArrayList();

        SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((sObj) -> {
            containedSoundObjects.add(sObj);
        });
      
        refreshVisualRectangle();
        
        select();
    }
    
    /**
     * Gesture constructor that sets containedObjects equal to given ArrayList
        of SoundObjects instead of copying the SELECTED_SOUNDOBJECT_ARRAY
     * @param selList ArrayList of SoundObjects within this group 
     * @param actionManager assigns reference to actionManager field
     * @param soundObjectPane assigns reference to pane field.
     */
    public Gesture(ArrayList<SoundObject> selList, ActionManager actionManager,
            Pane soundObjectPane) {
        visualRectangle = new Rectangle();
        visualRectangle.setMouseTransparent(true);
        containedSoundObjects = new ArrayList();
      
        containedSoundObjects = new ArrayList();
        containedSoundObjects = (ArrayList<SoundObject>) selList.clone();
        
        this.actionManager = actionManager;
        this.soundObjectPane = soundObjectPane;
        
        refreshVisualRectangle();
        
        select();
    }
    
    /**
     * Refreshes the current coordinates of the visualRectangle display.
     */
    private void refreshVisualRectangle(){
        setVisualRectangleCoords();
        double width = bottomX - topX;
        double height = bottomY - topY;
        visualRectangle.setX(topX);
        visualRectangle.setY(topY);
        visualRectangle.setWidth(width);
        visualRectangle.setHeight(height);
    }
    
    /**
     * Finds and sets the current surrounding coordinates of the Gesture.
     * Sets the current TopX, TopY, BottomX and BottomY.
     */
    public void setVisualRectangleCoords() {
        topX = 2000;
        topY = 1280;
        bottomX = 0;
        bottomY = 0;

        containedSoundObjects.forEach((sObj) -> {
            Rectangle r = sObj.visualRectangle;
            if (topX > r.getX()) {
                topX = r.getX();
            }
            if (topY > r.getY()) {
                topY = r.getY();
            }
            if (bottomX < (r.getX() + r.getWidth())) {
                bottomX = r.getX() + r.getWidth();
            }
            if (bottomY < (r.getY() + r.getHeight())) {
                bottomY = r.getY() + r.getHeight();
            }
        });
    }
    
    /**
     * Sets all notes within this group as selected. 
     */
    @Override
    public void select(){
        this.containedSoundObjects.forEach((note) -> {
            note.select();
        });
        
        selected = true;
        visualRectangle.getStyleClass().removeAll("unselectedGesture");
        visualRectangle.getStyleClass().add("selectedGesture");
        SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane); 
    }
    
    /**
     * Sets all notes within this group as unselected.
     */
    @Override
    public void unselect(){
        this.containedSoundObjects.forEach((note) -> {
            note.unselect();
        });
        
        selected = false;
        visualRectangle.getStyleClass().removeAll("selectedGesture");
        visualRectangle.getStyleClass().add("unselectedGesture");
        SoundObjectPaneController.staticUpdateSelectedArray(soundObjectPane); 
    }
    
    /**
     * Change the selection state of all notes contained within this gesture.
     */
    @Override
    public void toggleSelection(){
        this.containedSoundObjects.forEach((note) -> {
            note.toggleSelection();
        });
    }
    
    /**
     * Returns true if the SoundObject is selected.
     * 
     * @return boolean representing state of selected
     */
    @Override
    public boolean isSelected() {
        return selected;
    }        
    
    /**
     * Shifts all elements of gesture by given increment. 
     * This move includes all SoundObjects.
     * 
     * @param xInc number of horizontal pixels to shift notes
     * @param yInc number of vertical pixels to shift notes
     */
    @Override
    public void move(double xInc, double yInc){
        containedSoundObjects.forEach((note) -> {
            note.move(xInc, yInc);
        });
        refreshVisualRectangle();
    }
    
    /**
     * Checks if moving gesture will push it past the pane's borders.
     * 
     * @param x the "proposed" x move increment.
     * @param y the "proposed" y move increment.
     * @return onEdge is true if the move is illegal and false if its legal.
     */
    @Override
    public boolean isOnEdge(double x, double y){
        for (SoundObject item:containedSoundObjects) {
            if (item.isOnEdge(x, y)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Resizes all notes of gesture by given increment. 
     * Also updates the size of all gestureBoxes.
     * 
     * @param deltaLength number of pixels to change note duration by
     */
    @Override
    public void changeLength(int deltaLength){
        containedSoundObjects.forEach((note) -> {
            note.changeLength(deltaLength);
        });
        refreshVisualRectangle();
    }
    
    /**
     * Recursively snap all items in gesture to closest note pitch.
     * Precondition: the gesture has just been moved.
     * Postcondition: the location of the notes in the gesture is 
     * fixed so they sit in between staff lines.
     */
    @Override
    public void snapYInPlace() {
        containedSoundObjects.forEach((note) -> {
            note.snapYInPlace();
        });
        refreshVisualRectangle();
    }
    
    /**
     * Recursively snap all items in gesture to closest x value.
     * The NoteBars in containedSoundObjects must have references to their rectangles.
     * The location of the notes are snapped so that the x values fall only on
     * increments of the current value of snapXDistance.
     */
    @Override
    public void snapXInPlace() {
        containedSoundObjects.forEach((note) -> {
            note.snapXInPlace();
        });
        refreshVisualRectangle();
    }
    
    /**
     * Sets the visualRectangle's mouse event handlers and all contained notes
     * to the gesture event handlers found at the bottom of the class. 
     */
    @Override
    public void setHandlers() {
        this.visualRectangle.setOnMousePressed(handleGesturePressed);
        this.visualRectangle.setOnMouseDragged(handleGestureDragged);
        this.visualRectangle.setOnMouseReleased(handleGestureReleased);
        for (SoundObject sObj : containedSoundObjects) {
            sObj.setHandlers(handleGesturePressed, handleGestureDragged, 
                    handleGestureReleased);
        }
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
        for (SoundObject sObj : containedSoundObjects) {
            sObj.setHandlers(press, drag, release);
        }
    }
    
    /**
     * Adds the gesture to the given pane. 
     * Does not manage selection. Does not handle if given pane is null. 
     * Precondition: the gesture is not on the pane and the pane is not null.
     * 
     * @param soundObjectPane given to pane to add to.
     */
    @Override
    public void addToPane(Pane soundObjectPane) {
        if (!soundObjectPane.getChildren().contains(visualRectangle)) {
            soundObjectPane.getChildren().add(visualRectangle);
        }
        for (SoundObject sObj : containedSoundObjects) {
            sObj.addToPane(soundObjectPane);
        }
    }
    
    /**
     * Removes the gestureBox and all contained SoundObjects from given pane. 
     * Does not change selection state. Does not handle if given pane is null.
     * Does not handle exceptions if Object can not be removed from given pane.
     * Precondition: the gesture is not on the pane.
     * @param soundObjectPane the pane to remove the gestureBox from
     */
    @Override
    public void removeFromPane(Pane soundObjectPane){    
        containedSoundObjects.forEach((sObj) -> {
            sObj.removeFromPane(soundObjectPane);
        });
        soundObjectPane.getChildren().remove(visualRectangle);
        //TODO need a method to reset handlers of children to what they would be without this group
    }
    
    
    /**
     * Method for recursively grabbing all children of this Gesture. 
     * Returns an ArrayList of all children below this object not including self.
     * @return list of all, as in every level below, the current Gesture.
     *          returns empty ArrayList if no children.
     */
    @Override
    public ArrayList<SoundObject> getAllChildren() {
        ArrayList<SoundObject> allChildren = new ArrayList();
        for (SoundObject sObj : containedSoundObjects) {
            allChildren.addAll(sObj.getAllChildren());
        }
        return allChildren;
    }

    
    /**
     * Adds the gesture to the MidiPlayer. 
     * 
     * @param player given TunePlayer object
     */
    @Override
    public void addToMidiPlayer(MidiPlayer player) {
        this.containedSoundObjects.forEach((sObj) -> {
            sObj.addToMidiPlayer(player);
        });
    }

    /**
     * Resets handlers for all contained items to this top Gesture.
     */
    public void setTopGesture() {
        for (SoundObject sObj : containedSoundObjects) {
            sObj.setHandlers(handleGesturePressed, handleGestureDragged, handleGestureReleased);
            if (sObj.getTopGesture() == null) {
                sObj.setTopGesture(this);
            }
        }
    }

    /**
     * Adds the gestureBox to the given pane and sets all containedItems' 
     * handlers to this Gesture's. 
     * @param soundObjectPane the pane to add the gestureBox to
     */
    public void group(Pane soundObjectPane) {
        refreshVisualRectangle();
        soundObjectPane.getChildren().add(visualRectangle);
        setTopGesture();
    }
    
    
    /**
     * Un-groups the gesture by setting all contained item handlers to correct
     * objects, and removing the gestureBox rectangle from the given pane.
     * Resets the containedSoundObjects to previous event handlers and 
     * removes the visual rectangle. Does not handle being given the wrong pane.
     * @param soundObjectPane the value of soundObjectPane
     */
    public void ungroup(Pane soundObjectPane){    
        soundObjectPane.getChildren().remove(visualRectangle);
        containedSoundObjects.forEach((sObj) -> {
            sObj.setHandlers();
            if (sObj.getTopGesture() == this) {
                sObj.setTopGesture(null);
            }
        });
    }
    
    
    /**
     * Handles gesture pressed event. 
     * Prepares the proper Selection,Unselection, Move, and Stretch Actions
     * depending on the placement of the click, if control is down, and which
     * note(s) were already selected.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleGesturePressed = (MouseEvent event) -> {
        CompositionPaneController.tunePlayerObj.stop();
            
        latestX = event.getX();
        latestY = event.getY();
            
        actionList = new ArrayList();
            
        prepareSelectionAction(event.isControlDown());
            
        prepareMoveOrStretchAction();
        
    };
    
    /**
     * Handles gesture dragged event.
     * Selects and drags to move the gesture or drags to change duration, 
     * based on note click location conventions. Translates gesture and consumes 
     * event. Also updates note lists.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleGestureDragged = (MouseEvent event) -> {
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
        
        if (draggingLength){
            latestX = x;
            latestY = y;
        }
        else if (!sObjMove.isMoveFailed()){
            latestX = x;
            latestY = y;
        }
        event.consume();
    };
    
    /**
     * Handles mouse released.
     * Snaps note into place on lines, handles click note selections
     * based on the control button and consumes event.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleGestureReleased = (MouseEvent event) -> {        
        if (!event.isStillSincePress()) {
            if (draggingLength){
                sObjStretch.setFinalX((int)latestX);
                actionList.add(sObjStretch);
            }
            else {
                sObjMove.setLastCoords(latestX, latestY);
                actionList.add(sObjMove);
            }
            SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((sObj) -> {
                sObj.snapYInPlace();
            });
            draggingLength = false;
        }

        actionManager.putInUndoStack(actionList);
        event.consume();
    };
    
    
    /**
     * Creates a Move or Stretch Action depending on the placement of the
     * initial click.
     * Helper method to the handleGesturePressed event handler.
     */
    @Override
    public void prepareMoveOrStretchAction(){
        draggingLength = isDraggingLength(this);
        
        if (draggingLength) {
            sObjStretch = new LengthChangeAction(
                SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                (int) latestX);
        } 
        else {
            sObjMove = new MoveAction(
                SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY,
                latestX, latestY);
        }
    }
    
    /**
     * Checks whether the Gesture is being clicked in the stretching area of its
     * NoteBars.
     * 
     * @param sGest current Gesture
     * @return boolean; True if clicked in 10 rightmost pixels of the Rectangle,
     *          False otherwise
     */
    private boolean isDraggingLength(Gesture sGest){
        for (SoundObject sObj: sGest.containedSoundObjects){
            if (sObj instanceof Gesture){
                if (isDraggingLength((Gesture)sObj)){
                    return true;
                }
            }
            double editLengthMax =  (sObj.visualRectangle.getX() + sObj.visualRectangle.getWidth());
            double editLengthMin = editLengthMax - clickToEditLength;
            if ((editLengthMin <= latestX) && (latestX <= editLengthMax)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Parser that converts a Gesture to a String to be used in the 
     * Clipboard.
     * 
     * @return String in an XML-like format
     */
    @Override
    public String objectToXML(){
        String result = "";
	String startTag = "<gesture>";
        String endTag = " </gesture>";
        result = result + startTag;
        
        for (SoundObject soundobject: containedSoundObjects) {
            result = result + " " + soundobject.objectToXML();
        }
        
        result = result + endTag;
        return result;
    }
}

package tunecomposer;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import tunecomposer.actionclasses.Action;
import tunecomposer.actionclasses.GroupAction;
import tunecomposer.actionclasses.MoveAction;
import tunecomposer.actionclasses.SelectAction;
import tunecomposer.actionclasses.LengthChangeAction;
import tunecomposer.actionclasses.UnselectAction;

/**
 * This class creates and edits Gesture objects to display notes in the tune 
 * and be played in MidiPLayer.
 * @extends SoundObject
 * 
 * @author Emma Twersky
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
     * @param _actionManager 
     * @param soundObjectPane 
     */
    public Gesture(ArrayList<SoundObject>  selList, ActionManager _actionManager,
            Pane soundObjectPane) {
        visualRectangle = new Rectangle();
        visualRectangle.setMouseTransparent(true);
        containedSoundObjects = new ArrayList();
      
        containedSoundObjects = new ArrayList();
        containedSoundObjects = (ArrayList<SoundObject>) selList.clone();
        
        actionManager = _actionManager;
        pane = soundObjectPane;
        
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(pane); 
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(pane); 
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
     * Returns whether object is selected.
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
     * @param x number of horizontal pixels to shift notes
     * @param y number of vertical pixels to shift notes
     */
    @Override
    public void move(double x, double y){
        containedSoundObjects.forEach((note) -> {
            note.move(x, y);
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
     * @param l number of pixels to change note duration by
     */
    @Override
    public void changeLength(int l){
        containedSoundObjects.forEach((note) -> {
            note.changeLength(l);
        });
        refreshVisualRectangle();
    }
    
    /**
     * Recursively snap all items in gesture to closest note.
     */
    @Override
    public void snapInPlace() {
        containedSoundObjects.forEach((note) -> {
            note.snapInPlace();
        });
        refreshVisualRectangle();
    }
    
    /**
     * Sets the visualRectangle's mouse event handlers. 
     */
    @Override
    public void setHandlers() {
        visualRectangle.setOnMousePressed(handleGesturePressed);
        visualRectangle.setOnMouseDragged(handleGestureDragged);
        visualRectangle.setOnMouseReleased(handleGestureReleased);
        containedSoundObjects.forEach((sObj) -> {
            sObj.visualRectangle.setOnMousePressed(handleGesturePressed);
            sObj.visualRectangle.setOnMouseDragged(handleGestureDragged);
            sObj.visualRectangle.setOnMouseReleased(handleGestureReleased);
        });
    }
    
    public void isEqual(GroupAction groupAction) {
        
    }
    

    @Override
    public void addToPane(Pane soundObjectPane) {
        soundObjectPane.getChildren().add(visualRectangle);
        for (SoundObject sObj : containedSoundObjects) {
            sObj.addToPane(soundObjectPane);
        }
    }
    
    /**
     * Removes the gestureBox and all contained SoundObjects from given pane. 
     * Does not change selection state. Does not handle if given pane is null.
     * Does not handle exceptions if Object can not be removed from given pane.
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
     * Adds the gestureBox to the given pane and sets all containedItems' 
     * handlers to this Gesture's. 
     * Does not change selection state. Does not handle if given pane is null. 
     * Does not handle exceptions if Object can not be removed from given pane.
     * @param soundObjectPane the pane to add the gestureBox to
     */
    public void group(Pane soundObjectPane) {
//        setHandlers();
        refreshVisualRectangle();
        soundObjectPane.getChildren().add(visualRectangle);
        for (SoundObject sObj : containedSoundObjects) {
            sObj.setHandlers();
            if (sObj.getTopGesture() == null) {
                sObj.setTopGesture(this);
            }
        }
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
     * Handles note pressed event. 
     * Sets initial pressed values of the mouse and consumes the event.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleGesturePressed = (MouseEvent event) -> {
        CompositionPaneController.tunePlayerObj.stop();
        actionList = new ArrayList();

        latestX = event.getX();
        latestY = event.getY();
        ArrayList<SoundObject> thisGesture = new ArrayList();
        thisGesture.add((SoundObject) visualRectangle.getUserData());
        
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
            selectAction = new SelectAction(thisGesture);
            selectAction.execute();
            actionList.add(selectAction);
        }
        else if (event.isControlDown()){
            unselectAction = new UnselectAction(thisGesture);
            unselectAction.execute();
            actionList.add(unselectAction);
        }
        
        containedSoundObjects.forEach((sObj) -> {
            double editLengthMax =  (sObj.visualRectangle.getX() + sObj.visualRectangle.getWidth());
            double editLengthMin = editLengthMax - clickToEditLength;
            if ((editLengthMin <= latestX) && (latestX <= editLengthMax)) {
                draggingLength = true;
            }
        });
        
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
    };
    
    /**
     * Handles note dragged event.
     * Selects and drags to move the note or drags to change duration, 
     * based on note click location conventions. Translates note and consumes 
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
        
        
        latestX = x;
        latestY = y;
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
                sObj.snapInPlace();
            });
            draggingLength = false;
        }

        actionManager.putInUndoStack(actionList);
        event.consume();
    };
}

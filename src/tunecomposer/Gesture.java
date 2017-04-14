package tunecomposer;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public final class Gesture extends SoundObject{

    public Pane pane;
         
    public double topX;
    public double topY;
    public double bottomX;
    public double bottomY;
    
    Gesture(Pane musicPane){
        visualRectangle = new Rectangle();
        visualRectangle.setMouseTransparent(true);
        containedSoundObjects = new ArrayList();
        
        SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((sObj) -> {
            containedSoundObjects.add(sObj);
        });
        setHandlers();
      
        refreshVisualRectangle();
        pane = musicPane;
        pane.getChildren().add(visualRectangle);
        
        select();
    }
    
    /**
     * Refreshes the current coordinates of the visualRectangle.
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
     */
    public void setVisualRectangleCoords() {
        topX = 2000;
        topY = 1280;
        bottomX = 0;
        bottomY = 0;

        for (SoundObject sObj : containedSoundObjects){
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
        }
    }
    
    /**
     * Change the selection state of all notes contained within this gesture.
     * Will not set them all to the same value, only negates each current
     * state. 
     */
    @Override
    public void toggleSelection(){
        this.containedSoundObjects.forEach((note) -> {
            note.toggleSelection();
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(); 
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
        SoundObjectPaneController.updateSelectedSoundObjectArray(); 
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
     * Shifts all elements of gesture by given increment. This move includes notes
     * and gestureBoxes.
     * 
     * @param x number of horizontal pixels to shift notes
     * @param y number of vertical pixels to shift notes
     */
    @Override
    public void move(int x, int y){
        containedSoundObjects.forEach((note) -> {
            note.move(x, y);
        });
        refreshVisualRectangle();
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
     * Prepares to un-group the gesture.
     */
    public void ungroup(){    
        pane.getChildren().remove(visualRectangle);
        for (SoundObject sObj : containedSoundObjects) {
            sObj.setHandlers();
        }
    }
    
    /**
     * Delete all items in this gesture and the border surrounding them.
     */
    @Override
    public void delete(){    
        containedSoundObjects.forEach((note) -> {
            note.delete();
        });
        pane.getChildren().remove(visualRectangle);
    }
    
    @Override
    public void addToMidiPlayer(MidiPlayer player) {
        this.containedSoundObjects.forEach((sObj) -> {
            sObj.addToMidiPlayer(player);
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
        initialX = (int) event.getX();
        initialY = (int) event.getY();
        
        for (SoundObject sObj : containedSoundObjects) {
            int editLengthMax = (int) (sObj.visualRectangle.getX() + sObj.visualRectangle.getWidth());
            int editLengthMin = editLengthMax - clickToEditLength;
            if ((editLengthMin <= initialX) && (initialX <= editLengthMax)) {
                draggingLength = true;
            }
        }
        
        event.consume();
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
        int x = (int) event.getX();
        int y = (int) event.getY();
        
        if (!selected) {
            SoundObjectPaneController.unselectAllSoundObjects();
            select();
        }
        
        if (draggingLength) {
            SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((sObj) -> {
                sObj.changeLength(x - initialX);
            });
        }
        else {
            int translateX = (x - initialX);
            int translateY = (y - initialY);
            SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((sObj) -> {
                sObj.move(translateX, translateY);
            });
        }
        
        initialX = x;
        initialY = y;
        
        event.consume();
    };
    
    /**
     * Handles mouse released.
     * Snaps note into place on lines, handles click note selections
     * based on the control button and consumes event.
     * 
     * @param event the mouse click event
     */
    EventHandler<MouseEvent> handleGestureReleased = new EventHandler<MouseEvent>() {
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

            SoundObjectPaneController.SELECTED_SOUNDOBJECT_ARRAY.forEach((sObj) -> {
                sObj.snapInPlace();
            });

            event.consume();
        }
    };

    @Override
    public void setHandlers() {
        this.containedSoundObjects.forEach((sObj) -> {
            sObj.visualRectangle.setOnMousePressed(handleGesturePressed);
            sObj.visualRectangle.setOnMouseDragged(handleGestureDragged);
            sObj.visualRectangle.setOnMouseReleased(handleGestureReleased);
        });
    }

    
    /**
     * Reference to pane that the gesture should be added to. Useful for action
     * executions.
     * @param paneToAddTo 
     */
    @Override
    public void addToPane(Pane paneToAddTo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
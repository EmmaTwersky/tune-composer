/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;
import java.util.ArrayList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
/**
 *
 * @author lonbern
 */
public class Gesture extends SoundObject{
    
    
    /**
     * Array of SoundObjects that can include Gestures or NoteBars. Does not contain
     * all elements within the gesture, only ungrouped notes and outermost gestures.
     */
    private ArrayList<SoundObject> itemsInGesture = new ArrayList<SoundObject>();
    /**
     * The gesture that this is contained within. If null, then this has no 
     * encapsulating gesture.
     */
    private Gesture parentGesture;
    private boolean selected;
    
    @FXML
    public Pane compositionPane;
    
    
    Gesture(ArrayList<SoundObject> selectedItems, Pane compositionPane){
        for (SoundObject item : selectedItems){
            item.setParentGesture(this);
            itemsInGesture.add(item);
        }
        
        this.makeGestureBox();
        compositionPane.getChildren().add(rectangleVisual);
        
        select();
    }
    
    /**
     * Creates gestureBox rectangle using dimensions of objects in itemsInGesture.
     * 
     */
    private void makeGestureBox(){
        //rectangleVisual = new Rectangle(1,1,1,1);
        int left = findLeftMostCord();
        int right = findRightMostCord();
        int top = findTopMostCord();
        int bott = findBottomMostCord();
        int width = right - left;
        int height = bott - top;
        rectangleVisual = new Rectangle(left, top, width, height);
    }
    
    /**
     * Change the selection state of all notes contained within this gesture.
     * Will not set them all to the same value, only negates each note's current
     * state. 
     */
    @Override
    public void toggleSelection(){
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).toggleSelection();
        }
    }
    
    
    /**
     * Sets all notes within this group as selected. 
     */
    @Override
    public void select(){
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).select();
        }
        
        selected = true;
        rectangleVisual.getStyleClass().removeAll();
        rectangleVisual.getStyleClass().add("selectedGesture");
    }
    
    
    /**
     * Sets all notes within this group as unselected
     */
    @Override
    public void unselect(){
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).unselect();
        }
        
        selected = false;
        rectangleVisual.getStyleClass().removeAll();
        rectangleVisual.getStyleClass().add("unselectedGesture");
    }
    
    
    /**
     * returns whether object is selected.
     * @return boolean representing state of selected
     */
    @Override
    public boolean isSelected() {
        return selected;
    }
    
    
    /**
     * returns array of all first-depth items of this gesture.
     * @return 
     */
    public ArrayList<SoundObject> getItemsInGesture() {
        return itemsInGesture;
    }
    
    
    /**
     * Prepares for destruction of this object.
     * Sets parentGesture of all objects in itemsInGesture to null. Deletes 
     * gestureBox rectangle. No reference of this obj will exist.
     */
    public void ungroup(){
        for (SoundObject item : itemsInGesture){
            item.setParentGesture(null);
        }
        compositionPane.getChildren().remove(rectangleVisual);
    }
    
    
    /**
     * Shifts all elements of gesture by given increment. This move includes notes
     * and gestureBoxes.
     * 
     * @param xInc number of horizontal pixels to shift notes
     * @param yInc number of vertical pixels to shift notes
     */
    @Override
    public void move(int xInc, int yInc){
        for (SoundObject item : itemsInGesture){
            item.move(xInc, yInc);
        }
        makeGestureBox();
    }
    
    /**
     * Resizes all notes of gesture by given increment. 
     * Also updates the size of all gestureBoxes.
     * 
     * @param xInc number of pixels to change note duration by
     */
    @Override
    public void changeLength(int inc){
        System.out.println(itemsInGesture);
        for (SoundObject item : itemsInGesture){
            item.changeLength(inc);
        }
        makeGestureBox();
        System.out.println(itemsInGesture);
    }
    
    /**
     * Sets parentGesture field to given value. If null, then sets field to null.
     * Useful for updating relationships of gestures and creating the hierarchy.
     * Not for referencing the outer-most Gesture. 
     * 
     * @param parent the Gesture object that this object has been grouped in.
     */
    @Override
    public void setParentGesture(Gesture parent){
        parentGesture = parent;
    }
    
    
    /**
     * Returns this objects parentGesture.
     */
    @Override
    public Gesture getParentGesture(){
        return parentGesture;
    }
    
    
    /**
     * Recursiveley snap all items in gesture to closest note.
     * @param x
     * @param y 
     */
    @Override
    public void snapInPlace(double x, double y) {
        for (SoundObject item : itemsInGesture){
            item.snapInPlace(x, y);
        }
        makeGestureBox();
    }
    
    /**
     * Recursiveley snap all items in gesture to closest note
     */
    @Override
    public void snapInPlace() {
        for (SoundObject item : itemsInGesture){
            item.snapInPlace();
        }
        makeGestureBox();
    }
    
    
    /**
     * delete all items in this gesture and the border surrounding them.
     */
    @Override
    public void delete(){    
        for (SoundObject item : itemsInGesture){
            item.delete();
        }
        compositionPane.getChildren().remove(rectangleVisual);
    }
    
    
    /**
     * find x value of the rightmost side of the farthest right note.
     * @return 
     */
    @Override
    public int findRightMostCord() {
        int rightMost = 0;
        for (SoundObject item : itemsInGesture){
            int itemRightmost = item.findRightMostCord();
            if (itemRightmost > rightMost) {
                rightMost = itemRightmost;
            }
        }
        return rightMost;
    }
    
    
    /**
     * Find the leftmost coordinate out of all rectangles contained within
     * gesture.
     * @return 
     */
    @Override
    public int findLeftMostCord() {
        int leftMost = 0;
        for (SoundObject item : itemsInGesture){
            int itemLeftmost = item.findLeftMostCord();
            if (itemLeftmost < leftMost) {
                leftMost = itemLeftmost;
            }
        }
        return leftMost;
    }

    
    /**
     * Find topmost coordinate out of all rectangles contained within this gesture.
     * @return 
     */
    @Override
    public int findTopMostCord() {
        int topMost = 0;
        for (SoundObject item : itemsInGesture){
            int itemTopmost = item.findTopMostCord();
            if (itemTopmost < topMost) {
                topMost = itemTopmost;
            }
        }
        return topMost;
    }
    
    
    /**
     * Find bottom-most coordinate out of all rectangles contained within this
     * gesture.
     * @return 
     */
    @Override
    public int findBottomMostCord() {
        int bottomMost = 0;
        for (SoundObject item : itemsInGesture){
            int itemBottomMost = item.findBottomMostCord();
            if (itemBottomMost > bottomMost) {
                bottomMost = itemBottomMost;
            }
        }
        return bottomMost;
    }
    
    
    /**
     *
     * @param vol
     * @param player
     */
    @Override
    public void addToMidi(int vol, MidiPlayer player) {
        for (SoundObject item : itemsInGesture){
            item.addToMidi(vol, player);
        }

    }
    
    
    /**
     * Find and return the most encapsulating gesture to this note.
     * @return 
     */
    public SoundObject getTopParentGesture() {
        SoundObject tmp = this;
        while (tmp.getParentGesture() != null) {
            tmp = tmp.getParentGesture();
        }
        return tmp;
    }
}
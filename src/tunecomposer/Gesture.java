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
    
    private ArrayList<SoundObject> itemsInGesture = new ArrayList<SoundObject>();
    private Gesture parentGesture;
    private boolean selected;
    
    @FXML
    public Pane compositionPane;
    
    
    Gesture(ArrayList<SoundObject> selectedItems, Pane compositionPane){
        for (SoundObject item : selectedItems){
            item.setParentGesture(this);
            itemsInGesture.add(item);
        }
        selected = true;
        
        this.makeGestureBox();
        compositionPane.getChildren().add(rectangleVisual);
    }
    
    
    private void makeGestureBox(){
        //rectangleVisual = new Rectangle(1,1,1,1);
        int left = findLeftMostCord();
        int right = findRightMostCord();
        int top = findTopMostCord();
        int bott = findBottomMostCord();
        int width = right - left;
        int height = bott - top;
        rectangleVisual = new Rectangle(left, top, width, height);
        select();
    }
    
    
    @Override
    public void toggleSelection(){
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).toggleSelection();
        }
    }
    
    @Override
    public void select(){
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).select();
        }
        
        selected = true;
        rectangleVisual.getStyleClass().removeAll();
        rectangleVisual.getStyleClass().add("selectedGesture");
    }
    
    
    @Override
    public void unselect(){
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).unselect();
        }
        
        selected = false;
        rectangleVisual.getStyleClass().removeAll();
        rectangleVisual.getStyleClass().add("unselectedGesture");
    }
    
    
    @Override
    public boolean isSelected() {
        return selected;
    }
    
    
    public void ungroup(){
        for (SoundObject item : itemsInGesture){
            item.setParentGesture(null);
        }
        compositionPane.getChildren().remove(rectangleVisual);
    }
    
    
    @Override
    public void move(int xInc, int yInc){
        for (SoundObject item : itemsInGesture){
            item.move(xInc, yInc);
        }
    }
    
    
    @Override
    public void changeLength(int inc){
        for (SoundObject item : itemsInGesture){
            item.changeLength(inc);
        }
    }
    
    
    @Override
    public void setParentGesture(Gesture parent){
        parentGesture = parent;
    }
    
    
    @Override
    public Gesture getParentGesture(){
        return parentGesture;
    }
    
    
    @Override
    public void snapInPlace(double x, double y) {
        for (SoundObject item : itemsInGesture){
            item.snapInPlace(x, y);
        }
    }
    
    
    @Override
    public void delete(){
        for (SoundObject item : itemsInGesture){
            item.delete();
        }
        compositionPane.getChildren().remove(rectangleVisual);
    }
    
    
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
    
}
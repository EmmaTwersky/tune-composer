/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
/**
 *
 * @author lonbern
 */
public class Gesture extends SoundObject{
    
    private ArrayList<SoundObject> itemsInGesture = new ArrayList<SoundObject>();
    private Rectangle rectangleVisual;
    private Gesture parentGesture;
    private boolean selected;
    
    
    Gesture(ArrayList<SoundObject> selectedItems){
        for (SoundObject item : selectedItems){
            
            item.setParentGesture(this);
            
            itemsInGesture.add(item);
        }
        selected = true;
        
        this.makeGestureBox();
                
    }
    
    
    private void makeGestureBox(){
        
    }
    
    
    public void toggleSelection(){
        
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).toggleSelection();
        }
           
    }
    
    public void select(){
        
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).select();
        }
        
        rectangleVisual.getStyleClass().removeAll();
        rectangleVisual.getStyleClass().add("selectedGesture");
        
    }
    
    
    public void unselect(){
        
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).unselect();
        }
        
        rectangleVisual.getStyleClass().removeAll();
        rectangleVisual.getStyleClass().add("unselectedGesture");
        
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void ungroup(){
        
    }
    
    public void move(int xInc, int yInc){
        
    }
    
    
    public void changeLength(int inc){
        
    }
    
    public void setParentGesture(Gesture parent){
        parentGesture = parent;
    }
    
    public Gesture getParentGesture(){
        return parentGesture;
    }
    
    public void snapInPlace(double x, double y) {
        for (SoundObject item : itemsInGesture){
            item.snapInPlace(x, y);
        }
    }
    
    public void delete(){
        
    }
    
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
    
}
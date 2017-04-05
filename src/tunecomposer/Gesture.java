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
public class Gesture implements SoundObject{
    
    private ArrayList<SoundObject> itemsInGesture = new ArrayList<SoundObject>();
    private Rectangle gestureBox;
    private Gesture parentGesture;
    
    
    Gesture(ArrayList<SoundObject> selectedItems){
        for (SoundObject item : selectedItems){
            
            item.setParentGesture(this);
            
            itemsInGesture.add(item);
        }
        
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
        
        gestureBox.getStyleClass().removeAll();
        gestureBox.getStyleClass().add("selectedGesture");
        
    }
    
    
    public void unselect(){
        
        for (int i=0; i<itemsInGesture.size();i++){
            itemsInGesture.get(i).unselect();
        }
        
        gestureBox.getStyleClass().removeAll();
        gestureBox.getStyleClass().add("unselectedGesture");
        
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
    
    
}
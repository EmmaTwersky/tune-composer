/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

/**
 *
 * @author vivancr
 */
public interface SoundObject {
    
    public void select();
    public void unselect();
    public void toggleSelection();
    public void setParentGesture(Gesture parent);
    public Gesture getParentGesture();
    public void move(int x, int y);
    public void changeLength(int lengthInc);
    
}

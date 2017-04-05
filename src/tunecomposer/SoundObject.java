/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author vivancr
 */
public abstract class SoundObject {
    
    public Rectangle rectangleVisual;
    
    public abstract void select();
    public abstract void unselect();
    public abstract void toggleSelection();
    public abstract boolean isSelected();
    public abstract void setParentGesture(Gesture parent);
    public abstract Gesture getParentGesture();
    public abstract void move(int x, int y);
    public abstract void delete();
    public abstract void changeLength(int lengthInc);
    public abstract void snapInPlace(double x, double y);
    public abstract int findRightMostCord();
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author EmmaTwersky
 */
public class NoteBar {
    public final int instrument;
    public int pitch;
    public int startTick;
    public int noteLength;
    
    public Rectangle noteDisplay;

    private final int pitchRange = 128;
    private final int noteHeight = 10;
    private int defaultLength = 100;
    
    NoteBar(int instrum, double x, double y){
        instrument = instrum;
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        noteLength = defaultLength;
        noteDisplay = new Rectangle((int) x, (int) Math.round(y/10)*10, noteLength, noteHeight);
        noteDisplay.setId("noteBar");
    }
    
    public void display(Pane pane){
        pane.getChildren().add(noteDisplay);        
    }
    
    public void editNote(){
        
    }
    
    
}

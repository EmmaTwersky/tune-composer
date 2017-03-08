/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author EmmaTwersky
 */
public class NoteBar {
    public final String name;
    public final int instrument;
    public final int channel;
    public int pitch;
    public int startTick;
    public int length;
    public boolean selected = true;
    
    public Rectangle noteDisplay;

    private final int pitchRange = 128;
    private final int noteHeight = 10;
    private final int defaultLength = 100;
    
    /**
     * Load InstrumentSelection HashMap to look up instrument key values.
     */
    private final InstrumentSelection instrumentInfo = new InstrumentSelection();
    
    NoteBar(String instrumName, double x, double y){
        name = instrumName;
        instrument = instrumentInfo.getInstrumentValue(instrumName);
        channel = instrumentInfo.getInstrumentChannel(instrumName+"Channel");
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        length = defaultLength;
        noteDisplay = new Rectangle((int) x, (int) Math.round(y/noteHeight)* noteHeight, length, noteHeight);
        noteDisplay.setId(name);
        selected = true;
    }
    
    public void displayNewNote(Pane pane){
        noteDisplay.setStroke(Color.AQUA);
        //noteDisplay.setId("selectedNote");
        pane.getChildren().add(noteDisplay);
    }
    
    public boolean isSelected(){
        return selected;
    }

    public void editNote(Pane pane, double x, double y, double noteLength){
        pane.getChildren().remove(noteDisplay);
        pitch = pitchRange - (int) y / noteHeight;
        startTick = (int) x;
        length = (int) noteLength;
        noteDisplay = new Rectangle((int) x, (int) Math.round(y/10)*10, length, noteHeight);
        pane.getChildren().add(noteDisplay);
    }
    
    public void deleteNote(Pane pane){
        pane.getChildren().remove(noteDisplay);
    }
    
    public void selectNote(Pane pane){
        selected = true;
        pane.getChildren().remove(noteDisplay);
        noteDisplay.setStroke(Color.AQUA);
        //noteDisplay.setId("selectedNote");
        pane.getChildren().add(noteDisplay);
    }
    
    public void unselectNote(Pane pane){
        selected = false;
        pane.getChildren().remove(noteDisplay);
        noteDisplay.setStroke(Color.GREY);
        noteDisplay.setId(name);
        pane.getChildren().add(noteDisplay);
    }
    
    public void toggleNoteSelection(Pane pane){
        if (selected) {unselectNote(pane);}
        else {selectNote(pane);}
    }
}

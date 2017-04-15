package tunecomposer;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This class stores and controls the notes and creates the MidiPlayer to 
 * manipulate the tune.
 *
 * @author EmmaTwersky
 */
public class TunePlayer {        
    /**
     * One midi player is used throughout, so that it can be stopped.
     * Set resolution to 100 and beats per minute to 60.
     */
    private final int resolution = 100;
    private final int beatsPerMinute = 60;
    private final MidiPlayer player;
    
    TunePlayer() {
        this.player = new MidiPlayer(resolution, beatsPerMinute);
    }
    
    /**
     * Plays the sequence and begins the RedBar animation.
     * 
     * @param soundObjectPane
     */
    protected void play(Pane soundObjectPane) {
        playSequence(soundObjectPane);
    }
    
    /**
     * Plays the MidiPLayer.
     * Each time playSequence is called the sequence clears, adds all notes 
     * to the player, and starts from the beginning.
     * 
     * @param soundObjectPane
     */
    protected void playSequence(Pane soundObjectPane) {
        player.stop();
        player.clear();
        populateMidiPlayer(soundObjectPane);
        player.play();
    }
    
    /**
     * Adds all NoteBar objects in musicNotesArray to MidiPlayer.
     */
    private void populateMidiPlayer(Pane soundObjectPane) {
        soundObjectPane.getChildren().forEach((sObj) -> {
            ((SoundObject) ((Rectangle) sObj).getUserData()).addToMidiPlayer(player);
        });
    }
    
    /**
     * Stops the current MidiPlayer, clears MidiPlayer and stops RedBar.
     */
    public void stop() {
        player.stop();
        player.clear();
    }
}

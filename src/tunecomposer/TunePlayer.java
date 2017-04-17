package tunecomposer;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This class stores and controls the notes and creates the MidiPlayer to 
 * manipulate the tune.
 */
public class TunePlayer {    
    
    /**
     * One midi player is used throughout, so that it can be stopped.
     * Set resolution to 100 and beats per minute to 60.
     */
    private static final int RESOLUTION = 100;
    private static final int BEATS_PER_MINUTE = 60;
    private final MidiPlayer player;
    
    TunePlayer() {
        this.player = new MidiPlayer(RESOLUTION, BEATS_PER_MINUTE);
    }
    
    /**
     * Plays the SoundObjects on the given pane.
     * 
     * @param soundObjectPane pane the SoundObjects are kept in
     */
    protected void play(Pane soundObjectPane) {
        playSequence(soundObjectPane);
    }
    
    /**
     * Plays the MidiPLayer.
     * Each time playSequence is called the current sequence clears, all SoundObjects 
     * in given pane are added to the player, and player starts from the beginning.
     * 
     * @param soundObjectPane pane that contains the SoundObjects
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
            Rectangle r = (Rectangle) sObj;
            SoundObject s = (SoundObject) r.getUserData();
            s.addToMidiPlayer(player);
        });
    }
    
    /**
     * Stops the current MidiPlayer, clears MidiPlayer.
     */
    public void stop() {
        player.stop();
        player.clear();
    }
}
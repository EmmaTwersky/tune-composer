package tunecomposer;

import java.util.List;
import javafx.scene.Node;
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
    public static final int RESOLUTION = 20;
    public static int beatsPerMinute = 240;
    private final MidiPlayer player;
    
    TunePlayer() {
        this.player = new MidiPlayer(RESOLUTION, beatsPerMinute);
    }
    
    
    /**
     * Plays the MidiPLayer.
     * Each time play is called the current sequence clears, all SoundObjects 
     * in given pane are added to the player, and player starts from the startTick.
     * 
     * @param soundObjsToPlay pane that contains the SoundObjects
     * @param startTick where the player begins to play from.
     */
    public void play(List<Node> soundObjsToPlay, long startTick) {
        player.stop();
        player.clear();
        populateMidiPlayer(soundObjsToPlay);
        updateBPM();
        player.play(startTick);
    }
    
    /**
     * Adds given, all or selected, NoteBar objects to MidiPlayer.
     * 
     * @param soundObjsToPlay
     */
    private void populateMidiPlayer(List<Node> soundObjsToPlay) {
        for (Node sObj: soundObjsToPlay){
            Rectangle r = (Rectangle) sObj;
            SoundObject s = (SoundObject) r.getUserData();
            s.addToMidiPlayer(player);
        }
    }
    
    /**
     * Stops the current MidiPlayer, clears MidiPlayer.
     */
    public void stop() {
        player.stop();
        player.clear();
    }
    
    /**
     * Updates the BPM in the midi player according to the most recent user input.
     */
    private void updateBPM() {
        player.changeBPM(beatsPerMinute);
    }
}

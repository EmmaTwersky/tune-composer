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
    private static final int RESOLUTION = 100;
    private static final int BEATS_PER_MINUTE = 60;
    private final MidiPlayer player;
    
    TunePlayer() {
        this.player = new MidiPlayer(RESOLUTION, BEATS_PER_MINUTE);
    }
    
    
    /**
     * Plays the MidiPLayer.
     * Each time playSequence is called the current sequence clears, all SoundObjects 
     * in given pane are added to the player, and player starts from the beginning.
     * 
     * @param soundObjsToPlay pane that contains the SoundObjects
     */
    public void play(List<Node> soundObjsToPlay, long startTick) {
//        updateBPM();
        player.stop();
        player.clear();
        populateMidiPlayer(soundObjsToPlay);
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
}
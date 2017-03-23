package tunecomposer;

import javafx.scene.layout.Pane;
import javax.sound.midi.ShortMessage;

/**
 * This class stores and controls the notes and creates the MidiPlayer to 
 * manipulate the tune.
 *
 * @author EmmaTwersky
 */
public class TunePlayer {    
    /**
     * Set volume to maximum.      
     */
    private static final int VOLUME = 127;
    
    /**
     * Initialize a RedBar to track the progression of time.
     */
    private final RedBar redBarObj;
    
    /**
     * One midi player is used throughout, so that it can be stopped.
     * Set resolution to 100 and beats per minute to 60.
     */
    private final int resolution = 100;
    private final int beatsPerMinute = 60;
    private final MidiPlayer player;
    
    TunePlayer(Pane compositionPane) {
        this.player = new MidiPlayer(resolution, beatsPerMinute);
        redBarObj = new RedBar(compositionPane);
    }
    
    /**
     * Plays the sequence and begins the RedBar animation.
     */
    protected void play() {
        playSequence();
        redBarObj.playAnimation(CompositionPaneController.MUSIC_NOTES_ARRAY);
    }
    
    /**
     * Plays the MidiPLayer.
     * Each time playSequence is called the sequence clears, adds all notes 
     * to the player, and starts from the beginning.
     */
    protected void playSequence() {
        player.stop();
        player.clear();
        addNotesArrayToMidiPlayer();
        player.play();
    }
    
    /**
     * Adds all NoteBar objects in musicNotesArray to MidiPlayer.
     */
    private void addNotesArrayToMidiPlayer() {
        for (NoteBar note: CompositionPaneController.MUSIC_NOTES_ARRAY) {            
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + note.channel, 
                    note.instrument, 0, 0, note.channel);
            player.addNote(note.pitch, VOLUME, note.startTick, 
                    note.length, note.channel, 0);
        }
    }
    
    /**
     * Stops the current MidiPlayer, clears MidiPlayer and stops RedBar.
     */
    public void stop() {
        player.stop();
        player.clear();
        redBarObj.stopAnimation();
    } 
}

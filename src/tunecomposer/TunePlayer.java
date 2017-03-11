package tunecomposer;

import java.util.ArrayList;
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
     * Create array of NoteBar objects and selected NoteBar objects.      
     */
    protected static final ArrayList<NoteBar> MUSIC_NOTES_ARRAY = new ArrayList(); 
    protected static final ArrayList<NoteBar> SELECTED_NOTES_ARRAY = new ArrayList();
    
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
     * Fills the selectedNotesArray with the currently selected notes.
     */
    public static void updateSelectedNotesArray(){
        SELECTED_NOTES_ARRAY.clear();
        for (NoteBar note: MUSIC_NOTES_ARRAY) {            
            if (note.isSelected()) {
                note.selectNote();
                SELECTED_NOTES_ARRAY.add(note);
            }
        }
    }
    
    /**
     * Empties the selectedNotesArray and un-selects all notes.
     */
    public static void resetSelectedNotesArray(){
        for (NoteBar note: SELECTED_NOTES_ARRAY) {            
            note.unselectNote();
        }
        SELECTED_NOTES_ARRAY.clear();
    }
    
    /**
     * Plays the sequence and begins the RedBar animation.
     */
    protected void play() {
        playSequence();
        redBarObj.playAnimation(MUSIC_NOTES_ARRAY);
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
        for (NoteBar note: MUSIC_NOTES_ARRAY) {            
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

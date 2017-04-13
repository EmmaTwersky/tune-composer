package tunecomposer;

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
     */
    protected void play() {
        playSequence();
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
        for (SoundObject sObj: SoundObjectPaneController.SOUNDOBJECT_ARRAY) {
            sObj.addToMidiPlayer(player);
        }
    }
    
    /**
     * Stops the current MidiPlayer, clears MidiPlayer and stops RedBar.
     */
    public void stop() {
        player.stop();
        player.clear();
    }
}

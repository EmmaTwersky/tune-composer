/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.ShortMessage;

/**
 * @author Emma
 */
public class FXMLController implements Initializable{
    
    /**
     * Set volume to maximum.      
     */
    private static final int VOLUME = 127;
    
    /**
     * Create array of NoteBar objects and selected NoteBar objects.      
     */
    private ArrayList<NoteBar> musicNotesArray = new ArrayList(); 
    private ArrayList<NoteBar> selectedNotesArray = new ArrayList(); 
    
    /**
     * Initialize default note to "Piano" with length to 100 pixels.      
     */
    private static String selectedInstrument = "Piano";
    private static int noteLength = 100;
    
    /**
     * Set note height to 10 pixels, this is final.      
     */
    private final int noteHeight = 10; 
    private final int barLength = 100;
    private final int pitchRange = 128;
    private final int barRange = 20;
    
    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    private final int resolution = 100;
    private final int beatsPerMinute = 60;
    private final MidiPlayer player = new MidiPlayer(resolution, beatsPerMinute);
    
    /**
     * Create the pane for drawing.      
     */
    @FXML
    private Pane compositionPane;

    /**
     * Initialize a RedBar to track the progression of time.
     */
    private RedBar redBarObj; // = new RedBar(compositionPane);
    
    /**
     * Initialized with our FXML, draws initial setup of composition pane.
     * @param location
     * @param resources
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        for (int i = 0; i < pitchRange; i++) {
            Line staffLine = new Line(0, i*noteHeight, barRange*barLength, i*noteHeight);
            staffLine.setId("staffLine");
            compositionPane.getChildren().add(staffLine);
        }
        for (int i = 0; i < barRange; i++) {
            Line measureLine = new Line(i*barLength, 0, i*barLength, pitchRange*noteHeight);
            measureLine.setId("measureLine");
            compositionPane.getChildren().add(measureLine);
        }
        redBarObj = new RedBar(compositionPane);
    }
    
    /**
     * Handles the play button from the Actions menu.
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePlayScaleButtonAction(ActionEvent event) {
        play();
    }  
    
    /**
     * Plays the sequencer and starts the RedBar animation.
     */
    protected void play() {
        playSequence();
        redBarObj.playAnimation(musicNotesArray);
    }
    
    /**
     * Plays the sequencer.
     * Each time playSequence is called the sequence restarts from the beginning.
     */
    protected void playSequence() {
        player.stop();
        //player.restart();
        player.clear();
        addNotesArrayToMidiPlayer();
        player.play();
    }
    
    /**
     * Adds all NoteBar objects in musicNotesArray to MidiPlayer.
     */
    private void addNotesArrayToMidiPlayer() {
        for (NoteBar note: musicNotesArray) {            
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + note.channel, note.instrument, 0, 0, note.channel);
            player.addNote(note.pitch, VOLUME, note.startTick, note.length, note.channel, 0);
        }
    }
    
    /**
     * When the user clicks the "Stop playing" button in the actions menu, 
     * stop playing the scale.
     * @param event the button click event
     */
    @FXML 
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        stop();
    }    
    
    /**
     * Stops the current MidiPlayer, clears MidiPlayer and stops RedBar.
     */
    protected void stop() {
        player.stop();
        player.clear();
        redBarObj.stopAnimation();
    }  
    
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        for (NoteBar note: musicNotesArray) {
            note.selectNote(compositionPane);
        }
        updateSelectedNotesArray();
    }

    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        for (NoteBar note: selectedNotesArray) {
            note.deleteNote(compositionPane);
            musicNotesArray.remove(note);
            //selectedNotesArray.remove(note);
        }
        updateSelectedNotesArray();
    }

    /**
     * When the user clicks the "Exit" menu item in the file menu, 
     * exit the program.
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handles clicks on the composition pane, creates new NoteBar with 
     * default length and current instrument selection. 
     * Adds note to the musicNotesArray and displays note on compositionPane.
     * @param event the mouse click event
     */
    @FXML
    protected void handleCompPaneClick(MouseEvent event) {
        stop();
        boolean controlPressed = event.isControlDown();

        int x = (int) event.getX();
        int y = (int) event.getY();
        NoteBar clickedNote = clickedNote(x, y);

        if (clickedNote == null) {
            noNoteClicked(controlPressed, event);
        }
        else {
            noteClicked(controlPressed, clickedNote);
        }
        updateSelectedNotesArray();
    }  
    
    protected void noNoteClicked(boolean ctrlPressed, MouseEvent event) {
        if (ctrlPressed == false) {
                resetSelectedNotesArray();
            }
        NoteBar newNote = new NoteBar(selectedInstrument, event.getX(), event.getY());
        musicNotesArray.add(newNote);
        newNote.displayNewNote(compositionPane);
    }
    
    protected void noteClicked(boolean ctrlPressed, NoteBar clickedNote) {
        if (ctrlPressed) {
            clickedNote.toggleNoteSelection(compositionPane);
        }
        else {
            resetSelectedNotesArray();
            clickedNote.selectNote(compositionPane);
        }
    }
    
    protected NoteBar clickedNote(int x, int y) {
        for (NoteBar note: musicNotesArray){
            Rectangle r = note.noteDisplay;
            if (r.contains(x, y)) {
                return note;
            }
        }
        return null;
    }
    
    @FXML
    protected void handleCompPaneDrag(MouseEvent event) {
        stop();
        
        /*
        EventHandler onEntered = new EventHandler<DragEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ;
            }  
        };
        */
    }  
    
    protected void noNoteDragged(boolean ctrlPressed, MouseEvent event) {
        stop();
    }
    
    protected void noteDragged(boolean ctrlPressed, NoteBar clickedNote) {
        stop();
    }
    
    protected void updateSelectedNotesArray(){
        selectedNotesArray.clear();
        for (NoteBar note: musicNotesArray) {            
            if (note.isSelected()) {
                note.selectNote(compositionPane);
                selectedNotesArray.add(note);
            }
        }
    }
    
    protected void resetSelectedNotesArray(){
        for (NoteBar note: selectedNotesArray) {            
            note.unselectNote(compositionPane);
        }
        selectedNotesArray.clear();
    }
    
    @FXML
    ToggleGroup instrumentSelection;
    
    /**
     * Handles changes to the instrument.
     * @param event the menu selection event
     */
    @FXML
    protected void handleInstrumentMenuAction(ActionEvent event) {
        Node t = (Node) instrumentSelection.getSelectedToggle();
        selectedInstrument = t.getId();
    }
}

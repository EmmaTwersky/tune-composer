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
     * Create instrument value to keep track of current instrument selection.     
     */
    //private int instrument = 0;
    //private int channel = 0;
    
    /**
     * Create array of NoteBar objects.      
     */
    private ArrayList<NoteBar> musicNotesArray = new ArrayList(); 
    
    private ArrayList<NoteBar> selectedNotesArray = new ArrayList(); 
    
    /**
     * Initialize default note length to 100 pixels.      
     */
    private static int noteLength = 100;
    
    private String selectedInstrument = "Piano";
    
    /**
     * Initialize note height to 10 pixels, this is final.      
     */
    private final int noteHeight = 10;        
    
    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    private final MidiPlayer player; // = new MidiPlayer(100,60);
    
    /**
     * Create the pane for drawing.      
     */
    @FXML
    private Pane compositionPane;

    /**
     * Initialize a RedBar to track the progression of time.
     */
    private RedBar redBarObj; // = new RedBar(compositionPane);
    
    
    private final InstrumentSelection instrumentInfo = new InstrumentSelection();
    /**
     * Initializes a new MidiPlayer for this instance.
     */
    public FXMLController() {
        this.player = new MidiPlayer(100,60);
    }
    
    /**
     * Initialized with our FXML, draws initial setup of composition pane.
     * @param location
     * @param resources
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        for (int i = 0; i < 128; i++) {
            Line staffLine = new Line(0, i*10, 2000, i*10);
            staffLine.setId("staffLine");
            compositionPane.getChildren().add(staffLine);
        }
        for (int i = 0; i < 20; i++) {
            Line measureLine = new Line(i*100, 0, i*100, 1280);
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
            note.selected = true;
        }
    }

    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        updateSelected();
        for (NoteBar note: selectedNotesArray) {
            note.deleteNote(compositionPane);
            musicNotesArray.remove(note);
            selectedNotesArray.remove(note);
        }
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
        resetSelected();
        int x = (int) event.getX();
        int y = (int) event.getY();
        for (NoteBar note: musicNotesArray){
            Rectangle r = note.noteDisplay;
            if (r.contains(x, y)) {
                note.selected = true;
            }
        }
        updateSelected();
        //if () //not in a note bar, create a new note and select it, unselect everything else
        //else if //control and click create a new note bar and keep all others still selected
        
        NoteBar newNote = new NoteBar(selectedInstrument, event.getX(), event.getY());
        musicNotesArray.add(newNote);
        newNote.display(compositionPane);
    }  
    
    protected void updateSelected(){
        selectedNotesArray = new ArrayList(); //not working correctly
        for (NoteBar note: musicNotesArray) {            
            if (note.selected) {
                selectedNotesArray.add(note);
                note.selectNote(compositionPane);
            }
        }
    }
    
    protected void resetSelected(){
        for (NoteBar note: musicNotesArray) {            
            if (note.selected) {
                note.selected = false;
                note.unselectNote(compositionPane);
            }
        }
        selectedNotesArray = new ArrayList(); //not working correctly
    }
    
    @FXML
    ToggleGroup instrumentSelection;
    /**
     * Handles changes to the instrument.
     * http://stackoverflow.com/questions/37902660/javafx-button-sending-arguments-to-actionevent-function
     * @param event the menu selection event
     */
    @FXML
    protected void handleInstrumentMenuAction(ActionEvent event) {
        Node t = (Node) instrumentSelection.getSelectedToggle();
        selectedInstrument = t.getId();
        //int instrumentValue = instrumentInfo.getInstrumentValue(selectedInstrument);
        //int instrumentChan = instrumentInfo.getInstrumentChannel(selectedInstrument+"Channel");
        //instrument = instrumentValue;
        //channel = instrumentChan;
    }
}

package tunecomposer;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * Controls the note lines and features of the noteLinesPane.
 */
public class NoteLinesPaneController implements Initializable {
    
    /** 
     * Set note height to 10 pixels and note length to 100 pixels. 
     */
    private static final int NOTE_HEIGHT = 10; 
    private static final int INITIAL_NOTE_LENGTH = 80;
    
    /**
     * Set pitch range from 1 to 127 and bar range, 
     * the number of measures on the screen, to 20.
     */
    private static final int PITCH_RANGE = 128;
    private static final int BAR_RANGE = 180; 
    private static final int LABEL_FREQ = 7;
    private static final int LABEL_RANGE = BAR_RANGE / LABEL_FREQ;
    
    
    /**
     * Create the array list of note pitch labels.      
     */
    private final ArrayList<String> pitchList = 
            new ArrayList(Arrays.asList(" ","G"," ","F","E"," ","D"," ","C","B"," ","A"));
    
    /**
     * Create the pane which the note events take place on.      
     */
    @FXML
    public Pane noteLinesPane;
    
    /**
     * Create the pane which the label events take place on.      
     */
    @FXML
    public Pane pitchLabels;
    
    /**
     * Draws initial setup of note lines on the pane.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        for (int i = 0; i < PITCH_RANGE; i++) {
            Line staffLine = new Line(0, i * NOTE_HEIGHT, 
                    BAR_RANGE * INITIAL_NOTE_LENGTH, i * NOTE_HEIGHT);
            staffLine.setId("staffLine");
            noteLinesPane.getChildren().add(staffLine);
        }
        for (int i = 0; i < BAR_RANGE; i++) {
            Line measureLine = new Line(i * INITIAL_NOTE_LENGTH, 0, 
                    i * INITIAL_NOTE_LENGTH, PITCH_RANGE * NOTE_HEIGHT);
            measureLine.setId("measureLine");
            noteLinesPane.getChildren().add(measureLine);
        }
        for (int i = 0; i <= LABEL_RANGE; i++) {
            TextFlow tf = new TextFlow();
            tf.setMaxWidth(NOTE_HEIGHT / 2);
            tf.setTextAlignment(TextAlignment.RIGHT);
            
            for (int k = 0; k < PITCH_RANGE; k++) {
                Text pitch = new Text(pitchList.get(k % pitchList.size()));
                pitch.setId("label");
                tf.getChildren().add(pitch);
            }
            
            tf.relocate(i * INITIAL_NOTE_LENGTH * LABEL_FREQ, 0);
            pitchLabels.getChildren().add(tf);
        }
    }
}    
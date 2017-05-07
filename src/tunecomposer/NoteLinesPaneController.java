package tunecomposer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Controls the note lines and features of the noteLinesPane.
 */
public class NoteLinesPaneController implements Initializable {
    
    /** 
     * Set note height to 10 pixels and note length to 100 pixels. 
     */
    private static final int NOTE_HEIGHT = 10; 
    private static final int INITIAL_NOTE_LENGTH = 100;
    
    /**
     * Set pitch range from 1 to 127 and bar range, 
     * the number of measures on the screen, to 20.
     */
    private static final int PITCH_RANGE = 128;
    private static final int BAR_RANGE = 180; 
    
    private final ArrayList<String> pitchList = 
            new ArrayList(Arrays.asList(" ","G"," ","F","E"," ","D"," ","C","B"," ","A"));
    
    @FXML
    public Pane pitchLabels;
    
    /**
     * Create the pane which the note events take place on.      
     */
    @FXML
    public Pane noteLinesPane;
    
    /**
     * Draws initial setup of note lines on the pane.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        TextFlow tf = new TextFlow();
        tf.setMaxWidth(5);

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
        for (int i = 0; i < PITCH_RANGE; i++) {
            Text pitch = new Text(pitchList.get(i % pitchList.size()));
            pitch.setId("pitchLabel");
            tf.getChildren().add(pitch);
        }
        pitchLabels.getChildren().add(tf);
    }    
}
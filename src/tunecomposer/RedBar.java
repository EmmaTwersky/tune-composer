// Refactor handler into another method.
// Clarify frame rate's purpose of value 10??
package tunecomposer;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * This class creates and animates the red bar which shows the moment at which
 * the MidiPlayer is playing.
 * 
 * @author Emma Twersky
 */
public class RedBar {
    /**
     * Creates time line for animation and Rectangle red bar object.
     */
    private final Timeline timeline;
    private final Rectangle redBar;
    
    /**
     * Sets screen height to 1280 and width to 1 pixel.
     */
    private static final int SCREEN_HEIGHT = 1280;
    private static final int REDBAR_WIDTH = 1;
    private static final int FRAME_RATE = 10;
    
    /**
     * Sets final parent pane and initializes end of notes to time 0.
     */
    private final Pane parentPane;
    private int compositionEnd = 0;
    
    /**
     * Creates RedBar object on given pane. 
     * Sets line to have redBarWidth and height of screenHeight,
     * begins at top left corner of pane (0,0).
     * 
     * @param pane the pane the RedBar is on
     */
    RedBar(Pane pane) {
        parentPane = pane;
        redBar = new Rectangle(0, 0, REDBAR_WIDTH, SCREEN_HEIGHT);
        redBar.setId("redBar");
        parentPane.getChildren().add(redBar);
        timeline = new Timeline();
        redBar.setVisible(false);
    }
    
    /**
     * Moves the line across the screen at the speed set by movementSpeed, 
     * disappears at end of last note displayed.
     * 
     * @param noteList List of NoteBar objects that visually represent notes on the screen
     */
    public void playAnimation(ArrayList<NoteBar> noteList) {
        timeline.stop();
        timeline.getKeyFrames().clear();
        redBar.setX(0);
        redBar.setVisible(true);
        
        findEndCoordinate(noteList);
        
        KeyValue kv = new KeyValue(redBar.xProperty(), compositionEnd);
        Duration duration = Duration.millis(compositionEnd * FRAME_RATE);
                
        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent event) -> {
            timeline.stop();
            redBar.setVisible(false);
            redBar.setX(0);  
        };
        
        KeyFrame kf = new KeyFrame(duration, onFinished, kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }    
    
    /**
     * Stop the redBar animation and make it disappear.
     */
    public void stopAnimation() {
        timeline.stop();
        redBar.setVisible(false);
        redBar.setX(0);
    }
    
    /**
     * Returns the X value of the right side of the final note in noteList.
     * 
     * @param noteList the list of notes being played
     */
    private void findEndCoordinate(ArrayList<NoteBar> noteList) {
        noteList.stream().map((note) -> note.startTick + note.length).filter(
                (noteEnd) -> (noteEnd > compositionEnd)).forEachOrdered((noteEnd) -> {
            compositionEnd = noteEnd;
        });
    } 
}

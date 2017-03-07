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
 * @author EmmaTwersky
 */
public class RedBar {
    private Timeline timeline; // = new Timeline(); final??
    private Rectangle redBar;// = new Rectangle(0,0,1,1280); final??
    
    private final float movementSpeed = 100;
    private Pane parentPane;
    
    private final int screenHeight = 1280;
    private final int redBarWidth = 1;
    
    private int finalXValue = 0;
    
    /**
     * Creates RedBar object on given pane. 
     * Sets line to have redBarWidth and height of screenHeight,
     * begins at top left corner of pane (0,0).
     * @param pane the pane the RedBar is on
     */
    RedBar(Pane pane) {
        parentPane = pane;
        redBar = new Rectangle(0,0,redBarWidth,screenHeight);
        redBar.setId("redBar");
        parentPane.getChildren().add(redBar);
        timeline = new Timeline();
        redBar.setVisible(false);
    }
    
    /**
     * Moves the line across the screen at the speed set by 
     * movementSpeed, disappears at end of last note displayed.
     * @param noteList List of NoteBar objects that visually represent notes on the screen
     */
    public void playAnimation(ArrayList<NoteBar> noteList) {
        timeline.stop();
        timeline.getKeyFrames().clear();
        redBar.setX(0);
        redBar.setVisible(true);
        
        findEndCoordinate(noteList);
        System.out.println(finalXValue);
        
        KeyValue kv = new KeyValue(redBar.xProperty(), finalXValue);
        Duration duration = Duration.millis(finalXValue * 10);
                
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.stop();
                redBar.setVisible(false);
                redBar.setX(0);
            }  
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
     * @param noteList the list of notes being played
     */
    private void findEndCoordinate(ArrayList<NoteBar> noteList) {
        for (NoteBar note: noteList) {
            if (note.startTick > finalXValue) 
                finalXValue = note.startTick + note.noteLength;
        }
    } 
}

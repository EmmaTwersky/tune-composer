// Refactor handler into another method.
package tunecomposer;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * This class creates and animates the red bar which shows the moment at which
 * the MidiPlayer is playing.
 * 
 * @author Emma Twersky
 */
public class RedBarPaneController implements Initializable {
    /**
     * Creates time line for animation and Rectangle red bar object.
     */
    private Timeline timeline;
    
    @FXML
    private Rectangle RED_BAR;
    
    /**
     * Sets frame rate, AKA animation speed, to 10.
     */
    private static final int FRAME_RATE = 10;
    
    /**
     * Initializes end of notes to time 0.
     */
    private int compositionEnd = 0;
    
    @FXML
    public CompositionPaneController compositionPaneController;
    
    @FXML
    public RedBarPaneController redBarPaneController;
    
    /**
     * Creates RedBar object on given pane. 
     * Sets line to have redBarWidth and height of screenHeight,
     * begins at top left corner of pane (0,0).
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        timeline = new Timeline();
        RED_BAR.setVisible(false);
    }
    
    /**
     * Moves the line across the screen at the speed set by movementSpeed, 
     * disappears at end of last note displayed.
     */
    public void playAnimation() {
        timeline.stop();
        timeline.getKeyFrames().clear();
        RED_BAR.setX(0);
        RED_BAR.setVisible(true);
        
        findEndCoordinate(SoundObjectPaneController.SOUNDOBJECT_ARRAY);
        
        KeyValue kv = new KeyValue(RED_BAR.xProperty(), compositionEnd);
        Duration duration = Duration.millis(compositionEnd * FRAME_RATE);
                
        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent event) -> {
            timeline.stop();
            RED_BAR.setVisible(false);
            RED_BAR.setX(0);  
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
        RED_BAR.setVisible(false);
        RED_BAR.setX(0);
    }
    
    /**
     * Returns the X value of the right side of the final note in noteList.
     * 
     * @param noteList the list of notes being played
     */
    private void findEndCoordinate(ArrayList<SoundObject> itemList) {
        for (SoundObject sObj : itemList) {
            Rectangle r = sObj.visualRectangle;
            int end = (int) (r.getX() + r.getWidth());
            if (end > compositionEnd)
                compositionEnd = end;
        }
    } 
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Zach
 */
public class FXMLController implements Initializable{
    
    private static final int VOLUME = 127;
    private static int instrument = 0;
    private static int lastNote = 0;
    
    /**
     * Initialize a timeline animation object and redline to 
     * track the progression of time.
     */
    private final Timeline timeline = new Timeline();
    private final Rectangle redline = new Rectangle(0,0,1,1280);
    
    
    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    private final MidiPlayer player;
    
    /**
     * Create the pane for drawing.      
     */
    @FXML
    private Pane compositionPane;

    /**
     * Initializes a new MidiPlayer for this instance.
     */
    public FXMLController() {
        this.player = new MidiPlayer(100,60);
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
     * Plays the sequencer and starts the timeline.
     */
    protected void play() {
        playSequence();
        playTimeline();
    }
    
    /**
     * Plays the sequencer
     * Each time playSequence is called the sequence restarts from the beginning.
     */
    protected void playSequence() {
        player.stop();
        player.restart();
        player.play();
    }
    
    /**
     * Plays the redline animation on the timeline.
     */
    protected void playTimeline() {
        timeline.stop();
        redline.setX(0);
        final KeyValue kv = new KeyValue(redline.xProperty(), lastNote);
        Duration duration = Duration.millis(lastNote * 10);
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                timeline.stop();
                redline.setX(0);
            }
        };
        final KeyFrame kf = new KeyFrame(duration, onFinished, kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
    
    /**
     * Stops the current sequence
     * Each time stop() is called, the player stops playing and the timeline
     * stops animating the redline.
     */
    protected void stop() {
        player.stop();
        timeline.stop();
        redline.setX(0);
    }
    
    /**
     * Takes two int inputs for the x and y coordinates of the note to be added
     * draws a rectangle and attaches its left side at the mouse click location.
     * @param x
     * @param y
     */
    public void addNote(int x, int y) {
        System.out.println(instrument);
        Rectangle rect = new Rectangle(x, y, 100, 10);
        rect.setFill(Color.BLUE);
        compositionPane.getChildren().add(rect);
        int pitch = 127 - (y/10);
        player.addNote(pitch, VOLUME, x, 100, instrument, 0);
        if (x + 100 > lastNote) {
            lastNote = x + 100;
        }
    }  
    
     /**
     * Initialized with our FXML, draws initial setup of composition pane.
     * @param location
     * @param resources
     */
    @FXML
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        //draw all horizontal grey lines on the composition panel.
        for (int i = 0; i < 128; i++) {
            Line line = new Line(0, i*10, 2000, i*10);
            line.setStroke(Color.GREY);
            compositionPane.getChildren().add(line);
        }
        for (int i = 0; i < 20; i++) {
            Line line = new Line(i*100, 0, i*100, 1280);
            line.setStroke(Color.LIGHTGREY);
            compositionPane.getChildren().add(line);
        }        
        //draw the red line on the composition panel.
        redline.setFill(Color.RED);
        compositionPane.getChildren().add(redline);
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
     * When the user clicks the "Exit" menu item in the file menu, 
     * exit the program.
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }
    
    /**
     * Handles clicks on the composition pane, adding notes to the sequencer
     * and incrementing the timeline.
     * @param event the mouse click event
     */
    @FXML
    protected void handleCompPaneClick(MouseEvent event) {
        addNote((int)event.getX(), (int)Math.round(event.getY()/10)*10);
    }   
    
    /**
     * Handles changes to the instrument
     * http://stackoverflow.com/questions/37902660/javafx-button-sending-arguments-to-actionevent-function
     * @param event the menu selection event
     */
    @FXML
    protected void handleInstrumentMenuItemAction(ActionEvent event) {
        System.out.println(event.getSource());
    }   

}

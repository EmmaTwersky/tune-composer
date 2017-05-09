package tunecomposer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;

/**
 * Controllers and holds the current instrument selection.
 * The default is set to a Piano.
 */
public class InstrumentToolBarController implements Initializable {
    /**
     * Initialize default note to "Piano".      
     */
    public static String selectedInstrument = "Piano";
    
    /**
     * Constructs ToggleGroup from <TuneComposer.fxml>.
     */
    @FXML
    ToggleGroup instrumentSelection;
    
    @FXML
    Slider noteLength;
    
    /**
     * Initialize FXML Application. 
     * Creates ActionManager to control actions. 
     * Sets CompPaneController to hold ActionManagerReference.
     * 
     * @param location the source of the scene
     * @param resources the resources of the utility of the scene
     */
    @FXML
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        noteLength.valueProperty().addListener(
                (ObservableValue<? extends Number> observable, 
                        Number oldValue, Number newValue) -> {
                    NoteBar.noteLength = newValue.intValue();
        });
    }
    
    /**
     * Handles changes to the instrument selection menu.
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleInstrumentMenuAction(ActionEvent event) {
        Node toggle = (Node) instrumentSelection.getSelectedToggle();
        selectedInstrument = toggle.getId();
    }
    
    
}
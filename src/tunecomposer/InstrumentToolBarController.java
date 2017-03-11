package tunecomposer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;

/**
 * This controller class implements and holds the current instrument selection.
 * The default is set to a Piano.
 *
 * @author EmmaTwersky
 */
public class InstrumentToolBarController {
    /**
     * Initialize default note to "Piano".      
     */
    public static String selectedInstrument = "Piano";
    
    /**
     * Constructs ToggleGroup from <TuneComposer.fxml>.
     */
    @FXML
    ToggleGroup instrumentSelection;
    
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

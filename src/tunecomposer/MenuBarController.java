package tunecomposer;

import javafx.event.ActionEvent;
import javafx.fxml.*;

/**
 * This class controls the menu bar and menu bar actions.
 *
 * @author Emma Twersky
 */
public class MenuBarController {    
    /**
     * Handles the play button from the Actions menu.
     * 
     * @param event the menu selection event
     */
    @FXML 
    protected void handlePlayScaleButtonAction(ActionEvent event) {
        CompositionPaneController.tunePlayerObj.play();
    }
    
    /**
     * Handles the Stop menu item.
     * 
     * @param event the button click event
     */
    @FXML 
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        CompositionPaneController.tunePlayerObj.stop();
    }     
    
    /**
     * Handles the Select All menu item and selects all notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleSelectAllMenuItemAction(ActionEvent event) {
        CompositionPaneController.MUSIC_NOTES_ARRAY.forEach((note) -> {
            note.select();
        });
        CompositionPaneController.updateSelectedNotesArray();
    }

    /**
     * Handles the Delete menu item and deletes all selected notes.
     * 
     * @param event the button click event
     */
    @FXML
    protected void handleDeleteMenuItemAction(ActionEvent event) {
        for (NoteBar note: CompositionPaneController.SELECTED_NOTES_ARRAY) {
            note.delete();
            CompositionPaneController.MUSIC_NOTES_ARRAY.remove(note);
        }
        CompositionPaneController.updateSelectedNotesArray();
    }

    /**
     * Handles the Exit menu item and exits the scene.
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }
}

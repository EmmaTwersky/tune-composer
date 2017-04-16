package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.SoundObject;

/**
 * An action which stores deleting SoundObjects in the application.
 */
public class DeleteAction extends Action {

    /**
     * Array of SoundObjects affected by the action.
     */
    ArrayList<SoundObject> affectedObjs = new ArrayList<>();
    
    /**
     * Pane reference to the pane that all affectedObjs are on.
     */
    private final Pane soundObjectPane;

    /**
     * Constructs an action event to delete SoundObjects.
     * Sets affectObjs and soundObjectPane.
     * @param selectedObjs selList all SoundObjects to be affected
     * @param soundObjectPane the SoundObjectPane these selectedObjs are on
     */
    public DeleteAction(ArrayList<SoundObject> selectedObjs, Pane soundObjectPane) {
        affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
        this.soundObjectPane = soundObjectPane;
    }
    
    /**
     * Removes all affectedObjs from the soundObjectPane.
     */
    @Override
    public void execute() { 
        affectedObjs.forEach((sObj) -> {
            sObj.removeFromPane(soundObjectPane);
        });
    }
    
    /**
     * Adds all affectedObjs to the soundObjectPane.
     */
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.addToPane(soundObjectPane);
        });
    }

    /**
    * Re-removes all affectedObjs from the soundObjectPane. 
     */
    @Override
    public void redo() {
        execute();
    }
}

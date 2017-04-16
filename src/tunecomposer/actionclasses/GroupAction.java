package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.ActionManager;
import tunecomposer.SoundObject;
import tunecomposer.Gesture;

/**
 * An action which stores grouping SoundObjects in the application.
 */
public class GroupAction extends Action{
    
    /**
     * Gesture reference for the new Gesture created.
     */
    Gesture gesture;

    /**
     * Pane reference to the pane that Gesture is on.
     */
    private final Pane soundObjectPane;

    
    /**
     * Constructs an action event to create a Gesture.
     * Creates a new Gesture with the given selectedObjs.
     * Sets gesture and soundObjectPane and creates userData for the visualRectangle.
     * @param selectedObjs selList all SoundObjects to be affected
     * @param actionManager actionManager this event is within
     * @param soundObjectPane the SoundObjectPane these selectedObjs are on
     */
    public GroupAction(ArrayList<SoundObject> selectedObjs, 
            ActionManager actionManager, Pane soundObjectPane) {             
        gesture = new Gesture(selectedObjs, actionManager, soundObjectPane);
        this.soundObjectPane = soundObjectPane;
        gesture.visualRectangle.setUserData(gesture);
    }
    
    /**
     * Groups the given gesture.
     */
    @Override
    public void execute() {
        gesture.group(soundObjectPane);
    }

    /**
     * Ungroups the gesture.
     */
    @Override
    public void undo() {
        gesture.ungroup(soundObjectPane);
        gesture.containedSoundObjects.forEach((innerSObj) -> {
            innerSObj.select();
            innerSObj.visualRectangle.setUserData(innerSObj);
        });
    }
    
    /**
     * Regroup the gesture.
     */
    @Override
    public void redo() {
        execute();
    }
}

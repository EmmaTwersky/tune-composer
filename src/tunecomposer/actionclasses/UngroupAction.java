package tunecomposer.actionclasses;

import javafx.scene.layout.Pane;
import tunecomposer.Gesture;

/**
 * An action which ungroups given gesture.
 */
public class UngroupAction extends Action {
    
    /**
     * Gesture reference to be ungrouped.
     */
    final Gesture gesture;

    /**
     * Prepares object to ungroup gesture, but does not perform the action.
     * 
     * @param givenGest the gesture to be ungrouped
     * @param soundObjectPane the SoundObjectPane these selectedObjs are on
     */
    public UngroupAction(Gesture givenGest, Pane soundObjectPane) {
        this.soundObjectPane = soundObjectPane;
        gesture = givenGest;
    }
    
    /**
     * Ungroups the gesture.
     * Resets the user data of the gesture's containedObjects
     */
    @Override
    public void execute() {
        gesture.ungroup(soundObjectPane);
        gesture.containedSoundObjects.forEach((innerSObj) -> {
            innerSObj.select();
            innerSObj.visualRectangle.setUserData(innerSObj);
        });
    }

    /**
     * Groups the gesture, and updates userData of children.
     */
    @Override
    public void undo() {
        gesture.group(soundObjectPane);
    }

    /**
     * Ungroups the gesture. 
     */
    @Override
    public void redo() {
        execute();
    }
}

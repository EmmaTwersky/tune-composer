package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.ActionManager;
import tunecomposer.SoundObject;
import tunecomposer.Gesture;

/**
 * An action which groups the given SoundObjects.
 */
public class GroupAction extends Action{
    
    /**
     * Gesture reference for the new Gesture created.
     */
    private final Gesture gesture;
    
    /**
     * Sets up this action to group given SoundObjects, but doesn't execute action.
     * Creates a new Gesture with the given selectedObjs.
     * Sets gesture and soundObjectPane and creates userData for the visualRectangle.
     * 
     * @param selectedObjs all SoundObjects to be affected
     * @param actionManager actionManager this event is within
     * @param soundObjectPane the SoundObjectPane these selectedObjs are on
     */
    public GroupAction(ArrayList<SoundObject> selectedObjs, 
            ActionManager actionManager, Pane soundObjectPane) {  
        
        this.affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
        gesture = new Gesture(affectedObjs, actionManager, soundObjectPane);
        
        this.soundObjectPane = soundObjectPane;
        gesture.visualRectangle.setUserData(gesture);
    }
    
    /**
     * Groups the gesture.
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
     * Re-executes action.
     */
    @Override
    public void redo() {
        execute();
    }
}
package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.NoteBar;
import tunecomposer.SoundObject;

public class SelectAction extends Action {
    
    /**
     * Array of SoundObjects to be affected by the action.
     */
    ArrayList<SoundObject> affectedObjs;
    
    /**
     * Pane that all SoundObject visuals live within.
     */
    private final Pane soundObjectPane;

    /**
     * Sets up Action object as select or unselect. 
     * Does not yet change the selection state of notes. 
     * That is for the execute() method.
     * @param selList selList must contain all SoundObjects to be affected
     * @param soundObjPane Pane that holds all SoundObject visuals
     */
    public SelectAction(ArrayList<SoundObject> selList, Pane soundObjPane) {
        affectedObjs = (ArrayList<SoundObject>) selList.clone();
        soundObjectPane = soundObjPane;
    }
        
    /**
     * Selects all of the rectangles contained within affectedObjs.
     */
    @Override
    public void execute() {
        for (SoundObject sObj : affectedObjs) {
            sObj.select();
        }
    }
    
    @Override
    public void undo() {
        for (SoundObject sObj : affectedObjs) {
            sObj.unselect();
        }
    }
    
    @Override
    public void redo() {
        for (SoundObject sObj : affectedObjs) {
            sObj.select();
        }
    }
}

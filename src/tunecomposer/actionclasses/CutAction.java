package tunecomposer.actionclasses;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import tunecomposer.SoundObject;

/**
 * An action which cuts objects to the clipboard and removes from a pane.
 */
public class CutAction extends Action {
        
    DeleteAction deleteAction;
    CopyAction copyAction;
    
    /**
     * Constructs an action event to copy SoundObjects.
     * Sets affectObjs and soundObjectPane.
     * @param selectedObjs selList all SoundObjects to be affected
     * @param soundObjectPane
     */
    public CutAction(ArrayList<SoundObject> selectedObjs, Pane soundObjectPane) {
        affectedObjs = (ArrayList<SoundObject>) selectedObjs.clone();
        this.soundObjectPane = soundObjectPane;
        
        deleteAction = new DeleteAction(affectedObjs, this.soundObjectPane);
        copyAction = new CopyAction(affectedObjs);
    }
    
    /**
     * Copies all affectedObjs from the soundObjectPane.
     */
    @Override
    public void execute() {
        deleteAction.execute();
        copyAction.execute();
    }
    
    /**
     * Not possible.
     */
    @Override
    public void undo() {
        deleteAction.undo();
        copyAction.undo();
    }

    /**
     * Not possible. 
     */
    @Override
    public void redo() {
        deleteAction.redo();
        copyAction.redo();
    }
}

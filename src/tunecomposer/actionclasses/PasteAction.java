package tunecomposer.actionclasses;

import javafx.scene.input.Clipboard;
import javafx.scene.layout.Pane;

/**
 * An action which pastes clipboard contents to the soundObjectPane.
 */
public class PasteAction extends Action {
/**
     * Constructs an action event to paste SoundObjects.
     * Sets affectObjs and soundObjectPane.
     * @param soundObjectPane
     */
    public PasteAction(Pane soundObjectPane) {
        this.soundObjectPane = soundObjectPane;
        Clipboard clipboard;
        clipboard = Clipboard.getSystemClipboard();
                
        String sObjs = clipboard.getString();
        System.out.println(sObjs);
//        TODO: TURN STRING INTO OBJECTS
//          compare to most recent in undo stack (mayb happens in sound objectpane controller)
//        set affectedObjs to clipboard's string
    }
    
    /**
     * Pastes all affectedObjs to the soundObjectPane.
     */
    @Override
    public void execute() {        
//        affectedObjs.forEach((sObj) -> {
//            sObj.addToPane(soundObjectPane);
//        });
    }
    
    /**
     * Un-pastes the affectedObjs from the soundObjectPane.
     */
    @Override
    public void undo() {
//        affectedObjs.forEach((sObj) -> {
//            sObj.removeFromPane(soundObjectPane);
//        });
    }

    /**
     * Re-executes paste action. 
     */
    @Override
    public void redo() {
//        execute();
    }
}
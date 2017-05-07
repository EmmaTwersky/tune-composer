package tunecomposer.actionclasses;

import javafx.scene.input.Clipboard;
import javafx.scene.layout.Pane;
import tunecomposer.ActionManager;
import tunecomposer.SoundObjectParser;

/**
 * An action which pastes clipboard contents to the soundObjectPane.
 */
public class PasteAction extends Action {
    
    /**
     * String that the action copied off of. Used for action comparison.
     */
    private final String parseString;
    
    /**
     * The amount the objects in this action were offset by.
     */
    private int offset = 0;
    
    /**
     * Constructs an action event to paste SoundObjects.
     * Sets affectObjs and soundObjectPane.
     * 
     * @param soundObjectPane
     * @param am ActionManager passed to SoundObject constructors
     */
    public PasteAction(Pane soundObjectPane, ActionManager am) {
        this.soundObjectPane = soundObjectPane;
        Clipboard clipboard;
        clipboard = Clipboard.getSystemClipboard();
                
        parseString = clipboard.getString();
        
        SoundObjectParser parser = new SoundObjectParser(parseString, soundObjectPane, am);
        affectedObjs = parser.parseString();
    }
    
    /**
     * Pastes all affectedObjs to the soundObjectPane.
     */
    @Override
    public void execute() {   
        MoveAction movePaste = new MoveAction(affectedObjs, 0, 0);
        movePaste.move(offset, offset);

        affectedObjs.forEach((sObj) -> {
            sObj.snapYInPlace();
            sObj.addToPane(soundObjectPane);
            sObj.select();
        });
    }
    
    /**
     * Un-pastes the affectedObjs from the soundObjectPane.
     */
    @Override
    public void undo() {
        affectedObjs.forEach((sObj) -> {
            sObj.removeFromPane(soundObjectPane);
        });
    }

    /**
     * Re-executes paste action. 
     */
    @Override
    public void redo() {
        affectedObjs.forEach((sObj) -> {
            sObj.addToPane(soundObjectPane);
            sObj.select();
        });
    }
    
    /**
     * Returns the string that this object created its objects from.
     * @return string this object parsed into objects
     */
    public String getParseString() {
        return parseString;
    }
    
    /**
     * Return the number of pixels that the affectedObjs were offset by.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }
    
    /**
     * Sets this.offsetAmt to given amount.
     * The offset is applied at execution call.
     * @param offset int to move note down and right
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
}
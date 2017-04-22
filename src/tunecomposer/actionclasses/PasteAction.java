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
    private String parseString;
    
    /**
     * The amount the objects in this action were offset by.
     */
    private int offset = 10;
    
    /**
     * Constructs an action event to paste SoundObjects.
     * Sets affectObjs and soundObjectPane.
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
//        TODO: compare to most recent in undo stack (mayb happens in sound objectpane controller)

    }
    
    /**
     * Pastes all affectedObjs to the soundObjectPane.
     */
    @Override
    public void execute() {        
        affectedObjs.forEach((sObj) -> {
            sObj.addToPane(soundObjectPane);
            sObj.select();
        });
        MoveAction movePaste = new MoveAction(affectedObjs, 0, 0);
        movePaste.move(offset, offset);

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
        execute();
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
     */
    public int getOffset() {
        return offset;
    }
    
    /**
     * Method to check if this PasteAction is equal to another. 
     * @param other the other PasteAction to compare to
     */
    public boolean hasSameString(PasteAction other) {
        String otherStr = other.getParseString();
        if (parseString.equals(otherStr)) {
            return true;
        }
        return false;
    }
    
    /**
     * Sets this.offsetAmt to given amount.
     * The offset is applied at execution call.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
}
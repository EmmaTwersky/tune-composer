package tunecomposer.actionclasses;

import javafx.scene.layout.Pane;
import tunecomposer.NoteBar;
import tunecomposer.ActionManager;

/**
 * An action which stores adding a note in the application.
 */
public class AddNoteAction extends Action {

    /**
     * NoteBar reference for the new NoteBar created.
     */
    private final NoteBar note;

    /**
     * Constructs an action event to add a note.
     * Creates a new NoteBar with the given x and y values.
     * Sets note and soundObjectPane and creates userData for the visualRectangle.
     * 
     * @param x the x value for the top left corner of the note position
     * @param y the y value for the top left corner of the note position
     * @param actionManager the actionManager this note is a part of
     * @param soundObjectPane the SoundObjectPane this note is on
     */
    public AddNoteAction(double x, double y, ActionManager actionManager, Pane soundObjectPane) {
        this.soundObjectPane = soundObjectPane;
        
        note = new NoteBar(x, y, actionManager, this.soundObjectPane);
        note.visualRectangle.setUserData(note);
    }
    
    /**
     * Adds the note to the soundObjectPane. 
     * This allows the NoteBar to be played and show visually on the screen.
     */
    @Override
    public void execute() {
        note.addToPane(soundObjectPane);
    }
    
    /**
     * Removes note from the soundObjectPane.
     */
    @Override
    public void undo() {
        note.removeFromPane(soundObjectPane);
    }

    /**
     * Re-executes action.
     */
    @Override
    public void redo() {
        execute();
    }
    
    /**
     * Returns the note reference held by this action object. 
     * @return NoteBar object in this.note field
     */
    public NoteBar getNote() {
        return note;
    }
}
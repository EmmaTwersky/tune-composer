package tunecomposer.actionclasses;

import javafx.scene.layout.Pane;
import tunecomposer.NoteBar;
import tunecomposer.ActionManager;

/**
 * This class creates the action to add a note for the Action Manager stack.
 * 
 * @author Cooper Lazar
 */
public class AddNote extends Action {

    /**
     * Rectangle reference for the note shown in NoteBarPane.
     */
    private final NoteBar note;
    
    /**
     * Reference to the NoteBarPane that note should be added to.
     */
    private final Pane notePane;

    
    /**
     * Note is constructed with given x and y values. 
     * @param x the x value for the top left corner of the note's position
     * @param y the y value for the top left corner of the note's position
     * @param _notePane Reference to the notePane.
     * @param actionManager
     * 
     */
    public AddNote(double x, double  y, ActionManager actionManager, Pane _notePane)  {
        note = new NoteBar(x, y, actionManager, _notePane);
        notePane = _notePane;
        note.visualRectangle.setUserData(note);
    }
    
    /**
     * Adds rectangle to the NoteBarPane. This allows the NoteBar to be played,
     * and shows the note visually on the screen. Also sets this object to 
     * executed so that it can later be undone.
     */
    @Override
    public void execute() {
        note.addToPane(notePane);
    }
    
    /**
     * Removes rectangle from the pane, but holds a reference of it for redo().
     * Removing the rectangle removes access to the NoteBar object.
     */
    @Override
    public void undo() {
        note.removeFromPane(notePane);
    }


    /**
     * Used to redo this action after it has been undone. Will add the note back
     * onto the pane.
     */
    @Override
    public void redo() {
        execute();
    }
    
    /**
     * Returns the note reference held by this object. Useful function to get 
     * the reference and add it to the SOUNDOBJECT_ARRAY
     * @return NoteBar object in this.note field.
     */
    public NoteBar getNote() {
        return note;
    }
}

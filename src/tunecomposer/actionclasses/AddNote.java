/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer.actionclasses;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author lazarcl
 */
public class AddNote extends Action {

    /**
     * Rectangle reference for the note shown in NoteBarPane.
     */
    private final Rectangle rectangle;
    
    /**
     * Reference to the NoteBarPane that note should be added to.
     */
    private final Pane notePane;

    
    /**
     * Rectangle must already be created and linked to NoteBar before it is
     * passed to constructor. The caller can then forget the reference 
     * to the rectangle.
     * @param rect Rectangle to be displayed in NoteBarPane
     * @param _notePane Reference to the notePane.
     * @param execute if true, will execute action in constructor
     * 
     */
    public AddNote(Rectangle rect, Pane _notePane, boolean execute)  {
        rectangle = rect;
        notePane = _notePane;
        
        if (execute = true) {
            notePane.getChildren().add(rectangle);
            executed = true;
        }
    }
    
    
    /**
     * Removes rectangle from the pane, but holds a reference of it for redo().
     * Removing the rectangle removes access to the NoteBar object.
     */
    @Override
    public void undo() {
        if (executed != true) {
            return;
        }
        notePane.getChildren().remove(rectangle);
    }

    /**
     * Adds rectangle to the NoteBarPane. This allows the NoteBar to be played,
     * and shows the note visually on the screen. Also sets this object to 
     * executed so that it can later be undone.
     */
    @Override
    public void execute() {
        notePane.getChildren().add(rectangle);
        executed = true;
    }
    
}

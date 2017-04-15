package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author EmmaTwersky
 */
public class SoundObjectPaneController {

    @FXML
    public Pane soundObjectPane;
    
    /**
     * Object that contains the undo and redo stack for the program. 
     */
    private ActionManager actionManager;
    
    /**
     * Create array of NoteBar objects and selected NoteBar objects.      
     */
    public static ArrayList<SoundObject> SELECTED_SOUNDOBJECT_ARRAY = new ArrayList();
         
    /**
     * Create array to temporarily store selected NoteBar objects.      
     */
    public static ArrayList<SoundObject> TEMP_SELECTED_SOUNDOBJ_ARRAY = new ArrayList();
    
    /**
     * Fills the SELECTED_SOUNDOBJECT_ARRAY with the currently selected notes.
     */
    public static void updateSelectedSoundObjectArray(Pane pane){
        SELECTED_SOUNDOBJECT_ARRAY.clear();
        for (Node n: pane.getChildren()) {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) (r).getUserData();
            if (sObj.isSelected()) {
                SELECTED_SOUNDOBJECT_ARRAY.add(sObj);
            }
        }
    }
    
    /**
     * Empties the SELECTED_SOUNDOBJECT_ARRAY and un-selects all notes.
     * @param pane
     */
    public static void unselectAllSoundObjects(Pane pane){
        for (Node n: pane.getChildren()) {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) (r).getUserData();
            if (sObj.isSelected()) {
                sObj.unselect();
            }
        }
        SELECTED_SOUNDOBJECT_ARRAY.clear();
    }
        
    public void group() {
        Gesture g = new Gesture();
        g.visualRectangle.setUserData(g);
        g.addToPane(soundObjectPane);
        
//        unselectAllSoundObjects();
        
//        g.containedSoundObjects.forEach((sObj) -> {
//            SOUNDOBJECT_ARRAY.remove(sObj);
//        });
//        SOUNDOBJECT_ARRAY.add(g);
        
        updateSelectedSoundObjectArray(soundObjectPane);
    }
    
    public void ungroup() {
        for (SoundObject sObj : SELECTED_SOUNDOBJECT_ARRAY) {
            if (sObj instanceof Gesture) {
                ((Gesture) sObj).ungroup(soundObjectPane);
                sObj.containedSoundObjects.forEach((innerSObj) -> {
                    innerSObj.select();
                    innerSObj.visualRectangle.setUserData(innerSObj);
                });
            }
        }
        updateSelectedSoundObjectArray(soundObjectPane);
    }
    
    
    /**
     * Set the actionManager to the instance that contains the undo and 
     * redo stacks. If given manager is null, then throws NullPointerException.
     * @param manager
     * @throws NullPointerException
     */
    public void setActionManager(ActionManager manager) 
                                     throws NullPointerException{
        if (manager == null) {
            throw new NullPointerException();
        }
        actionManager = manager;
    }
}

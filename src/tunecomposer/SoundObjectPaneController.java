package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import java.lang.NullPointerException;

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
    public static ArrayList<SoundObject> SOUNDOBJECT_ARRAY = new ArrayList(); 
    public static ArrayList<SoundObject> SELECTED_SOUNDOBJECT_ARRAY = new ArrayList();
         
    /**
     * Create array to temporarily store selected NoteBar objects.      
     */
    public static ArrayList<SoundObject> TEMP_SELECTED_SOUNDOBJ_ARRAY = new ArrayList();
    
    /**
     * Fills the SELECTED_SOUNDOBJECT_ARRAY with the currently selected notes.
     */
    public static void updateSelectedSoundObjectArray(){
        SELECTED_SOUNDOBJECT_ARRAY.clear();
        for (SoundObject sObj: SOUNDOBJECT_ARRAY) {
            if (sObj.isSelected()) {
                SELECTED_SOUNDOBJECT_ARRAY.add(sObj);
            }
        }
    }
    
    /**
     * Empties the SELECTED_SOUNDOBJECT_ARRAY and un-selects all notes.
     */
    public static void unselectAllSoundObjects(){
        for (SoundObject sObj: SOUNDOBJECT_ARRAY) { 
            if (sObj.isSelected()) {
                sObj.unselect();
            }
        }
        SELECTED_SOUNDOBJECT_ARRAY.clear();
    }
        
    public void group() {
        Gesture g = new Gesture();
        g.addToPane(soundObjectPane);
        
//        unselectAllSoundObjects();
        
        g.containedSoundObjects.forEach((sObj) -> {
            SOUNDOBJECT_ARRAY.remove(sObj);
        });
        SOUNDOBJECT_ARRAY.add(g);
        
        updateSelectedSoundObjectArray();
    }
    
    public void ungroup() {
        for (SoundObject sObj : SELECTED_SOUNDOBJECT_ARRAY) {
            if (sObj instanceof Gesture) {
                ((Gesture) sObj).ungroup(soundObjectPane);
                sObj.containedSoundObjects.forEach((innerSObj) -> {
//                    innerSObj.select();
                    SOUNDOBJECT_ARRAY.add(innerSObj);
                });
                SOUNDOBJECT_ARRAY.remove(sObj);
            }
        }
        updateSelectedSoundObjectArray();
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
    
}//end Class

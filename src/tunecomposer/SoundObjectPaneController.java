package tunecomposer;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author EmmaTwersky
 */
public class SoundObjectPaneController {

    @FXML
    public Pane soundObjectPane;
    
    /**
     * Create array of NoteBar objects and selected NoteBar objects.      
     */
    public static final ArrayList<SoundObject> SOUNDOBJECT_ARRAY = new ArrayList(); 
    public static final ArrayList<SoundObject> SELECTED_SOUNDOBJECT_ARRAY = new ArrayList();
         
    /**
     * Create array to temporarily store selected NoteBar objects.      
     */
    public static final ArrayList<SoundObject> TEMP_SELECTED_SOUNDOBJ_ARRAY = new ArrayList();
    
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
        Gesture g = new Gesture(soundObjectPane);
        
        unselectAllSoundObjects();
        
        g.containedSoundObjects.forEach((sObj) -> {
            SOUNDOBJECT_ARRAY.remove(sObj);
        });
        SOUNDOBJECT_ARRAY.add(g);
        
        updateSelectedSoundObjectArray();
    }
    
    public void ungroup() {
        for (SoundObject sObj : SELECTED_SOUNDOBJECT_ARRAY) {
            if (sObj instanceof Gesture) {
                SOUNDOBJECT_ARRAY.remove(sObj);
                sObj.containedSoundObjects.forEach((innerSObj) -> {
                    innerSObj.select();
                    SOUNDOBJECT_ARRAY.add(innerSObj);
                });
                ((Gesture) sObj).ungroup();
            }
        }
        updateSelectedSoundObjectArray();
    }
}

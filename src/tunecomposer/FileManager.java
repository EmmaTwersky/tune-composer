/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.util.Observable;
import javafx.scene.layout.Pane;
import tunecomposer.actionclasses.Action;

/**
 *
 * @author lonbern
 */
public class FileManager extends Observable {
    
    /**
     * Path to the file that is currently loaded in the composition pane.
     * If null, then the composition pane has not been associated to a file,
     * and one will have to be specified before saving.
     */
    private String filePath;
    
    /**
     * Last action performed before last save. 
     * Actions in the undoStack cannot be modified, so keep reference of top 
     * Action during each save. Compare this to the current top of undoStack 
     * to see if an action has been performed. If null, then stack was empty.
     */
    private Action lastSaveAction;
    
    /**
     * Reference to the composition pane's actionManager for note creation and 
     * comparing undoStacks.
     */
    private ActionManager actionManager;
    
    /**
     * Pane that all soundObjects are kept within.
     * Passed into note constructors.
     */
    private Pane soundObjPane;
    
    /**
     * Initializes all the fields, used to prepare for saving and loading. Not 
     * for building FileManager object before saving or loading.
     * @param aManager ActionManager for the compositionPane wish to save/load
     * @param sObjPane pane to save and load soundObjects from/to
     */
    public FileManager(Pane sObjPane, ActionManager aManager) {
        actionManager = aManager;
        soundObjPane = sObjPane;
        
        filePath = "~/Desktop/TuneComposerSave.txt";
    }
    
    /**
     * Saves the current state of the composition pane to the file in filePath.
     * Does not check to see if filePath is null. Will create a new file if file
     * in filePath does not exist. Will overwrite data stored in filePath.
     */
    public void save(){
        try{
          // Create file 
            File file = new File(filePath);
            System.out.println("made file");
            System.out.println(file.getPath());
//            System.out.println(file.getAbsoluteFile());
//            System.out.println(file.getCanonicalPath());
//            System.out.println(file.getCanonicalFile());
            Writer out = new BufferedWriter(new FileWriter(file));
            System.out.println("made out");
            out.write("Hello Java");
            System.out.println("wrote to out");
            //Close the output stream
            out.close();
        } catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Prompts the user to choose where to save data, and what to call the file
     * before saving. If the given filename already exists in chosen directory
     * then throws an Exception.
     * @throws FileAlreadyExistsException if chosen filepath and name exists.
     */
    public void saveAs() throws FileAlreadyExistsException {
        
       
    }
    
    /**
     * Prompts user to chose a file to open, and loads it into the compositionPane.
     * If file has not been saved before open() is called, then will prompt user
     * to save the current pane before opening another file.
     */
    public void open(){
        
    }
    
    /**
     * Returns true if soundObjPane has changed since last save, and false if
     * it hasn't. 
     * Also returns true if the file has never been saved or if filePath is null.
     * @return 
     */
    @Override
    public boolean hasChanged() {
        return false;
    }
    
    /**
     * Update lastSaveAction from top of undoStack in actionManger.
     * If the stack is empty, then sets lastSaveAction to null.
     */
    public void updateLastSaveAction() {
        
    }
    
    /**
     * Get the string representation of the entire soundObjectPane.
     * Returns the found string. If pane empty, then returns empty String. 
     * String must be in the format that soundObjectParser expects for loading. 
     * Does not perform any error checking on the string. 
     * @return string representing all soundObjects in this.soundObjectPane
     */
    private String getPaneObjectsInString() {
        return "";
    }
    
    /**
     * Prompt user to save the current file.
     * Used before executing action that would erase unsaved data. Returns 
     * false if the user selected cancel.
     * @return
     *      true if user selects yes or no. False if selected cancel.
     */
    private boolean promptToSave() {
        return false;
    }
    
    /**
     * Prompt user for what file they wish to open, and set filePath to selection.
     * If user selects cancel, then filePath is unchanged. If user selects a 
     * non txt file, then filePath will be unchanged, and the file browser
     * will reopen.
     * @return
     *      true if filePath was changed. False if filePath unchanged
     */
    private boolean promptOpenFilePath() {
        return false;
    }
    
    /**
     * Prompt the user to choose where to save the file, and what to name it,
     * then sets filePath to chosen path.
     * If user presses cancel, then filePath is unch
     * @return
     *      true if filePath changed, false if filePath unchanged
     */
    private boolean promptSaveFilePath() {
        return false;
    }
    
}

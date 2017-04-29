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
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static tunecomposer.SoundObjectParser.soundObjsToXML;
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
    private ArrayList<Action> lastSaveAction;
    
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
        
        filePath = "/home/lazarcl/Desktop/TuneComposerSave.txt";
    }
    
    /**
     * Saves the current state of the composition pane to the file in filePath.
     * Does not check to see if filePath is null. Will create a new file if file
     * in filePath does not exist. Will overwrite data stored in filePath.
     */
    public void save(){
        try{
          // Create file 
            System.out.println("made file");
            File file = new File(filePath);
            System.out.println(file.getPath());
            Writer out = new BufferedWriter(new FileWriter(file));
            String parseStr = getPaneObjectsAsString();
            out.write(parseStr);
            System.out.println("wrote to " + filePath);
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
//        createNewContactBox();
        if (promptSaveFilePath()) {
            System.out.println("Path set to: " + filePath);
            save();
        }
        else {
            System.out.println("path not set");
        }
       
    }
    
    /**
     * Prompts user to chose a file to open, and loads it into the compositionPane.
     * If file has not been saved before open() is called, then will prompt user
     * to save the current pane before opening another file.
     */
    public void open(){
        promptOpenFilePath();
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
        setChanged();
        lastSaveAction = actionManager.peekUndoStack();
        notifyObservers();
    }
    
    /**
     * Get the string representation of the entire soundObjectPane.
     * Returns the found string. If pane empty, then returns empty String. 
     * String must be in the format that soundObjectParser expects for loading. 
     * Does not perform any error checking on the string. 
     * @return string representing all soundObjects in this.soundObjectPane
     */
    private String getPaneObjectsAsString() {
        ArrayList<SoundObject> itemsArray = new ArrayList();
        for (Node n: soundObjPane.getChildren()) {
            Rectangle r = (Rectangle) n;
            SoundObject sObj = (SoundObject) (r).getUserData();
            if (sObj.getTopGesture() == null) {
                itemsArray.add(sObj);
            }
        }
        return soundObjsToXML(itemsArray);
    }
    
    /**
     * Prompt user to save the current file.
     * Used before executing action that would erase unsaved data. Returns 
     * false if the user selected cancel.
     * @return
     *      true if user selects yes or no. False if selected cancel.
     */
    private boolean promptToSave() {
        
////        Alert about = new Alert();
//        about.setTitle("Save");
//        about.setContentText("Would you like to save your changes?");
//        about.showAndWait();
        
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
        Stage choosingStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
             new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(choosingStage);
        if (selectedFile != null) {
            System.out.println("Filepath: " + selectedFile.getPath() );
            filePath = selectedFile.getPath();
            return true;
        }
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

        TextInputDialog input = new TextInputDialog("Untitled.txt");
        input.setTitle("Save As");
        input.setHeaderText("File Name:");
        ButtonType okay = new ButtonType("Okay", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
        Optional<String> result;
        result = input.showAndWait();
        
        if (result.isPresent()){
            Stage mainStage = new Stage();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open Resource File");
            File selectedFile = directoryChooser.showDialog(mainStage);
            
            if (selectedFile != null) {
                System.out.println("Filepath: " + selectedFile.getPath() );
                filePath = selectedFile.getPath();
                filePath = filePath + "/" + result.get();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the action that was at the top of the stack at the time that
     * the last save was made.
     * @return 
     */
    public ArrayList<Action> getLastSaveAction(){
        return lastSaveAction;
    }
    
}

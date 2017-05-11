package tunecomposer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.NONE;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static tunecomposer.SoundObjectParser.soundObjsToXML;
import tunecomposer.actionclasses.Action;

/**
 * Handles the creation, saving and opening of files.
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
        
    }
    
    /**
     * Creates a new, empty file.
     * If working composition is open and has unsaved changes, prompts user
     * if they want to save their changes.
     */    
    public void newFile(){
        if (hasUnsavedChanges()){
            if (promptToSave()){
                clearSession();
                filePath = null;
            }
        }
        else{
            clearSession();
            filePath = null;
        }
    }
    
    /**
     * Saves the current state of the composition pane to the file in filePath.
     * Does not check to see if filePath is null. Will create a new file if file
     * in filePath does not exist. Will overwrite data stored in filePath.
     */
    public void save(){
        if (!hasSavedAs()){
            try{
            saveAs();
            } catch(Exception e){
                System.err.println("Error: " + e.getMessage()); 
            }    
        }
        else{
            try{
                File file = new File(filePath);
                Writer out = new BufferedWriter(new FileWriter(file));
                String parseStr = getPaneObjectsAsString();
                out.write(parseStr);
                updateLastSaveAction();
                out.close();
            }
            catch (Exception e){
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Prompts the user to choose where to save data, and what to call the file
     * before saving. If the given filename already exists in chosen directory
     * then throws an Exception.
     * @throws FileAlreadyExistsException if chosen filepath and name exists.
     * @return
     *      true if the user went threw with the saveAs, false if the user canceled it
     */
    public boolean saveAs() throws FileAlreadyExistsException {
        boolean saveCanceled;
        String path = getSaveFilePath();
        if (path != null) {
            filePath = path;
            save();
            saveCanceled = true;
        }
        else {
            saveCanceled = false;
        }
        return saveCanceled;  
    }
     
    
    /**
     * Prompts user to chose a file to open, and loads it into the compositionPane.
     * If file has not been saved before open() is called, then will prompt user
     * to save the current pane before opening another file.
     */
    public void open(){
        if (hasUnsavedChanges()){
            if (!promptToSave()){
                return;
            }
        }

        if (promptOpenFilePath()){
            try{
                FileReader fileRead = new FileReader(filePath);
                BufferedReader buffRead = new BufferedReader(fileRead);
                try{
                    String fileText = buffRead.readLine();
                    SoundObjectParser parser = 
                            new SoundObjectParser(fileText,soundObjPane, actionManager);
                    clearSession();
                    parser.parseString().forEach((sObj) -> {
                        sObj.addToPane(soundObjPane);
                    });
                    setChanged();
                    notifyObservers();
                }
                catch(IOException ex){
                    System.err.println("An error occured while reading the file");
                }
            }
            catch(FileNotFoundException ex){
                System.err.println("Unable to open file '" + filePath + "'");
            } 
        }
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
    private void updateLastSaveAction() {
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
     * Clears the current session by clearing the action stacks, deleting all
     * sound objects, and resenting the file name and last save action.
     * Used for "new".
     */
    private void clearSession(){
        setChanged();
        soundObjPane.getChildren().clear();
        actionManager.undoStack.clear();
        actionManager.redoStack.clear();
        lastSaveAction = null;
        notifyObservers();
    }
    
    /**
     * Returns if the user has or has not "saved as" during the current session.
     * @return 
     *      true if the user has saved as. false if the user has not.
     */
    private boolean hasSavedAs(){
        return (filePath != null);
    }
    
    /**
     * Returns if the user has unsaved changes in the current session.
     * @return 
     *      true if there are unsaved changes. false if there are no unsaved changes.
     */
    public boolean hasUnsavedChanges(){
        return (lastSaveAction != actionManager.peekUndoStack());
    }
    
    /**
     * Prompt user to save the current file.
     * Used before executing action that would erase unsaved data. Returns 
     * false if the user selected cancel.
     * @return
     *      true if user selects yes or no. False if selected cancel.
     */
    protected boolean promptToSave() {
        Alert alert = new Alert(NONE);
        alert.setTitle("Save");
        alert.setContentText("Would you like to save your changes?");
        
        ButtonType save = new ButtonType("Save");
        ButtonType cancel= new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        ButtonType dontSave = new ButtonType("Don't Save");
        
        alert.getButtonTypes().setAll(save, cancel, dontSave);
        
        Optional<ButtonType> result = alert.showAndWait();
                
        if (result.get() == save){
            if (hasSavedAs()){
                save();
                return true;
            }
            else{
                try{
                    return saveAs();
                }
                catch (FileAlreadyExistsException e){
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
        
        else if (result.get() == cancel){
            return false;
        }
         
        else if (result.get() == dontSave){
            return true;
        }        
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
            filePath = selectedFile.getPath();
            return true;
        }
        return false;
    }
    
    /**
     * Prompt the user to choose where to save the file, and what to name it,
     * returns the selected path.
     * The name of the file always ends in .txt. If the user pressed 'cancel', 
     * then null is returned.
     * @return
     *      String of chosen path, null if save was canceled 
     */
    private String getSaveFilePath() {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save As");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                String path = file.getPath();
                if (!path.endsWith(".txt")) {
                    path += ".txt";
                }
                return path;
            }
            return null;
    }
    
    /**
     * Returns the action that was at the top of the stack at the time that
     * the last save was made.
     * @return 
     */
    public ArrayList<Action> getLastSaveAction(){
        return lastSaveAction;
    }
    
    public boolean promptToExit(){
        if (hasUnsavedChanges()){
            return promptToSave();
        }
        return true; //true if we want to exit
    }
    
}

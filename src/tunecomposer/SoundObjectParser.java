/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.lang.Integer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Class to convert soundObjects between string and object representations.
 * @author lazarcl
 */
public class SoundObjectParser {
    
    /**
     * Initially, the string to be parsed to SoundObjects. 
     * Will be reassigned as reader steps through, trimming the beginning so 
     * the beginning of the string is always the next section to read.
     * Tags cannot be inside tags: <tag<tag>>
     */
    private String iterateStr;
    
    /**
     * All the keywords for the different fields in a NoteBar object's string
     * representation.
     */
    private ArrayList<String> noteBarKeywords = new ArrayList();
    
    /**
     * Pane all sound Objects are placed in. Used in SoundObject constructors.
     */
    private final Pane soundObjPane;
    
    /**
     * ActionManager used for all undo/redos in the program.
     * Used for SoundObject constructors
     */
    private final ActionManager actionManager;
    
    /**
     * Instantiates the string field that will be stepped through as the class
     * reads the given XML string.
     * @param stringToParse string in XML format to convert into an object
     * @param soundObjPane must be pane all SoundObjects are put in
     * @param am must be the actionManager being used for undo/redo.
     */
    public SoundObjectParser(String stringToParse, Pane soundObjPane, ActionManager am) {
        this.soundObjPane = soundObjPane;
        this.actionManager = am;
        
        iterateStr = stringToParse.toLowerCase();
        
        noteBarKeywords.add("x");
        noteBarKeywords.add("y");
        noteBarKeywords.add("width");
        noteBarKeywords.add("instrument");
    }
    
    public ArrayList<SoundObject> parseString() {
        ArrayList<SoundObject> sObjs = stringToObjects(false);
        if (sObjs == null) {
            System.out.println("sObjs in parseString() was returned null");
        }
        if (sObjs.isEmpty()) {
            System.out.println("tried to paste objects, but no content found in clipboard: ");
            System.out.println("\t"+iterateStr);
            Thread.dumpStack();
        }
        return sObjs;
    }
    
    /**
     * Convert given string of SoundObject representations into an ArrayList of
     * fully instantiated SundObjects. Input string must have open and close
     * tags for all objects. See example at end of file.
     * @param isRecusive true if this is a recusive call, false if not.
     * @return ArrayList of SoundObjects created from a given, valid, string
     */
    public ArrayList<SoundObject> stringToObjects(boolean isRecursive) {
        if (iterateStr.isEmpty()) {
            System.out.println("empty iterateStr");
            Thread.dumpStack();
        }
        
        ArrayList<SoundObject> foundSoundObjs = new ArrayList();
        
        String tag;
        while (true) {
            tag = getNextTag();
            moveThroughNextTag();
            if (tag.equals("<notebar>")) {
                ArrayList<Integer> noteData = getNoteData();
                NoteBar note = makeNote(noteData);
                foundSoundObjs.add(note);
                moveThroughNextTag();
            }
            else if (tag.equals("<gesture>")) {
                ArrayList<SoundObject> gestureContents;
                gestureContents = stringToObjects(true);
                if (!gestureContents.isEmpty()) {
                    Gesture gest = createGesture(gestureContents);
                    foundSoundObjs.add(gest);
                }
            }
            else if (tag.equals("</gesture>")) {
               return foundSoundObjs;
            }
            else if (tag.equals("NO TAG")) {
                    System.out.println(isRecursive);
                if (isRecursive) {
                    String error = "No end tag in XML: " + iterateStr;
                    throw new InvalidXMLTagException(error);
                }
                else {
                    for (SoundObject sObj : foundSoundObjs) {
                        System.out.println("add item" + sObj);
//                        sObj.addToPane(soundObjPane);
                    }
                    return foundSoundObjs;
                }
            }
            else {
                String error = "Tag, '"+tag+"', not recognized: ";
                throw new InvalidXMLTagException(error);
            }
        }
    }
    
    /**
     * Get the index for the beginning of the next tag in
     * iterateStr.
     * @param start Integer reference to alter to start of next tag's index
     * @return true if tag found, false if no tag found
     * @throws InvalidXMLTagException if empty tag, or no end to tag
     */
    private int getNextTagStartIndex() throws InvalidXMLTagException {
        int tagStart = iterateStr.indexOf("<");
        
        if ( iterateStr.isEmpty() ) {
            return -1;
        }
        
        //check if tag contains nothing
        String nextChar = iterateStr.substring(tagStart+1, tagStart+2);
        if ( nextChar.equals('>') ) {
            String error = "empty tag found: " + iterateStr;
            throw new InvalidXMLTagException(error);
        }
        
        // check if a stray less-than symbol is before the greater-than symbol.
        int closeTag = iterateStr.indexOf(">");
        if (tagStart > closeTag) {
            String error = "stray close tag found: " + iterateStr;
            throw new InvalidXMLTagException(error);
        }
        // no tagStart, but has a close tag
        if (tagStart == -1 && closeTag > -1) {
            String error = "no start to tag, but there is a close: " + iterateStr;
            throw new InvalidXMLTagException(error);
        }
        return tagStart;
    }
    
    /**
     * Get the end index of provided tag's start index in iterateStr.
     * Provide the tag to find the end index of by giving the start index.
     * @param tagStart index the tag starts at
     * @return the index of the less-than symbol that closes the tag
     * @throws InvalidXMLTagException if no end found to tag
     */
    private int getEndIndexOfTag(int tagStart) throws InvalidXMLTagException {
        int tagEnd = iterateStr.indexOf(">", tagStart);
        if ( tagEnd == -1 ) {
            String error = "No end tag found: \n" + iterateStr;
            throw new InvalidXMLTagException(error);
        }
        return tagEnd;
    }
    
    /**
     * Returns next open or close tag in given string. 
     * Tag type is not checked to be valid except for a greater-than, less-than,
     * and at least one character inside. Any character before the greater-than 
     * will be ignored. Returns "NO TAG" if no tag is found in given string.
     * @throws InvalidXMLTagException
     *          If no closing for a tag found or tags are empty
     * @return "NO TAG" if no tag is found, else the next tag in iterateStr
     */
    public String getNextTag() throws InvalidXMLTagException{
        int tagStart = getNextTagStartIndex();
        if ( tagStart == -1 ) {
            return "NO TAG";
        }
        int tagEnd = getEndIndexOfTag(tagStart);
        //if tagEnd is end of iterateStr, return tagStart to end
        if ( tagEnd == iterateStr.length() - 1 ) {
            return iterateStr.substring(tagStart);
        }
        else {
            return iterateStr.substring(tagStart, tagEnd+1);
        }
    }
    
    /**
     * Trims off the beginning of iterateStr until the end of the next tag. If
     * No more tags, then does nothing. The next tag must not be empty.
     */
    public void moveThroughNextTag() {
        int tagStart = getNextTagStartIndex();
        if ( tagStart == -1 ) {
            return;
        }
        int tagEnd = getEndIndexOfTag(tagStart);
        iterateStr = iterateStr.substring(tagEnd + 1);
    }
    
    /**
     * Reads the given string for NoteBar data. Spelling must be correct, no
     * spaces between variable name, the colon, and the value. Spaces should
     * separate each datapoint. Will begin reading at start of given str, and
     * returns when reaching end tag. Exception if finds anything unexpected.
     * @return 
     *       ArrayList with values of note's x and y coordinate, length, and
     *       instrument number in order added to noteBarKeywords in constructor.
     */
    public ArrayList<Integer> getNoteData() {    
        String endTag = getNextTag();
        if (!endTag.equals("</notebar>")) {
            String error = "'</notebar>' expected and not found: \n" + iterateStr;
            throw new InvalidXMLTagException(error);
        }
        int tagStart = getNextTagStartIndex();
        
        ArrayList<Integer> noteValues = new ArrayList();
        String dataString = iterateStr.substring(0, tagStart);
        
        //find all required values for NoteBars
        for (String field : noteBarKeywords) {
            Integer value = getDataFieldValue(field, dataString);
            noteValues.add(value);
        }
        System.out.println("note data: " + noteValues);
        return noteValues;
    }
    
    /**
     * Get the value of a field from a given string. 
     * After given field, find the sequence of values in given string, and 
     * return as int. No spaces, if field-name not in given string, then throws
     * exception. Field name must be followed directly by a colon, and then by
     * the value. Value must be a sequence of number-characters with no letters
     * or symbols.
     * @param fieldName field name of which to return value of
     * @param str string extract value from
     * @return value extracted from the string as an int
     * @throws IndexOutOfBoundsException if fieldName not in str
     */
    private int getDataFieldValue(String fieldName, String str) 
                                throws IndexOutOfBoundsException {
        if (!str.contains(fieldName)) {
            String error = "data string does not contain fieldName: '" 
                                        + fieldName + "' str: '" + str;
            throw new IndexOutOfBoundsException(error);
        }
        //get index before and after value
        int valIndex = str.indexOf(fieldName) + fieldName.length();
        int endValIndex = str.indexOf(" ", valIndex);
        //grab value and return as int
        String valString = str.substring(valIndex+1, endValIndex);
        int valInt = 0;
        try {
            valInt = Integer.parseInt(valString);
        } catch (NumberFormatException ex) {
            System.out.println("bad data field value, "
                    + "(fieldName, str): (" + fieldName + ", " + str + ")");
            Thread.dumpStack();
        }
        return valInt;
    }

    private NoteBar makeNote(ArrayList<Integer> noteData) {
        int x = noteData.get(0);
        int y = noteData.get(1);
        int width = noteData.get(2);
        int instrument = noteData.get(3);
        
        NoteBar note = new NoteBar(x, y, width, instrument, actionManager, soundObjPane);
        note.visualRectangle.setUserData(note);

        return note;
    }
    
    /**
     * Creates a gesture with the given contents. Sets the mouse handlers for
     * all children of gesture.
     * @param gestureContents populated ArrayList of SoundObjects
     * @return 
     */
    private Gesture createGesture(ArrayList<SoundObject> gestureContents) {
        //add if gest not empty
        Gesture gest = new Gesture(gestureContents, actionManager, soundObjPane);
        gest.visualRectangle.setUserData(gest);
        gest.groupDontAddVisual();
        return gest;
    }
    
    /**
     * Exceptions for tags in the given XML that are not declared, empty, or
     * unending.
     */
    private static class InvalidXMLTagException extends StringIndexOutOfBoundsException {
        
        public InvalidXMLTagException(String s) {
            super(s);
        }
    }
}




/* EXAMPLE XML TREE:
<gesture>
	<gesture>
		<notebar 
			x:10
			y:340
			width:39
			instrument:1
		</notebar>
		<notebar
			x:359
			y:499
			width:39
			instrument:1
		</notebar>
	</gesture>
	<notebar>
		x:339
		y:399
		width:45
		instrument:43
	</notebar>
</gesture>
*/
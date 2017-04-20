/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import com.sun.javaws.exceptions.InvalidArgumentException;
import java.util.ArrayList;

/**
 * Class to convert soundObjects between string and object representations.
 * @author lazarcl
 */
public class SoundObjectParser {
    
    
    /**
     * Convert given string of SoundObject representations into an ArrayList of
     * fully instantiated SundObjects. Input string must have open and close
     * tags for all objects. See example at end of file.
     * @param inString 
     *          String in XML format that is error free. 
     * @return ArrayList of SoundObjects created from a given, valid, string
     */
    public ArrayList<SoundObject> stringToObjects(String inString) {
        ArrayList<SoundObject> foundSoundObjs = new ArrayList();
        //TODO
//        while
//        tag = getNextTag(str)
//        if t is notebar:
//            get data
//            make notebar
//            add to foundSoundObjs
//        elif t is gesture:
//            s = stringToObjs(str)
//            if nothing found, then don't add
//            add s to foundSoundObjs
//        elif t is /gesture:
//           create gesture, gest, containing all items in foundSoundObjects
//           return gest;
//        elif:
//           no tag found, are we at end of string?
//           yes: return foundSoundObjs
//           no: error
                    
                    
                    
        return null;
    }
    
    
    /**
     * Returns next open or close tag in given string. 
     * Tag type is not checked to be valid except for a greater-than, less-than,
     * and at least one character inside. Any character before the greater-than 
     * will be ignored. Returns "NO TAG" if no tag is found in given string.
     * @param inString string with no characters before a XML tag.
     * @throws InvalidXMLTagException
     *          If no closing for a tag found or tags are empty
     */
    private String getNextTag(String inString) 
            throws InvalidXMLTagException{
        String tag = "";
        int tagStart = inString.indexOf("<");
        int tagEnd = inString.indexOf(">", tagStart);
        if ( tagStart == -1 ) {
            return "NO TAG";
        }
        if ( tagEnd == -1 ) {
            String error = "No end tag found: \n" + inString;
            throw new InvalidXMLTagException(error);
        }
        if ( tagStart == (tagEnd-1) ) {
            String error = "empty tag found: " + inString;
            throw new InvalidXMLTagException(error);
        }
        tag = inString.substring(tagStart, tagEnd+1);
        System.out.println(tag);
        return tag;
    }
    
    /**
     * Reads the given string for NoteBar data. Spelling must be correct, no
     * spaces between variable name, the colon, and the value. Spaces should
     * separate each datapoint. Will begin reading at start of given str, and
     * returns when reaching end tag. Exception if finds anything unexpected.
     * @param str string beginning directly after the found notebar tag
     * @return 
     *       ArrayList with values of note's x and y coordinate, length, and
     *       instrument number in that order
     */
    private ArrayList<Integer> getNoteData(String str) {
        ArrayList<Integer> data = new ArrayList();
        //TODO
        
        
        return data;
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
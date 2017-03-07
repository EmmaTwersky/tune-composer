/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * @author EmmaTwersky
 */

public class InstrumentSelection {
    private static final int PIANO = 0; //1-8
    private static final int PIANO_CHANNEL = 0;
    private static final int HARPSICORD = 7; //7
    private static final int HARPSICORD_CHANNEL = 1;
    private static final int MARIMBA = 13; //13
    private static final int MARIMBA_CHANNEL = 2;
    private static final int ORGAN = 17; //17-24
    private static final int ORGAN_CHANNEL = 3;
    private static final int ACCORDIAN = 22; //22
    private static final int ACCORDIAN_CHANNEL = 4;
    private static final int GUITAR = 25; //25-32
    private static final int GUITAR_CHANNEL = 5;
    private static final int VIOLIN = 41; //41
    private static final int VIOLIN_CHANNEL = 6;
    private static final int FRENCHHORN = 61; //61
    private static final int FRENCHHORN_CHANNEL = 7;
    
    public static final HashMap INSTRUMENT_VALUES = new HashMap();
    
    
    public int setInstrumentValue(String instrument){
        
    }
}

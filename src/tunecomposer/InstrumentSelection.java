package tunecomposer;

import java.util.HashMap;

/**
 * This class creates and stores HashMaps to access MidiPlayer value constants.
 * @author EmmaTwersky
 */
public class InstrumentSelection {
    /**
     * The Midi value of a given instrument name, found at
     * <https://www.midi.org/specifications/item/gm-level-1-sound-set>.
     * The channels signify that each instrument is a private MidiEvent channel.
     */
    private static final int PIANO = 0; //1-8
    private static final int PIANO_CHANNEL = 0;
    private static final int HARPSICHORD = 7; //7
    private static final int HARPSICHORD_CHANNEL = 1;
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
    
    /**
     * Initializes the HashMaps to store the value and channel information.
     */
    public HashMap<String, Integer> INSTRUMENT_VALUES = new HashMap<String, Integer>(8);
    public HashMap<String, Integer> INSTRUMENT_CHANNELS = new HashMap<String, Integer>(8);

    /**
     * Constructs the value and channel HashMaps.
     */
    InstrumentSelection() {
        INSTRUMENT_VALUES.put("Piano", PIANO);
        INSTRUMENT_VALUES.put("Harpsichord", HARPSICHORD);
        INSTRUMENT_VALUES.put("Marimba", MARIMBA);
        INSTRUMENT_VALUES.put("Organ", ORGAN);
        INSTRUMENT_VALUES.put("Accordion", ACCORDIAN);
        INSTRUMENT_VALUES.put("Guitar", GUITAR);
        INSTRUMENT_VALUES.put("Violin", VIOLIN);
        INSTRUMENT_VALUES.put("FrenchHorn", FRENCHHORN);
        
        INSTRUMENT_CHANNELS.put("Piano", PIANO_CHANNEL);
        INSTRUMENT_CHANNELS.put("Harpsichord", HARPSICHORD_CHANNEL);
        INSTRUMENT_CHANNELS.put("Marimba", MARIMBA_CHANNEL);
        INSTRUMENT_CHANNELS.put("Organ", ORGAN_CHANNEL);
        INSTRUMENT_CHANNELS.put("Accordion", ACCORDIAN_CHANNEL);
        INSTRUMENT_CHANNELS.put("Guitar", GUITAR_CHANNEL);
        INSTRUMENT_CHANNELS.put("Violin", VIOLIN_CHANNEL);
        INSTRUMENT_CHANNELS.put("FrenchHorn", FRENCHHORN_CHANNEL);
    }
    
    /**
     * Returns the Midi value of a given instrument name.
     * 
     * @param instrument the instrument name
     * @return the integer value the MidiPlayer recognizes for that instrument
     */
    public int getInstrumentValue(String instrument){
        return INSTRUMENT_VALUES.get(instrument);
    }
    
    /**
     * Returns a channel value for a given instrument name.
     * 
     * @param instrument the instrument name
     * @return the integer value the MidiPlayer channel
     */
    public int getInstrumentChannel(String instrument){
        return INSTRUMENT_CHANNELS.get(instrument);
    }
}

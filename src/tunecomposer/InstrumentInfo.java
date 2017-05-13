package tunecomposer;

import java.util.HashMap;

/**
 * Creates and stores HashMaps to access MidiPlayer value constants.
 */
public class InstrumentInfo {
    /**
     * The Midi value of a given instrument name, found at
     * <https://www.midi.org/specifications/item/gm-level-1-sound-set>.
     * The channels signify that each instrument is a private MidiEvent channel.
     */
    private static final int PIANO = 5; //1-8
    private static final int PIANO_CHANNEL = 0;
    
    private static final int HARPSICHORD = 7; //7
    private static final int HARPSICHORD_CHANNEL = 1;
    
    private static final int MARIMBA = 13; //13
    private static final int MARIMBA_CHANNEL = 2;
    
    private static final int ORGAN = 18; //17-24
    private static final int ORGAN_CHANNEL = 3;
    
    private static final int ACCORDIAN = 22; //22
    private static final int ACCORDIAN_CHANNEL = 4;
    
    private static final int GUITAR = 29; //25-32
    private static final int GUITAR_CHANNEL = 5;
    
    private static final int VIOLIN = 42; //41
    private static final int VIOLIN_CHANNEL = 6;
    
    private static final int FRENCHHORN = 61; //61
    private static final int FRENCHHORN_CHANNEL = 7;
    
    private static final int BASS = 96;
    private static final int BASS_CHANNEL = 8;
    
    /**
     * Initializes the HashMaps to store the value and channel information.
     */
    public HashMap<String, Integer> INSTRUMENT_VALUES = new HashMap();
    
    /**
     * Map instrument names to their MIDI id values.
     * Instrument names are Strings, and the ids are integers. The ids must
     * be valid MidiPlayer ids that are recognized by the libarary.
     */
    public HashMap<String, Integer> INSTRUMENT_CHANNELS = new HashMap();

    /**
     * Constructs the value and channel HashMaps.
     */
    InstrumentInfo() {
        INSTRUMENT_VALUES.put("Piano", PIANO);
        INSTRUMENT_VALUES.put("Harpsichord", HARPSICHORD);
        INSTRUMENT_VALUES.put("Marimba", MARIMBA);
        INSTRUMENT_VALUES.put("Organ", ORGAN);
        INSTRUMENT_VALUES.put("Accordion", ACCORDIAN);
        INSTRUMENT_VALUES.put("Guitar", GUITAR);
        INSTRUMENT_VALUES.put("Violin", VIOLIN);
        INSTRUMENT_VALUES.put("FrenchHorn", FRENCHHORN);
        INSTRUMENT_VALUES.put("Bass", BASS);
        
        INSTRUMENT_CHANNELS.put("Piano", PIANO_CHANNEL);
        INSTRUMENT_CHANNELS.put("Harpsichord", HARPSICHORD_CHANNEL);
        INSTRUMENT_CHANNELS.put("Marimba", MARIMBA_CHANNEL);
        INSTRUMENT_CHANNELS.put("Organ", ORGAN_CHANNEL);
        INSTRUMENT_CHANNELS.put("Accordion", ACCORDIAN_CHANNEL);
        INSTRUMENT_CHANNELS.put("Guitar", GUITAR_CHANNEL);
        INSTRUMENT_CHANNELS.put("Violin", VIOLIN_CHANNEL);
        INSTRUMENT_CHANNELS.put("FrenchHorn", FRENCHHORN_CHANNEL);
        INSTRUMENT_CHANNELS.put("Bass", BASS_CHANNEL);
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

    /**
     * Return the key in INSTRUMENT_VALUES that is mapped to given int.
     * If no map is found to given value, then prints error in console and 
     * returns "NOT FOUND".
     * @param instrument MIDI player instrument id value
     * @return Instrument name, or "NOT FOUND" if instrument name not recognized
     */
    public String getInstName(int instrument) {
        for (String key : INSTRUMENT_VALUES.keySet()) {
          if (INSTRUMENT_VALUES.get(key) == instrument) {
            return key;               // we found it
          }
        }
        return "NOT FOUND"; //give default piano
    }
}

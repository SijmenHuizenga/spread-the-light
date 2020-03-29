package nl.fsteamdelft.spreadthelight.morse;

import java.util.HashMap;
import java.util.Map;

public class MorseCode {
    
    public static final HashMap<Character, String> dict;
    
    static {
        dict = new HashMap<>();
        dict.put('a', "._");
        dict.put('b', "_...");
        dict.put('c', "_._.");
        dict.put('d', "_..");
        dict.put('e', ".");
        dict.put('f', ".._.");
        dict.put('g', "__.");
        dict.put('h', "....");
        dict.put('i', "..");
        dict.put('j', ".___");
        dict.put('k', "_._");
        dict.put('l', "._..");
        dict.put('m', "__");
        dict.put('n', "_.");
        dict.put('o', "___");
        dict.put('p', ".__.");
        dict.put('q', "__._");
        dict.put('r', "._.");
        dict.put('s', "...");
        dict.put('t', "_");
        dict.put('u', ".._");
        dict.put('v', "..._");
        dict.put('w', ".__");
        dict.put('x', "_.._");
        dict.put('y', "_.__");
        dict.put('z', "__..");
        dict.put('1', ".____");
        dict.put('2', "..___");
        dict.put('3', "...__");
        dict.put('4', "...._");
        dict.put('5', ".....");
        dict.put('6', "_....");
        dict.put('7', "__...");
        dict.put('8', "___..");
        dict.put('9', "____.");
        dict.put('0', "_____");
    }

    public static char code2letter(String code) {
        for(char knownLetter : dict.keySet()) {
            if(code.equals(dict.get(knownLetter))){
                return knownLetter;
            }
        }
        return '?';
    }

}

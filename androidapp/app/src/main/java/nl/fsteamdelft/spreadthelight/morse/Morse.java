package nl.fsteamdelft.spreadthelight.morse;

import java.util.ArrayList;
import java.util.List;

public class Morse {

    public static List<MorseChar> encode(String message) {
        List<MorseChar> outCodes = new ArrayList<>();

        if (message.isEmpty()) {
            return new ArrayList<>();
        }

        for (char c : message.toCharArray()) {
            outCodes.add(MorseChar.fromChar(c));
        }

        return outCodes;
    }
}
package nl.fsteamdelft.spreadthelight.morse;

import java.util.Arrays;
import java.util.List;

import static nl.fsteamdelft.spreadthelight.morse.MorseCode.DASH;
import static nl.fsteamdelft.spreadthelight.morse.MorseCode.DOT;

public enum MorseChar
{
    A(      Arrays.asList(DOT, DASH)),
    B(      Arrays.asList(DASH, DOT, DOT ,DOT)),
    C(      Arrays.asList(DASH, DOT, DASH ,DOT)),
    D(      Arrays.asList(DASH, DOT, DOT)),
    E(      Arrays.asList(DOT)),
    F(      Arrays.asList(DOT, DOT, DASH ,DOT)),
    G(      Arrays.asList(DASH, DASH, DOT)),
    H(      Arrays.asList(DOT, DOT, DOT ,DOT)),
    I(      Arrays.asList(DOT, DOT)),
    J(      Arrays.asList(DOT, DASH, DASH ,DASH)),
    K(      Arrays.asList(DASH, DOT, DASH )),
    L(      Arrays.asList(DOT, DASH, DOT ,DOT)),
    M(      Arrays.asList(DASH, DASH )),
    N(      Arrays.asList(DASH, DOT )),
    O(      Arrays.asList(DASH, DASH, DASH )),
    P(      Arrays.asList(DOT, DASH, DASH, DOT)),
    Q(      Arrays.asList(DASH, DASH, DOT, DASH)),
    R(      Arrays.asList(DOT, DASH ,DOT)),
    S(      Arrays.asList(DOT, DOT ,DOT)),
    T(      Arrays.asList(DASH)),
    U(      Arrays.asList(DOT, DOT, DASH )),
    V(      Arrays.asList(DOT, DOT, DOT ,DASH)),
    W(      Arrays.asList(DOT, DASH ,DASH)),
    X(      Arrays.asList(DASH, DOT, DOT ,DASH)),
    Y(      Arrays.asList(DASH, DOT, DASH ,DASH)),
    Z(      Arrays.asList(DASH, DASH, DOT ,DOT)),
    ONE(    Arrays.asList(DOT, DASH, DASH, DASH ,DASH)),
    TWO(    Arrays.asList(DOT, DOT, DASH, DASH ,DASH)),
    THREE(  Arrays.asList(DOT, DOT, DOT, DASH ,DASH)),
    FOUR(   Arrays.asList(DOT, DOT, DOT, DOT ,DASH)),
    FIVE(   Arrays.asList(DOT, DOT, DOT, DOT ,DOT)),
    SIX(    Arrays.asList(DASH, DOT, DOT, DOT ,DOT)),
    SEVEN(  Arrays.asList(DASH, DASH, DOT, DOT ,DOT)),
    EIGHT(  Arrays.asList(DASH, DASH, DASH, DOT ,DOT)),
    NINE(   Arrays.asList(DASH, DASH, DASH, DASH ,DOT)),
    ZERO(   Arrays.asList(DASH, DASH, DASH, DASH ,DASH));

    private List<MorseCode> codes;

    MorseChar(List<MorseCode> codes) {
        this.codes = codes;
    }

    public List<MorseCode> getMorseCodeSequence()
    {
        return this.codes;
    }

    public static MorseChar fromChar(char c)
    {
        if(java.lang.Character.isDigit(c))
            return fromDigit(c);

        return valueOf(String.valueOf(c).toUpperCase());
    }

    public static MorseChar fromDigit(char c)
    {
        switch (c)
        {
            case '1':
                return ONE;
            case '2':
                return TWO;
            case '3':
                return THREE;
            case '4':
                return FOUR;
            case '5':
                return FIVE;
            case '6':
                return SIX;
            case '7':
                return SEVEN;
            case '8':
                return EIGHT;
            case '9':
                return NINE;
            case '0':
                return ZERO;
            default:
                throw new IllegalArgumentException();
        }
    }
}
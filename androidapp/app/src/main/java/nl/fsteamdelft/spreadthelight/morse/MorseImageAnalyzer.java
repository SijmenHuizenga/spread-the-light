package nl.fsteamdelft.spreadthelight.morse;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.databinding.ObservableField;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import static nl.fsteamdelft.spreadthelight.ui.SendingFragment.*;

public class MorseImageAnalyzer implements ImageAnalysis.Analyzer {

    private ObservableField<String> output;
    private LinkedList<Float> brightnessHistory = new LinkedList<>();
    private float brighnessRunningAverage = 100f;

    // how much the time may differ from the actual time to still be detected.
    private static final int fidality = MORSE_TIMEUNIT / 4;

    // high = true = ON
    // low = false = OFF
    // Detection shows the light is on/of value since hiLowStatChangeMoment
    private boolean hiLowState = false;
    private long hiLowStateChangeMoment = 0;
    private String code = "";
    private String sentence ="";

    public MorseImageAnalyzer(ObservableField<String> brightnesss) {
        this.output = brightnesss;
        for(int i = 0; i < 180; i++) {
            brightnessHistory.add(100f);
        }
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        Rect aoi = getAreaOfIntrest(image.getCropRect());

        // Since format in ImageAnalysis is YUV, image.planes[0] contains the Y (luminance) plane
        ImageProxy.PlaneProxy plane = image.getPlanes()[0];
        ByteBuffer buffer = plane.getBuffer();
        byte[] bytes = new byte[image.getWidth()*image.getHeight()];
        buffer.get(bytes);

        long total = 0;
        for(int x = aoi.left; x <= aoi.right; x++) {
            for(int y = aoi.top; y <= aoi.bottom; y++) {
                total += ((int) bytes[y*image.getWidth()+x]) & 0xFF;
            }
        }
        float brightness = total / (float)(aoi.width()*aoi.height());

        brightnessHistory.addFirst(brightness);
        float removedAverage = brightnessHistory.removeLast();
        brighnessRunningAverage = (brightnessHistory.size() * brighnessRunningAverage + brightness - removedAverage) / brightnessHistory.size();


        boolean highlow;
        if(hiLowState && brightness < brighnessRunningAverage - 10) {
            highlow = false;
        } else if(!hiLowState && brightness > brighnessRunningAverage + 10) {
            highlow = true;
        } else {
            image.close();
            return;
        }


        long now = System.currentTimeMillis();
        long timediff = now - hiLowStateChangeMoment;
        System.out.println("average: " + brighnessRunningAverage + " brightness: " + brightness + " timediff: " + timediff);
        if(highlow) {
            // LOW -> HIGH
            if(timediff - fidality < MORSE_TIMEUNIT_LETTER && timediff + fidality > MORSE_TIMEUNIT_LETTER) {
                sentence += MorseCode.code2letter(code);
                code = "";
                System.out.println("/");
            } else if(timediff - fidality < MORSE_TIMEUNIT_SPACE && timediff + fidality > MORSE_TIMEUNIT_SPACE) {
                sentence += " ";
                System.out.println(" ");
            } else {
                System.out.println("? low->hi" + timediff);
            }
        } else {
            // HIGH -> LOW
            if(timediff - fidality < MORSE_TIMEUNIT_DASH && timediff + fidality > MORSE_TIMEUNIT_DASH) {
                code += "_";
                System.out.println("_");
            } else if(timediff - fidality < MORSE_TIMEUNIT_DOT && timediff + fidality > MORSE_TIMEUNIT_DOT) {
                code += ".";
                System.out.println(".");
            } else {
                System.out.println("? hi->low" + timediff);
            }
        }
        hiLowStateChangeMoment = now;
        hiLowState = highlow;

        this.output.set(sentence + code);

        image.close();
    }

    public static Rect getAreaOfIntrest(Rect area) {
        //center 50x50 pixels
        return new Rect(area.centerX()-25, area.centerY()-25, area.centerX()+25, area.centerY()+25);
    }
}

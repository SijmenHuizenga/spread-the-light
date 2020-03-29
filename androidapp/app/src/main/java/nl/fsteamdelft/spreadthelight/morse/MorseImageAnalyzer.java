package nl.fsteamdelft.spreadthelight.morse;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.databinding.ObservableField;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class MorseImageAnalyzer implements ImageAnalysis.Analyzer {

    private ObservableField<String> brightnesssOutput;
    private LinkedList<Float> brightnessHistory = new LinkedList<>();
    private float brighnessRunningAverage = 100f;

    public MorseImageAnalyzer(ObservableField<String> brightnesss) {
        this.brightnesssOutput = brightnesss;
        for(int i = 0; i < 90; i++) {
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

        this.brightnesssOutput.set("Average: "+ brighnessRunningAverage);

        boolean highlow = brightness > brighnessRunningAverage;

        System.out.println(System.currentTimeMillis() + " " + brightness + " " + brighnessRunningAverage + " " + (highlow ? 1 : 0));
        image.close();
    }

    public static Rect getAreaOfIntrest(Rect area) {
        //center 50x50 pixels
        return new Rect(area.centerX()-25, area.centerY()-25, area.centerX()+25, area.centerY()+25);
    }
}

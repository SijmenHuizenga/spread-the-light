package nl.fsteamdelft.spreadthelight.morse;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import java.nio.ByteBuffer;

public class MorseImageAnalyzer implements ImageAnalysis.Analyzer {


    private ObservableField<String> brightnesss;

    public MorseImageAnalyzer(ObservableField<String> brightnesss) {
        this.brightnesss = brightnesss;
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
        float average = total / (float)(aoi.width()*aoi.height());
        this.brightnesss.set(""+average);

        System.out.println(System.currentTimeMillis() + " " + average);
        image.close();
    }

    public static Rect getAreaOfIntrest(Rect area) {
        //center 50x50 pixels
        return new Rect(area.centerX()-25, area.centerY()-25, area.centerX()+25, area.centerY()+25);
    }
}

package nl.fsteamdelft.spreadthelight.morse;

import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.media.Image;
import android.media.ImageReader;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import java.nio.ByteBuffer;

public class MorseImageAnalyzer implements ImageAnalysis.Analyzer {

    long timeSinceLastFrame = 0;

    @Override
    public void analyze(@NonNull ImageProxy image) {
        // area of intrest is the center 50x50 pixels
        Rect area = image.getCropRect();
        Rect aoi = new Rect(area.centerX()-25, area.centerY()-25, area.centerX()+25, area.centerY()+25);

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
        System.out.println(average);

//        System.out.println("ms since last frame: " + (timeSinceLastFrame - System.currentTimeMillis()));
        timeSinceLastFrame = System.currentTimeMillis();
        image.close();
    }
}

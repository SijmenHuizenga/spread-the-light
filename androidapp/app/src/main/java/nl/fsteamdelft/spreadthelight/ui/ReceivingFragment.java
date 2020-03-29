package nl.fsteamdelft.spreadthelight.ui;

import android.os.Bundle;
import android.util.Size;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.camera.core.*;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.common.util.concurrent.ListenableFuture;
import nl.fsteamdelft.spreadthelight.R;
import nl.fsteamdelft.spreadthelight.morse.MorseImageAnalyzer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ReceivingFragment extends Fragment {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_receiving, container, false);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this.getContext());
        previewView = root.findViewById(R.id.preview_view);
        previewView.post(this::startCamera);
        return root;
    }

    private void startCamera() {
        CameraSelector selector = (new CameraSelector.Builder())
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = (new Preview.Builder())
                .setTargetResolution(new Size(640, 480)).build();
        preview.setSurfaceProvider(previewView.getPreviewSurfaceProvider());

        ImageAnalysis imageAnalysis = (new ImageAnalysis.Builder())
                .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetResolution(new Size(640, 480))
                .build();

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), new MorseImageAnalyzer());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProviderFuture.get().bindToLifecycle(this, selector, imageAnalysis, preview);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this.getContext()));
    }

}
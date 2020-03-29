package nl.fsteamdelft.spreadthelight.ui;

import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.Fragment;
import com.google.common.util.concurrent.ListenableFuture;
import nl.fsteamdelft.spreadthelight.R;
import nl.fsteamdelft.spreadthelight.databinding.FragmentReceivingBinding;
import nl.fsteamdelft.spreadthelight.morse.MorseImageAnalyzer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ReceivingFragment extends Fragment {

    private FragmentReceivingBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    public final ObservableField<String> brightness = new ObservableField<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_receiving, container, false);
        binding.setBrightness(brightness);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this.getContext());
        binding.previewView.post(this::startCamera);
        return binding.getRoot();
    }

    private void startCamera() {
        CameraSelector selector = (new CameraSelector.Builder())
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = (new Preview.Builder())
                .setTargetResolution(new Size(640, 480)).build();
        preview.setSurfaceProvider(binding.previewView.getPreviewSurfaceProvider());

        ImageAnalysis imageAnalysis = (new ImageAnalysis.Builder())
                .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetResolution(new Size(640, 480))
                .build();

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), new MorseImageAnalyzer(brightness));

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProviderFuture.get().bindToLifecycle(this, selector, imageAnalysis, preview);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this.getContext()));
    }

}
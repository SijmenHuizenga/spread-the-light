package nl.fsteamdelft.spreadthelight.ui;

import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.common.util.concurrent.ListenableFuture;
import nl.fsteamdelft.spreadthelight.R;
import nl.fsteamdelft.spreadthelight.morse.Morse;
import nl.fsteamdelft.spreadthelight.morse.MorseChar;
import nl.fsteamdelft.spreadthelight.morse.MorseCode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class SendingFragment extends Fragment implements View.OnClickListener {

    //The length of a dot is 1 time unit.
    //A dash is 3 time units.
    //The space between symbols (dots and dashes) of the same letter is 1 time unit.
    //The space between letters is 3 time units.
    //The space between words is 7 time units.
    public static final int MORSE_TIMEUNIT = 200;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Camera camera;
    private Button flashlightsubmitbutton;
    private EditText flashlightinput;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sending, container, false);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this.getContext());

        flashlightinput = root.findViewById(R.id.flashlightinput);
        flashlightsubmitbutton = root.findViewById(R.id.flashlightsubmitbutton);
        flashlightsubmitbutton.setOnClickListener(this);
        root.post(this::startCamera);
        return root;
    }

    private void startCamera() {
        CameraSelector selector = (new CameraSelector.Builder())
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = (new ImageAnalysis.Builder())
            .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(new Size(1, 1))
            .build();

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), image -> {
            // we have to have a consumer, else we can't access the torch.
        });

        cameraProviderFuture.addListener(() -> {
            try {
                camera = cameraProviderFuture.get().bindToLifecycle(this, selector, imageAnalysis);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this.getContext()));
    }

    @Override
    public void onClick(View view) {
        flashlightsubmitbutton.setText("Sending...");
        flashlightsubmitbutton.setEnabled(false);
        flashlightinput.setEnabled(false);

        Executors.newSingleThreadExecutor().submit(() -> {
            send(flashlightinput.getText().toString());

            getActivity().runOnUiThread(() -> {
                flashlightsubmitbutton.setText("Send!");
                flashlightsubmitbutton.setEnabled(true);
                flashlightinput.setEnabled(true);
            });

        });
    }

    private void send(String text) {
        for (String word : text.split(" ")) {
            for(MorseChar c : Morse.encode(text)) {
                for(MorseCode code : c.getMorseCodeSequence()) {
                    camera.getCameraControl().enableTorch(true);
                    sleep(code == MorseCode.DOT ? MORSE_TIMEUNIT : MORSE_TIMEUNIT *3);
                    camera.getCameraControl().enableTorch(false);
                    sleep(MORSE_TIMEUNIT);
                }
                sleep(MORSE_TIMEUNIT*3);
            }
            sleep(MORSE_TIMEUNIT*7);
        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
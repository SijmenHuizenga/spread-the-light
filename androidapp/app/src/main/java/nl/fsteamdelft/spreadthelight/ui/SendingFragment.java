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
import nl.fsteamdelft.spreadthelight.morse.MorseCode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class SendingFragment extends Fragment implements View.OnClickListener {

    public static final int MORSE_TIMEUNIT = 200;

    // low timeouts
    public static final int MORSE_TIMEUNIT_LETTER = 3*MORSE_TIMEUNIT;
    public static final int MORSE_TIMEUNIT_SPACE = 7 * MORSE_TIMEUNIT;
    public static final int MORSE_TIMEUNIT_CODES = MORSE_TIMEUNIT;

    // hi timeouts
    public static final int MORSE_TIMEUNIT_DASH = 3*MORSE_TIMEUNIT;
    public static final int MORSE_TIMEUNIT_DOT = MORSE_TIMEUNIT;



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
        char[] letters = text.toLowerCase().toCharArray();
        for (int letterIndex = 0; letterIndex < letters.length; letterIndex++) {
            if (letters[letterIndex] == ' ') {
                //The space between words is 7 time units.
                sleep(MORSE_TIMEUNIT_SPACE);

                // After waiting for a space we continue to the next letter immediatly
                continue;
            }

            //The space between letters is 3 time units.
            if (letterIndex != 0 && letters[letterIndex-1] != ' ') {
                // Do not sleep if it's the first character.
                // Do not sleep when the previous letter was a space
                sleep(MORSE_TIMEUNIT_LETTER);
            }

            String code = MorseCode.dict.get(letters[letterIndex]);
            if (code == null) {
                //for now, skip characters unknown to our dictionary
                continue;
            }

            char[] signals = code.toCharArray();
            for (int i = 0; i < signals.length; i++) {
                camera.getCameraControl().enableTorch(true);
                long start = System.currentTimeMillis();
                if (signals[i] == '.') {
                    //The length of a dot is 1 time unit.
                    sleep(MORSE_TIMEUNIT_DOT);
                } else if (signals[i] == '_') {
                    //A dash is 3 time units.
                    sleep(MORSE_TIMEUNIT_DASH);
                } else {
                    throw new IllegalStateException("Illegal Charaacter");
                }
                camera.getCameraControl().enableTorch(false);
                System.out.println(System.currentTimeMillis()-start);
                if (i < signals.length - 1) {
                    //The space between symbols (dots and dashes) of the same letter is 1 time unit.
                    sleep(MORSE_TIMEUNIT_CODES);
                }
            }
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
package com.longrobo.v2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvLog;
    private EditText etInput;
    private Button btnSend, btnMic, btnPerm;
    private SwitchMaterial swMain, swSound;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLog = findViewById(R.id.tvLog);
        etInput = findViewById(R.id.etInput);
        btnSend = findViewById(R.id.btnSend);
        btnMic = findViewById(R.id.btnMic);
        btnPerm = findViewById(R.id.btnPerm);
        swMain = findViewById(R.id.swMain);
        swSound = findViewById(R.id.swSound);

        checkAndRequestPermissions();

        btnPerm.setOnClickListener(v -> openPermissionSettings());
        swMain.setOnCheckedChangeListener((b, checked) -> {
            if (checked) {
                Intent fg = new Intent(this, LongRoboForegroundService.class);
                ContextCompat.startForegroundService(this, fg);
                appendLog(">> Da bat che do chay nen");
                SoundHelper.playTing(this);
            } else {
                stopService(new Intent(this, LongRoboForegroundService.class));
                appendLog(">> Da tat che do chay nen");
            }
        });

        btnSend.setOnClickListener(v -> {
            String text = etInput.getText().toString().trim();
            if (!text.isEmpty()) {
                handleUserMessage(text);
                etInput.setText("");
            }
        });

        btnMic.setOnClickListener(v -> startVoiceInput());
        appendLog("Longrobo-V2 san sang. Hay bat 2 quyen ben duoi.");
    }

    private void handleUserMessage(String msg) {
        appendLog("Ban: " + msg);
        String reply = AiBrain.getReply(msg);
        appendLog("Robot: " + reply);
        if (swSound.isChecked()) SoundHelper.playTing(this);
    }

    private void startVoiceInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 101);
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("vi", "VN"));
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Noi di Long...");

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() { appendLog(">> Dang nghe..."); }
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onError(int error) { appendLog(">> Loi micro: " + error); }
            @Override public void onResults(Bundle results) {
                ArrayList<String> list = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (list != null && !list.isEmpty()) handleUserMessage(list.get(0));
            }
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
        speechRecognizer.startListening(intent);
    }

    private void checkAndRequestPermissions() {
        ArrayList<String> need = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            need.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            need.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!need.isEmpty()) ActivityCompat.requestPermissions(this, need.toArray(new String[0]), 100);
    }

    private void openPermissionSettings() {
        try {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(this, "Bat Longrobo Accessibility", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }
    }

    private void appendLog(String s) { tvLog.append("\n" + s); }
}

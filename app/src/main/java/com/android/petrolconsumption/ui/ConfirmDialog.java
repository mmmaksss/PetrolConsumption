package com.android.petrolconsumption.ui;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.BaseCommand;
import com.android.petrolconsumption.voice.RecognizerListener;
import com.android.petrolconsumption.voice.SpeechKitHelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends Dialog {
    private static final String SPLIT_EXPR = " ";
    private final BaseCommand command;
    private final Context context;
    private final ConfirmDialogListener listener;

    private Button buttonYes;
    private Button buttonNo;

    public ConfirmDialog(Context context, BaseCommand command,
            ConfirmDialogListener listener) {
        super(context);

        this.context = context;
        this.command = command;
        this.listener = listener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);

        ((TextView) findViewById(R.id.dialog_confirm_title))
                .setText(this.command.getCommandInfo());
        this.buttonYes = (Button) findViewById(R.id.dialog_confirm_button_yes);
        this.buttonNo = (Button) findViewById(R.id.dialog_confirm_button_no);

        this.buttonYes
                .setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onYes();
                    }
                });

        this.buttonNo
                .setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNo("OnClick");
                    }
                });

        SpeechKitHelper.recreateRecognizer(this.recognizerListener);

        SpeechKitHelper.startRecognizing();
    }

    @Override
    public void onBackPressed() {
        if (ConfirmDialog.this.listener != null) {
            ConfirmDialog.this.listener.onNo("onBackPressed");
        }
        super.onBackPressed();
    }

    @Override
    public void dismiss() {
        SpeechKitHelper.stopRecognizing();
        super.dismiss();
    }

    private RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onReadyForSpeech() {
            // do nothing
        }

        @Override
        public void onRmsChanged(int audioLevel) {
            // do nothing
        }

        @Override
        public void onEndOfSpeech() {
            // do nothing
        }

        @Override
        public void onError(int error) {
            onNo("onError:" + error);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onResults(String results) {
            if (results.trim().length() > 0) {
                String confirm = ConfirmDialog.this.context.getString(
                        R.string.dialog_confirm_text_yes).toLowerCase();
                String[] words = results.trim().split(ConfirmDialog.SPLIT_EXPR);
                boolean confirmed = true;
                for (int i = 0; i < words.length; i++) {
                    if (!words[i].toLowerCase().equals(confirm)) {
                        confirmed = false;
                        break;
                    }
                }
                if (confirmed) {
                    onYes();
                } else {
                    onNo("onResults:" + results);
                }
            } else {
                onNo("onResults:" + results);
            }
        }
    };

    private void onYes() {
        if (ConfirmDialog.this.listener != null) {
            ConfirmDialog.this.listener.onYes(this.command);
        }
        dismiss();
    }

    private void onNo(String why) {
        if (ConfirmDialog.this.listener != null) {
            ConfirmDialog.this.listener.onNo(why);
        }
        dismiss();
    }
}

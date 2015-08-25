package com.android.petrolconsumption.commands.newsearcher;

import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SearchCommand extends Command {
    private static final String URL_GOOGLE_SEARCH = "http://www.google.com/search?q=";
    private static final String URL_YANDEX_SEARCH = "http://yandex.ru/yandsearch?text=";

    public SearchCommand(int wordsArrayResId, Context context) {
        super(wordsArrayResId, context, null);
        this.action = new Action() {

            @Override
            public void execute() {
                String searchUrl;
                if (VoiceRecognizerPreferences.getRecognizerLanguage()
                        .startsWith("ru")) {
                    searchUrl = URL_YANDEX_SEARCH
                            + SearchCommand.this.postCommandWords;
                } else {
                    searchUrl = URL_GOOGLE_SEARCH
                            + SearchCommand.this.postCommandWords;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(searchUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SearchCommand.this.context.startActivity(intent);
            }
        };
    }
}

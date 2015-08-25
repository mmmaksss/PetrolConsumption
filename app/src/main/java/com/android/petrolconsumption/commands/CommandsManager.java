package com.android.petrolconsumption.commands;

import android.content.Context;

import com.android.petrolconsumption.commands.newsearcher.NewCommandSearcher;
import com.android.petrolconsumption.commands.oldsearcher.CommandSearcher;
import com.android.petrolconsumption.utils.NotInitializedException;

public class CommandsManager {
    private static volatile CommandsManager instance_;
    private BaseCommandSearcher commandSearcher;
    private static boolean IS_NEW_SEACHER_USED = true;

    public static CommandsManager getInstance() {
        if (instance_ == null) {
            throw new NotInitializedException(
                    "CommandsManager instance not initialized!");
        }
        return instance_;
    }

    public static synchronized void initialize(Context context) {
        if (instance_ == null) {
            instance_ = new CommandsManager(context);
        }
    }

    private CommandsManager(Context context) {
        if (IS_NEW_SEACHER_USED) {
            this.commandSearcher = new NewCommandSearcher(
                    context);
        } else {
            this.commandSearcher = new CommandSearcher(
                    context);
        }
    }

    public synchronized BaseCommand findCommand(String text) {
        if (text != null && text.trim().length() > 0) {
            return this.commandSearcher.getCommand(text);
        }
        return null;
    }
    
    public synchronized void updateLanguage() {
        this.commandSearcher.updateLanguage();
    }
}

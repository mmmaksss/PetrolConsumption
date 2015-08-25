package com.android.petrolconsumption.commands.newsearcher;

import com.android.petrolconsumption.commands.Actions;
import com.android.petrolconsumption.commands.BaseCommand;

import android.content.Context;

public class Command extends BaseCommand {
    protected int commandId;
    protected Action action;
    protected Context context;
    protected String lastUsedCommandPhrase;
    protected String postCommandWords;

    /**
     * Constructor
     * 
     * @param wordsArrayResId
     *            - resource id for array of words that corresponding to command
     * @param action
     *            - action that should be performed by current command
     * @param context
     *            - application context
     */
    public Command(int wordsResId, Context context, Action action) {
        this.commandId = wordsResId;
        this.action = action;
        this.context = context;
    }

    public Command(int wordsResId, Context context) {
        this.commandId = wordsResId;
        this.context = context;
    }

    @Override
    public void execute() {
        if (this.action != null) {
            this.action.execute();
        } else {
            Actions.getInstance().doAction(this.commandId, this.context);
        }
    }

    public interface Action {
        public abstract void execute();
    }

    public void setLastUsedCommandPhrase(String phrase) {
        this.lastUsedCommandPhrase = phrase;
    }

    public void setPostCommandWords(String postCommandWords) {
        this.postCommandWords = postCommandWords;
    }

    @Override
    public String getCommandInfo() {
        return this.lastUsedCommandPhrase;
    }
}

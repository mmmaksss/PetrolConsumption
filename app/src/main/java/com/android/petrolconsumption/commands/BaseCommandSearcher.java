package com.android.petrolconsumption.commands;

public abstract class BaseCommandSearcher {

    public abstract BaseCommand getCommand(String phrase);

    public abstract void updateLanguage();
}

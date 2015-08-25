package com.android.petrolconsumption.commands.oldsearcher;

import com.android.petrolconsumption.commands.Actions;
import com.android.petrolconsumption.commands.BaseCommand;
import com.android.petrolconsumption.commands.commandsDb.CommandsId;

import android.content.Context;

public class Command extends BaseCommand {
    private final CommandsId id;
    private final Context context;

    /**
     * Constructor
     */
    Command(CommandsId id, Context context) {
        this.id = id;
        this.context = context;
    }

    public void execute() {
        Actions.getInstance().doAction(this.id, this.context);
    }

    public CommandsId getCommandId() {
        return id;
    }

    @Override
    public String getCommandInfo() {
        return this.context.getString(this.id.getTextResId());
    }

}

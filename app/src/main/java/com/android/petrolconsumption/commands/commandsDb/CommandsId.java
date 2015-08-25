package com.android.petrolconsumption.commands.commandsDb;

import java.util.List;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.oldsearcher.CommandSearcher;
import com.android.petrolconsumption.commands.oldsearcher.CommandSearcher.Percentage;

public enum CommandsId {
    NONE(-1, null, null),
    OPEN_COMMON_SETTINGS(R.string.command_open_common_settings,
            new int [] {R.string.command_open, R.string.command_start,
            R.string.command_launch},
            new int [] {R.string.with_settings, R.string.with_common}),
    CLOSE_COMMON_SETTINGS(R.string.command_close_common_settings,
            new int [] {R.string.command_close,
            R.string.command_finish, R.string.command_minimize},
            new int [] {R.string.with_settings, R.string.with_common}),
    OPEN_APPLICATION_SETTINGS(
            R.string.command_open_application_settings,
            new int[] {R.string.command_open, R.string.command_start,
            R.string.command_launch },
            new int[] {R.string.with_settings, R.string.with_application }),
    CLOSE_APPLICATION_SETTINGS(
            R.string.command_close_application_settings,
            new int[] {R.string.command_close, R.string.command_finish,
            R.string.command_minimize },
            new int[] {R.string.with_settings, R.string.with_application}),
    OPEN_HOME(R.string.command_open_main_screen,
            new int [] {R.string.command_open, R.string.command_start,
            R.string.command_launch},
            new int [] {R.string.with_main_srceen}),
    TURN_ON_LAMP(R.string.command_turn_on_lamp,
            new int [] {R.string.command_open, R.string.command_start,
            R.string.command_launch, R.string.command_turn_on},
            new int [] {R.string.with_lamp}),
    TURN_OFF_LAMP(R.string.command_turn_off_lamp,
            new int [] {R.string.command_close, R.string.command_stop,
            R.string.command_finish, R.string.command_turn_off},
            new int [] {R.string.with_lamp}),
    TURN_ON_SOS(R.string.command_turn_on_sos,
            new int [] {R.string.command_open, R.string.command_start,
            R.string.command_launch, R.string.command_turn_on},
            new int [] {R.string.with_sos}),
    TURN_OFF_SOS(R.string.command_turn_off_sos,
            new int [] {R.string.command_close, R.string.command_stop,
            R.string.command_finish, R.string.command_turn_off},
            new int [] {R.string.with_sos}),
    CHANGE_DIALOG_SETTINGS(R.string.command_change_dialog_settings,
            new int [] {R.string.command_change},
            new int [] {R.string.with_dialog, R.string.with_settings}),
    CHANGE_RECOGNIZER(R.string.command_change_recognizer,
            new int [] {R.string.command_change},
            new int [] {R.string.with_recognizer}),
    CHANGE_LANGUAGE(R.string.command_change_language,
            new int [] {R.string.command_change},
            new int [] {R.string.with_language}),;

    private final int[] commands;
    private final int[] withWhoam;
    private final int textResId;

    private CommandsId(int textResId, int[] commands, int[] withWhoam) {
        this.commands = commands;
        this.withWhoam = withWhoam;
        this.textResId = textResId;
    }

    public boolean isCommand(Percentage command, List<Percentage> whoms) {
        int counterEqualsID = 0;
        for (int i = 0; i < this.withWhoam.length; i++) {
            for (int j = 0; j < whoms.size(); j++) {
                Percentage whom = whoms.get(j);
                if (!whom.equals(command)) {
                    if (whom.getPercentageWithWhom() < CommandSearcher.PERCENT_COMMAND_WHOM_BORDER) {
                        break;
                    }
                    if (this.withWhoam[i] == whom.getWithWhomId()) {
                        counterEqualsID++;
                        break;
                    }
                }
            }
        }

        if (counterEqualsID != this.withWhoam.length) {
            return false;
        }

        for (int i=0; i<this.commands.length; i++) {
            if (command.getCommandId() == this.commands[i]) {
                return true;
            }
        }

        return false;
    }

    public int getTextResId() {
        return this.textResId;
    }
}

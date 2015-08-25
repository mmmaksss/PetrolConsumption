package com.android.petrolconsumption.commands;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.commandsDb.CommandsId;

import android.content.Context;
import android.content.Intent;

public class Actions {
    private static volatile Actions instance_;

    private ActionListener listener = null;

    public static Actions getInstance() {
        if (instance_ == null) {
            instance_ = new Actions();
        }
        return instance_;
    }

    private Actions() {
    }

    public synchronized void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public synchronized void removeListener() {
        this.listener = null;
    }

    public synchronized void doAction(CommandsId commandsId, Context context) {
        switch (commandsId) {
        case OPEN_COMMON_SETTINGS:
            openCommonSettings(commandsId, context);
            break;

        case CLOSE_COMMON_SETTINGS:
            goHome(commandsId, context);
            break;

        case OPEN_HOME:
            goHome(commandsId, context);
            break;

        case OPEN_APPLICATION_SETTINGS:
            openApplicationSettings(commandsId);
            break;

        case CLOSE_APPLICATION_SETTINGS:
            closeApplicationSettings(commandsId);
            break;

        case CHANGE_DIALOG_SETTINGS:
            changeDialogSettings(commandsId);
            break;

        case CHANGE_LANGUAGE:
            changeLanguage(commandsId);
            break;

        case CHANGE_RECOGNIZER:
            changeRecognizer(commandsId);
            break;

        default:
            break;
        }
    }

    public synchronized void doAction(int commandsId, Context context) {
        switch (commandsId) {
        case R.array.command_open_common_settings:
            doAction(CommandsId.OPEN_COMMON_SETTINGS, context);
            break;

        case R.array.command_open_main_screen:
            doAction(CommandsId.OPEN_HOME, context);
            break;

        case R.array.command_turn_on_lamp:
            doAction(CommandsId.TURN_ON_LAMP, context);
            break;

        case R.array.command_turn_off_lamp:
            doAction(CommandsId.TURN_OFF_LAMP, context);
            break;

        case R.array.command_turn_on_sos:
            doAction(CommandsId.TURN_ON_SOS, context);
            break;

        case R.array.command_turn_off_sos:
            doAction(CommandsId.TURN_OFF_SOS, context);
            break;

        case R.array.command_open_application_settings:
            doAction(CommandsId.OPEN_APPLICATION_SETTINGS, context);
            break;

        case R.array.command_close_application_settings:
            doAction(CommandsId.CLOSE_APPLICATION_SETTINGS, context);
            break;

        case R.array.command_change_dialog_settings:
            doAction(CommandsId.CHANGE_DIALOG_SETTINGS, context);
            break;

        case R.array.command_change_language:
            doAction(CommandsId.CHANGE_LANGUAGE, context);
            break;

        case R.array.command_change_recognizer:
            doAction(CommandsId.CHANGE_RECOGNIZER, context);
            break;
        default:
            break;
        }
    }

    private static void goHome(CommandsId commandsId,Context context) {
        Intent openHome = new Intent(Intent.ACTION_MAIN);
        openHome.addCategory(Intent.CATEGORY_HOME);
        openHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openHome);
    }

    private static void openCommonSettings(CommandsId commandsId,Context context) {
        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(settingsIntent); 
    }

    private void openApplicationSettings(CommandsId id) {
        if (this.listener != null) {
            this.listener.performAction(id);
        }
    }

    private void closeApplicationSettings(CommandsId id) {
        if (this.listener != null) {
            this.listener.performAction(id);
        }
    }

    private void changeDialogSettings(CommandsId id) {
        if (this.listener != null) {
            this.listener.performAction(id);
        }
    }

    private void changeRecognizer(CommandsId id) {
        if (this.listener != null) {
            this.listener.performAction(id);
        }
    }

    private void changeLanguage(CommandsId id) {
        if (this.listener != null) {
            this.listener.performAction(id);
        }
    }

    public interface ActionListener {
        void performAction(CommandsId id);
    }
}

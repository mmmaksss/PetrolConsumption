package com.android.petrolconsumption.commands.commandsDb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources.NotFoundException;

import com.android.petrolconsumption.R;

public class CommandsDB {
    public class DBInfo {
        public final int resId;
        public final String word;

        public DBInfo(int resId, String word) {
            this.resId = resId;
            this.word = word;
        }
    }

    private static int[] COMANDS_DB_RES_ID = {
        R.string.command_close,
        R.string.command_finish,
        R.string.command_launch,
        R.string.command_minimize,
        R.string.command_open,
        R.string.command_select,
        R.string.command_start,
        R.string.command_stop,
        R.string.command_turn_off,
        R.string.command_turn_on,
        R.string.command_change
    };

    private static int[] WITH_WHOM_DB_RES_ID = {
        R.string.with_settings,
        R.string.with_main_srceen,
        R.string.with_lamp,
        R.string.with_sos,
        R.string.with_common,
        R.string.with_application,
        R.string.with_dialog,
        R.string.with_language,
        R.string.with_recognizer
    };

    private static volatile CommandsDB instance_;
    private List<DBInfo> comandsList;
    private List<DBInfo> withWhomList;

    public static CommandsDB getInstance() {
        if (instance_ == null) {
            instance_ = new CommandsDB();
        }
        return instance_;
    }

    private CommandsDB() {
        this.comandsList = new ArrayList<CommandsDB.DBInfo>();
        this.withWhomList = new ArrayList<CommandsDB.DBInfo>();
    }

    public void updateDB(Context context) {
        this.comandsList.clear();
        this.withWhomList.clear();
        for (int i=0; i<CommandsDB.COMANDS_DB_RES_ID.length; i++) {
            String commandWord = null;
            try {
                commandWord = context
                        .getString(CommandsDB.COMANDS_DB_RES_ID[i]);
            } catch (NotFoundException e) {
                // do nothing
            }
            if (commandWord != null && commandWord.trim().length() > 0) {
                this.comandsList.add(new DBInfo(CommandsDB.COMANDS_DB_RES_ID[i],
                        commandWord));
            }
        }
        for (int i=0; i<CommandsDB.WITH_WHOM_DB_RES_ID.length; i++) {
            String whomWord = null;
            try {
                whomWord = context
                        .getString(CommandsDB.WITH_WHOM_DB_RES_ID[i]);
            } catch (NotFoundException e) {
                // do nothing
            }
            if (whomWord != null && whomWord.trim().length() > 0) {
                this.withWhomList.add(new DBInfo(CommandsDB.WITH_WHOM_DB_RES_ID[i],
                        whomWord));
            }
        }
    }

    public List<DBInfo> getCommandsList() {
        return this.comandsList;
    }

    public List<DBInfo> getWithWhomList() {
        return this.withWhomList;
    }
}

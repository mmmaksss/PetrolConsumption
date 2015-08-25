package com.android.petrolconsumption.commands.oldsearcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.BaseCommand;
import com.android.petrolconsumption.commands.BaseCommandSearcher;
import com.android.petrolconsumption.commands.commandsDb.CommandsDB;
import com.android.petrolconsumption.commands.commandsDb.CommandsId;
import com.android.petrolconsumption.commands.commandsDb.CommandsDB.DBInfo;

import android.content.Context;
import android.util.Log;

public class CommandSearcher extends BaseCommandSearcher {
    private final static String SPLIT_EXPR = " ";

    private final static int PERCENT_SEARCH_COMMAND_BORDER = 100;
    private final static int PERCENT_SEARCH_WHOM_BORDER = 100;
    private final static int PERCENT_COMMAND_COMMAND_BORDER = 70;
    public final static int PERCENT_COMMAND_WHOM_BORDER = 51;

    private String[] wordExceptions;
    private String[] wordCorrectionRu = null;
    private String[] wordCorrectionEn = null;

    private final Context context;

    public CommandSearcher(Context context) {
        this.context = context;
        this.wordExceptions = context.getResources().getStringArray(R.array.word_exceptions);
        try {
            this.wordCorrectionRu = context.getResources().getStringArray(R.array.word_correction_ru);
            this.wordCorrectionEn = context.getResources().getStringArray(R.array.word_correction_en);
        } catch (Throwable t) {
            // do nothing
        }
        CommandsDB.getInstance().updateDB(context);
    }

    public BaseCommand getCommand(final String text) {
        if (text == null || text.trim().length() == 0) {
            return null;
        }

        List<String> splitText = prepareText(text);

        CommandsId id = calculate(splitText);

        if (id != CommandsId.NONE) {
            return new Command(id, this.context);
        }

        return null;
    }

    public void updateLanguage() {
        this.wordExceptions = this.context.getResources().getStringArray(R.array.word_exceptions);
        CommandsDB.getInstance().updateDB(this.context);
    }

    private List<String> prepareText(String text) {
        String [] splitText = text.trim()
                .toLowerCase().split(CommandSearcher.SPLIT_EXPR);

        for (int i=0; i<splitText.length; i++) {
            if (splitText[i].length() < 2) {
                splitText[i] = null;
            } else {
                for (int j = 0; j < this.wordExceptions.length; j++) {
                    if (splitText[i].equals(this.wordExceptions[j])) {
                        splitText[i] = null;
                        break;
                    }
                }
                if (this.wordCorrectionRu != null && this.wordCorrectionEn != null) {
                    for (int j = 0; j < this.wordCorrectionEn.length; j++) {
                        if (splitText[i].equals(this.wordCorrectionEn[j])) {
                            splitText[i] = this.wordCorrectionRu[j];
                            break;
                        }
                    }
                }
            }
        }

        List<String> list = new ArrayList<String>();

        for (int i=0; i<splitText.length; i++) {
            if (splitText[i] != null) {
                list.add(splitText[i]);
            }
        }

        return list;
    }

    private CommandsId calculate(List<String> splitText) {
        List<Percentage> percentageList = new ArrayList<CommandSearcher.Percentage>();
        for (int i = 0; i < splitText.size(); i++) {
            percentageList.add(new Percentage(splitText.get(i)));
        }
        for (int i = 0; i < percentageList.size(); i++) {
            percentageList.get(i).calculate();
        }
        Collections.sort(percentageList, 
                new Comparator<Percentage>() {
                    @Override
                    public int compare(Percentage object1, Percentage object2) {
                        return (int) (object2.percentageCommand - object1.percentageCommand);
                    }
            
        });
        for (int i=0; i< percentageList.size(); i++) {
            Log.d("CommandSearcher", percentageList.get(i).word);
            Log.d("CommandSearcher", "" + percentageList.get(i).percentageCommand);
            Log.d("CommandSearcher", "" + percentageList.get(i).commandId);
            Log.d("CommandSearcher", "" + percentageList.get(i).percentageWithWhom);
            Log.d("CommandSearcher", "" + percentageList.get(i).withWhomId);
        }
        return searchComand(percentageList);
    }

    private int getLevenshteinDistance(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for(int i = 0; i <= n; i ++) {
            D2[i] = i;
        }

        for(int i = 1; i <= m; i ++) {
            D1 = D2;
            D2 = new int[n + 1];
            for(int j = 0; j <= n; j ++) {
                if(j == 0) D2[j] = i;
                else {
                    int cost = (s1.charAt(i - 1) != s2.charAt(j - 1)) ? 1 : 0;
                    if(D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if(D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        int maxLength = Math.max(m, n);
        int percentage = (maxLength - D2[n]) * 100 / maxLength;
        return percentage;
    }

    public class Percentage {
        private final String word;
        private int percentageCommand = 0;
        private int commandId;
        private int percentageWithWhom = 0;
        private int withWhomId;

        public Percentage(String word) {
            this.word = word;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Percentage) {
                return this.word.equals(((Percentage)o).getWord());
            }
            return false;
        }

        public String getWord() {
            return this.word;
        }
        
        public int getPercentageWithWhom() {
            return this.percentageWithWhom;
        }

        public int getWithWhomId() {
            return this.withWhomId;
        }
        
        public int getCommandId() {
            return this.commandId;
        }

        public int getPercentageCommand() {
            return this.percentageCommand;
        }

        public void calculate() {
            List<DBInfo> commandsList = CommandsDB.getInstance().getCommandsList();
            int percentage = 0;
            for (int i=0; i<commandsList.size(); i++) {
                percentage = getLevenshteinDistance(commandsList.get(i).word,
                        this.word);
                if (percentage > this.percentageCommand) {
                    this.percentageCommand = percentage;
                    this.commandId = commandsList.get(i).resId;
                }
                if (this.percentageCommand >= CommandSearcher.PERCENT_SEARCH_COMMAND_BORDER) {
                    break;
                }
            }
            List<DBInfo> withWhomList = CommandsDB.getInstance().getWithWhomList();
            for (int i=0; i<withWhomList.size(); i++) {
                percentage = getLevenshteinDistance(withWhomList.get(i).word,
                        this.word);
                if (percentage > this.percentageWithWhom) {
                    this.percentageWithWhom = percentage;
                    this.withWhomId = withWhomList.get(i).resId;
                }
                if (this.percentageWithWhom >= CommandSearcher.PERCENT_SEARCH_WHOM_BORDER) {
                    break;
                }
            }
        }
    }

    private CommandsId searchComand(List<Percentage> parcentageList) {
        List<Percentage> withWhomSortedList = new ArrayList<CommandSearcher.Percentage>();
        for (int i = 0; i < parcentageList.size(); i++) {
            withWhomSortedList.add(parcentageList.get(i));
        }
        Collections.sort(withWhomSortedList, 
                new Comparator<Percentage>() {
                    @Override
                    public int compare(Percentage object1, Percentage object2) {
                        return (int) (object2.percentageWithWhom - object1.percentageWithWhom);
                    }
            
        });

        Percentage command = null;
        CommandsId id = CommandsId.NONE;
        for (int i=0; i < parcentageList.size(); i++) {
            command = parcentageList.get(i);
            if (command.percentageCommand < CommandSearcher.PERCENT_COMMAND_COMMAND_BORDER) {
                return CommandsId.NONE;
            }

            id = isCommand(command, withWhomSortedList);

            if (id != CommandsId.NONE) {
                return id;
            }
        }
        return CommandsId.NONE;
    }

    private CommandsId isCommand(Percentage command, List<Percentage> whoms) {
        if (CommandsId.OPEN_HOME.isCommand(command, whoms)) {
            return CommandsId.OPEN_HOME;
        }
        if (CommandsId.TURN_ON_LAMP.isCommand(command, whoms)) {
            return CommandsId.TURN_ON_LAMP;
        }
        if (CommandsId.TURN_OFF_LAMP.isCommand(command, whoms)) {
            return CommandsId.TURN_OFF_LAMP;
        }
        if (CommandsId.TURN_ON_SOS.isCommand(command, whoms)) {
            return CommandsId.TURN_ON_SOS;
        }
        if (CommandsId.TURN_OFF_SOS.isCommand(command, whoms)) {
            return CommandsId.TURN_OFF_SOS;
        }
        if (CommandsId.OPEN_COMMON_SETTINGS.isCommand(command, whoms)) {
            return CommandsId.OPEN_COMMON_SETTINGS;
        }
        if (CommandsId.CLOSE_COMMON_SETTINGS.isCommand(command, whoms)) {
            return CommandsId.CLOSE_COMMON_SETTINGS;
        }
        if (CommandsId.OPEN_APPLICATION_SETTINGS.isCommand(command, whoms)) {
            return CommandsId.OPEN_APPLICATION_SETTINGS;
        }
        if (CommandsId.CLOSE_APPLICATION_SETTINGS.isCommand(command, whoms)) {
            return CommandsId.CLOSE_APPLICATION_SETTINGS;
        }
        if (CommandsId.CHANGE_DIALOG_SETTINGS.isCommand(command, whoms)) {
            return CommandsId.CHANGE_DIALOG_SETTINGS;
        }
        if (CommandsId.CHANGE_LANGUAGE.isCommand(command, whoms)) {
            return CommandsId.CHANGE_LANGUAGE;
        }
        if (CommandsId.CHANGE_RECOGNIZER.isCommand(command, whoms)) {
            return CommandsId.CHANGE_RECOGNIZER;
        }
        return CommandsId.NONE;
    }
}

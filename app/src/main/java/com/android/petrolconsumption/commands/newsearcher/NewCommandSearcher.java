package com.android.petrolconsumption.commands.newsearcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.BaseCommandSearcher;
import com.android.petrolconsumption.commands.newsearcher.Command.Action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class NewCommandSearcher extends BaseCommandSearcher {
    private final static String SPLIT_EXPR = " ";
    private final static int MIN_PERCENT_COMMAND_COMPARATION = 40;
    private final static int MIN_LENGTH_START_PART_OF_WORD = 40;
    private boolean isRevertCommandPhrase = true;
    private HashMap commandsList = new HashMap();

    private Integer commandIds[];

    private final Context context;

    private Searcher searcher = new Searcher(null);

    public NewCommandSearcher(Context context) {
        this.context = context;
        initializeCommands();
    }

    private void updateCommandIds() {
        // update commandIds list
        Set<Integer> keySet = this.commandsList.keySet();
        if (keySet != null) {
            this.commandIds = new Integer[keySet.size()];
            keySet.toArray(this.commandIds);
        } else {
            this.commandIds = new Integer[keySet.size()];
        }
    }

    public void updateCommandSearcher() {
        if (this.commandIds != null) {
            int length = this.commandIds.length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    int resId = this.commandIds[i];
                    String[] commandPhrases = this.context.getResources()
                            .getStringArray(resId);
                    for (int j = 0; j < commandPhrases.length; j++) {
                        String[] commandPhraseWords = commandPhrases[j].trim()
                                .toLowerCase().split(SPLIT_EXPR);
                        List<String> wordsList = Arrays
                                .asList(commandPhraseWords);
                        if (this.searcher != null) {
                            this.searcher.addPhrase(wordsList, resId);
                            if (isRevertCommandPhrase) {
                                Collections.reverse(wordsList);
                                this.searcher.addPhrase(wordsList, resId);
                            }
                        }
                    }
                }
            }
            Log.d("CommandSearcher_test", this.searcher.toString());
        }
    }

    public Command getCommand(String phrase) {
        String[] commandPhraseWords = phrase.trim().toLowerCase()
                .split(SPLIT_EXPR);
        List<String> wordsList = Arrays.asList(commandPhraseWords);
        String commandPhrase = "";
        String postCommandWords = "";

        int size = wordsList.size();
        for (int i = 0; i < size; i++) {
            List<String> subList = wordsList.subList(i, size);
            Searcher searcher = this.searcher;
            commandPhrase = "";
            int subListSize = subList.size();
            for (int j = 0; j < subListSize; j++) {
                Searcher newSearcher = searcher
                        .approximateSearchNextWords(subList.get(j));
                if (newSearcher == null) {
                    int id = searcher.getId();
                    if (id > 0) {
                        for (int k = j; k < subListSize; k++) {
                            postCommandWords += subList.get(k) + " ";
                        }
                        Log.d("CommandSearcher_test", "commandPhrase:"
                                + commandPhrase);
                        Log.d("CommandSearcher_test", "postCommandWords:"
                                + postCommandWords);
                        Command command = (Command) this.commandsList.get(id);
                        if (command != null) {
                            command.setLastUsedCommandPhrase(commandPhrase);
                            command.setPostCommandWords(postCommandWords);
                        }
                        return command;
                    } else {
                        break;
                    }
                } else {
                    searcher = newSearcher;
                    commandPhrase += searcher.currentWord + " ";
                    if (subListSize == j + 1) {
                        Log.d("CommandSearcher_test", "commandPhrase:"
                                + commandPhrase);
                        if (searcher.id > 0) {
                            Command command = (Command) this.commandsList
                                    .get(searcher.id);
                            if (command != null) {
                                command.setLastUsedCommandPhrase(commandPhrase);
                                command.setPostCommandWords(postCommandWords);
                            }
                            return command;
                        }

                    }
                }
            }
        }

        return null;
    }

    private class Searcher implements Comparable {
        protected String currentWord = null;
        private int id = -1;
        private final int level;
        TreeSet<Searcher> nextWords = new TreeSet<Searcher>();

        Searcher(List<String> wordList, int id, int level) {
            Log.d("CommandSearcher",
                    "Searcher() - phrase:" + wordList.toString() + ", level:"
                            + level);
            this.level = level;
            if (this.level <= wordList.size() && this.level > 0) {
                this.currentWord = (String) wordList.get(this.level - 1);
                if (this.level == wordList.size()) {
                    this.currentWord = wordList.get(this.level - 1);
                    this.id = id;
                    Log.d("CommandSearcher", "Searcher() " + "level:" + level
                            + " this.currentWord:" + this.currentWord);
                } else {
                    Searcher newSecher = new Searcher(wordList, id,
                            this.level + 1);
                    Log.d("CommandSearcher",
                            "Searcher() " + "level:" + level
                                    + " this.currentWord:" + this.currentWord
                                    + " nextWords.size before add:"
                                    + this.nextWords.size()
                                    + this.nextWords.toString());
                    this.nextWords.add(newSecher);
                    Log.d("CommandSearcher",
                            "Searcher() " + "level:" + level
                                    + " this.currentWord:" + this.currentWord
                                    + " nextWords.size after add:"
                                    + this.nextWords.size()
                                    + this.nextWords.toString());
                }
            } else {
                // TODO: throw new
                // Exception("Incorrect value of level in Searcher");
            }
        }

        public int getId() {
            return this.id;
        }

        Searcher(String currentWord) {
            this.level = 0;
            this.currentWord = currentWord;
        }

        public void addPhrase(List<String> wordList, int id) {
            Log.d("CommandSearcher", "addPhrase() " + "level:" + level
                    + " this.currentWord:" + this.currentWord + " wordList:"
                    + wordList.toString() + this.nextWords.toString());
            if (this.currentWord == null
                    || ((wordList.size() > this.level) && wordList.get(
                            this.level - 1).equals(this.currentWord))) {
                Searcher searcher = searchNextWords(wordList.get(this.level));
                if (searcher != null) {
                    searcher.addPhrase(wordList, id);
                } else {
                    Log.d("CommandSearcher",
                            "addPhrase() " + "level:" + level
                                    + " this.currentWord:" + this.currentWord
                                    + " nextWords.size before add:"
                                    + this.nextWords.size()
                                    + this.nextWords.toString());
                    this.nextWords.add(new Searcher(wordList, id,
                            this.level + 1));
                    Log.d("CommandSearcher",
                            "addPhrase() " + "level:" + level
                                    + " this.currentWord:" + this.currentWord
                                    + " nextWords.size after add:"
                                    + this.nextWords.size()
                                    + this.nextWords.toString());
                }

                // TODO: throw new Exception("Command name" +
                // wordList.toString()
                // + " is duplicated exception");

            }
        }

        private Searcher searchNextWords(String searchWord) {
            if (this.nextWords.size() > 0) {
                SortedSet<Searcher> result = nextWords.subSet(new Searcher(
                        searchWord), new Searcher(searchWord
                        + Character.MIN_VALUE));
                if (result.size() > 0) {
                    Log.d("CommandSearcher",
                            "searchNextWords():" + result.first().currentWord);
                    return result.first();
                }
            }
            return null;
        }

        public int getCommandId(List<String> wordsList) {
            if (wordsList.size() == this.level) {
                return this.id;
            } else {
                Searcher searcher = approximateSearchNextWords(wordsList
                        .get(level));
                if (searcher != null) {
                    return searcher.getCommandId(wordsList);
                } else {
                    return -1;
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.currentWord != null) {
                sb.append(this.currentWord + "(" + this.id + "): ");
            }
            sb.append("{");
            if (this.nextWords != null) {
                sb.append("(" + this.nextWords.size() + ")");
                Iterator it = this.nextWords.iterator();
                while (it.hasNext()) {
                    Searcher next = (Searcher) it.next();
                    sb.append(next.toString() + " ");
                }
            } else {
                sb.append("(0)");
            }
            sb.append("}");
            return sb.toString();
        }

        protected Searcher approximateSearchNextWords(String searchWord) {
            Searcher firstSearcher = new Searcher(searchWord);
            Searcher lastSearcher = new Searcher(searchWord
                    + Character.MAX_VALUE);

            int numChar = (int) (searchWord.length() - searchWord.length()
                    * MIN_LENGTH_START_PART_OF_WORD / 100);

            Log.d("CommandSearcher",
                    "searchNextWords() nextWords:" + nextWords.toString());

            Searcher currentSearcher = null;
            int comparationOfCurrentSearcher = 0;
            // int length
            for (int i = 0; i < numChar; i++) {
                String word = searchWord.substring(0, searchWord.length() - i);
                firstSearcher.setCurrentWord(word);
                lastSearcher.setCurrentWord(word + Character.MAX_VALUE);
                SortedSet<Searcher> result = nextWords.subSet(firstSearcher,
                        lastSearcher);
                if (result.size() > 0) {
                    Log.d("CommandSearcher", "searchNextWords() resuls:"
                            + result.toString());

                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Searcher node = (Searcher) it.next();
                        int comparation = getLevenshteinDistance(searchWord,
                                node.currentWord);
                        if (comparation > comparationOfCurrentSearcher) {
                            comparationOfCurrentSearcher = comparation;
                            currentSearcher = node;
                        }
                        Log.d("CommandSearcher",
                                "searchNextWords() " + node.toString() + "%: "
                                        + comparation);
                    }
                    if (currentSearcher != null) {
                        Log.d("CommandSearcher", "searchNextWords() "
                                + currentSearcher.toString() + "%: "
                                + comparationOfCurrentSearcher);
                    }
                    break;
                }
            }
            Log.d("CommandSearcher", "comparationOfCurrentSearcher: "
                    + comparationOfCurrentSearcher);
            if (comparationOfCurrentSearcher > MIN_PERCENT_COMMAND_COMPARATION) {
                return currentSearcher;
            } else {
                return null;
            }
        }

        private void setCurrentWord(String word) {
            this.currentWord = word;
        }

        @Override
        public int compareTo(Object another) {
            Searcher entry = (Searcher) another;

            if (currentWord != null && entry.currentWord != null) {
                return this.currentWord.compareTo(entry.currentWord);
            }
            return 0;
        }
    }

    public static int getLevenshteinDistance(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            D2[i] = i;
        }

        for (int i = 1; i <= m; i++) {
            D1 = D2;
            D2 = new int[n + 1];
            for (int j = 0; j <= n; j++) {
                if (j == 0) D2[j] = i;
                else {
                    int cost = (s1.charAt(i - 1) != s2.charAt(j - 1)) ? 1 : 0;
                    if (D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost) D2[j] = D2[j - 1] + 1;
                    else if (D1[j] < D1[j - 1] + cost) D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        int maxLength = Math.max(m, n);
        int percentage = (int) (((float) (maxLength - D2[n]) / ((float) maxLength)) * 100);
        return percentage;
    }

    private void initializeCommands() {
        addCommand(R.array.command_open_common_settings);
        addCommand(R.array.command_close_common_settings);
        addCommand(R.array.command_change_dialog_settings);
        addCommand(R.array.command_change_language);
        addCommand(R.array.command_change_recognizer);
        addCommand(R.array.command_close_application_settings);
        addCommand(R.array.command_open_application_settings);
        addCommand(R.array.command_open_main_screen);
        addCommand(R.array.command_turn_off_lamp);
        addCommand(R.array.command_turn_on_lamp);
        addCommand(R.array.command_turn_on_sos);
        addCommand(R.array.command_turn_off_sos);

        addCommand(R.array.command_open_youtube, new Action() {
            @Override
            public void execute() {
                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                NewCommandSearcher.this.context.startActivity(intent);
            }
        });

        addCommand(R.array.command_open_browser, new Action() {
            @Override
            public void execute() {
                String url = "http://www.google.com";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                NewCommandSearcher.this.context.startActivity(intent);
            }
        });

        addCommand(new SearchCommand(R.array.command_search, this.context));

        updateCommandIds();
    }

    private void addCommand(int wordsArrayResId) {
        addCommand(wordsArrayResId, null);
    }

    private void addCommand(int wordsArrayResId, Action action) {
        Command command = new Command(wordsArrayResId, this.context, action);
        addCommand(command);
    }

    public void addCommand(Command command) {
        this.commandsList.put(command.commandId, command);
    }

    @Override
    public void updateLanguage() {
        updateCommandIds();
        updateCommandSearcher();
    }
}

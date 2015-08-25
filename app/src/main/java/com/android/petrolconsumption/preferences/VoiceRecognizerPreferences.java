package com.android.petrolconsumption.preferences;

import java.util.Locale;
import java.util.Map;

import com.android.petrolconsumption.PetrolConsumptionApplication;
import com.android.petrolconsumption.commands.CommandsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class VoiceRecognizerPreferences implements SharedPreferences {

    public static final String RECOGNIZER_LANGUAGE_RUSSIAN = "ru_RU";
    public static final String RECOGNIZER_LANGUAGE_ENGLISH = "en_US";
    private static final String DEFAULT_RECOGNIZER_LANGUAGE = VoiceRecognizerPreferences.RECOGNIZER_LANGUAGE_RUSSIAN;
    private static final boolean DEFAULT_IS_NUANCE_RECOGNIZER = true;
    private static final boolean DEFAULT_SHOW_CONFIRM_DIALOG = true;
    private static final boolean DEFAULT_LOOPER_ENABLED = false;

    public static enum VoiceRecognizerPreferenceKey {
        /** If true nuance else native android */
        IS_NUANCE_RECOGNIZER("IS_NUANCE_RECOGNIZER", Boolean.class), SHOW_CONFIRM_DIALOG(
                "SHOW_CONFIRM_DIALOG", Boolean.class), RECOGNIZER_LANGUAGE(
                "RECOGNIZER_LANGUAGE", String.class), LOOPER_ENABLED(
                "LOOPER_ENABLED", Boolean.class);

        private String key;
        private Class<?> type;

        private VoiceRecognizerPreferenceKey(String key, Class<?> type) {
            this.key = key;
            this.type = type;
        }

        public String key() {
            return this.key;
        }

        public Class<?> type() {
            return this.type;
        }
    }

    /** Singleton instance */
    private static VoiceRecognizerPreferences mSettingsObject = null;
    /** Shared preferences to be wrapped by this class */
    private static SharedPreferences mPreferencesObject = null;
    /** preferences file name */
    private final static String SHARED_PREFERENCES_FILENAME = "VoiceRecognizerPreferences";

    /**
     * This is main private constructor. A private Constructor prevents any
     * other class from instantiating.
     * 
     * @param context
     *            - Context object needed for SharedPreferences creation
     */
    private VoiceRecognizerPreferences(Context context) {
        try {
            VoiceRecognizerPreferences.setSharedPreferences(context
                    .getSharedPreferences(getSharedPreferencesFileName(),
                            Context.MODE_PRIVATE));
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * Setter for static field mPreferences
     * 
     * @param preferences
     *            - new value for static field mPreferences
     */
    private static void setSharedPreferences(SharedPreferences preferences) {
        mPreferencesObject = preferences;
    }

    /**
     * This is common creator for VoiceRecognizer component.
     * 
     * @return {@link VoiceRecognizerPreferences} instance
     */
    public static synchronized VoiceRecognizerPreferences getVoiceRecognizerSettings() {
        if (null == mSettingsObject) {
            mSettingsObject = new VoiceRecognizerPreferences(
                    PetrolConsumptionApplication.getAppContext());
        }

        return mSettingsObject;
    }

    /**
     * This override needed in order to not allow anybody to create another
     * instance of this class. Exception will be thrown if any attempt to do so.
     * 
     * @see java.lang.Object#clone()
     * 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Function determines if settings contains requested value.
     * 
     * @param settingsKey
     *            - MobileTvPreferenceKey to be checked for existence;
     * @return true - if setting with requested key presented; false -
     *         otherwise.
     */
    public boolean contains(VoiceRecognizerPreferenceKey settingsKey) {
        return contains(settingsKey.key());
    }

    @Override
    public boolean contains(String key) {
        boolean retval = false;

        if (null != mPreferencesObject) {
            retval = mPreferencesObject.contains(key);
        }

        return retval;
    }

    /**
     * Common wrapper for {@link SharedPreferences.Editor} methods access. As
     * we're not expecting intensive usage of this function, so it commits
     * changes to SharedPreference in the end. </br> Refer to
     * VoiceRecognizerPreferenceKey for details of types of settings values.
     * 
     * @param settingsKey
     *            - VoiceRecognizerPreferenceKey value;
     * @param value
     *            - value to be set to the settings.
     * 
     * @throws ClassCastException
     */
    public synchronized void setSetting(
            VoiceRecognizerPreferenceKey settingsKey, Object value)
            throws ClassCastException {

        Editor settingsEditor = edit();
        boolean bChanged = false;
        String key = settingsKey.key();
        Class<?> type = settingsKey.type();

        if (null != settingsEditor) {
            if (String.class == type) {
                settingsEditor.putString(key, (String) value);
                bChanged = true;
            } else if (Boolean.class == type) {
                settingsEditor.putBoolean(key, (Boolean) value);
                bChanged = true;
            } else if (Integer.class == type) {
                settingsEditor.putInt(key, (Integer) value);
                bChanged = true;
            } else if (Long.class == type) {
                settingsEditor.putLong(key, (Long) value);
                bChanged = true;
            }

            if (bChanged) {
                settingsEditor.commit();
            }
        }
    }

    @Override
    public Editor edit() {
        Editor editor = null;

        if (null != mPreferencesObject) {
            editor = mPreferencesObject.edit();
        }

        return editor;
    }

    @Override
    public Map<String, ?> getAll() {
        Map<String, ?> map = null;

        if (null != mPreferencesObject) {
            map = mPreferencesObject.getAll();
        }

        return map;
    }

    /**
     * Wrapper for {@link SharedPreferences#getBoolean(String, boolean)}
     * 
     * @param settingsKey
     *            - {@link VoiceRecognizerPreferenceKey} key enumeration item
     *            which value should be retrieved
     * @return boolean represented by the key
     * 
     * @throws ClassCastException
     */
    public boolean getBoolean(VoiceRecognizerPreferenceKey settingsKey)
            throws ClassCastException {
        return getBoolean(settingsKey.key(), false);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        boolean retval = defValue;

        if (null != mPreferencesObject) {
            retval = mPreferencesObject.getBoolean(key, defValue);
        }

        return retval;
    }

    /**
     * Wrapper for {@link SharedPreferences#getInt(String, int)}
     * 
     * @param settingsKey
     *            - {@link VoiceRecognizerPreferenceKey} key enumeration item
     *            which value should be retrieved
     * @return int represented by the key
     * 
     * @throws ClassCastException
     */
    public int getInt(VoiceRecognizerPreferenceKey settingsKey)
            throws ClassCastException {
        return getInt(settingsKey.key(), -1);
    }

    @Override
    public int getInt(String key, int defValue) {
        int retval = defValue;

        if (null != mPreferencesObject) {
            retval = mPreferencesObject.getInt(key, defValue);
        }

        return retval;
    }

    /**
     * Wrapper for {@link SharedPreferences#getLong(String, long)}
     * 
     * @param settingsKey
     *            - {@link VoiceRecognizerPreferenceKey} key enumeration item
     *            which value should be retrieved
     * @return long represented by the key
     * 
     * @throws ClassCastException
     */
    public long getLong(VoiceRecognizerPreferenceKey settingsKey)
            throws ClassCastException {
        return getLong(settingsKey.key(), -1);
    }

    @Override
    public long getLong(String key, long defValue) {
        long retval = defValue;

        if (null != mPreferencesObject) {
            retval = mPreferencesObject.getLong(key, defValue);
        }

        return retval;
    }

    /**
     * Wrapper for {@link SharedPreferences#getFloat(String, float)}
     * 
     * @param settingsKey
     *            - {@link VoiceRecognizerPreferenceKey} key enumeration item
     *            which value should be retrieved
     * @return float represented by the key
     * 
     * @throws ClassCastException
     */
    public float getFloat(VoiceRecognizerPreferenceKey settingsKey)
            throws ClassCastException {
        return getFloat(settingsKey.key(), 0);
    }

    @Override
    public float getFloat(String key, float defValue) {
        float retval = defValue;

        if (null != mPreferencesObject) {
            retval = mPreferencesObject.getFloat(key, defValue);
        }

        return retval;
    }

    /**
     * Wrapper for {@link SharedPreferences#getString(String, String)}
     * 
     * @param settingsKey
     *            - {@link VoiceRecognizerPreferenceKey} key enumeration item
     *            which value should be retrieved
     * @return String represented by the key
     * 
     * @throws ClassCastException
     */
    public String getString(VoiceRecognizerPreferenceKey settingsKey)
            throws ClassCastException {
        return getString(settingsKey.key(), null);
    }

    @Override
    public String getString(String key, String defValue) {
        String retval = defValue;

        if (null != mPreferencesObject) {
            retval = mPreferencesObject.getString(key, defValue);
        }

        return retval;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener listener) {
        if (null != mPreferencesObject) {
            mPreferencesObject
                    .registerOnSharedPreferenceChangeListener(listener);
        }
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener listener) {
        if (null != mPreferencesObject) {
            mPreferencesObject
                    .unregisterOnSharedPreferenceChangeListener(listener);
        }
    }

    /**
     * Get application Shared Preference file name
     * 
     * @return File name of the Shared Preference file
     */
    private static String getSharedPreferencesFileName() {
        return SHARED_PREFERENCES_FILENAME;
    }

    public static boolean isNuanceRecognizer() {
        boolean isNuanceRecognizer = VoiceRecognizerPreferences.DEFAULT_IS_NUANCE_RECOGNIZER;

        if (null != mPreferencesObject) {
            isNuanceRecognizer = mPreferencesObject.getBoolean(
                    VoiceRecognizerPreferenceKey.IS_NUANCE_RECOGNIZER.key(),
                    isNuanceRecognizer);
        }

        return isNuanceRecognizer;
    }

    public static String getRecognizerLanguage() {
        String recognizerLanguage = VoiceRecognizerPreferences.DEFAULT_RECOGNIZER_LANGUAGE;

        if (null != mPreferencesObject) {
            recognizerLanguage = mPreferencesObject.getString(
                    VoiceRecognizerPreferenceKey.RECOGNIZER_LANGUAGE.key(),
                    recognizerLanguage);
        }

        return recognizerLanguage;
    }

    public static boolean isShowConfitmDialog() {
        boolean isShowConfitmDialog = VoiceRecognizerPreferences.DEFAULT_SHOW_CONFIRM_DIALOG;

        if (null != mPreferencesObject) {
            isShowConfitmDialog = mPreferencesObject.getBoolean(
                    VoiceRecognizerPreferenceKey.SHOW_CONFIRM_DIALOG.key(),
                    isShowConfitmDialog);
        }

        return isShowConfitmDialog;
    }

    public static boolean isLooperEnabled() {
        boolean isSpeachButtonEnabled = VoiceRecognizerPreferences.DEFAULT_LOOPER_ENABLED;

        if (null != mPreferencesObject) {
            isSpeachButtonEnabled = mPreferencesObject.getBoolean(
                    VoiceRecognizerPreferenceKey.LOOPER_ENABLED.key(),
                    isSpeachButtonEnabled);
        }

        return isSpeachButtonEnabled;
    }

    public static boolean setLanguage(String language, Context context) {
        String currentLanguage = getRecognizerLanguage();
        if (!currentLanguage.equals(language)
                && (language.equals(RECOGNIZER_LANGUAGE_RUSSIAN) || language
                        .equals(RECOGNIZER_LANGUAGE_ENGLISH))) {
            VoiceRecognizerPreferences.getVoiceRecognizerSettings().setSetting(
                    VoiceRecognizerPreferenceKey.RECOGNIZER_LANGUAGE, language);
            setupLanguage();
            return true;
        }
        return false;
    }

    public static void setupLanguage() {
        Context context = PetrolConsumptionApplication.getAppContext();
        String languageToLoad = getRecognizerLanguage().substring(0, 2);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
        CommandsManager.getInstance().updateLanguage();
    }
}

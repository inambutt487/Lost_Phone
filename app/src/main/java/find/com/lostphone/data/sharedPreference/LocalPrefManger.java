package find.com.lostphone.data.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Aurang Zeb on 4/18/2016.
 */
public class LocalPrefManger implements IPrefConst {
    public static final String TAG = LocalPrefManger.class.getSimpleName();
    public static final String LOST_PHONE_SHARPREFS = "lostPhoneSharePref";

    private static void saveString(Context mContext, String mKey, String mValue) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mKey, mValue);
        editor.apply();
    }

    private static String getString(Context mContext, String mKey, String mDefValue) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(mKey, mDefValue);
    }

    private static void saveBoolean(Context mContext,
                                    String mKey, boolean mValue) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mKey, mValue);
        editor.apply();
    }

    private static boolean getBoolean(Context mContext, String mKey, boolean mDefValue) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(mKey, mDefValue);
    }

    private static void saveArrayListString(Context mContext, String mKey, HashSet mValue) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(mKey, mValue);
        editor.apply();
    }


    private static Set getArrayListString(Context mContext, String mKey, HashSet mDefValue) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getStringSet(mKey, mDefValue);
    }


    private static int getInt(Context mContext, String mKey, int mDefValue) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(mKey, mDefValue);
    }




    private static void saveInt(Context mContext, String mKey, int mValue) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mKey, mValue);
        editor.apply();
    }

    private static long getLong(Context mContext, String mKey, long mDefValue) {

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getLong(mKey, mDefValue);
    }

    private static void saveLong(Context mContext, String mKey, long mValue) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(LOST_PHONE_SHARPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(mKey, mValue);
        editor.apply();
    }

    public static boolean getEnableLockPhoneSMS(Context mContext) {

        return getBoolean(mContext, KEY_ENABLE_LOCK_PHONE_SMS, false);
    }

    public static void setEnableLockPhoneSMS(Context mContext, boolean mValue) {
        saveBoolean(mContext, KEY_ENABLE_LOCK_PHONE_SMS, mValue);
    }

    public static String getSecretCommandSMS(Context mContext) {

        return getString(mContext, KEY_SECRET_COMMAND_SMS, "lockmyphone");
    }

    public static void setEnableLockPhoneSMS(Context mContext, String mValue) {
        saveString(mContext, KEY_SECRET_COMMAND_SMS, mValue);
    }


    public static String getLockCodeSMS(Context mContext) {

        return getString(mContext, KEY_LOCK_CODE_SMS, "12345");
    }

    public static void setLockCodeSMS(Context mContext, String mValue) {
        saveString(mContext, KEY_LOCK_CODE_SMS, mValue);
    }

}

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

    public static void setSecretCommandSMS(Context mContext, String mValue) {
        saveString(mContext, KEY_SECRET_COMMAND_SMS, mValue);
    }


    public static String getLockCodeSMS(Context mContext) {

        return getString(mContext, KEY_LOCK_CODE_SMS, "12345");
    }

    public static void setLockCodeSMS(Context mContext, String mValue) {
        saveString(mContext, KEY_LOCK_CODE_SMS, mValue);
    }

    /*Ring Phone*/
    public static boolean getRingPhoneEnable(Context mContext) {

        return getBoolean(mContext, KEY_ENABLE_RING_PHONE, false);
    }

    public static void setRingPhoneEnable(Context mContext, boolean mValue) {
        saveBoolean(mContext, KEY_ENABLE_RING_PHONE, mValue);
    }

    public static String getRingSecretCommand(Context mContext) {

        return getString(mContext, KEY_RING_CODE, "ringmyphone");
    }

    public static void setRingSecretCommand(Context mContext, String mValue) {
        saveString(mContext, KEY_RING_CODE, mValue);
    }

    /* Phone Location*/

    public static boolean getLocationPhoneEnable(Context mContext) {

        return getBoolean(mContext, KEY_ENABLE_LOCATION_PHONE, false);
    }

    public static void setLocationPhoneEnable(Context mContext, boolean mValue) {
        saveBoolean(mContext, KEY_ENABLE_LOCATION_PHONE, mValue);
    }

    public static String getLocationSecretCommand(Context mContext) {

        return getString(mContext, KEY_LOCATION_CODE, "getPhoneLocation");
    }

    public static void setLocationSecretCommand(Context mContext, String mValue) {
        saveString(mContext, KEY_LOCATION_CODE, mValue);
    }

    /* Phone Location*/
    public static String getTrustedContactFirst(Context mContext) {

        return getString(mContext, KEY_TRUSTED_CONTACT_FIRST, "");
    }

    public static void setTrustedContactFirst(Context mContext, String mValue) {
        saveString(mContext, KEY_TRUSTED_CONTACT_FIRST, mValue);
    }


    public static String getTrustedContactSecond(Context mContext) {

        return getString(mContext, KEY_TRUSTED_CONTACT_SECOND, "");
    }

    public static void setTrustedContactSecond(Context mContext, String mValue) {
        saveString(mContext, KEY_TRUSTED_CONTACT_SECOND, mValue);
    }

    public static String getLockCodeAntiTheft(Context mContext) {

        return getString(mContext, KEY_SECRET_COMMAND_ANTI_THEFT, "12345");
    }

    public static void setLockCodeAntiTheft(Context mContext, String mValue) {
        saveString(mContext, KEY_SECRET_COMMAND_ANTI_THEFT, mValue);
    }



    public static boolean getAntiTheftEnable(Context mContext) {

        return getBoolean(mContext, KEY_ENABLE_ANTI_THEFT, false);
    }

    public static void setAntiTheftEnable(Context mContext, boolean mValue) {
        saveBoolean(mContext, KEY_ENABLE_ANTI_THEFT, mValue);
    }

    public static String getMessageAntiTheft(Context mContext) {

        return getString(mContext, KEY_MESSAGE_ANTI_THEFT, "");
    }

    public static void setMessageAntiTheft(Context mContext, String mValue) {
        saveString(mContext, KEY_MESSAGE_ANTI_THEFT, mValue);
    }

    public static String getSimSerialNumber(Context mContext) {

        return getString(mContext, KEY_SIM_SERIAL_NUMBER, "");
    }

    public static void setSimSerialNumber(Context mContext, String mValue) {
        saveString(mContext, KEY_SIM_SERIAL_NUMBER, mValue);
    }

}

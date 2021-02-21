package aroundu.snvk.com.uaroundu.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppUtils {
    public static void setToken(Context context, String token) {
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString("Token", token);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getToken(Context context) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getString("Token", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setId(Context context, String Id) {
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString("ad_id", Id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getId(Context context) {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getString("ad_id", "-1");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setFirstRun(Context context, Boolean isFirtRun) {
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("first_run", isFirtRun);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean getFirstRun(Context context) {
        try {
            return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("first_run", true));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public static void setFirstGuide(Context context, Boolean isFirtGuide) {
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("First_Guide", isFirtGuide);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean getFirstGuide(Context context) {
        try {
            return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("First_Guide", true));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public static void setIsBusTipRemembered(Context context, Boolean isRemembered){
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("Bus_Tip", isRemembered);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean getIsBusTipRemembered(Context context) {
        try {
            return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Bus_Tip", false));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public static void setIsCoverageTipRemembered(Context context, Boolean isRemembered){
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("Coverage_Tip", isRemembered);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean getIsCoverageTipRemembered(Context context) {
        try {
            return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Coverage_Tip", false));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public static void setIsBusStopTipRemembered(Context context, Boolean isRemembered){
        try {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("Bus_Stop_Tip", isRemembered);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean getIsBusStopTipRemembered(Context context) {
        try {
            return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Bus_Stop_Tip", false));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }
}

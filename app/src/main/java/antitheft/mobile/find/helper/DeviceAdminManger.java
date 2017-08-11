package antitheft.mobile.find.helper;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import antitheft.mobile.find.receiver.DeviceAdminReceiverLockPhone;

/**
 * Created by Aurang Zeb on 31-Jul-17.
 */

public class DeviceAdminManger {

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    public DeviceAdminManger(Context context) {
        devicePolicyManager = (DevicePolicyManager)
                context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName =
                new ComponentName(context, DeviceAdminReceiverLockPhone.class);
    }

    public boolean isActiveAdmin() {
        return devicePolicyManager.isAdminActive(componentName);
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return devicePolicyManager;
    }

    public void setDevicePolicyManager(DevicePolicyManager devicePolicyManager) {
        this.devicePolicyManager = devicePolicyManager;
    }

    public ComponentName getComponentName() {
        return componentName;
    }

    public void setComponentName(ComponentName componentName) {
        this.componentName = componentName;
    }

    public void removeAdminRights() {
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    public boolean lockWithPIN(String pin) {

        devicePolicyManager.setPasswordQuality(componentName,
                DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        devicePolicyManager.setPasswordMinimumLength(componentName, 5);
        return devicePolicyManager.resetPassword(pin,
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
    }

/*    public static boolean lockwithPIN(){
        ComponentName compName1;
        DevicePolicyManager deviceManager1;
        Context context1 = MyApplication.getAppContext();
        deviceManager1 = (DevicePolicyManager)context1.getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName1 = new ComponentName(context1, AdminReceiver.class);
        deviceManager1.setPasswordQuality(compName1,DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        deviceManager1.setPasswordMinimumLength(compName1, 5);
        boolean result = deviceManager1.resetPassword("123456",DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        Log.i("lockwithPIN","El bloqueo con password ha resultado correcto: " + result);
        return result;
    }

    public static boolean unlockwithPIN(){

        ComponentName compName1;
        DevicePolicyManager deviceManager1;
        Context context1 = MyApplication.getAppContext();
        deviceManager1 = (DevicePolicyManager)context1.getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName1 = new ComponentName(context1, AdminReceiver.class);
        deviceManager1.setPasswordMinimumLength(compName1, 0);
        boolean result = deviceManager1.resetPassword("", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        Log.i("unlockwithPIN","El desbloqueo con password ha resultado correcto: " + result);
        return result;
    }*/


}

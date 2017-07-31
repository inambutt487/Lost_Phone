package find.com.lostphone.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import find.com.lostphone.receiver.DeviceAdminReceiverLockPhone;

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
}

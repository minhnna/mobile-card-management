package vn.com.cardmanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DesiredCapabilities {
    String app = "D:\\AppiumJavaJar\\MobiFone Next_v4.7_apkpure.com.apk";
    String deviceName;
    String platformName="Android";
    @JsonProperty("app-package")
    String appPackage = "vn.mobifone.mobifonenext";
    boolean autoGrantPermissions =true;
    int newCommandTimeout = 86400;
    boolean fullReset = false;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public boolean isAutoGrantPermissions() {
        return autoGrantPermissions;
    }

    public void setAutoGrantPermissions(boolean autoGrantPermissions) {
        this.autoGrantPermissions = autoGrantPermissions;
    }

    public int getNewCommandTimeout() {
        return newCommandTimeout;
    }

    public void setNewCommandTimeout(int newCommandTimeout) {
        this.newCommandTimeout = newCommandTimeout;
    }

    public boolean isFullReset() {
        return fullReset;
    }

    public void setFullReset(boolean fullReset) {
        this.fullReset = fullReset;
    }
}

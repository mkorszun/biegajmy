package com.biegajmy.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Device implements Serializable {
    enum Platform {android, ios}

    @SerializedName("device_token") public String deviceToken;
    @SerializedName("platform") public Platform platform;

    public Device(String deviceToken, Platform platform) {
        this.deviceToken = deviceToken;
        this.platform = platform;
    }

    public Device(String deviceToken) {
        this(deviceToken, Platform.android);
    }

    @Override public String toString() {
        return String.format("DeviceInfo = (%s/%s)", deviceToken, platform);
    }
}

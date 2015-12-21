package com.sayzenberg.huebandremote;

import android.util.Log;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;

/**
 * Created by Stanley Ayzenberg on 12/21/15.
 */
public class HueManager {

    private PHHueSDK phHueSDK;
    private PHBridge bridge;
    private static final int MAX_HUE = 65536;
    public static final String TAG = "QuickStart";

    public HueManager() {
        phHueSDK = PHHueSDK.create();
    }

    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };


    public void changeLightBrightness(int brightness) {
        if (this.bridge == null) {
            this.bridge = phHueSDK.getSelectedBridge();
        }

        if (brightness < 0) {
            brightness = 0;
        }
        else if(brightness > 254) {
            brightness = 254;
        }

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for(PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            if(brightness > 0) {
                lightState.setOn(true);
                lightState.setBrightness(brightness);
            }
            else {
                lightState.setOn(false);
            }
            bridge.updateLightState(light, lightState, listener);
        }
    }

    public void disconnect() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
        }
    }

}

package com.sayzenberg.huebandremote;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.microsoft.band.BandClient;
import com.microsoft.band.tiles.TileButtonEvent;
import com.microsoft.band.tiles.TileEvent;

public class BandService extends Service {

    public static final int COMMAND_LIGHTS_ON = 1;
    public static final int COMMAND_LIGHTS_OFF = 2;

    private BandClient client = null;

    private HueManager hueManager;

    public BandService() {

    }

    public BandService(HueManager hueManager, BandClient client) {
        this.hueManager = hueManager;
        this.client = client;
    }

    public class BandServiceBinder {
        BandService getService() {
            return BandService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(TileEvent.ACTION_TILE_OPENED);
        filter.addAction(TileEvent.ACTION_TILE_BUTTON_PRESSED);
        filter.addAction(TileEvent.ACTION_TILE_CLOSED);
        registerReceiver(messageReceiver, filter);
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == TileEvent.ACTION_TILE_BUTTON_PRESSED) {
                TileButtonEvent buttonData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
                if(buttonData.getElementID() == COMMAND_LIGHTS_ON) {
                    hueManager.changeLightBrightness(254);
                }
                else if (buttonData.getElementID() == COMMAND_LIGHTS_OFF) {
                    hueManager.changeLightBrightness(0);
                }
            }
        }
    };

}

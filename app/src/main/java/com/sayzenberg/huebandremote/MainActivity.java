package com.sayzenberg.huebandremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.TileButtonEvent;
import com.microsoft.band.tiles.TileEvent;
import com.microsoft.band.tiles.pages.FlowPanel;
import com.microsoft.band.tiles.pages.FlowPanelOrientation;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.tiles.pages.PageLayout;
import com.microsoft.band.tiles.pages.TextButton;
import com.microsoft.band.tiles.pages.TextButtonData;

import java.util.List;
import java.util.UUID;

public class MainActivity extends ActionBarActivity {

    public static final int COMMAND_LIGHTS_ON = 1;
    public static final int COMMAND_LIGHTS_OFF = 2;

    private BandClient client = null;
    private BandService service = null;
    private Button pairBandButton;
    private TextView txtStatus;

    private boolean isRegistered = false;

    private static final UUID tileId = UUID.fromString("cc0D508F-70A3-47D4-BBA3-812BADB1F8Aa");
    private static final UUID pageId1 = UUID.fromString("b1234567-89ab-cdef-0123-456789abcd00");

    private HueManager hueManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        hueManager = new HueManager();
        Button lightsOnButton;
        lightsOnButton = (Button) findViewById(R.id.buttonOn);
        lightsOnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hueManager.changeLightBrightness(254);
            }
        });
        Button lightsOffButton;
        lightsOffButton = (Button) findViewById(R.id.buttonOff);
        lightsOffButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hueManager.changeLightBrightness(0);
            }
        });
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        pairBandButton = (Button) findViewById(R.id.buttonPairBand);
        pairBandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");
                new appTask().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        hueManager.disconnect();
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                // Do nothing as this is happening during destroy
            } catch (BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(TileEvent.ACTION_TILE_OPENED);
            filter.addAction(TileEvent.ACTION_TILE_BUTTON_PRESSED);
            filter.addAction(TileEvent.ACTION_TILE_CLOSED);
            registerReceiver(messageReceiver, filter);
            isRegistered = true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(messageReceiver);
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == TileEvent.ACTION_TILE_OPENED) {
                TileEvent tileOpenData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
                appendToUI("Tile open event received\n" + tileOpenData.toString()+ "\n\n");
            } else if (intent.getAction() == TileEvent.ACTION_TILE_BUTTON_PRESSED) {
                TileButtonEvent buttonData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
                appendToUI("Button event received\n" + buttonData.toString()+ "\n\n");
                if(buttonData.getElementID() == COMMAND_LIGHTS_ON) {
                    hueManager.changeLightBrightness(254);
                }
                else if (buttonData.getElementID() == COMMAND_LIGHTS_OFF) {
                    hueManager.changeLightBrightness(0);
                }
            } else if (intent.getAction() == TileEvent.ACTION_TILE_CLOSED) {
                TileEvent tileCloseData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
                appendToUI("Tile close event received\n" + tileCloseData.toString()+ "\n\n");
            }
        }
    };

    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.append(string);
            }
        });
    }

    private boolean doesTileExist(List<BandTile> tiles, UUID tileId) {
        for (BandTile tile:tiles) {
            if (tile.getTileId().equals(tileId)) {
                return true;
            }
        }
        return false;
    }

    private boolean addTile() throws Exception {
        if (doesTileExist(client.getTileManager().getTiles().await(), tileId)) {
            return true;
        }

		/* Set the options */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap tileIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.b_icon, options);

        BandTile tile = new BandTile.Builder(tileId, "Button Tile", tileIcon)
                .setPageLayouts(createButtonLayout())
                .build();
        appendToUI("Button Tile is adding ...\n");
        if (client.getTileManager().addTile(this, tile).await()) {
            appendToUI("Button Tile is added.\n");
            return true;
        } else {
            appendToUI("Unable to add button tile to the band.\n");
            return false;
        }
    }

    private PageLayout createButtonLayout() {
        return new PageLayout(
                new FlowPanel(15, 0, 260, 105, FlowPanelOrientation.VERTICAL)
                        .addElements(new TextButton(0, 5, 210, 45).setMargins(0, 5, 0 ,0).setId(COMMAND_LIGHTS_ON).setPressedColor(Color.BLUE))
                        .addElements(new TextButton(0, 0, 210, 45).setMargins(0, 5, 0 ,0).setId(COMMAND_LIGHTS_OFF).setPressedColor(Color.BLUE))
        );
    }

    private void updatePages() throws BandIOException {
        client.getTileManager().setPages(tileId,
                new PageData(pageId1, 0)
                        .update(new TextButtonData(COMMAND_LIGHTS_ON, "Lights on"))
                        .update(new TextButtonData(COMMAND_LIGHTS_OFF, "Lights off")));
        appendToUI("Send button page data to tile page \n\n");
    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        appendToUI("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }

    private class appTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    appendToUI("Band is connected.\n");
                    if (addTile()) {
                        updatePages();
//                        service = new BandService(hueManager, client);
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case DEVICE_ERROR:
                        exceptionMessage = "Please make sure bluetooth is on and the band is in range.\n";
                        break;
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    case BAND_FULL_ERROR:
                        exceptionMessage = "Band is full. Please use Microsoft Health to remove a tile.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occurred: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }
}

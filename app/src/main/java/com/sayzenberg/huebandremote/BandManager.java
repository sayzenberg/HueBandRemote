package com.sayzenberg.huebandremote;

/**
 * Created by Stanley Ayzenberg on 12/21/15.
 */
public class BandManager {
//
//    public static final int COMMAND_LIGHTS_ON = 1;
//    public static final int COMMAND_LIGHTS_OFF = 2;
//
//    private BandClient client = null;
//    private Button pairBandButton;
//    private TextView txtStatus;
//
//    private static final UUID tileId = UUID.fromString("cc0D508F-70A3-47D4-BBA3-812BADB1F8Aa");
//    private static final UUID pageId1 = UUID.fromString("b1234567-89ab-cdef-0123-456789abcd00");
//
//    private HueManager hueManager;
//
//    public BandManager(HueManager hueManager) {
//        this.hueManager = hueManager;
//    }
//
//    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction() == TileEvent.ACTION_TILE_BUTTON_PRESSED) {
//                TileButtonEvent buttonData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
//                if(buttonData.getElementID() == COMMAND_LIGHTS_ON) {
//                    hueManager.changeLightBrightness(254);
//                }
//                else if (buttonData.getElementID() == COMMAND_LIGHTS_OFF) {
//                    hueManager.changeLightBrightness(0);
//                }
//            }
//        }
//    };
//
//    private boolean doesTileExist(List<BandTile> tiles, UUID tileId) {
//        for (BandTile tile:tiles) {
//            if (tile.getTileId().equals(tileId)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean addTile() throws Exception {
//        if (doesTileExist(client.getTileManager().getTiles().await(), tileId)) {
//            return true;
//        }
//
//		/* Set the options */
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inScaled = false;
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap tileIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.b_icon, options);
//
//        BandTile tile = new BandTile.Builder(tileId, "Button Tile", tileIcon)
//                .setPageLayouts(createButtonLayout())
//                .build();
////        appendToUI("Button Tile is adding ...\n");
//        if (client.getTileManager().addTile(this, tile).await()) {
////            appendToUI("Button Tile is added.\n");
//            return true;
//        } else {
////            appendToUI("Unable to add button tile to the band.\n");
//            return false;
//        }
//    }
//
//    private PageLayout createButtonLayout() {
//        return new PageLayout(
//                new FlowPanel(15, 0, 260, 105, FlowPanelOrientation.VERTICAL)
//                        .addElements(new TextButton(0, 5, 210, 45).setMargins(0, 5, 0 ,0).setId(COMMAND_LIGHTS_ON).setPressedColor(Color.BLUE))
//                        .addElements(new TextButton(0, 0, 210, 45).setMargins(0, 5, 0, 0).setId(COMMAND_LIGHTS_OFF).setPressedColor(Color.BLUE))
//        );
//    }
//
//    private void updatePages() throws BandIOException {
//        client.getTileManager().setPages(tileId,
//                new PageData(pageId1, 0)
//                        .update(new TextButtonData(COMMAND_LIGHTS_ON, "Lights on"))
//                        .update(new TextButtonData(COMMAND_LIGHTS_OFF, "Lights off")));
////        appendToUI("Send button page data to tile page \n\n");
//    }
//
//    private boolean getConnectedBandClient() throws InterruptedException, BandException {
//        if (client == null) {
//            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
//            if (devices.length == 0) {
////                appendToUI("Band isn't paired with your phone.\n");
//                return false;
//            }
//            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
//        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
//            return true;
//        }
//
////        appendToUI("Band is connecting...\n");
//        return ConnectionState.CONNECTED == client.connect().await();
//    }
//
//    private class appTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                if (getConnectedBandClient()) {
////                    appendToUI("Band is connected.\n");
//                    if (addTile()) {
//                        updatePages();
//                    }
//                } else {
////                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
//                }
//            } catch (BandException e) {
//                String exceptionMessage = "";
//                switch (e.getErrorType()) {
//                    case DEVICE_ERROR:
//                        exceptionMessage = "Please make sure bluetooth is on and the band is in range.\n";
//                        break;
//                    case UNSUPPORTED_SDK_VERSION_ERROR:
//                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
//                        break;
//                    case SERVICE_ERROR:
//                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
//                        break;
//                    case BAND_FULL_ERROR:
//                        exceptionMessage = "Band is full. Please use Microsoft Health to remove a tile.\n";
//                        break;
//                    default:
//                        exceptionMessage = "Unknown error occurred: " + e.getMessage() + "\n";
//                        break;
//                }
////                appendToUI(exceptionMessage);
//
//            } catch (Exception e) {
////                appendToUI(e.getMessage());
//            }
//            return null;
//        }
//    }
}

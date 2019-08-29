package com.bluefletch.motorola;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class DataWedgeIntentHandler {

    private static String TAG = DataWedgeIntentHandler.class.getSimpleName();
    private Context applicationContext;
    private static String DEFAULT_ACTION = "com.bluefletch.motorola.datawedge.ACTION";
    public String dataWedgeAction = DEFAULT_ACTION;

    /**
     * This function must be called with the intent Action as configured in the
     * DataWedge Application
     **/
    public void setDataWedgeIntentAction(String action) {
        Log.i(TAG, "Setting data wedge intent to " + action);
        if (action == null || "".equals(action))
            return;
        this.dataWedgeAction = action;

    }

    protected ScanCallback<BarcodeScan> scanCallback;

    public void setScanCallback(ScanCallback<BarcodeScan> callback) {
        scanCallback = callback;
    }

    public DataWedgeIntentHandler(Context context) {
        TAG = this.getClass().getSimpleName();
        applicationContext = context;
    }

    /**
     * Register the broadcast receiver
     */
    public void start(String action) {
        IntentFilter filter = new IntentFilter();

        if (action != null) {
            filter.addAction(action);
        } else {
            filter.addAction(dataWedgeAction);
        }

        filter.addCategory("android.intent.category.DEFAULT");
        applicationContext.registerReceiver(dataReceiver, filter);
        startScanner();
    }

    /**
     * Unregister the broadcast receiver
     */
    public void stop() {
        try {
            applicationContext.unregisterReceiver(dataReceiver);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        stopScanner();
    }

    public void startScanner() {
        try {
            String softScanTrigger = "com.symbol.datawedge.api.ACTION_SOFTSCANTRIGGER";
            String extraData1 = "com.symbol.datawedge.api.EXTRA_PARAMETER";
            Intent i1 = new Intent();
            i1.setAction(softScanTrigger);
            i1.putExtra(extraData1, "START_SCANNING");
            applicationContext.sendBroadcast(i1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void stopScanner() {
        try {
            String softScanTrigger = "com.symbol.datawedge.api.ACTION_SOFTSCANTRIGGER";
            String extraData1 = "com.symbol.datawedge.api.EXTRA_PARAMETER";
            Intent i1 = new Intent();
            i1.setAction(softScanTrigger);
            i1.putExtra(extraData1, "STOP_SCANNING");
            applicationContext.sendBroadcast(i1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Receiver to handle receiving data from intents
     */
    private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Data receiver trigged");
            try {
                String barcodeData = intent.getStringExtra("com.motorolasolutions.emdk.datawedge.data_string");
                if (barcodeData != null) {
                    scanCallback.execute(new BarcodeScan(barcodeData));
                }
            } catch (Exception ex) {
                Log.e(TAG, "Exception raised during callback processing.", ex);
            }
        }
    };
}
package com.bluefletch.motorola.plugin;

import android.util.Log;

import com.bluefletch.motorola.DataWedgeIntentHandler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MotorolaDatawedgePlugin extends CordovaPlugin {

    private static String TAG = "MotorolaDatawedgePlugin";
    private DataWedgeIntentHandler wedge;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        wedge = new DataWedgeIntentHandler(cordova.getActivity().getBaseContext());
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        switch (Actions.getActionByName(action)) {
        case START_SCANNER:

            String intentAction = null;

            if (args.length() > 0) {
                intentAction = args.getString(0);
            }

            if (intentAction != null && intentAction.length() > 0) {
                Log.i(TAG, "Intent action length  " + intentAction.length());
                wedge.setDataWedgeIntentAction(intentAction);
            }

            wedge.start(intentAction);

            break;
        case SCAN_REGISTER:
            if ("scanner.register".equals(action)) {
                wedge.setScanCallback(scan -> {
                    Log.i(TAG, "Scan result [" + scan.Barcode + "-" + scan.Barcode + "].");
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("barcode", scan.Barcode);
                        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
                        pluginResult.setKeepCallback(true);
                        callbackContext.sendPluginResult(pluginResult);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error building json object", e);

                    }
                });
            }
            break;

        case STOP_SCANNER:
            wedge.stop();
            break;
        case INVALID:
            JSONObject obj = new JSONObject();
            obj.put("barcode", Actions.INVALID.getDescription());
            callbackContext.error(obj);
            break;
        }

        return true;
    }

    /**
     * Always close the current intent reader
     */
    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        wedge.stop();
    }

    /**
     * Always resume the current activity
     */
    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        wedge.start(wedge.dataWedgeAction);
    }

    public enum Actions {

        START_SCANNER("start", "Start scanner on barcode reader."),
        SCAN_REGISTER("scanner.register", "Register scanner callback."), STOP_SCANNER("stop", "Stop scanner callback."),
        INVALID("", "Invalid or not found action.");

        private String action;
        private String description;

        Actions(String action, String description) {
            this.action = action;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static Actions getActionByName(String action) {
            for (Actions a : Actions.values()) {
                if (a.action.equalsIgnoreCase(action)) {
                    return a;
                }
            }
            return INVALID;
        }
    }
}
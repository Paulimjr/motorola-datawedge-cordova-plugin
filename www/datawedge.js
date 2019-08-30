cordova.define("com.bluefletch.motorola.MotorolaDataWedge", function (require, exports, module) {
    var cordova = require('cordova');
    var exec = require('cordova/exec');

    /**
            * Constructor.
    *
    * @returns {DataWedge}
    */
    function DataWedge() {

    };

    /**
     * Turn on DataWedge (default profile) and listen for event.  Listens for hardward button events.
     * 
     * @param successCallback - Success function should expect a barcode to be passed in
     * @param intentAction - action to listen for.  This is what you configured in the DataWedge app.  
     *       Defaults to: "com.bluefletch.motorola.datawedge.ACTION";
     */
    DataWedge.prototype.start = function (intentAction) {
        var args = [];
        if (intentAction) {
            args[0] = intentAction;
        }
        exec(null, null, 'MotorolaDataWedge', 'start', args);
    };

    /**
     * Turn off DataWedge plugin
     */
    DataWedge.prototype.stop = function () {

        exec(null, null, 'MotorolaDataWedge', 'stop', []);
    };


    /**
     * Register a callback for scan events.  This function will be called when barcdoes are read
     */
    DataWedge.prototype.registerForBarcode = function (callback) {

        exec(callback, null, 'MotorolaDataWedge', 'scanner.register', []);
    };


    //create instance
    var DataWedge = new DataWedge();

    module.exports = DataWedge;
});
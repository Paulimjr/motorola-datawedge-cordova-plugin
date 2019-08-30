Cordova Motorola DataWedge Plugin
============

This is a Cordova/Phonegap plugin to interact with Motorola ruggedized devices' Barcode Scanners Readers (eg, ET1, MC40, TC55).  The plugin works by interacting with the "DataWedge" application configured to output scan events.

=============

This plugin is compatible with plugman.  To install, run the following from your project command line: 
```$ cordova plugin add https://github.com/BlueFletch/motorola-datawedge-cordova-plugin.git```


==============

<h3>Configure DataWedge:</h3>
You have two options to interact with the current version of the DataWedge:

1. Broadcast an intent from the Default (0) profile.  This can be a custom action of your choosing.  NOTE: Category must be empty.
2. Create a custom profile associated to your app, the option Intent Delivery select "BroadCast Intent".  This must use the default plugin action: _"com.bluefletch.motorola.datawedge.ACTION"_ and EMPTY category.

The DataWedge User Guide is located here: `https://launchpad.motorolasolutions.com/documents/dw_user_guide.html`

Intent configuration: `https://launchpad.motorolasolutions.com/documents/dw_user_guide.html#_intent_output`

Special configuration for option 2:

1. You'll need to first create a Profile in your DataWedge Application to run **BroadCast Intent** for an intent on scan events as applicable.  NOTE: you MUST set the action to: _"com.bluefletch.motorola.datawedge.ACTION"_ and category to EMPTY/BLANK
2. Associate your app to the DataWedge profile so it loads with your app. Configure this under `(Your profile) > Associated apps > New app/activity (menu button) > (Select your app)`
3. Lastly, you need to set your application to be "singleTop" in Cordova.  This will make sure each scan doesn't launch a new instance of your app. Add the following to your config.xml: 
```<preference name="AndroidLaunchMode" value="singleTop" />```


<h3>To Use:</h3>

1) First, you need to "activate" the plugin and OPTIONALLY tell it what intent to listen for.  Default is _"com.bluefletch.motorola.datawedge.ACTION"_
```
   document.addEventListener("deviceready", function(){ 
      if (window.datawedge) {
      	 datawedge.start(); //uses default
         //datawedge.start("com.yourintent.whatever_you_configured_to_broadcast_in_default_profile");
      }
   });
```

2) Register for callbacks for barcode scanning readers:
```
   document.addEventListener("deviceready", function(){ 
       ...
       datawedge.registerForBarcode(function(data){
           var barcode  = data.barcode;
           console.log("Barcode scanned : " + barcode);
       });

```

=============
<h3>More API options:</h3>

<h5>Scanner: </h5>
* You can wire a soft button to the barcode scanner by calling `datawedge.start()`
* Turn off the scanner manually using: `datawedge.stop()`

==============
Copyright 2014 BlueFletch Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


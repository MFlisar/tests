# BUGS

### Android

Following issues are **Android Connect IQ app** related (https://play.google.com/store/apps/details?id=com.garmin.android.apps.connectmobile)

* **[BUG1]:** the app does not open settings of a custom watchface nor does it list the installed custom watchfaces on a Fenix 7X (it does do both with a Fenix 6X) - check out following screenshot from using this app with the Fenix 7X SS:

  <img src="Android_Fenix7X.jpg" width="400">

* **[BUG2]:** inside the settings of a custom watchface, the app does not show the help urls that a watchface developer can define (check out the helpUrl field that's defined here: https://developer.garmin.com/connect-iq/core-topics/app-settings/) - this helpUrl did work in the past though (and it works inside the windows app) - check out following screenshot from using this app with the Fenix 6X Pro:

  <img src="Android_Fenix6x_helpUrl.jpg" width="400">

### Windows

Following issues are **Windows App** related:

* **[BUG3]:** on a Fenix 7X the windows app does not work with settings of a custom watchface either, it does following:
  * it does not show settings translations but does show their resource identifier (which may be readable or may not)
  * the same is true for list values
  * it does not show labels for settings at all, checkboxes are e.g. checkboxes without any text anywhere...
  * check out following screenshot from using this app with the Fenix 7X SS:

    <img src="Window_Fenix7X_missing_texts.jpg" width="400">

        

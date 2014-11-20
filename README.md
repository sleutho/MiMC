## Android App for a NanoBlogger weblog

    http://public.beuth-hochschule.de/~sleuthold/blog


The [weblog](http://public.beuth-hochschule.de/~sleuthold/blog) is powered by [NanoBlogger 3.4.2](http://nanoblogger.sourceforge.net/). A [Python script](/cgi-script/nb_api.cgi) provides a GET-API via JSON to support the backend.

The following features are implemented:

* View each post in a WebView
* Navigate *back* and *forward*
* Notifications
* Sync via Android SyncAdapter using an anonymous account
* Notify new weblog posts
* Browse posts via *category* or *archive*
* Share link to individual posts

This app was only shared privately and was never published on the Play Store platform. However, a compiled version can be [download here](http://public.beuth-hochschule.de/~sleuthold/files/android/MiMC.apk) for testing.

The Android app could be reused for almost every compatible NanoBlogger weblog, as long as your webhost comes with cgi and Python support.

![](https://raw.githubusercontent.com/sleutho/MiMC/master/screenshots/device-2012-03-10-1.png)

![](https://raw.githubusercontent.com/sleutho/MiMC/master/screenshots/device-2012-03-10-2.png)

![](https://raw.githubusercontent.com/sleutho/MiMC/master/screenshots/device-2012-03-10-3.png)

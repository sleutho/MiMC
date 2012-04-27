## Android App for a NanoBlogger webblog

    http://public.beuth-hochschule.de/~sleuthold/blog


The weblog is powered by **NanoBlogger 3.4.2**. A Python cgi-script/nb_api.cgi provides a web-api for the Android app to interface with the weblog via JSON.

The following features are implemented:

* view each post in a WebView
* navigate *back* and *forward*
* update app
* sync via Android SyncAdapter using an anonymous account
* notify new weblog posts
* browse posts via *category* or *archive*
* share posts


The Android app could be reused for almost every compatible NanoBlogger weblog, as long as your webhost comes with cgi-bin support.


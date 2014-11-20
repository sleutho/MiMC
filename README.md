## Android App for a NanoBlogger webblog

    http://public.beuth-hochschule.de/~sleuthold/blog


The weblog is powered by [NanoBlogger 3.4.2](http://nanoblogger.sourceforge.net/). A [Python script](/cgi-script/nb_api.cgi) provides a GET-API via JSON to support the backend.

The following features are implemented:

* View each post in a WebView
* Navigate *back* and *forward*
* Notifications
* Sync via Android SyncAdapter using an anonymous account
* Notify new weblog posts
* Browse posts via *category* or *archive*
* Share link to individual posts


The Android app could be reused for almost every compatible NanoBlogger weblog, as long as your webhost comes with cgi and Python support.

# MiMC - Me in Motor City

## Android App for a NanoBlogger webblog:

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


The following web queries are implemented to support the app:
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=latest&format=html
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=version&format=html
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=categories&format=html
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=categories&category=daily&format=html
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=archive&format=html
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=archive&year=2012&format=html
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=entry&tag=2012-02-26T07_07_06.txt
https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=nav&tag=2012-02-26T07_07_06.txt&format=html
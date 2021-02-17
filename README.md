# NCTB
With interactive design NCTB app provide all kinds of NCTB books from class 1 to class 10 for Bangladeshi students. Web view is used for reading the books in online.
Students can also download books from Google Drive for offline reading. Books are categorized by the class of the student so that it's easy to
find books for a student of specific class

## How to use the source code:
### Config file location:
             nctbandroid\app\src\main\java\org\richit\nctb

#### Details about the variables in the Config.java:

* To show admob ads, put your admob info these variables: `admobAppId`,`admobBannerAds`,`admobInterAds`,`admobRewardAds`

* To show facebook ads ads, put your facebook ad info these variables: `facebookAdsBanner`,`facebookAdsInter`,`facebookAdsReward`

* To show mopub ads ads, put your mopub ad info these variables:  `mopubAdsBanner`,`mopubAdsInter`,`mopubAdsReward`

* To show startApp ads, put your admob info this variable: `startAppAds` ( You can either put them in PLAIN TEXT or encrypted. Both functions/methods are implemented in `GlobalMethods.java` but the PLAIN TEXT function is currently commented/disabled )

* To promote your own apps within your own apps, this library ( https://github.com/p32929/HouseAds2 ) is used and this JSON ( https://github.com/p32929/SomeHowTosAndTexts/blob/master/HouseAdsJson/house_ads2.json ) is used. To change the infos, you can use a JSON url and put it into `houseAdsUrl` variable in the Config class

* To show information about your team in the About screen, this library ( https://github.com/p32929/MaterialOfficeAbout ) is used and this JSON ( https://github.com/p32929/SomeHowTosAndTexts/blob/master/Office/OfficeInfoMaterial.json ) is used. To change the infos, you can use a JSON url and put it into `officeAboutUrl` variable in the Config class

* To change the Book List, use a JSON link and put it in `booksDataUrl` variable

* To install web view on your device Google Play Store web view link is put in `webViewUrl` variable

* To download PDF of the books for offline reading google drive link is put in `bookDriveLink` variable

* To give some feedback about the app feedback email is put in `feedBackEmail` variable

#### Library details:

* For loading images easily, we used this library: https://github.com/square/picasso

* For showing updates and loading all the books' infos, we used this library: https://github.com/p32929/AndroidAppUpdater

* For JSON serialization/deserialization, we used this library: https://github.com/google/gson

* For other functionality like about, rate, support etc. we use drawer from this library: https://github.com/mikepenz/MaterialDrawer

* For downloading books from server we use this library: https://github.com/amitshekhariitbhu/Fast-Android-Networkingr

* For reporting app crashes, we used this library: https://github.com/MindorksOpenSource/CrashReporter

#### For any assistance about the source code, feel free to contact us:
Facebook: https://www.facebook.com/p32929
Skype: p32929
WhatsApp: +8801796306262
Portfolio: https://rich-it.github.io/

Thank you...
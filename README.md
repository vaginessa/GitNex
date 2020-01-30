[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Release](https://img.shields.io/badge/dynamic/json.svg?label=release&url=https://gitea.com/api/v1/repos/gitnex/GitNex/releases&query=$[0].tag_name)](https://gitea.com/gitnex/GitNex/releases)
[![Build Status](https://drone.gitea.com/api/badges/gitnex/GitNex/status.svg)](https://drone.gitea.com/gitnex/GitNex)
[![Crowdin](https://badges.crowdin.net/gitnex/localized.svg)](https://crowdin.com/project/gitnex)

[<img alt="Become a Patroen" src="https://c5.patreon.com/external/logo/become_a_patron_button@2x.png" height="40"/>](https://www.patreon.com/mmarif)
[<img alt="Donate using Liberapay" src="https://liberapay.com/assets/widgets/donate.svg" height="40"/>](https://liberapay.com/mmarif/donate)

# GitNex - Android client for Gitea

GitNex is a free, open-source Android client for Git repository management tool Gitea. Gitea is a community managed fork of Gogs, lightweight code hosting solution written in Go.

GitNex is licensed under GPLv3 License. See the LICENSE file for the full license text.
No trackers are used and source code is available here for anyone to audit.

## Downloads
[<img alt='Get it on F-droid' src='https://gitlab.com/fdroid/artwork/raw/master/badge/get-it-on.png' height="80"/>](https://f-droid.org/en/packages/org.mian.gitnex/)
[<img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height="80"/>](https://play.google.com/store/apps/details?id=org.mian.gitnex)
[<img alt='Download APK' src='https://gitnex.com/img/download-apk.png' height="80"/>](https://gitea.com/gitnex/GitNex/releases)

## Note about Gitea version
Please make sure that you are on Gitea **1.10.x** stable release or later. Below this may not work as one would expect because of the newly added objects to the API at later versions. Please consider updating your Gitea server.

Check the versions [compatibility page](https://gitea.com/gitnex/GitNex/wiki/Compatibility) which lists all the supported versions with compatibility ratio.

## Build from source
Option 1 - Download the source code, open it in Android Studio and build it there.

Option 2 - Open terminal(Linux) and cd to the project dir. Run `./gradlew build`.

## Features
- File and directory browser
- Create files
- Explore repositories
- Issues list
- Pull requests
- Merge pull request
- [MANY MORE](https://gitea.com/gitnex/GitNex/wiki/Features)

## Contributing
[CONTRIBUTING](https://gitea.com/gitnex/GitNex/src/branch/master/CONTRIBUTING.md)

## Translation
Help us translate GitNex to your native language.

We use [Crowdin](https://crowdin.com/project/gitnex) for translation.
If your language is not listed, please request [here](https://gitea.com/gitnex/GitNex/issues) to add it to the project.

**Link: https://crowdin.com/project/GitNex**

## Screenshots:

<img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/001.png" alt="001.png" width="200"/>  | <img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/002.png" alt="002.png" width="200"/> | <img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/003.png" alt="003.png" width="200"/> | <img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/004.png" alt="004.png" width="200"/>
---|---|---|---
<img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/005.png" alt="005.png" width="200"/> | <img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/006.png" alt="006.png" width="200"/> | <img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/007.png" alt="007.png" width="200"/>  | <img src="https://gitea.com/gitnex/GitNex/raw/branch/master/fastlane/metadata/android/en-US/images/phoneScreenshots/008.png" alt="008.png" width="200"/>


## FAQ
[Faq](https://gitea.com/gitnex/GitNex/wiki/FAQ)

## Links
[Website](https://gitnex.com)

[Wiki](https://gitea.com/gitnex/GitNex/wiki/Home)

[Website Repository](https://gitlab.com/mmarif4u/gitnex-website)

[Troubleshoot Guide](https://gitea.com/gitnex/GitNex/wiki/Troubleshoot-Guide)

## Thanks
Thanks to all the open source libraries, contributors and donators.

Open source libraries
- Retrofit
- Gson
- Okhttp
- ViHtarb/tooltip
- Picasso
- Markwon
- Prettytime
- Amulyakhare/textdrawable
- Vdurmont/emoji-java
- Abumoallim/android-multi-select-dialog
- Pes/materialcolorpicker
- Hendraanggrian/socialview
- HamidrezaAmz/BreadcrumbsView
- Chrisbanes/PhotoView
- Pddstudio/highlightjs-android
- Apache/commons-io
- Caverock/androidsvg
- Droidsonroids.gif/android-gif-drawable
- Barteksc/AndroidPdfViewer

[Follow me on Fediverse - mastodon.social/@mmarif](https://mastodon.social/@mmarif)

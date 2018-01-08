# CountryPicker 
CountryPicker is a library that consist of a PhoneNumberEditText and a standalone Picker for countries. PhoneNumberEditText offers and EditText for smooth selection of any Countries dial code and other intesting features.


[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6feafc81bc7e460f9f514e2b6c578adc)](https://www.codacy.com/app/po10cio/CountryPicker?utm_source=github.com&utm_medium=referral&utm_content=po10cio/CountryPicker&utm_campaign=badger)
[![Build Status](https://travis-ci.org/po10cio/Android-Kotlin-Boilerplate.svg?branch=master)](https://travis-ci.org/po10cio/Android-Kotlin-Boilerplate)
[![](https://jitpack.io/v/po10cio/TimeLineView.svg)](https://jitpack.io/#po10cio/TimeLineView)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/99c821f544424d9480e92ccc84bd0097)](https://www.codacy.com/app/po10cio/CountryPicker?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=po10cio/CountryPicker&amp;utm_campaign=Badge_Grade)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-TimeLineView-green.svg?style=flat )]( https://android-arsenal.com/details/1/6540 )
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/po10cio/TimeLineView/blob/master/LICENSE.md) 


## Showcase

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="showcase/showcase1.png" alt="" width="240">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="showcase/showcase2.png" alt="" width="240">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="showcase/showcase3.png" alt="" width="240">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;


## Quick Setup
### 1. Include library

**Using Gradle**

TimelineView is currently available in on Jitpack so add the following line before every other thing if you have not done that already.

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
	
Then add the following line 

``` gradle
dependencies {
  compile 'com.github.po10cio:CountryPicker:1.0.0'
}
```

**Using Maven**

Also add the following lines before adding the maven dependency

```maven
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Then add the dependency

``` maven
<dependency>
  <groupId>com.github.po10cio</groupId>
  <artifactId>CountryPicker</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 2. Usage
In your XML layout include the TimelineView as follows:

```xml
<me.jerryhanks.countrypicker.PhoneNumberEditText
        android:id="@+id/countryPicker"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginEnd="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="58dp"
        android:hint="08030720816"
        app:cp_autoDetectCountry="false"
        app:cp_fastScrollerBubbleColor="@color/colorPrimary"
        app:cp_fastScrollerBubbleTextAppearance="@style/TextAppearance.AppCompat.Medium"
        app:cp_fastScrollerHandleColor="@color/colorAccent"
        app:cp_preferredCountries="ng,dz,au,az"
        app:cp_rememberLastSelection="true"
        app:cp_setCountryCodeBorder="true"
        app:cp_showCountryCodeInView="true"
        app:cp_showCountryDialCodeInView="true"
        app:cp_showFastScroll="true"
        app:cp_showFullScreeDialog="true" />
      
```
## XML Attributes

List of xml attribues that are available in PhoneNumberEdittext

| XML Attribute | Description   | Default |
| ------------- |:---------------:|:---------:| 
| cp_autoDetectCountry      | Enables auto detection of the country the device is currently being used | true|
|cp_searchAllowed|Enables search functionality in the CountryPicker.|true
|cp_showFastScroll|Determines whether the Fastscroller button is show or not.|true
|cp_dialogKeyboardAutoPopup|Use this to toggle Kwyboard auto popup for  CountryPicker in dialog mode. |true
|cp_showFullScreeDialog|Use this to switch between Dialog and full screen Pickers| false
|cp_showCountryCodeInView|Dtermins whether the country code is shown in the EditText|true
|cp_showCountryCodeInList|Determins whether the Country Code is shown in the picker list.|true
|cp_showCountryDialCodeInView|Determind if the Country Dial code is shown in the view.|true
|cp_showCountryDialCodeInList|Dtermins if the Country Code is show in the picker lsit.|true
|cp_setCountryCodeBorder|Determins whether a fancy border is shown around the Picker view.|false
|cp_defaultCountryName|Use this to specify the dafult country you want to show in the PickerView|Empty
|cp_preferredCountries|Use this to enter comma seperated list of prefferd countries.|Empty
|cp_fastScrollerBubbleColor| Sets the color of the fast scroller bubble color| #5e64ce
|cp_fastScrollerBubbleTextAppearance|Sets the testAppearance of the fastScroller| TextAppearance.AppCompat.Medium|
|cp_fastScrollerHandleColor|Sets the fastscroller handle color| #8f93d1

## Dependencies

[Recycler Fast scroller](https://github.com/FutureMind/recycler-fast-scroll) by [FutureMind](https://github.com/FutureMind)

[Android Port of libPhonenumber](https://github.com/MichaelRocks/libphonenumber-android) by [Michael Rozumyanskiy](https://github.com/MichaelRocks)




## Changelog

See the [changelog](/CHANGELOG.md) file.


## License

CountryPicker is distributed under the MIT license. [See LICENSE](https://github.com/po10cio/TimeLineView/blob/master/LICENSE.md) for details.

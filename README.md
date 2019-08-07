# Rutetider

This is a huge project that I developed in the summer of 2017. It is a published app making it possible to
look up timetables, bus/train/tram/subway/boat routes and more entirely without an internet connection. It believe it is still the only Norwegian
travel app with these offline capabilities. This is done by downloading and heavily refactoring/compressing the Ruter© published
General Transit Feed Specification (GTFS) (by Google Transit) database (For detailes see the README in _Data-Preprocessing_).

<img src=Screenshots/MainMenu.webp width="168" height="299"> <img src=Screenshots/Lines2.webp width="168" height="299">

# App status

The app was released in August 2017, have 100+ downloads and is rated 5 star. In March 2018 Ruter shut down their API portal "Ruter Labs", and while Entur©
now publishes useable NeTEx data, this data is in such a different format that it would require rebuilding many components of the app. As of
now the project isn't updated to be compatible with the NeTEx data format, and I don't plan on fixing it in the immediate future as this project
was mainly built for learning purposes.

* Google play link: https://play.google.com/store/apps/details?id=com.olivernord.android.avgangeroffline&hl

# What I learned

* How to use Android Studio to build apps.
* How to manage (and not to manage!) a sizeable (7000 lines of code) app project.
* Data preprocessing/cleaning/compressing (this part is actually half the project)
* Software testing/bugfixing and logging
* Maintaining and continously updating the app
*  .txt based database I/O
* Woring with the GTFS data specification
* Graphic design and GUI/UX design
* Working with licensed artwork



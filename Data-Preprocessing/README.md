# Data preprocessing/refactoring/compression

**Note: This part is separate to the apk**

This is in many ways the apps secret sauce. Ruters GTFS data in .txt is **1-1.5 GB** or more in size. Having this stored locally on phones would put a lot of strain on storage space and memory, and searching through this data would be unusably slow. Through all these pre-processing steps I completely restructure the data, and the end result is that the .txt data is less than **10 MB**, and orders of magnitude faster to search (no decompression required either).

The complete list of changes to the datastructure is to long to list here (and I wouldn't remember every improvement), but here are some key changes:
* The list of stops is sorted by the number of connected travels, not alphabetically for increased search efficieny. 
* Stop names and route-names are indexed instead of written in full.
* Route-data structure is completey rewritten. The entire route is represented in a single line by the order of stops.
* Timing is not written in full every time (12:00:00), instead relative time is implemented.
* Unused data is removed: GPS coordinates, service numbers, distance stats
* Routes are stored once instead of one version for every day because they are almost always identical (exceptions are stored).
* Route IDs changed, restrucured and reassigned with efficient ordering. 

And much more! I could have continued to refactor the data but a 10 MB dataset is both negligible in size and near instant to search through so further improvements weren't required. If you want to see the GTFS data files after these operations head to https://github.com/sthoresen/Rutetider/tree/master/app/src/main/assets

# Code structure, performance and commenting

This project was written for my eyes only with no performance or readability goals so it is propper spaghetti. A number of steps are likely redundant as the code was written on the go with little planning or refactoring along the way. Keep in mind that this program was designed specifically to work with Ruters implementation of the GTFS specification, so it won't work without modification on every GTFS dataset.

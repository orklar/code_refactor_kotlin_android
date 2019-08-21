# code_refactor_kotlin_android
In the most basic terms, this class refactors the implementated libraries in order to seperate the version numbers from the implementation lines for easier maintanance.


Instructions:
-------------
Select the file to refactor.(Note: The resulting files will be saved to the same directory as this given file.)

Example:
-------
Input:

//Location 

implementation "com.google.android.gms:play-services-location:16.0.0"

Output(app):

//Location

implementation "com.google.android.gms:play-services-location:${rootVersion.playServicesLocation}"

Output(secondary):

//Location

playServicesLocation = "16.0.0"

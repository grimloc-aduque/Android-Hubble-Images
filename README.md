# Android-Hubble-Images
Android APP that displays images of galaxies retrieved from the hubble telescope web page: https://esahubble.org/images/archive/category/galaxies/?

# Technologies
Android/Java
* Intent for screen launching
* AsyncTask for background task launching
    * Download of small icons
    * Download of big images
* SharedPreferences and Gson for data persistance
* Widgets for programatically accessing UI components
    * Button
    * ImageView
    * TextView
    * RecyclerView

Layout
* XML

# Design Patterns
* Singleton: To manage the Historical across the APP
* Cache-Aside: To save and retrieve previously downloaded images from the cache

# Blackjack App Activities
|Main Activity|Detail Activity|Lonk Click|
|:-:|:-:|:-:|
|<img src="https://github.com/grimloc-aduque/Android-Hubble-Images/blob/master/git_images/main_activity.png" style="width:250px;"/>|<img src="https://github.com/grimloc-aduque/Android-Hubble-Images/blob/master/git_images/detalle_activity.png" style="width:250px;"/>|<img src="https://github.com/grimloc-aduque/Android-Hubble-Images/blob/master/git_images/on_longclick.png" style="width:250px;"/>

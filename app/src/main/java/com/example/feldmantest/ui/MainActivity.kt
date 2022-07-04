package com.example.feldmantest.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.feldmantest.R
import com.example.feldmantest.databinding.ActivityMainBinding
import com.example.feldmantest.model.Album
import com.example.feldmantest.model.Comment
import com.example.feldmantest.model.Photo
import com.example.feldmantest.network.RetroFitService
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        getPhotosAlbumsAndComments()
    }

    private fun getPhotosAlbumsAndComments() {
        CoroutineScope(Dispatchers.IO).launch {
            val gallery = getAlbumsPhotosComments()

            val gson = Gson()
            val commentsJSON = gson.toJson(gallery.comments)
            val albumsJSON = gson.toJson(gallery.albums)
            val photosJSON = gson.toJson(gallery.photos)

            withContext(Dispatchers.Main) {
                createDialogs(commentsJSON,albumsJSON,photosJSON)
            }
        }
    }

    private fun createDialogs(commentsJSON: String, albumsJSON: String, photosJSON:String) {
        createDialog("Comments",commentsJSON) {
            createDialog("Albums", albumsJSON) {
                createDialog("Photos", photosJSON, null)
            }
        }
    }

    data class Gallery(
        val comments: List<Comment> = emptyList(),
        val albums: List<Album> = emptyList(),
        val photos: List<Photo> = emptyList()
    )

    private suspend fun getAlbumsPhotosComments(): Gallery = coroutineScope {
        val commentsRequest = async(Dispatchers.IO) { RetroFitService.getService().getCommentsList() }
        val albumsRequest = async(Dispatchers.IO) { RetroFitService.getService().getAlbumsList() }
        val photosRequest = async(Dispatchers.IO) { RetroFitService.getService().getPhotosList() }
        val comments = commentsRequest.await()
        val albums = albumsRequest.await()
        val photos = photosRequest.await()
        Gallery(comments,albums,photos)
    }

    private fun createDialog(
        title: String,
        message:String,
        onPositiveButtonClicked: ((String) -> Unit)?
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

        builder.setMessage(message)

        builder.setTitle(title)
        builder.setCancelable(false)
        builder
            .setPositiveButton(
                "Ok"
            ) { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClicked?.invoke(title)
            }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
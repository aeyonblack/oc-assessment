package com.example.feldmantest

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.feldmantest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        RetroFitService.getService().getComments().enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful) {
                    val comments = response.body()

                    RetroFitService.getService().getAlbums().enqueue(object : Callback<Album> {
                        override fun onResponse(call: Call<Album>, response: Response<Album>) {
                            if (response.isSuccessful) {
                                val albums = response.body()

                                RetroFitService.getService().getPhotos().enqueue(object : Callback<Photo> {
                                    override fun onResponse(call: Call<Photo>, response: Response<Photo>) {
                                        if (response.isSuccessful) {
                                            val photos = response.body()

                                            val gson = Gson()
                                            val commentsJSON = gson.toJson(comments)
                                            val albumsJSON = gson.toJson(albums)
                                            val photosJSON = gson.toJson(photos)

                                            val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

                                            // Set the message show for the Alert time
                                            builder.setMessage(commentsJSON)

                                            // Set Alert Title
                                            builder.setTitle("Comments")
                                            builder.setCancelable(false)
                                            builder
                                                .setPositiveButton(
                                                    "Ok"
                                                ) { dialog, _ -> // When the user click yes button
                                                    // then app will close
                                                    dialog.dismiss()
                                                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

                                                    // Set the message show for the Alert time
                                                    builder.setMessage(albumsJSON)

                                                    // Set Alert Title
                                                    builder.setTitle("Albums")
                                                    builder.setCancelable(false)
                                                    builder
                                                        .setPositiveButton(
                                                            "Ok"
                                                        ) { dialog, _ -> // When the user click yes button
                                                            // then app will close
                                                            dialog.dismiss()
                                                            val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

                                                            // Set the message show for the Alert time
                                                            builder.setMessage(photosJSON)

                                                            // Set Alert Title
                                                            builder.setTitle("Photos")
                                                            builder.setCancelable(false)
                                                            builder
                                                                .setPositiveButton(
                                                                    "Ok"
                                                                ) { dialog, _ -> // When the user click yes button
                                                                    // then app will close
                                                                    dialog.dismiss()
                                                                }

                                                            // Create the Alert dialog
                                                            val alertDialog: AlertDialog = builder.create()

                                                            // Show the Alert Dialog box
                                                            alertDialog.show()
                                                        }

                                                    // Create the Alert dialog
                                                    val alertDialog: AlertDialog = builder.create()

                                                    // Show the Alert Dialog box
                                                    alertDialog.show()
                                                }

                                            // Create the Alert dialog
                                            val alertDialog: AlertDialog = builder.create()

                                            // Show the Alert Dialog box
                                            alertDialog.show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Photo>, t: Throwable) {
                                        Log.e(
                                            "[GET_PHOTO_FAIL]",
                                            t.message ?: ""
                                        )

                                        t.printStackTrace()
                                    }
                                })
                            }
                        }

                        override fun onFailure(call: Call<Album>, t: Throwable) {
                            Log.e(
                                "[GET_ALBUM_FAIL]",
                                t.message ?: ""
                            )

                            t.printStackTrace()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e(
                    "[GET_COMMENT_FAIL]",
                    t.message ?: ""
                )

                t.printStackTrace()
            }
        })
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
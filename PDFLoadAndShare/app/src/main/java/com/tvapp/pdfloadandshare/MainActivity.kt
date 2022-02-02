package com.tvapp.pdfloadandshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()

        shareButton.setOnClickListener {
            sharePDF()
        }
    }

    private fun sharePDF() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                applicationContext, "com.tvapp.pdfloadandshare.fileprovider",
                viewModel.getPdfFileUri()
            ))
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            type = "application/pdf"
        }
        val sendIntent = Intent.createChooser(shareIntent, null)
        startActivity(sendIntent)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(fileDir = filesDir) as T
            }
        }).get(MainActivityViewModel::class.java)

        viewModel.isFileReadyObserver.observe(this, Observer <Boolean>{
            progressBar.visibility = View.GONE

            if(!it) {
                Toast.makeText(this@MainActivity, "Failed to download PDF", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "PDF Downloaded successfully", Toast.LENGTH_LONG).show()
                try {
                    pdfView.fromUri(FileProvider.getUriForFile(
                        applicationContext,
                        "com.tvapp.pdfloadandshare.fileprovider",
                        viewModel.getPdfFileUri()))
                        .load()
                }catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Failed to download PDF", Toast.LENGTH_LONG).show()

                }
            }
        })
        viewModel.downloadPdfFile("https://firebasestorage.googleapis.com/v0/b/fashionatyourstep-a8df7.appspot.com/o/Pie_Charts.pdf?alt=media&token=4b50a8ad-80c7-48d6-a681-3ee66d0e8018")
    }
}
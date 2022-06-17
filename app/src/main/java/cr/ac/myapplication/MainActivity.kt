package cr.ac.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var buttonPlay: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonStop: Button
    private lateinit var songName: TextView
    private lateinit var buttonNext: Button
    private lateinit var buttonBack: Button

    private lateinit var mediaPlayer: MediaPlayer

    companion object{
        var OPEN_DIRECTORY_RC = 1
        lateinit var songs:Array<DocumentFile>
        var songNumber:Int = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mediaPlayer = MediaPlayer()
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_RC)

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPause = findViewById(R.id.buttonPause)
        buttonStop = findViewById(R.id.buttonStop)
        buttonNext =findViewById(R.id.next)
        buttonBack = findViewById(R.id.back)
        songName = findViewById(R.id.song)

        setOnClickListeners(this)
    }
    private fun setOnClickListeners(context: Context) {
        buttonPlay.setOnClickListener {
            mediaPlayer.start()
            Toast.makeText(context, "Reproduciendo...", Toast.LENGTH_SHORT).show()
        }

        buttonPause.setOnClickListener {
            mediaPlayer.pause()
            Toast.makeText(context, "En pausa...", Toast.LENGTH_SHORT).show()
        }

        buttonStop.setOnClickListener {
            this.stopSong(context)
            Toast.makeText(context, "Parando...", Toast.LENGTH_SHORT).show()
        }
        buttonNext.setOnClickListener {
            if(songNumber < songs.size-1){
                songNumber++
                Toast.makeText(context, "Siguiente...", Toast.LENGTH_SHORT).show()
            }else{
                songNumber=0
                Toast.makeText(context, "De vuelta al inicio...", Toast.LENGTH_SHORT).show()
            }
            this.stopSong(context)
            mediaPlayer.start()
            songName.text = songs[songNumber].name
        }
        buttonBack.setOnClickListener {
            if(songNumber >= 1){
                songNumber--
                Toast.makeText(context, "Siguiente...", Toast.LENGTH_SHORT).show()
            }else{
                songNumber=songs.size-1
                Toast.makeText(context, "De vuelta al inicio...", Toast.LENGTH_SHORT).show()
            }
            this.stopSong(context)
            mediaPlayer.start()
            songName.text = songs[songNumber].name
        }
    }

    private fun stopSong(context: Context){
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(context,songs[songNumber].uri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== OPEN_DIRECTORY_RC){
            if(resultCode== Activity.RESULT_OK){
                var directoryUri = data?.data?: return
                Log.e("Diretory:",directoryUri.toString())
                var rootTree = DocumentFile.fromTreeUri(this,directoryUri)
                songs = rootTree!!.listFiles()
                songName.text = songs[songNumber].name
                mediaPlayer.setDataSource(this, songs[songNumber].uri)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }

        }
    }
}
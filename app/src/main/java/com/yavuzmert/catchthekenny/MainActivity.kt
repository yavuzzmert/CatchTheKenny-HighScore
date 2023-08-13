package com.yavuzmert.catchthekenny

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.yavuzmert.catchthekenny.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    private var highScore: Int=0

    var score = 0

    var imageArray = ArrayList<ImageView>()

    var runnable = Runnable{}
    var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        sharedPref = getSharedPreferences("com.yavuzmert.catchthekenny", Context.MODE_PRIVATE)
        val scoreLast = sharedPref.getInt("highScore",-1)

        if(scoreLast != -1)
        {
            highScore = scoreLast
            binding.highesScoreText.text = scoreLast.toString()
        }


        //Image Array
        imageArray.add(binding.imageView1)
        imageArray.add(binding.imageView2)
        imageArray.add(binding.imageView3)
        imageArray.add(binding.imageView4)
        imageArray.add(binding.imageView5)
        imageArray.add(binding.imageView6)
        imageArray.add(binding.imageView7)
        imageArray.add(binding.imageView8)
        imageArray.add(binding.imageView9)

        hidingImage()

        //Countdown timer
        object : CountDownTimer(15500, 1000){
            override fun onTick(p0: Long) {
                binding.timeText.text = "Time: ${p0/1000}"
            }

            override fun onFinish() {
                binding.timeText.text = "Time: 0"
                handler.removeCallbacks(runnable)
                for(image in imageArray){
                    image.visibility = View.INVISIBLE
                }
                //alert dialog
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game Over")
                alert.setMessage("Restart the Game")
                alert.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialogInterface, i ->
                    //restart
                    val intentFromMain = intent
                    finish()
                    startActivity(intentFromMain)
                })

                alert.setNegativeButton("No", DialogInterface.OnClickListener{ dialogInterface, i ->
                    Toast.makeText(this@MainActivity,"Game over!", Toast.LENGTH_LONG).show()
                })
                alert.show()
            }

        }.start()

    }

    fun score(view: View){
        score++
        binding.scoreText.text = "Score: ${score}"


        if(score != null && score >= highScore)
        {
            binding.highesScoreText.text = score.toString()
            sharedPref.edit().putInt("highScore", score).apply()
        }

    }

    fun hidingImage(){

        runnable = object : Runnable{
            override fun run() {
                for(image in imageArray)
                {
                    image.visibility = View.INVISIBLE
                }
                val random = Random.nextInt(10)

                imageArray[random].visibility = View.VISIBLE

                handler.postDelayed(runnable, 500)
            }
        }
        handler.post(runnable)
    }
}
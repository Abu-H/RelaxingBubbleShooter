package com.example.bubbleshooter

import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var gameLayout: FrameLayout
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameLayout = FrameLayout(this)
        setContentView(gameLayout)

        // Start background music
        mediaPlayer = MediaPlayer.create(this, R.raw.soft_music)
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(0.3f, 0.3f)
        mediaPlayer.start()

        // On touch, create a bubble
        gameLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                addBubble(event.x)
            }
            true
        }
    }

    private fun addBubble(startX: Float) {
        val bubble = ImageView(this)
        bubble.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bubble))
        val size = 100
        val params = FrameLayout.LayoutParams(size, size)
        bubble.x = startX - size / 2
        bubble.y = gameLayout.height.toFloat() - size
        gameLayout.addView(bubble, params)

        // Animate upward
        CoroutineScope(Dispatchers.Main).launch {
            while (bubble.y > -size) {
                bubble.y -= 5f
                delay(16L) // ~60 FPS
            }
            gameLayout.removeView(bubble)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}

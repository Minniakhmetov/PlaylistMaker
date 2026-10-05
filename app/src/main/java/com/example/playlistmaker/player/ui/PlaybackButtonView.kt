package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {


    private var trackIsPlaying = false
    private val imagePause: Drawable?
    private val imageStart: Drawable?

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonStartStop,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imagePause = getDrawable(R.styleable.ButtonStartStop_imageResIdPause)
                imageStart = getDrawable(R.styleable.ButtonStartStop_imageResIdPlay)
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imagePause?.setBounds(0, 0, measuredWidth, measuredHeight)
        imageStart?.setBounds(0, 0, measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        if (trackIsPlaying) {
            imagePause?.draw(canvas)
        } else {
            imageStart?.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                return false
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun updateButtonState(state: Boolean) {
        if (this.trackIsPlaying != state) {
            this.trackIsPlaying = state
            invalidate()
        }
    }
}

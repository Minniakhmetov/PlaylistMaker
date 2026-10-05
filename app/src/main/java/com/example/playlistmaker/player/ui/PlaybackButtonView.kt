package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {


    private var trackIsPlaying = false
    private val imageBitmapPause: Bitmap?
    private val imageBitmapStart: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonStartStop,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imageBitmapPause =
                    getDrawable(R.styleable.ButtonStartStop_imageResIdPause)?.toBitmap()
                imageBitmapStart =
                    getDrawable(R.styleable.ButtonStartStop_imageResIdPlay)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (trackIsPlaying) {
            imageBitmapPause?.let {
                canvas.drawBitmap(imageBitmapPause, null, imageRect, null)
            }
        } else {
            imageBitmapStart?.let {
                canvas.drawBitmap(imageBitmapStart, null, imageRect, null)
            }
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
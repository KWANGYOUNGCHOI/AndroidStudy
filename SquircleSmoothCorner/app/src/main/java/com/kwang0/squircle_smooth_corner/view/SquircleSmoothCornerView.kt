package com.kwang0.squircle_smooth_corner.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

class SquircleSmoothCornerView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var WIDTH = 400f
    private var HEIGHT = 400f
    private var SKETCH_ROUND_RECT_RADIUS = 50f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mPaint: Paint? = null
    private var ROUND_TL = true
    private  var ROUND_TR = true
    private  var ROUND_BL = true
    private  var ROUND_BR = true
    private var isSquare = false

    init {
        mPaint = Paint()
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = 12f
        mPaint!!.color = Color.rgb(253, 86, 85)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w * 1.0f / 2
        mCenterY = h * 1.0f / 2
    }

    fun getRoundRadius(): Float {
        return SKETCH_ROUND_RECT_RADIUS
    }

    fun setRoundRadius(radius: Float) {
        SKETCH_ROUND_RECT_RADIUS = radius
        this.invalidate()
    }

    fun getRectWidth(): Float {
        return WIDTH
    }

    fun setRectWidth(WIDTH: Float) {
        this.WIDTH = WIDTH
        this.invalidate()
    }

    fun getRectHeight(): Float {
        return HEIGHT
    }

    fun setRectHeight(HEIGHT: Float) {
        this.HEIGHT = HEIGHT
        this.invalidate()
    }

    fun isSquare(): Boolean {
        return isSquare
    }

    fun setIsSquare(square: Boolean) {
        isSquare = square
        this.invalidate()
    }

    fun setRectRoundEnable(
        tl: Boolean,
        tr: Boolean,
        bl: Boolean,
        br: Boolean
    ) {
        ROUND_TL = tl
        this.ROUND_TR = tr
        this.ROUND_BL = bl
        this.ROUND_BR = br
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(0f, 0f)
        mPaint!!.isAntiAlias = true
        val path: Path
        if (isSquare) {
            path = sketchSmoothRect(
                0f, 0f, WIDTH, WIDTH, SKETCH_ROUND_RECT_RADIUS, SKETCH_ROUND_RECT_RADIUS,
                ROUND_TL, ROUND_TR, ROUND_BL, ROUND_BR
            )
            canvas.drawPath(path, mPaint!!)
        } else {
            path = sketchSmoothRect(
                0f, 0f, WIDTH, HEIGHT, SKETCH_ROUND_RECT_RADIUS, SKETCH_ROUND_RECT_RADIUS,
                ROUND_TL, ROUND_TR, ROUND_BL, ROUND_BR
            )
        }
        canvas.drawPath(path, mPaint!!)
    }

    private fun sketchSmoothRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        rx: Float,
        ry: Float,
        tl: Boolean,
        tr: Boolean,
        bl: Boolean,
        br: Boolean
    ): Path {
        var rx = rx
        var ry = ry
        val path = Path()
        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        val posX = mCenterX - width / 2
        val posY = mCenterY - height / 2
        if (width > height) {
            rx *= height / width
        } else {
            ry *= width / height
        }
        for (i in 0..359) {
            val j = i.toFloat()
            val angle = (j * 2f * Math.PI / 360.0)
            val cosX = cos(angle.toDouble()).toFloat()
            val x = abs(cosX).toDouble().pow(rx / 100f.toDouble()).toFloat() * 50f * abs(cosX + 0.0000000001f) / (cosX + 0.0000000001f) + 50f
            val sinY = sin(angle.toDouble()).toFloat()
            val y = abs(sinY).toDouble().pow(ry / 100f.toDouble()).toFloat() * 50f * abs(sinY + 0.0000000001f) / (sinY + 0.0000000001f) + 50f
            val percentX = x / 100f
            val percentY = y / 100f
            if (j == 0f) path.moveTo(
                percentX * width + posX,
                percentY * height + posY
            ) else if (!br && j < 45) {
                path.lineTo(width + posX, height + posY)
            } else if (!br && j >= 45 && j < 90) {
                path.lineTo(posX + width / 2, height + posY)
            } else if (!bl && j >= 90 && j < 135) {
                path.lineTo(posX, height + posY)
            } else if (!bl && j >= 135 && j < 180) {
                path.lineTo(posX, height / 2 + posY)
            } else if (!tl && j >= 180 && j < 225) {
                path.lineTo(posX, posY)
            } else if (!tl && j >= 225 && j < 270) {
                path.lineTo(posX + width / 2, posY)
            } else if (!tr && j >= 270 && j < 315) {
                path.lineTo(posX + width, posY)
            } else if (!tr && j >= 315 && j < 360) {
                path.lineTo(posX + width, posY + height / 2)
            } else path.lineTo(percentX * width + posX, percentY * height + posY)
        }
        path.close()
        return path
    }

    fun getMAXRadius(width: Float, height: Float): Float {
        val minBorder = if (width > height) {
            height
        } else {
            width
        }
        return minBorder / 2f
    }

    private fun getRadiusInMaxRange(
        width: Float,
        height: Float,
        radius: Float
    ): Float {
        return min(radius, getMAXRadius(width, height))
    }
}

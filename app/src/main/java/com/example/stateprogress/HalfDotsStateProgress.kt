package com.example.stateprogress


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import java.util.*

@Suppress("DEPRECATION")
class HalfDotsStateProgressBar : View {
    private var padding = 0
    private var progressPlaceHolderColor = 0
    private var progressBarColor = 0
    private var progressPlaceHolderWidth = 0
    private var progressBarWidth = 0
    private var percent = 0
    private val mPath = Path()
    internal var top = 0
    internal var left = 0
    internal var right = 0
    internal var bottom = 0
    private var values = ArrayList<Int>()

    //Constructors
    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttrs(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        setAttrs(context, attrs)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onDraw(canvas: Canvas) {
        padding =
            if (progressBarWidth > progressPlaceHolderWidth) progressBarWidth + 25
            else progressPlaceHolderWidth + 25
        top = padding
        left = padding
        right = measuredWidth
        bottom = measuredHeight
        val progressAmount = percent * 1.0.toFloat()
        mPath.addArc(progressBarRectF, -180f, 180f)
        val pm = PathMeasure(mPath, false)
        val xyCoordinate = FloatArray(2)
        val pathLength = pm.length
        val arcPoints = arrayOfNulls<PointF>(181)
        for (i in 0..180) {
            pm.getPosTan(pathLength * i / 180, xyCoordinate, null)
            arcPoints[i] = PointF(xyCoordinate[0], xyCoordinate[1])
        }
        canvas.drawPath(mPath, getPaint(progressBarColor, progressBarWidth))
        canvas.drawArc(
            progressBarRectF,
            -180f,
            180f,
            false,
            getPaint(progressPlaceHolderColor, progressPlaceHolderWidth)
        ) //arg2: For the starting point, the starting point is 0 degrees from the positive direction of the x coordinate system. How many angles are arg3 selected to rotate clockwise?
        canvas.drawArc(
            progressBarRectF,
            180f,
            progressAmount,
            false,
            getPaint(progressBarColor, progressBarWidth)
        ) //arg2: For the starting point, the starting point is 0 degrees from the positive direction of the x coordinate system. How many angles are arg3 selected to rotate clockwise?

        val dotRadius = progressBarWidth.toFloat() - 5// Adjust the radius of the dots as needed

        for (dotPosition in intArrayOf(30, 60, 120, 150)) {
            val dotX = arcPoints[dotPosition]!!.x
            val dotY = arcPoints[dotPosition]!!.y

            when (dotPosition) {
                30 -> if ((getTheOneSixthOfThemaxiumValue() <= percent.toFloat()/* && percent.toFloat() < getTheOneFifthOfThemaxiumValue() * 2*/)) {
                    drawForSelected(canvas, dotX, dotY, dotRadius)
                } else {
                    drawForUnSelected(canvas, dotX, dotY, dotRadius)
                }

                60 -> {
                    if (getTheOneSixthOfThemaxiumValue() * 2 <= percent.toFloat() /*&& percent.toFloat() < getTheOneFifthOfThemaxiumValue() * 3*/) {
                        drawForSelected(canvas, dotX, dotY, dotRadius)
                    } else {
                        drawForUnSelected(canvas, dotX, dotY, dotRadius)
                    }
                }

                120 -> {
                    if ((getTheOneSixthOfThemaxiumValue() * 4 <= percent.toFloat() /*&& percent.toFloat() < getTheOneFifthOfThemaxiumValue() * 4*/)) {
                        drawForSelected(canvas, dotX, dotY, dotRadius)
                    } else {
                        drawForUnSelected(canvas, dotX, dotY, dotRadius)
                    }
                }

                150 -> {
                    if (getTheOneSixthOfThemaxiumValue() * 5 <= percent.toFloat() /*&& percent.toFloat() < getTheOneFifthOfThemaxiumValue() * 3*/) {
                        drawForSelected(canvas, dotX, dotY, dotRadius)
                    } else {
                        val dotDrawable = context.getDrawable(R.drawable.circle_unselected)
                        dotDrawable!!.setBounds(
                            (dotX - dotRadius).toInt(),
                            (dotY - dotRadius).toInt(),
                            (dotX + dotRadius).toInt(),
                            (dotY + dotRadius).toInt()
                        )

// Draw the drawable
                        dotDrawable.draw(canvas)
                    }
                }
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawForUnSelected(
        canvas: Canvas,
        dotX: Float,
        dotY: Float,
        dotRadius: Float
    ) {
        val dotDrawable = context.getDrawable(R.drawable.circle_unselected)
        dotDrawable!!.setBounds(
            (dotX - dotRadius).toInt(),
            (dotY - dotRadius).toInt(),
            (dotX + dotRadius).toInt(),
            (dotY + dotRadius).toInt()
        )

// Draw the drawable
        dotDrawable.draw(canvas)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawForSelected(
        canvas: Canvas,
        dotX: Float,
        dotY: Float,
        dotRadius: Float
    ) {
        val dotDrawable = resources.getDrawable(R.drawable.circle_selected)
        dotDrawable.setBounds(
            (dotX - dotRadius).toInt(),
            (dotY - dotRadius).toInt(),
            (dotX + dotRadius).toInt(),
            (dotY + dotRadius).toInt()
        )

// Draw the drawable
        dotDrawable.draw(canvas)
    }

    //Private Methods
    private fun setAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.SemiCircleArcProgressBar, 0, 0)
        try {
            progressPlaceHolderColor = typedArray.getColor(
                com.jasvir.seekbar.R.styleable.SemiCircleArcProgressBar_progressPlaceHolderColor,
                Color.GRAY
            )
            progressBarColor = typedArray.getColor(
                com.jasvir.seekbar.R.styleable.SemiCircleArcProgressBar_progressBarColor,
                Color.WHITE
            )
            progressPlaceHolderWidth = typedArray.getDimension(
                com.jasvir.seekbar.R.styleable.SemiCircleArcProgressBar_progressPlaceHolderWidth,
                25f
            )
                .toInt()
            progressBarWidth =
                typedArray.getDimension(
                    com.jasvir.seekbar.R.styleable.SemiCircleArcProgressBar_progressBarWidth,
                    10f
                )
                    .toInt()
            percent = typedArray.getInt(
                com.jasvir.seekbar.R.styleable.SemiCircleArcProgressBar_percent,
                76
            )
        } finally {
            typedArray.recycle()
        }
    }

    fun setData(values: ArrayList<Int>) {
        this.values = values
        invalidate()
    }

    //canvas.drawPaint(paint);
    // paint.se

    private fun setupPaint(): Paint {
        val drawPaint = Paint()
        drawPaint.color = progressPlaceHolderColor
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 5f
        drawPaint.style = Paint.Style.FILL_AND_STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        return drawPaint
    }

    private fun getPaint(color: Int, strokeWidth: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        return paint
    }

    /*private fun getPaintStrokCirecle(color: Int, strokeWidth: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()*.4f
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        return paint
    }
    private fun getPaintFillUnSelectedCirecle(color: Int, strokeWidth: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL
        paint.strokeWidth = strokeWidth.toFloat()*.01f
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        return paint
    }
    private fun getPaintSelectedCirecle(color: Int, strokeWidth: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL
        paint.strokeWidth = strokeWidth.toFloat()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        return paint
    }*/

    private fun getTheOneSixthOfThemaxiumValue(): Float =
        if (!values.isNullOrEmpty()) values.max() * 1 / 6f else 0f

    private val progressBarRectF: RectF
        private get() = RectF(
            left.toFloat(),
            top.toFloat(),
            (right - padding).toFloat(),
            (bottom - padding).toFloat()
        )

    //Setters
    fun setProgressPlaceHolderColor(color: Int) {
        progressPlaceHolderColor = color
        postInvalidate()
    }

    fun setProgressBarColor(color: Int) {
        progressBarColor = color
        postInvalidate()
    }

    fun setProgressPlaceHolderWidth(width: Int) {
        progressPlaceHolderWidth = width
        postInvalidate()
    }

    fun setProgressBarWidth(width: Int) {
        progressBarWidth = width
        postInvalidate()
    }

    fun setPercent(percent: Int) {
        this.percent = Math.min(percent, 180)
        postInvalidate()
    }

    //Custom Setter
    fun setPercentWithAnimation(percent: Int) {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            var step = 0
            override fun run() {
                if (step <= percent) setPercent(step++)
            }
        }, 0, 12)
    }
}
package org.sketchshare.android.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var paint = Paint()
    private var paths = mutableListOf<MutableList<Pair<Float, Float>>>()

    init {
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (path in paths) {
            for (i in 0 until path.size - 1) {
                val startX = path[i].first
                val startY = path[i].second
                val endX = path[i + 1].first
                val endY = path[i + 1].second
                canvas.drawLine(startX, startY, endX, endY, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val newPath = mutableListOf<Pair<Float, Float>>()
                newPath.add(Pair(event.x, event.y))
                paths.add(newPath)
            }

            MotionEvent.ACTION_MOVE -> {
                if (paths.isNotEmpty()) {
                    val lastPath = paths.last()
                    lastPath.add(Pair(event.x, event.y))
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                // Do nothing on ACTION_UP
            }
        }
        return true
    }


    fun clearCanvas() {
        paths.forEach { it.clear() }
        paths.clear()
        invalidate()
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            paths.lastOrNull()?.clear()
            paths.removeLast()
            invalidate()
        }
    }


    fun shareDrawing(activity: AppCompatActivity) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        val uri = getImageUri(activity, bitmap)
        if (uri != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/jpeg"
            }
            activity.startActivity(Intent.createChooser(shareIntent, "Share Drawing"))
        }
    }

    private fun getImageUri(context: Context, bitmap: Bitmap): Uri? {
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Drawing", null)
        return Uri.parse(path)
    }
}
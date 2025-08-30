package org.sketchshare.android

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import org.sketchshare.android.ui.DrawingView

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        drawingView = findViewById(R.id.drawingView)

        val clearButton: ImageButton = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            drawingView.clearCanvas()
        }

        val shareButton: ImageButton = findViewById(R.id.saveButton)
        shareButton.setOnClickListener {
            shareButton.isEnabled = false
            drawingView.shareDrawing(this)
            shareButton.postDelayed({ shareButton.isEnabled = true }, 1000)
        }

        val undoButton: ImageButton = findViewById(R.id.undoButton)
        undoButton.setOnClickListener {
            undoButton.isEnabled = false
            drawingView.undo()
            shareButton.postDelayed({ undoButton.isEnabled = true }, 200)
        }
    }
}

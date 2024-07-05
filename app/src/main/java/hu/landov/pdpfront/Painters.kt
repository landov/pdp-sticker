package hu.landov.pdpfront

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

class Painters {

    val hairline = Paint().apply {
        color = Color.BLACK
        isAntiAlias = false
        style = Paint.Style.STROKE
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
    }

    val backLine = Paint().apply {
        color = Color.parseColor("#46222E")
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
    }

    val redLine = Paint().apply {
        color = Color.parseColor("#FF5744")
        //color = Color.BLUE
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
    }

    val purpleLine = Paint().apply {
        color = Color.parseColor("#B577FF")
        //color = Color.BLUE
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
    }

    val ledline = Paint().apply {
        color = Color.RED
        //color = Color.parseColor("#7C1B10")
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
    }

    val blackline = Paint().apply {
        color = Color.BLACK
        //color = Color.parseColor("#7C1B10")
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
    }

    val textline = Paint().apply {
        color = Color.WHITE
        //color = Color.parseColor("#7C1B10")
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
        typeface = Typeface.DEFAULT_BOLD
    }

    val textdecline = Paint().apply {
        color = Color.WHITE
        //color = Color.parseColor("#7C1B10")
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
        typeface = Typeface.DEFAULT_BOLD
    }

    val silverline = Paint().apply {
        color = Color.LTGRAY
        //color = Color.parseColor("#7C1B10")
        isAntiAlias = false
        style = Paint.Style.FILL
        strokeWidth = 3/72f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
        typeface = Typeface.DEFAULT_BOLD
    }

    val divider = Paint().apply {
        color = Color.parseColor("#46222E")
        //color = Color.parseColor("#7C1B10")
        isAntiAlias = false
        style = Paint.Style.STROKE
        strokeWidth = 0.4.toPxX()
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = 25f
    }
}
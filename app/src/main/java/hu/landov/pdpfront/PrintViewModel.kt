package hu.landov.pdpfront

import android.app.Application
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import java.io.OutputStream

const val PAGE_HEIGHT = 842 * 1
const val PAGE_WIDTH = 595 * 1
const val PITCH = 3.2
const val SMALL_ROUND_RADIUS = 0.7

class PrintViewModel(val app: Application) : AndroidViewModel(app) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(app);
    private val _startX = 72f
    private var _startY = PAGE_HEIGHT - 72f
    private val _formState: MutableState<FormState>
    val formState: State<FormState>
        get() = _formState

    init {
        val redColor = prefs.getString(PREF_RED, baseRedColor)
        val purpleColor = prefs.getString(PREF_PURPLE, basePurpleColor)
        val backColor = prefs.getString(PREF_BACKGROUND, baseBackColor)
        _formState = mutableStateOf(
            FormState(redColor!!, purpleColor!!, backColor!!)
        )
    }

    fun setRed(colorString: String) {
        _formState.value = _formState.value.copy(redColor = colorString)
    }

    fun setPurple(colorString: String) {
        _formState.value = _formState.value.copy(purpleColor = colorString)
    }

    fun setBack(colorString: String) {
        _formState.value = _formState.value.copy(backColor = colorString)
    }

    fun incNumber() {
        if (_formState.value.stickerNumber < 3)
            _formState.value =
                _formState.value.copy(stickerNumber = _formState.value.stickerNumber + 1)
    }

    fun decNumber() {
        if (_formState.value.stickerNumber > 1)
            _formState.value =
                _formState.value.copy(stickerNumber = _formState.value.stickerNumber - 1)
    }


    fun print(outputStream: OutputStream) {
        val front = FrontDocument()
        val document = front.getDocument(false)
        document.writeTo(outputStream)
        document.close()
    }

    private inner class FrontDocument {
        private lateinit var document: PdfDocument
        private lateinit var canvas: Canvas
        private lateinit var page: PdfDocument.Page

        val painters = Painters()


        private fun draw(wireframe: Boolean) {

            Log.d("PDP", canvas.density.toString())
            val boxPainter = painters.hairline
            val holePainter = painters.hairline
            val ledPainter = painters.ledline

            val backPainter = painters.backLine
            backPainter.color = Color.parseColor(_formState.value.backColor)
            val divider = painters.divider
            val redPainter = painters.redLine
            redPainter.color = Color.parseColor(_formState.value.redColor)
            val blackPainter = painters.blackline
            val purplePainter = painters.purpleLine
            purplePainter.color = Color.parseColor(_formState.value.purpleColor)
            val textPainter = painters.textline
            val silverPainter = painters.silverline

            drawBox(-1.0, 0.0, 104.4, 36.8, backPainter)

            //Numbers

            drawSingleNumBox(4.8, 3.2, redPainter)
            drawTripleNumBox(11.2, 3.2, purplePainter, divider)
            drawTripleNumBox(20.8, 3.2, redPainter, divider)
            drawTripleNumBox(30.4, 3.2, purplePainter, divider)
            drawTripleNumBox(40.0, 3.2, redPainter, divider)
            drawTripleNumBox(49.6, 3.2, purplePainter, divider)
            drawTripleNumBox(59.2, 3.2, redPainter, divider)
            drawTripleNumBox(68.8, 3.2, purplePainter, divider)

            textPainter.textSize = (PITCH / 3).toPxY()
            drawNumbers(textPainter)

            drawSingleNumBox(78.4, 3.2, redPainter)
            drawBox(78.4, 1.2, 1.4, 2.0, redPainter)
            canvas.drawText("load", _startX + 77.2.toPxX(), _startY - 3.2.toPxY(), textPainter)
            canvas.drawText("addr", _startX + 77.2.toPxX(), _startY - 2.2.toPxY(), textPainter)
            drawSingleNumBox(81.6, 3.2, purplePainter)
            drawBox(80.2, 1.2, 2.8, 2.0, purplePainter)
            canvas.drawText("exm", _startX + 80.4.toPxX(), _startY - 2.7.toPxY(), textPainter)
            drawSingleNumBox(84.8, 3.2, redPainter)
            drawBox(83.4, 1.2, 2.8, 2.0, redPainter)
            canvas.drawText("def", _startX + 83.75.toPxX(), _startY - 2.7.toPxY(), textPainter)
            drawSingleNumBox(88.0, 3.2, purplePainter)
            drawBox(86.6, 1.2, 2.8, 2.0, purplePainter)
            canvas.drawText("cont", _startX + 86.75.toPxX(), _startY - 2.7.toPxY(), textPainter)
            drawSingleNumBox(91.2, 3.2, redPainter)
            drawBox(89.8, 1.2, 2.8, 2.0, redPainter)
            canvas.drawText("enab", _startX + 89.9.toPxX(), _startY - 3.5.toPxY(), textPainter)
            canvas.drawText("halt", _startX + 90.1.toPxX(), _startY - 2.2.toPxY(), textPainter)
            drawSingleNumBox(94.4, 3.2, purplePainter)
            drawBox(93.0, 1.2, 1.4, 2.0, purplePainter)
            drawBox(89.8, 3.0, 6.0, 0.4, backPainter)
            canvas.drawText("s in", _startX + 93.2.toPxX(), _startY - 3.5.toPxY(), textPainter)
            canvas.drawText("s bu", _startX + 93.15.toPxX(), _startY - 2.2.toPxY(), textPainter)
            drawSingleNumBox(97.6, 3.2, redPainter)
            canvas.drawText("start", _startX + 96.4.toPxX(), _startY - 2.8.toPxY(), textPainter)


            //Horizontal LEDs
            drawHLedBack(14.4, 9.07, 2, blackPainter)
            drawHLeds(14.4, 9.07, 2, ledPainter)
            drawHLedBack(24.0, 9.07, 16, blackPainter)
            drawHLeds(24.0, 9.07, 16, ledPainter)
            drawHLedBack(4.8, 17.6, 22, blackPainter)
            drawHLeds(4.8, 17.6, 22, ledPainter)

            drawRoundRect(3.4, 21.2, 2.8, 1.4, redPainter)
            drawBox(3.4, 21.2, 2.8, 0.7, redPainter)
            drawBox(4.8, 21.2, 1.4, 1.4, redPainter)
            drawBox(6.6, 21.2, 9.2, 1.4, purplePainter)
            drawBox(16.2, 21.2, 9.2, 1.4, redPainter)
            drawBox(25.8, 21.2, 9.2, 1.4, purplePainter)
            drawBox(35.4, 21.2, 9.2, 1.4, redPainter)
            drawBox(45.0, 21.2, 9.2, 1.4, purplePainter)
            drawBox(54.6, 21.2, 9.2, 1.4, redPainter)
            drawRoundRect(64.2, 21.2, 9.2, 1.4, purplePainter)
            drawBox(64.2, 21.2, 5.2, 1.4, purplePainter)
            drawBox(64.2, 21.2, 9.2, 0.7, purplePainter)

            textPainter.textSize = (PITCH / 2.5).toPxY()
            canvas.drawText("ADDRESS", _startX + 68.2.toPxX(), _startY - 21.4.toPxY(), textPainter)

            drawRoundRect(22.6, 12.67, 2.8, 1.4, redPainter)
            drawBox(22.6, 12.67, 2.8, 0.7, redPainter)
            drawBox(24.0, 12.67, 1.4, 1.4, redPainter)
            drawBox(25.8, 12.67, 9.2, 1.4, purplePainter)
            drawBox(35.4, 12.67, 9.2, 1.4, redPainter)
            drawBox(45.0, 12.67, 9.2, 1.4, purplePainter)
            drawBox(54.6, 12.67, 9.2, 1.4, redPainter)
            drawRoundRect(64.2, 12.67, 9.2, 1.4, purplePainter)
            drawBox(64.2, 12.67, 5.2, 1.4, purplePainter)
            drawBox(64.2, 12.67, 9.2, 0.7, purplePainter)

            textPainter.textSize = (PITCH / 2.7).toPxY()
            canvas.drawText("DATA", _startX + 70.2.toPxX(), _startY - 12.87.toPxY(), textPainter)

            drawRoundRect(13.0, 12.67, 6.0, 1.4, blackPainter)

            textPainter.textSize = (PITCH / 2.7).toPxY()
            canvas.drawText("PARITY", _startX + 14.2.toPxX(), _startY - 12.87.toPxY(), textPainter)
            textPainter.textSize = (PITCH / 3).toPxY()
            canvas.drawText("HIGH", _startX + 13.3.toPxX(), _startY - 11.87.toPxY(), textPainter)
            canvas.drawText("LOW", _startX + 16.5.toPxX(), _startY - 11.87.toPxY(), textPainter)

            canvas.drawText(
                "POWER  LOCK",
                _startX + 3.4.toPxX(),
                _startY - 13.37.toPxY(),
                textPainter
            )
            canvas.drawText("OFF", _startX + 3.4.toPxX(), _startY - 7.0.toPxY(), textPainter)

            canvas.drawRoundRect(
                _startX + (36.6 - PITCH / 2 - 0.2).toPxX(),
                _startY - (25.07 + PITCH).toPxY(),
                _startX + (72 + PITCH / 2 - 0.2).toPxX(),
                _startY - (25.07 - PITCH / 2 + 0.2).toPxY(),
                SMALL_ROUND_RADIUS.toPxX(),
                SMALL_ROUND_RADIUS.toPxY(),
                blackPainter
            )
            drawHLeds(36.8, 25.07, 12, ledPainter)
            textPainter.textSize = (PITCH / 3).toPxY()
            canvas.drawText("PAR", _startX + 35.6.toPxX(), _startY - 27.4.toPxY(), textPainter)
            canvas.drawText("ERR", _startX + 35.6.toPxX(), _startY - 26.5.toPxY(), textPainter)
            canvas.drawText("ADRS", _startX + 38.4.toPxX(), _startY - 27.4.toPxY(), textPainter)

            canvas.drawText("ERR", _startX + 38.8.toPxX(), _startY - 26.5.toPxY(), textPainter)
            canvas.drawText("RUN", _startX + 42.0.toPxX(), _startY - 27.4.toPxY(), textPainter)
            canvas.drawText("PAS", _startX + 45.2.toPxX(), _startY - 27.4.toPxY(), textPainter)
            canvas.drawText("MAST", _startX + 48.2.toPxX(), _startY - 27.4.toPxY(), textPainter)
            canvas.drawText("USR", _startX + 51.6.toPxX(), _startY - 27.4.toPxY(), textPainter)
            canvas.drawText("SUPR", _startX + 54.5.toPxX(), _startY - 27.4.toPxY(), textPainter)
            canvas.drawText("KRNL", _startX + 57.6.toPxX(), _startY - 27.4.toPxY(), textPainter)
            canvas.drawText("DATA", _startX + 60.5.toPxX(), _startY - 27.4.toPxY(), textPainter)

            canvas.drawText(
                "ADDRESSING",
                _startX + 65.0.toPxX(),
                _startY - 27.4.toPxY(),
                textPainter
            )
            canvas.drawText("16", _startX + 64.7.toPxX(), _startY - 26.5.toPxY(), textPainter)
            canvas.drawText("18", _startX + 67.9.toPxX(), _startY - 26.5.toPxY(), textPainter)
            canvas.drawText("22", _startX + 71.1.toPxX(), _startY - 26.5.toPxY(), textPainter)


            //Draw Vertical leds
            drawRoundRect(76.9, 15.4, 22.4, 12.2, blackPainter)
            drawRoundRect(76.9, 7.6, 22.4, 6.1, blackPainter)

            canvas.drawText("USR D", _startX + 81.3.toPxX(), _startY - 25.63.toPxY(), textPainter)
            canvas.drawText("SUPR D", _startX + 81.3.toPxX(), _startY - 22.63.toPxY(), textPainter)
            canvas.drawText("KRNL D", _startX + 81.3.toPxX(), _startY - 19.63.toPxY(), textPainter)
            canvas.drawText("CONS P", _startX + 81.3.toPxX(), _startY - 16.63.toPxY(), textPainter)

            canvas.drawText("USR O", _startX + 87.7.toPxX(), _startY - 25.63.toPxY(), textPainter)
            canvas.drawText("SUPR O", _startX + 87.7.toPxX(), _startY - 22.63.toPxY(), textPainter)
            canvas.drawText("KRNL O", _startX + 87.7.toPxX(), _startY - 19.63.toPxY(), textPainter)
            canvas.drawText("PROG P", _startX + 87.7.toPxX(), _startY - 16.63.toPxY(), textPainter)

            canvas.drawText("DATA", _startX + 81.3.toPxX(), _startY - 12.47.toPxY(), textPainter)
            canvas.drawText("PATHS", _startX + 81.3.toPxX(), _startY - 11.37.toPxY(), textPainter)
            canvas.drawText("BUS RG", _startX + 81.3.toPxX(), _startY - 8.92.toPxY(), textPainter)

            canvas.drawText(
                "\u00B5 ADRS",
                _startX + 87.7.toPxX(),
                _startY - 12.47.toPxY(),
                textPainter
            )
            canvas.drawText("FPP/CPU", _startX + 87.7.toPxX(), _startY - 11.37.toPxY(), textPainter)
            canvas.drawText("DISPLAY", _startX + 87.7.toPxX(), _startY - 9.47.toPxY(), textPainter)
            canvas.drawText("REGISTER", _startX + 87.7.toPxX(), _startY - 8.37.toPxY(), textPainter)

            drawWLeds(80.0, 25.83, 4, ledPainter)
            drawWLeds(80.0, 12.07, 2, ledPainter)
            drawWLeds(86.4, 25.83, 4, ledPainter)
            drawWLeds(86.4, 12.07, 2, ledPainter)


            //DRAW PDP11
            val assetManager = app.assets

            var instr = assetManager.open("logo3.png")
            var bitmap = BitmapFactory.decodeStream(instr)
            val logoRect = Rect(

                (_startX + 3.8.toPxX()).toInt(),
                (_startY - 28.0.toPxX()).toInt(),
                (_startX + 16.8.toPxX()).toInt(),
                (_startY - 24.0.toPxX()).toInt()
            )
            //val logoRect = Rect(10,10,100,100)
            canvas.drawBitmap(bitmap, null, logoRect, blackPainter)
            instr.close()

            //Holes
            drawCircle(6.4, 10.57, 3.2, purplePainter)
            drawCircle(96.6, 10.57, 2.0, purplePainter)
            drawCircle(96.0, 21.33, 2.0, purplePainter)

            //Plate
            drawBox(130.0, 0.0, 20.0, 10.0, silverPainter)
            drawBox(130.5, 0.5, 19.0, 9.0, blackPainter)
            textPainter.textSize = (PITCH / 1.5).toPxY()
            canvas.drawText(
                "Micro PDP 11",
                _startX + 132.0.toPxX(),
                _startY - 7.0.toPxY(),
                textPainter
            )

            textPainter.textSize = (PITCH / 3).toPxY()
            canvas.drawText("Serial:", _startX + 132.0.toPxX(), _startY - 6.0.toPxY(), textPainter)
            drawBox(132.0, 3.0, 16.0, 2.8, silverPainter)
            textPainter.textSize = (PITCH / 1.8).toPxY()
            canvas.drawText(
                "www.landov.hu",
                _startX + 134.0.toPxX(),
                _startY - 1.1.toPxY(),
                textPainter
            )


            //Header
            _startY = _startY - 50.0.toPxY()
            drawBox(0.0, 0.0, 107.0, 19.0, boxPainter)
            drawRoundRect(1.3, 1.3, 104.4, 8.7, redPainter)
            drawBox(1.3, 6.3, 104.4, 3.7, redPainter)
            drawRoundRect(1.3, 10.4, 104.4, 7.3, purplePainter)
            drawBox(1.3, 10.4, 104.4, 3.3, purplePainter)

            instr = assetManager.open("digital.png")
            bitmap = BitmapFactory.decodeStream(instr)
            val digiRect = Rect(
                (_startX + 10.3.toPxX()).toInt(),
                (_startY - 17.6.toPxX()).toInt(),
                (_startX + 40.6.toPxX()).toInt(),
                (_startY - 10.10.toPxX()).toInt()
            )
            //val digiRect = Rect(10,10,100,100)
            canvas.drawBitmap(bitmap, null, digiRect, blackPainter)
            instr.close()

            instr = assetManager.open("pdp11.png")
            bitmap = BitmapFactory.decodeStream(instr)

            val pdpRect = Rect(
                (_startX + 50.0.toPxX()).toInt(),
                (_startY - 17.2.toPxX()).toInt(),
                (_startX + 64.9.toPxX()).toInt(),
                (_startY - 10.6.toPxX()).toInt()
            )
            //val digiRect = Rect(10,10,100,100)
            canvas.drawBitmap(bitmap, null, pdpRect, blackPainter)
            instr.close()

            val decText = painters.textdecline
            val typeFace = Typeface.createFromAsset(assetManager, "Indonesia.ttf")
            decText.typeface = typeFace
            decText.textSize = 7f
            canvas.drawText(
                "digital equipment corporation\u00B7maynard.massachusetts",
                _startX + 10.3.toPxX(),
                _startY - 8.3.toPxY(),
                decText
            )
            _startY = _startY - 30.0.toPxX()
        }

        private fun drawRoundRect(
            x: Double,
            y: Double,
            width: Double,
            height: Double,
            paint: Paint,
        ) {
            canvas.drawRoundRect(
                _startX + x.toPxX(),
                _startY - (y + height).toPxY(),
                _startX + (x + width).toPxY(),
                _startY - y.toPxY(),
                0.7.toPxX(),
                0.7.toPxY(),
                paint
            )
        }

        private fun drawNumbers(paint: Paint) {
            val rect = Rect()
            for (i in 21 downTo 0) {
                paint.getTextBounds(i.toString(), 0, i.toString().length, rect)
                canvas.drawText(
                    i.toString(),
                    _startX + (4.8 + (21 - i) * PITCH).toPxX() - rect.width() / 2,
                    _startY - 3.2.toPxY() + 1f,
                    paint
                )
            }
        }

        private fun drawSingleNumBox(x: Double, y: Double, paint: Paint) {
            val width = PITCH - 0.4
            val height = PITCH * 1.25
            canvas.drawRoundRect(
                (x - width / 2).toPxX() + _startX,
                _startY - (y - height / 2).toPxY(),
                (x + width / 2).toPxX() + _startX,
                _startY - (y + height / 2).toPxY(),
                1.0.toPxY(),
                1.0.toPxY(),
                paint
            )
        }

        private fun drawTripleNumBox(x: Double, y: Double, paint: Paint, divider: Paint) {
            val width = 3 * PITCH - 0.4
            val height = PITCH * 1.25
            canvas.drawRoundRect(
                (x - width / 2).toPxX() + _startX,
                _startY - (y - height / 2).toPxY(),
                (x + width / 2).toPxX() + _startX,
                _startY - (y + height / 2).toPxY(),
                1.0.toPxY(),
                1.0.toPxY(),
                paint
            )
            canvas.drawLine(
                _startX + (x - PITCH / 2).toPxX(), _startY - (y - height / 2).toPxY(),
                _startX + (x - PITCH / 2).toPxX(), _startY - (y + height / 2).toPxY(), divider
            )
            canvas.drawLine(
                _startX + (x + PITCH / 2).toPxX(), _startY - (y - height / 2).toPxY(),
                _startX + (x + PITCH / 2).toPxX(), _startY - (y + height / 2).toPxY(), divider
            )
        }

        private fun drawBox(x: Double, y: Double, width: Double, height: Double, paint: Paint) {
            canvas.drawRect(
                x.toPxX() + _startX,
                _startY - y.toPxY() - height.toPxY(),
                x.toPxX() + width.toPxX() + _startX,
                _startY - y.toPxY(),
                paint
            )
        }

        private fun drawCircle(x: Double, y: Double, d: Double, paint: Paint) {
            canvas.drawCircle(
                x.toPxX() + _startX,
                _startY - y.toPxY(),
                (d.toPxX() / 2),
                paint
            )
        }

        private fun drawHLedBack(x: Double, y: Double, count: Int, paint: Paint) {
            val width = PITCH - 0.4
            val height = PITCH * 1.25
            val bot = (PITCH / 2 - 0.2)
            val top = 2 * PITCH - bot
            for (i in 1..count) {
                val nx = x + ((i - 1) * 3.2)
                canvas.drawRoundRect(
                    (nx - width / 2).toPxX() + _startX,
                    _startY - (y + top).toPxY(),
                    (nx + width / 2).toPxX() + _startX,
                    _startY - (y - bot).toPxY(),
                    0.7.toPxY(),
                    0.7.toPxY(),
                    paint
                )
            }
        }

        private fun drawHLeds(x: Double, y: Double, count: Int, paint: Paint) {


            for (i in 1..count) {


                drawCircle(x + ((i - 1) * 3.2), y, 2.0, paint)
            }
        }

        private fun drawWLeds(x: Double, y: Double, count: Int, paint: Paint) {
            for (i in 1..count) {
                drawCircle(x, y - ((i - 1) * 3.0), 2.0, paint)
            }
        }


        fun getDocument(wireframe: Boolean): PdfDocument {
            document = PdfDocument()
            page = document.startPage(
                PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 0).create()
            )
            canvas = page.canvas
            for (i in 1.._formState.value.stickerNumber)
                draw(wireframe)
            document.finishPage(page)
            return document
        }

    }
}
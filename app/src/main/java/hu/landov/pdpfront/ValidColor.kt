package hu.landov.pdpfront

fun validColor(colorString: String): Boolean {
    try {
        val color = android.graphics.Color.parseColor(colorString)
        return true
    } catch (e: IllegalArgumentException) {
        return false
    }
}
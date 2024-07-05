package hu.landov.pdpfront

fun Double.toPxX() : Float {
    return this.toFloat() / 25.4f * 72f
}

fun Double.toPxY() : Float {
    return this.toFloat() / 25.4f * 72f
}
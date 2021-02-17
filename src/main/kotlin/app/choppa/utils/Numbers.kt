package app.choppa.utils

class Numbers {
    companion object {
        /**
         * Round a double to N decimal places.
         */
        fun Double.round(decimalPlaces: Int = 2): Double =
            "%.${decimalPlaces}f".format(this).toDouble()
    }
}

package app.choppa.utils

import java.lang.Integer.toHexString

class Color {
    companion object {
        const val GREY = -858993409

        /**
         * Convert an Integer to be an expanded RGB hexadecimal color.
         * This method also support RGB alpha channels conversions.
         *
         * @receiver Int  The Integer to convert
         * @return String The RGB hexadecimal color (i.e. #RRGGBB)
         */
        fun Int.toRGBAHex(): String =
            "#${toHexString(this).padStart(8, '0')}"

        /**
         * Convert an String RGB hexadecimal color to Integer RGBA.
         * This method also support RGB alpha channels conversions.
         *
         * @receiver String The RGB hexadecimal color (i.e. #RRGGBB)
         * @return Int      The converted Integer RGBA
         */
        fun String.toRGBAInt(): Int = this
            .substring(1)
            .let {
                when (it.length) {
                    6 -> "${it}ff".toLong(16).toInt()
                    8 -> it.toLong(16).toInt()
                    else -> throw IllegalArgumentException("Hex [$this] value is invalid.")
                }
            }
    }
}

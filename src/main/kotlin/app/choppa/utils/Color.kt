package app.choppa.utils

import java.lang.Integer.toHexString

class Color {
    companion object {
        const val RED = -1407643649
        const val GREEN = 1655133951
        const val BLUE = 5476863
        const val YELLOW = -171882497
        const val PURPLE = 1733141759
        const val GREY = -858993409
        const val BROWN = 880111103

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
                    else -> throw IllegalArgumentException("Hex value [$this] format is invalid. (Valid hex color format: #AABBCC)")
                }
            }
    }
}

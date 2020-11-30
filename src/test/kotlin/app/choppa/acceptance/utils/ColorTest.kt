package app.choppa.acceptance.utils

import app.choppa.utils.Color.Companion.toRGBAHex
import app.choppa.utils.Color.Companion.toRGBAInt
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ColorTest {
    @ParameterizedTest
    @MethodSource("rgbaIntToRgbaHexStringTestArgs")
    internal fun `convert RGBA Int to RGBA Hex String Color`(rgbaInt: Int, rgbaHex: String) {
        val result = rgbaInt.toRGBAHex()
        result shouldBeEqualTo rgbaHex
    }

    @ParameterizedTest
    @MethodSource("rgbaHexStringToRgbaIntTestArgs")
    internal fun `convert RGBA Hex String to RGBA Int`(rgbaHex: String, rgbaInt: Int) {
        val result = rgbaHex.toRGBAInt()
        result shouldBeEqualTo rgbaInt
    }

    @ParameterizedTest
    @MethodSource("rgbHexStringToRgbaIntTestArgs")
    internal fun `convert RGB Hex String to RGBA Int`(rgbHex: String, rgbaInt: Int) {
        rgbHex.toRGBAInt() shouldBeEqualTo rgbaInt
    }

    companion object {
        @JvmStatic
        fun rgbaIntToRgbaHexStringTestArgs(): Stream<Arguments?>? {
            return Stream.of(
                arguments(-1, "#ffffffff"),
                arguments(-16777216, "#ff000000"),
                arguments(16711680, "#00ff0000"),
                arguments(65280, "#0000ff00"),
                arguments(255, "#000000ff"),
                arguments(0, "#00000000"),
            )
        }

        @JvmStatic
        fun rgbaHexStringToRgbaIntTestArgs(): Stream<Arguments?>? {
            return Stream.of(
                arguments("#ffffffff", -1),
                arguments("#ff000000", -16777216),
                arguments("#00ff0000", 16711680),
                arguments("#0000ff00", 65280),
                arguments("#000000ff", 255),
                arguments("#00000000", 0),
            )
        }

        @JvmStatic
        fun rgbHexStringToRgbaIntTestArgs(): Stream<Arguments?>? {
            return Stream.of(
                arguments("#ffffff", -1),
                arguments("#ff0000", -16776961),
                arguments("#00ff00", 16711935),
                arguments("#0000ff", 65535),
                arguments("#000000", 255),
            )
        }
    }
}

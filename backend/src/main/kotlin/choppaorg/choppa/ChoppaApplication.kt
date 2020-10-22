package choppaorg.choppa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChoppaApplication

fun main(args: Array<String>) {
	runApplication<ChoppaApplication>(*args)
}

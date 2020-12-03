package app.choppa.support.matchers

fun <T> List<T>.containsInAnyOrder(elements: List<T>): Boolean =
    elements.size == this.size && elements.distinct().all { this.contains(it) }

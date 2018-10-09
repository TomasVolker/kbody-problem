package tomasvolker

inline fun <T> List<T>.forAllPairs(block: (item1: T, item2: T) -> Unit) {

    for (i in 0 until size) {

        val item1 = this[i]

        for (j in (i + 1) until size) {
            val item2 = this[j]

            block(item1, item2)

        }
    }

}
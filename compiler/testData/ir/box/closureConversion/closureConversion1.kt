fun foo(x: String): String {
    fun bar(y: String) = x + y
    return bar("K")
}

fun box() = foo("O")
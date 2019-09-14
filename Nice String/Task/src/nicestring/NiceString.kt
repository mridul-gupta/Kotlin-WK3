package nicestring

fun String.isNice(): Boolean {
    var count: Int = 0

    //bu, ba, be
    /*if (!(this.contains("bu") || this.contains("ba") || this.contains("be"))) {
        count++
    }*/
    val pass1 = setOf("bu", "ba", "be").none { this.contains(it) }

    // three vowels
    /*var vowel = 0
    for (i in 0 until (this.length)) {
        when (this[i]) {
            'a','e','i','o','u' -> vowel++
        }
    }
    if (vowel >= 3) count++*/

    val pass2 = count { it in setOf('a', 'e', 'i', 'o', 'u') } >= 3

    // double letter
    /*for (i in 0 until (this.length -1)) {
        if (this[i] == this[i+1]) {
            count++
            break
        }
    }*/
    val pass3 = zipWithNext().any { it.first == it.second }

    return listOf(pass1, pass2, pass3).count { it == true } > 1
}


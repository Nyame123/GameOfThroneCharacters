package com.bismark.gameofthronecharacters

/**
 * Returns empty String with no characters
 **/
fun String.Companion.empty(): String = ""

/**
 * Return the receiver or the empty String if the receiver is `null`
 **/
fun String?.toSafeString(): String {
    if (this == null) return String.empty()
    return toString()
}

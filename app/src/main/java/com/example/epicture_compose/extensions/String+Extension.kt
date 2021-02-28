package com.example.epicture_compose.extensions

fun String.isValidExtension(): Boolean {
    return this.endsWith(".jpeg") ||
            this.endsWith(".jpg") ||
            this.endsWith(".png")
}

fun ignoreCaseOpt(ignoreCase: Boolean) =
        if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

fun String?.indexesOf(pat: String, ignoreCase: Boolean = true): List<Int> =
        pat.toRegex(ignoreCaseOpt(ignoreCase))
                .findAll(this?: "")
                .map { it.range.first }
                .toList()
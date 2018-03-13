package io.matchmore.sdk.utils

fun <T1: Any, T2: Any> unwrap(p1: T1?, p2: T2?, block: (T1, T2) -> Unit, failBlock: (() -> Unit)? = null) {
    if (p1 != null && p2 != null) block.invoke(p1, p2) else failBlock?.invoke()
}
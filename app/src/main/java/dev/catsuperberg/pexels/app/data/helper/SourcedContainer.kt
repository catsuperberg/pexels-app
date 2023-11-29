package dev.catsuperberg.pexels.app.data.helper

data class SourcedContainer<T, U>(val data: T, val source: U? = null)

// Annotation values is used in the KSP processor.
@file:Suppress("UNUSED")

package dev.medzik.android.datastore

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class DataStore(val name: String)

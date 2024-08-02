// Annotation values is used in the KSP processor.
@file:Suppress("UNUSED")

package dev.medzik.android.datastore

/**
 * Creates a DataStore for the annotated class.
 * Requires the class to be also annotated with @Serializable.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class DataStore(val name: String)

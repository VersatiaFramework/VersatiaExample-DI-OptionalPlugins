package com.github.marcoral.versatia.example.optionalplugins

import com.github.marcoral.versatia.ConditionalOnPlugin

/** A condition written once: an annotation annotated with `@ConditionalOnPlugin` counts as the condition itself. */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@ConditionalOnPlugin("Vault")
annotation class NeedsVault

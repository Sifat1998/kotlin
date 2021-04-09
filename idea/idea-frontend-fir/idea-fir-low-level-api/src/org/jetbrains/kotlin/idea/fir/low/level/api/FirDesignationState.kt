/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.fir.low.level.api

import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.idea.fir.low.level.api.api.FirDeclarationDesignation

internal class FirDesignationState(val designation: FirDeclarationDesignation, val firFile: FirFile) {
    /**
     * Holds current declaration index
     * if `currentIndex in [0, designation.path.lastIndex]` then current declaration is in path
     * if `currentIndex == `designation.path.lastIndex + 1` then current declaration is our target declaration
     * if `currentIndex > designation.path.lastIndex + 1` then we are inside current declaration
     */
    private var currentIndex = -1

    fun canGoNext(): Boolean = currentIndex < designation.path.size

    val currentDeclarationIfPresent: FirDeclaration?
        get() = when (currentIndex) {
            in designation.path.indices -> designation.path[currentIndex]
            designation.path.size -> designation.declaration
            else -> null
        }

    val currentDeclaration: FirDeclaration
        get() = currentDeclarationIfPresent
            ?: error("Went inside target declaration")

    fun goNext() {
        if (currentIndex <= designation.path.size) {
            currentIndex++
        } else {
            throw IndexOutOfBoundsException()
        }
    }
}
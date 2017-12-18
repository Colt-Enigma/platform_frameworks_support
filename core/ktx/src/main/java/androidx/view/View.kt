/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE") // Aliases to other public API.

package androidx.view

import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewTreeObserver

/**
 * Performs the given action when this view is next laid out.
 *
 * @see doOnLayout
 */
inline fun View.doOnNextLayout(crossinline action: (View) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int,
                                    oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            action(view)
            view.removeOnLayoutChangeListener(this)
        }
    })
}

/**
 * Performs the given action when this view is laid out. If the view has been laid out, the action
 * will be performed straight away, otherwise the action will be performed after the
 * view is laid out.
 *
 * @see doOnNextLayout
 */
@RequiresApi(19)
fun View.doOnLayout(action: (View) -> Unit) {
    if (isLaidOut) {
        action(this)
    } else {
        doOnNextLayout {
            action(it)
        }
    }
}

/**
 * Performs the given action when the view tree is about to be drawn.
 */
fun View.doOnPreDraw(action: (View) -> Unit) {
    val vto = viewTreeObserver
    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            action(this@doOnPreDraw)
            when {
                vto.isAlive -> vto.removeOnPreDrawListener(this)
                else -> viewTreeObserver.removeOnPreDrawListener(this)
            }
            return true
        }
    })
}

/**
 * Updates this view's relative padding. This version of the method allows using named parameters
 * to just set one or more axes.
 *
 * @see View.setPaddingRelative
 */
@RequiresApi(17)
fun View.updatePaddingRelative(start: Int = paddingStart,
                               top: Int = paddingTop,
                               end: Int = paddingEnd,
                               bottom: Int = paddingBottom) {
    setPaddingRelative(start, top, end, bottom)
}

/**
 * Updates this view's padding. This version of the method allows using named parameters
 * to just set one or more axes.
 *
 * @see View.setPadding
 */
fun View.updatePadding(left: Int = paddingLeft,
                       top: Int = paddingTop,
                       right: Int = paddingRight,
                       bottom: Int = paddingBottom) {
    setPadding(left, top, right, bottom)
}
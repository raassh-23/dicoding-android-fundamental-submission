package com.raassh.dicodinggithubuserapp.misc

import android.view.View

fun visibility(visible: Boolean) = if (visible) {
    View.VISIBLE
} else {
    View.INVISIBLE
}
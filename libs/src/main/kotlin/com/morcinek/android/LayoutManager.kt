package com.morcinek.android

import androidx.recyclerview.widget.GridLayoutManager


fun GridLayoutManager.setupSpanSizeLookup(getSpanSize: (position: Int) -> Int) = apply {
    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) = getSpanSize(position)
    }
}
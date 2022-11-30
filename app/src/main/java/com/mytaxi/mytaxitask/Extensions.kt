package com.mytaxi.mytaxitask

import android.view.View

fun View.show() = run { this.visibility = View.VISIBLE }


fun View.hide() = run { this.visibility = View.INVISIBLE }


fun View.invisible() = run { this.visibility = View.INVISIBLE }
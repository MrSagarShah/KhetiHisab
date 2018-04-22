package com.hisab.kheti.utils

import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.text.SimpleDateFormat
import java.util.*

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, bundle: Bundle? = null) {
    supportFragmentManager.inTransaction {
        bundle?.let {
            fragment.arguments = it
        }
        add(frameId, fragment)
    }
}

fun AppCompatActivity.addFragmentWithBackstack(fragment: Fragment, frameId: Int, bundle: Bundle? = null) {
    supportFragmentManager.inTransaction {
        bundle?.let {
            fragment.arguments = it
        }
        add(frameId, fragment).addToBackStack(null)
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, bundle: Bundle? = null) {
    supportFragmentManager.inTransaction {
        bundle?.let {
            fragment.arguments = it
        }
        replace(frameId, fragment)
    }
}


fun AppCompatActivity.replaceFragmentWithBackStack(fragment: Fragment, frameId: Int, bundle: Bundle? = null) {
    supportFragmentManager.inTransaction {
        bundle?.let {
            fragment.arguments = it
        }
        replace(frameId, fragment).addToBackStack(null)
    }
}

fun View.isVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()


fun Date.convertDobToAppFormatDate(): String {
    return SimpleDateFormat(APP_DATE_PATTERN).format(this)
}
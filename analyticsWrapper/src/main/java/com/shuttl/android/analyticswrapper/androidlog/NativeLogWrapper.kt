package com.shuttl.android.analyticswrapper.androidlog

import android.util.Log

object NativeLogWrapper {
    val tag = NativeLogWrapper::class.java.name

    var packageName = ""
    var androidLogEnabled = true
    var isPackageNameVisible = false
    var isLengthShouldWrap = true

    var classNameLength = 15
    var packageAndClassNameLength = 35
    var methodNameLength = 15

    private fun androidLog(level: Int, msg: String, customTag: String?) {
        if (!androidLogEnabled) return

        val stackTrace = Thread.currentThread().stackTrace
        val elementIndex: Int = getElementIndex(stackTrace)
        if (elementIndex == 0) return

        val element = stackTrace[elementIndex]
        val result = StringBuilder()

        addClassName(element, result)
        addMethodName(element, result)

        addMessage(msg, result)

        val tag = if (customTag == null) tag else "$tag::$customTag"

        Log.println(level, tag, result.toString())
    }

    private fun getElementIndex(stackTrace: Array<StackTraceElement>?): Int {
        if (stackTrace == null) return 0
        for (i in 2..stackTrace.size) {
            val className = stackTrace[i].className ?: ""
            if (className.contains(this.javaClass.simpleName)) continue
            return i
        }
        return 0
    }

    private fun addClassName(element: StackTraceElement, result: StringBuilder) {
        val fullClassName = element.className
        val maxLength = if (isPackageNameVisible) packageAndClassNameLength else classNameLength

        var classNameFormatted = if (isPackageNameVisible) {
            fullClassName.replace(packageName, "")
        } else {
            fullClassName.substring(fullClassName.lastIndexOf('.') + 1)
        }

        if (isLengthShouldWrap) classNameFormatted = wrapString(classNameFormatted, maxLength)
        result.append(classNameFormatted)

        addSpaces(result, maxLength - classNameFormatted.length)
        result.append(" # ")
    }

    private fun addMethodName(element: StackTraceElement, result: StringBuilder) {
        var methodName = element.methodName
        if (isLengthShouldWrap) methodName = wrapString(methodName, methodNameLength)
        result.append("$methodName()")
        addSpaces(result, methodNameLength - methodName.length)
    }

    private fun addMessage(msg: String, result: StringBuilder) {
        if (msg.isNotEmpty()) {
            result.append("=> ")
            result.append(msg)
        }
    }

    private fun wrapString(string: String, maxLength: Int): String {
        return string.substring(0, string.length.coerceAtMost(maxLength))
    }

    private fun addSpaces(result: StringBuilder, spaces: Int) {
        result.append(" ".repeat(spaces))
    }

    /*
    //////////////////////////////
    Android.Log Wrapper Functions
    //////////////////////////////
     */

    @JvmOverloads
    fun sLogV(msg: String = "", customTag: String? = null) {
        androidLog(Log.VERBOSE, msg, customTag)
    }

    @JvmOverloads
    fun sLogD(msg: String = "", customTag: String? = null) {
        androidLog(Log.DEBUG, msg, customTag)
    }

    @JvmOverloads
    fun sLogI(msg: String = "", customTag: String? = null) {
        androidLog(Log.INFO, msg, customTag)
    }

    @JvmOverloads
    fun sLogW(msg: String = "", customTag: String? = null) {
        androidLog(Log.WARN, msg, customTag)
    }

    @JvmOverloads
    fun sLogE(msg: String = "", customTag: String? = null) {
        androidLog(Log.ERROR, msg, customTag)
    }

    @JvmOverloads
    fun sLogA(msg: String = "", customTag: String? = null) {
        androidLog(Log.ASSERT, msg, customTag)
    }

}
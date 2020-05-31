package com.sl.mirai.plugin

object OSUtil {
    //判断当前系统
    var isWindows = judge()
    /** 根据不同的系统返回路径分隔符*/
    var pathBreak = if (isWindows)"\\" else "/"
    private fun judge():Boolean{
        val os:String? = System.getProperty("os.name")
        return os != null && os.contains("windows")
    }
}
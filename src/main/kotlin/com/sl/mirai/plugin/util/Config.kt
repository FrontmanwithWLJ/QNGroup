package com.sl.mirai.plugin.util

import com.sl.mirai.plugin.ExamplePluginMain.keepDigital
import java.io.File
import java.lang.StringBuilder

/**
 * 暂时替代mirai的Config，读取lon列表总是读成float
 */
class Config(private val pathStr: String, private val fileName: String) {

   init{
        val path = File(pathStr)
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File(fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        //linux下必须的
        file.setWritable(true)
    }

    fun getLongList(): List<Long> {
        val json = FileUtil.read(pathStr,fileName)
        if (json == "")return ArrayList()
        val strList = json.split(",")
        val list = ArrayList<Long>()
        for (str in strList) {
            val id = str.keepDigital().toLong()
            if (id!=0L)list.add(id)
        }
        return list
    }

    fun set(value:List<Long>?){
        if (value == null)return
        val stringBuilder = StringBuilder()
        value.forEach {
            stringBuilder.append("$it,")
        }
        FileUtil.write(pathStr,fileName,stringBuilder.toString())
    }
}
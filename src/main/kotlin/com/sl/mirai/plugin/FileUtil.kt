package com.sl.mirai.plugin

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

object FileUtil {
    /**
     * @param append 默认覆盖
     */
    fun write(pathStr: String, name: String, content: String, append: Boolean = false): Boolean {
        val path: File
        val file: File
        var writer:OutputStreamWriter? = null
        try {
            path = File(pathStr)
            if (!path.exists()) {
                path.mkdirs()
            }
            file = File(path, name)
            println("writer$path$name")
            if (!file.exists()) {
                file.createNewFile()
            }
            //写之前先判断是否已经存在
            val t = read(pathStr,name)
            if (t.contains(content))return false
            file.setWritable(true)//linux需要这句话才能写入
            writer = OutputStreamWriter(FileOutputStream(file,append))
            writer.write(content)
            writer.flush()
        } catch (e: Exception) {
            println(pathStr+name)
            e.printStackTrace()
            return false
        } finally {
            writer?.close()
        }
        return true
    }

    /**
     * @return 返回文件内容
     * */
    fun read(path: String, name: String): String {
        val file = File(path, name)
        if (!file.canRead() or !file.exists()) {
            return ""
        }
        try {
            return file.readText(StandardCharsets.UTF_8)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ""
    }
}
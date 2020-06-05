package com.sl.mirai.plugin.manager


import com.sl.mirai.plugin.util.Config
import com.sl.mirai.plugin.util.FileUtil
import com.sl.mirai.plugin.util.OSUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object QNGroupManager {
    //需要处理消息的群
    private lateinit var list: ArrayList<Long>
    private lateinit var config: Config

    //开始时调用
    fun init(config: Config) {
        QNGroupManager.config = config
        list = config.getLongList() as ArrayList<Long>
        print("已添加的群列表：")
        list.forEach{
            print(it)
        }
    }

    fun add(id: Long): Boolean {
        if (!list.contains(id)) {
            list.add(id)
            save()
            return true
        }
        return false
    }

    fun del(id: Long): Boolean {
        if (list.contains(id)) {
            list.remove(id)
            save()
            return true
        }
        return false
    }

    fun contain(id: String): Boolean {
        var tmp = 0L
        try {
            tmp = id.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return contain(tmp)
    }

    fun contain(id: Long): Boolean {
        return list.contains(id)
    }

    fun record(basePath: String, id: Long, name: String): Boolean {
        return FileUtil.write(
            "$basePath${OSUtil.pathBreak}$id${OSUtil.pathBreak}${getWeekBeginDate()}${OSUtil.pathBreak}",
            "learnRecord.txt",
            name + "\n",
            true
        )
    }

    fun createExcel(basePath: String, id: Long): Boolean {
        return DataManager.buildExcel(
            "$basePath${OSUtil.pathBreak}${id}${OSUtil.pathBreak}${getWeekBeginDate()}${OSUtil.pathBreak}",
            "learnRecord.txt",
            "data.txt"
        )
    }

    //插件停止运行前调用
    fun save() {
        //保存群列表
        config.set(list)
    }

    //获取本周周一日期
    fun getWeekBeginDate(): String? {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val cld = Calendar.getInstance(Locale.CHINA)
        cld.firstDayOfWeek = 2
        cld.timeInMillis = System.currentTimeMillis()
        cld[7] = 2
        return df.format(cld.time)
    }
}
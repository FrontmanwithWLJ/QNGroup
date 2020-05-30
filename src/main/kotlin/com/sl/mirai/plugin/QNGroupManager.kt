package com.sl.mirai.plugin


import net.mamoe.mirai.console.plugins.Config
import net.mamoe.mirai.console.plugins.ToBeRemoved
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object QNGroupManager {
    //需要处理消息的群
    private lateinit var list:ArrayList<Long>
    private lateinit var config:Config

    //开始时调用
    fun init(config: Config) {
        this.config = config
        val tmp = config["group"]
        if (tmp != null){
            list = tmp as ArrayList<Long>
        }else{
            list = ArrayList()
        }
    }

    fun add(id: Long): Boolean {
        if (!list.contains(id)) {
            list.add(id)
            return true
        }
        return false
    }

    fun del(id: Long): Boolean {
        if (list.contains(id)) {
            list.remove(id)
            return true
        }
        return false
    }

    fun contain(id:String):Boolean{
        var tmp = 0L
        try {
            tmp = id.toLong()
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
        return contain(tmp)
    }

    fun contain(id: Long): Boolean {
        return list.contains(id)
    }

    fun record(basePath: String,id: Long,name:String):Boolean{
        return FileUtil.write("$basePath\\$id\\${getWeekBeginDate()}\\","learnRecord.txt",name+"\n",true)
    }

    fun createExcel(basePath:String,id: Long): Boolean {
        return DataManager.buildExcel("$basePath\\${id}\\${getWeekBeginDate()}\\", "learnRecord.txt","data.txt")
    }

    //插件停止运行前调用
    @OptIn(ToBeRemoved::class)
    fun save(){
        config["group"] = list
        config.save()
    }

    //获取本周周一日期
    private fun getWeekBeginDate(): String? {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val cld = Calendar.getInstance(Locale.CHINA)
        cld.firstDayOfWeek = 2
        cld.timeInMillis = System.currentTimeMillis()
        cld[7] = 2
        return df.format(cld.time)
    }
}
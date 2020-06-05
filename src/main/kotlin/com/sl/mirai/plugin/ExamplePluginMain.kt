package com.sl.mirai.plugin

import com.sl.mirai.plugin.manager.QNGroupManager
import com.sl.mirai.plugin.util.Config
import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.*
import java.util.regex.Pattern


object ExamplePluginMain : PluginBase() {
//    /**注册生成表格命令*/
//    private val createExcel = registerCommand {
//        name = "CreateExcel"
//        alias = listOf("ce", "CE")
//        description = "生成本周青年大学习统计表格"
//        usage = "ce|CE|CreateExcel [QQ GroupID]"
//    }
//
//    /**添加群监听*/
//    private val addGroup = registerCommand {
//        name = "AddGroup"
//        alias = listOf("ag")
//        description = "添加群监听"
//        usage = "ag [QQ GroupID]"
//    }
//
//    /**删除群监听*/
//    private val delGroup = registerCommand {
//        name = "DelGroup"
//        alias = listOf("dg")
//        description = "删除群监听"
//        usage = "dg [QQ GroupID]"
//    }


    //用于放每个群的统计数据
    private val basePath = dataFolder.absolutePath
    private val config = Config(basePath,"groupList.txt")

    override fun onLoad() {
        super.onLoad()
        QNGroupManager.init(config)
        dataFolder
    }

    override fun onEnable() {
        super.onEnable()
        subscribeGroupMessages {
            (has<Image>() and (contains("青年") or contains("大学习"))) {
                if (QNGroupManager.contain(group.id)) {
                    //条件 青年大学习+一张图片
                    //println("get picture")
                    QNGroupManager.record(basePath, group.id, senderName)
//                FileUtil.write(basePath+"/"+group.id,"/${}/learnRecord.txt",senderName+"\n",true)
                    reply(sender.at() + "已收到青年大学习截图")
                }
            }
        }

        //todo 表情还是数字，后面再去查文档
        subscribeFriendMessages {
            (contains("表格") and sentBy(1844977240L)) {
                reply("好的，主人！" + Face.baobao.toString() + "\n请稍等")
                if (!QNGroupManager.contain(it.keepDigital())) {
                    reply("目前暂未开启此群的统计功能，请手动添加哦")
                } else {
                    val groupId = it.keepDigital().toLong()
                    val target = QNGroupManager.createExcel(basePath, groupId)
                    reply(if (target) "http://39.97.127.33:1999/$groupId/${QNGroupManager.getWeekBeginDate()}/book.xls" else "生成失败")
                }
            }

            (contains("添加") and sentBy(1844977240L)) {
                reply(if (QNGroupManager.add(it.keepDigital().toLong())) "添加成功" else "添加失败，此群已添加")
//                try {
//                    if (QNGroupManager.add(it.keepDigital().toLong())) {
//                        reply("添加成功")
//                    } else {
//                        reply("添加失败")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    reply("添加失败")
//                }
            }
            (contains("删除") and sentBy(1844977240L)) {
                reply(if (QNGroupManager.del(it.keepDigital().toLong())) "删除成功" else "删除失败,未添加此群")
//                try {
//                    if (QNGroupManager.del(it.keepDigital().toLong())) {
//                        reply("删除成功")
//                    } else {
//                        reply("删除失败")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    reply("删除失败")
//                }
            }
            contains("帮助"){
                reply("添加群进入统计列表 eg: 添加 [群号]\n从统计列表删除群 eg: 删除 [群号]\n生成指定群的统计表格 eg: 表格 [群号]")
            }
        }
    }

    override fun onDisable() {
        //保存群列表
        QNGroupManager.save()
        super.onDisable()
    }

//    override fun onCommand(command: Command, sender: CommandSender, args: List<String>) {
//        super.onCommand(command, sender, args)
//        when (command.name) {
//            "CreateExcel" -> {
//                try {
//                    val id = args[1].toLong()
//                    if (QNGroupManager.contain(id)) {
//                        val target = QNGroupManager.createExcel(basePath,id)
//                        if (target) {
//
//                        }
//                    } else {
//                        throw Exception("未")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    sender.sendMessageBlocking("群号输入有误")
//                }
//            }
//            "AddGroup" -> {
//            }
//            "DelGroup" -> {
//            }
//        }
//    }

    fun String.keepDigital(): String {
        val newString = StringBuffer()
        val matcher = Pattern.compile("\\d").matcher(this)
        while (matcher.find()) {
            newString.append(matcher.group())
        }
        if (newString.isEmpty())newString.append(0)
        return newString.toString()
    }
}
package com.sl.mirai.plugin

import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.info

object ExamplePluginMains : PluginBase() {
    override fun onLoad() {
        super.onLoad()
    }

    override fun onEnable() {
        super.onEnable()

        logger.info("Plugin loaded!")

        subscribeMessages {
            "greeting" reply { "Hello ${sender.nick}" }
        }


        subscribeGroupMessages {
            contains(""){
                
            }
        }

        subscribeAlways<MessageRecallEvent> { event ->
            logger.info { "${event.authorId} 的消息被撤回了" }
        }
    }

    override fun onDisable() {
        super.onDisable()

    }
}
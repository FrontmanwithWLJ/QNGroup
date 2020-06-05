package com.sl.mirai.plugin.manager

import com.sl.mirai.plugin.bean.MemberInfo
import com.sl.mirai.plugin.util.OSUtil
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import jxl.write.WriteException
import java.io.*
import java.nio.charset.StandardCharsets


object DataManager {
    private fun initBook(pathStr: String, name: String): WritableWorkbook? {
        println("初始化表格")
        var workbook: WritableWorkbook? = null
        try {
            val path = File(pathStr)
            if (!path.exists()) {
                path.mkdirs()
            }
            val file = File(pathStr + OSUtil.pathBreak + name)
            file.setWritable(true)
            file.delete()
            file.createNewFile()
            workbook = Workbook.createWorkbook(file)
            val sheet: WritableSheet = workbook.createSheet("青年大学习", 0)
            val title = arrayOf("序号", "单位名称", "姓名", "学号", "辅导员", "备注")
            for (i in title.indices) {
                //println("设置列元素名")
                val label = Label(i, 0, title[i])
                sheet.addCell(label)
            }
        } catch (var10: WriteException) {
            var10.printStackTrace()
        } catch (var10: IOException) {
            var10.printStackTrace()
        }
        return workbook
    }

    private fun write(sheet: WritableSheet, memberInfo: MemberInfo, row: Int) {
        println("开始写入" + memberInfo.name)
        for (i in 1 until row) {
            if (sheet.getCell(2, i).contents == memberInfo.name) {
                return
            }
        }
        try {
            sheet.addCell(Label(0, row, "" + row))
            sheet.addCell(Label(1, row, "13001801团支部"))
            sheet.addCell(Label(2, row, memberInfo.name))
            sheet.addCell(Label(3, row, "" + memberInfo.id))
            sheet.addCell(Label(4, row, "刘旭"))
            sheet.addCell(Label(5, row, memberInfo.type))
        } catch (var5: WriteException) {
            var5.printStackTrace()
        }
    }

    fun buildExcel(pathStr: String, recordFileName: String, dataFileName: String): Boolean {
        var workbook: WritableWorkbook? = null
        var inputStream: FileInputStream? = null
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        var count = 1
        val var12: Boolean
        try {
            val path = File(pathStr)
            println("path:$pathStr")
            if (path.exists()) {
                val record = File(pathStr + OSUtil.pathBreak + recordFileName)
                println("file:$recordFileName")
                if (!record.exists()) {
                    var12 = false
                    return var12
                }
                val baseData = File(path.parent + OSUtil.pathBreak + dataFileName)
                println("data:" + path.parent)
                if (!baseData.exists()) {
                    var12 = false
                    return var12
                }
                workbook = initBook(pathStr, "book.xls")
                val sheet: WritableSheet = workbook!!.getSheet("青年大学习")
                inputStream = FileInputStream(record)
                inputStreamReader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
                bufferedReader = BufferedReader(inputStreamReader)
                var memberInfoTmp: MemberInfo? = null
                println("开始读取记录")
                //Java直接转kotlin注意这里    BufferedReader.readLine 返回声明是String！ 它可以返回null，所以下面的meberName眼改成String？
                var memberName: String?
                while (true) {
                    memberName = bufferedReader.readLine()
                    if (memberName == null)break
                    if (memberName != "" && memberName != "\r\n") {
                        memberInfoTmp = getMemberInfo(
                            memberName,
                            baseData
                        )
                    }
                    if (memberInfoTmp != null) {
                        println("开始写入表格")
                        write(sheet, memberInfoTmp, count)
                        memberInfoTmp = null
                        ++count
                    }
                }
                return true
            }
            var12 = false
        } catch (var26: IOException) {
            var26.printStackTrace()
            return true
        } finally {
            try {
                if (count > 1) {
                    println("保存表格到本地")
                    workbook!!.write()
                    workbook.close()
                }
                bufferedReader?.close()
                inputStreamReader?.close()
                inputStream?.close()
            } catch (var25: WriteException) {
            } catch (var25: IOException) {
            }
        }
        return var12
    }

    private fun getMemberInfo(memberName: String, dataFile: File): MemberInfo? {
        println("开始读取data文件$memberName")
        var memberInfo: MemberInfo? = null
        var inputStream: InputStream? = null
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        try {
            inputStream = FileInputStream(dataFile)
            inputStreamReader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
            bufferedReader = BufferedReader(inputStreamReader)
            var strLine: String
            while (true) {
                strLine = bufferedReader.readLine()
                if (strLine == null)break
                if (strLine.contains(memberName)) {
                    val tmp = strLine.split(",",";")
                    println("找到成员:$strLine 数组长度:${tmp.size}")
                    memberInfo = MemberInfo(tmp[0], tmp[1].toLong(), tmp[2])
                    break
                }
            }
        } catch (var17: IOException) {
            var17.printStackTrace()
        } finally {
            try {
                bufferedReader?.close()
                inputStreamReader?.close()
                inputStream?.close()
            } catch (var16: IOException) {
            }
        }
        return memberInfo
    }
}

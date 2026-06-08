package com.lottery.accountbook.util

import android.content.Context
import android.os.Environment
import com.lottery.accountbook.data.entity.LotteryRecord
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ExcelExporter {
    fun exportRecords(context: Context, records: List<LotteryRecord>, fileName: String): File? {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("彩票记录")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 创建标题行
        val header = sheet.createRow(0)
        val headers = arrayOf("购买日期", "彩票类型", "投注金额", "投注号码", "购彩理由", "中奖金额", "备注")
        headers.forEachIndexed { i, title ->
            val cell = header.createCell(i)
            cell.setCellValue(title)
        }

        // 填充数据
        records.forEachIndexed { index, record ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(dateFormat.format(Date(record.date)))
            row.createCell(1).setCellValue(record.lotteryType)
            row.createCell(2).setCellValue(record.betAmount)
            row.createCell(3).setCellValue(record.numbers ?: "")
            row.createCell(4).setCellValue(record.reason ?: "")
            row.createCell(5).setCellValue(record.winAmount)
            row.createCell(6).setCellValue(record.note ?: "")
        }

        // 保存文件
        return try {
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "彩票记账本")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "$fileName.xlsx")
            FileOutputStream(file).use { outputStream ->
                workbook.write(outputStream)
            }
            workbook.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
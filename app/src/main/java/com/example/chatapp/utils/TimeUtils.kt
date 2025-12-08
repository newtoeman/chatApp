package com.example.chatapp.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    private val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
    private val yearFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun formatTimestamp(inputTimestamp: String): String {
        // 假设输入格式为 "yyyy-MM-dd HH:mm:ss" 或类似格式
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(inputTimestamp) ?: return inputTimestamp
            
            val now = Calendar.getInstance()
            val inputCalendar = Calendar.getInstance().apply { time = date }
            
            // 检查是否是今天
            if (isToday(inputCalendar)) {
                simpleDateFormat.format(date)
            } 
            // 检查是否是昨天
            else if (isYesterday(inputCalendar)) {
                "昨天 ${simpleDateFormat.format(date)}"
            }
            // 检查是否是今年
            else if (inputCalendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                dayFormat.format(date)
            }
            // 其他情况显示年月日
            else {
                yearFormat.format(date)
            }
        } catch (e: Exception) {
            inputTimestamp // 如果解析失败，返回原始格式
        }
    }

    private fun isToday(calendar: Calendar): Boolean {
        val today = Calendar.getInstance()
        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(calendar: Calendar): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)
    }
}
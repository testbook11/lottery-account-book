package com.lottery.accountbook.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect

@Composable
fun PaperBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 绘制横格线模拟笔记本
        Canvas(modifier = Modifier.fillMaxSize()) {
            val lineSpacing = 60f
            val lineColor = Color(0xFFD8CFC0)
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = lineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
                y += lineSpacing
            }
            // 左边距红线
            drawLine(
                color = Color(0xFFC0392B),
                start = Offset(40f, 0f),
                end = Offset(40f, size.height),
                strokeWidth = 2f
            )
        }
    }
}
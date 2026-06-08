package com.lottery.accountbook.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.lottery.accountbook.ui.theme.*

@Composable
fun BarChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier,
    barColor: Color = ChartBlue
) {
    if (data.isEmpty()) {
        EmptyDataMessage("数据不足，无法生成图表")
        return
    }
    Column(modifier = modifier.padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val maxValue = data.values.maxOrNull() ?: 1.0
            val barCount = data.size
            val barWidth = size.width / (barCount * 2 + 1)
            val gap = barWidth

            data.entries.forEachIndexed { index, (label, value) ->
                val barHeight = (value / maxValue * size.height * 0.8).toFloat()
                val x = gap + index * (barWidth + gap)
                val y = size.height - barHeight
                // 绘制柱状
                drawRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )
                // 绘制标签
                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    x + barWidth / 2,
                    size.height - 10f,
                    android.graphics.Paint().apply {
                        color = InkDarkGray.toArgb()
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 24f
                    }
                )
            }
        }
    }
}

@Composable
fun LineChart(
    data: List<Pair<String, Double>>, // 时间序列数据
    modifier: Modifier = Modifier,
    lineColor: Color = ChartOrange
) {
    if (data.size < 2) {
        EmptyDataMessage("数据不足，无法生成图表")
        return
    }
    Column(modifier = modifier.padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val maxValue = data.maxOf { it.second }.takeIf { it > 0 } ?: 1.0
            val minValue = data.minOf { it.second }.coerceAtMost(0.0)
            val range = maxValue - minValue
            val stepX = size.width / (data.size - 1)
            val points = data.mapIndexed { index, pair ->
                Offset(
                    index * stepX,
                    size.height - ((pair.second - minValue) / range * size.height * 0.8).toFloat() - size.height * 0.1f
                )
            }
            // 绘制折线
            for (i in 0 until points.size - 1) {
                drawLine(
                    color = lineColor,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = 3f
                )
            }
            // 绘制点
            points.forEach {
                drawCircle(color = lineColor, radius = 5f, center = it)
            }
        }
    }
}
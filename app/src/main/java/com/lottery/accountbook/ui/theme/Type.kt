package com.lottery.accountbook.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 假设项目中有手写风格字体文件，这里使用系统衬线体代替
val HandwritingFont = FontFamily.Default
val SerifFont = FontFamily.Serif

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = InkBlack
    ),
    headlineLarge = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = InkBlack
    ),
    titleLarge = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = InkBlack
    ),
    bodyLarge = TextStyle(
        fontFamily = HandwritingFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = InkDarkGray
    ),
    bodyMedium = TextStyle(
        fontFamily = HandwritingFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = InkDarkGray
    ),
    labelLarge = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = InkGray
    ),
    labelSmall = TextStyle(
        fontFamily = SerifFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = InkLightGray
    )
)
package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BBCILogoView(
    modifier: Modifier = Modifier,
    width: Int = 180,
    height: Int = 85
) {
    Box(
        modifier = modifier
            .size(width.dp, height.dp)
            .padding(top = 8.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Red Orbit Arc & Arrow Canvas drawing (TAXT Style Logo)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Red Orbit Ellipse Arc
            val orbitPath = Path().apply {
                moveTo(w * 0.18f, h * 0.88f)
                cubicTo(w * 0.05f, h * 0.5f, w * 0.22f, h * 0.15f, w * 0.62f, h * 0.35f)
            }
            drawPath(
                path = orbitPath,
                color = Color(0xFFE60000),
                style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
            )

            // Orbit head dot
            drawCircle(
                color = Color(0xFFE60000),
                radius = 4.5.dp.toPx(),
                center = Offset(w * 0.44f, h * 0.38f)
            )

            // Red Play/Arrow Icon on top right
            val arrowPath = Path().apply {
                moveTo(w * 0.74f, h * 0.32f)
                lineTo(w * 0.94f, h * 0.46f)
                lineTo(w * 0.74f, h * 0.62f)
                close()
            }
            drawPath(
                path = arrowPath,
                color = Color(0xFFE60000),
                style = Fill
            )
        }

        // Maroon Card with BBCI & Watermark
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.85f)
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF8A1C14), // Maroon
            shadowElevation = 6.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background Watermark text inside card
                Text(
                    text = "BBCI KSA",
                    color = Color.White.copy(alpha = 0.15f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "BBCI",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "SAUDI GOVTECH & IT",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

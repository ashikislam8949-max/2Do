package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TwoDoTechLogo(
    modifier: Modifier = Modifier,
    width: Int = 180,
    height: Int = 85
) {
    Surface(
        modifier = modifier
            .size(width.dp, height.dp)
            .padding(top = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF12324A),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "2Do Tech",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.2.sp,
                maxLines = 1
            )
            Text(
                text = "BUSINESS • IT • GOV SERVICES",
                color = Color(0xFF72D8C9),
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

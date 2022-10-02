package com.bismark.gameofthronecharacters.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bismark.gameofthronecharacters.R

@Composable
fun ImageItemRow(
    modifier: Modifier,
    itemLabel: String,
    labelValue: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = itemLabel,
            fontSize = 12.sp,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = labelValue,
            color = colorResource(id = R.color.black),
            fontSize = 14.sp
        )
    }

}

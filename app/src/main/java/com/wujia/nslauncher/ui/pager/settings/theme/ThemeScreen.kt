package com.wujia.nslauncher.ui.pager.settings.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

annotation class ThemeItem {
    companion object {
        const val DAY = 0
        const val NIGHT = 1
        const val SYSTEM = 2
    }
}


@Preview
@Composable
fun ThemeScreen(
    onClick: (() -> Unit)? = null,
    @ThemeItem selectItem: Int = 0
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 20.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(1.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 10.dp)
                .clickable {
                    onClick?.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RectangleShape
                    )
                    .background(Color.White)
            )


            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "白天",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.weight(1f))

            Icon(
                modifier = Modifier.padding(start = 15.dp),
                imageVector = Icons.Filled.CheckCircle,
                tint = Color.Blue,
                contentDescription = null
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(1.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 10.dp)
                .clickable {
                    onClick?.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RectangleShape
                    )
                    .background(Color.Black)
            )

            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "黑夜",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.weight(1f))

            Icon(
                modifier = Modifier.padding(start = 15.dp),
                imageVector = Icons.Filled.CheckCircle,
                tint = Color.Blue,
                contentDescription = null
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(1.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 10.dp)
                .clickable {
                    onClick?.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RectangleShape
                    )
                    .background(Color.Cyan)
            )

            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "跟随系统",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.weight(1f))

            Icon(
                modifier = Modifier.padding(start = 15.dp),
                imageVector = Icons.Filled.CheckCircle,
                tint = Color.Blue,
                contentDescription = null
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(1.dp)
        )

    }

}
package com.wujia.nslauncher.ui.pager.settings.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wujia.nslauncher.R

@Preview
@Composable
fun AboutScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    modifier = Modifier
                        .size(100.dp),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "NS Launcher",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )

                    Text(
                        text = "版本号：1.0.0",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "作者：林栩link",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                    )
                }
            }
        }

        item {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(1.dp)
            )
        }

        item() {
            Item(title = "意见反馈") {

            }
        }

        item() {
            Item(title = "隐私政策") {

            }
        }

        item() {
            Item(title = "版本更新") {

            }
        }

    }
}

@Composable
internal fun Item(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null
        )
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(1.dp)
    )
}

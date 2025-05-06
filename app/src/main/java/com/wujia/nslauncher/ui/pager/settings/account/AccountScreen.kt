package com.wujia.nslauncher.ui.pager.settings.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import com.wujia.nslauncher.R

@Preview
@Composable
fun AccountScreen(
    onClick: (() -> Unit)? = null,
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


            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "昵称",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "林栩link",
                color = Color.Blue,
                style = MaterialTheme.typography.titleSmall
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

            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "编辑头像",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.weight(1f))

            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(R.drawable.logo),
                contentScale = ContentScale.Fit,
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
package com.wujia.nslauncher.ui.view

import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wujia.nslauncher.R

/**
 * 通用顶部状态栏
 */
@Composable
fun StatusBar(
    modifier: Modifier = Modifier,
    title: String = "当前标题",
    @DrawableRes titleIcon: Int = R.drawable.ic_all_app,
    onTitleClick: () -> Unit = {},
    showRightIcon: Boolean = false,
    @StatusBarType vararg types: Int
) {
    Box(
        modifier = modifier
    ) {
        // 左侧区域
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp)
                .clickable { onTitleClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(titleIcon),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                contentDescription = title,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 18.sp
            )
        }
        // 右侧区域
        if (showRightIcon) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 20.dp)
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                types.forEach {
                    when (it) {
                        StatusBarType.GROUP -> {
                            StatusIconWithLabel(R.drawable.ic_l, "分组")
                            Spacer(modifier = Modifier.width(16.dp))
                        }

                        StatusBarType.ORDER -> {
                            StatusIconWithLabel(R.drawable.ic_r, "排序/筛选")
                        }
                    }
                }
            }
        }
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp)
        )
    }
}

/**
 * 通用底部状态栏
 */
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    @StatusBarType vararg types: Int,
    onBClick: (() -> Unit)? = null,
    onAClick: (() -> Unit)? = null,
    onPlusClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
    ) {
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 10.dp)
        )

        // 左侧区域
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
            painter = painterResource(R.drawable.ic_game_device),
            contentDescription = null,
        )

        // 右侧区域
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 20.dp)
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {

            types.forEach {
                when (it) {
                    StatusBarType.SELECTION -> {
                        StatusIconWithLabel(R.drawable.ic_plus, "选项") {
                            onPlusClick?.invoke()
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    StatusBarType.RETURN -> {
                        StatusIconWithLabel(R.drawable.ic_b, "返回") {
                            onBClick?.invoke()
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    StatusBarType.START -> {
                        StatusIconWithLabel(R.drawable.ic_a, "确定") {
                            onAClick?.invoke()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusIconWithLabel(@DrawableRes resId: Int, label: String, onClick: (() -> Unit)? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
        onClick?.invoke()
    }) {
        Icon(
            painter = painterResource(resId),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@IntDef
annotation class StatusBarType {
    companion object {
        const val SELECTION = 0
        const val START = 1
        const val RETURN = 2
        const val GROUP = 3
        const val ORDER = 4
    }
}

@Preview
@Composable
fun StatusBarPreview() {
    Column {
        StatusBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            title = "当前标题",
            titleIcon = R.drawable.ic_all_app,
            showRightIcon = true,
        )

        BottomBar(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            types = intArrayOf(StatusBarType.SELECTION, StatusBarType.RETURN, StatusBarType.START)
        )
    }
}
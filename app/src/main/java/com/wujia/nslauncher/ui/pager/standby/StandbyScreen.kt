package com.wujia.nslauncher.ui.pager.standby

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.wujia.nslauncher.R
import com.wujia.nslauncher.ui.view.DotIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StandbyScreen(navHostController: NavHostController? = null) {
    var isLeftPanelExpanded by remember { mutableStateOf(true) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // 左侧面板动画
    val animatedWidth by animateDpAsState(
        targetValue = if (isLeftPanelExpanded) screenWidth / 3 else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "widthAnimation"
    )

    Row(modifier = Modifier.fillMaxSize()) {
        // 左侧广告位
        AdsPager(
            modifier = Modifier
                .width(animatedWidth)
                .background(Color.Black)
                .fillMaxHeight()
        )

        // 右侧内容区域
        StandbyPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White),
            navHostController = navHostController,
            isExpanded = isLeftPanelExpanded,
            onClose = { isLeftPanelExpanded = false },
            onBack = { isLeftPanelExpanded = true }
        )
    }
}

@Composable
private fun AdsPager(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        // 广告内容实现
        Text("广告位", color = Color.White, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun StandbyPager(
    modifier: Modifier = Modifier,
    navHostController: NavHostController?,
    isExpanded: Boolean = true,
    onClose: () -> Unit,
    onBack: () -> Unit
) {
    var clickCount by remember { mutableIntStateOf(0) }
    var lastClickTime by remember { mutableLongStateOf(0L) }
    // 用于管理超时任务
    var timeoutJob by remember { mutableStateOf<Job?>(null) }

    // 重置状态函数
    fun resetState() {
        clickCount = 0
        lastClickTime = 0L
    }

    // 处理点击逻辑
    fun handleClick() {
        onClose()
        val now = System.currentTimeMillis()
        // 取消之前的超时任务
        timeoutJob?.cancel()
        // 更新点击状态
        if (now - lastClickTime > 2000) {
            clickCount = 1
        } else {
            clickCount++
        }
        lastClickTime = now
        // 启动新的超时任务
        timeoutJob = CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (System.currentTimeMillis() - lastClickTime >= 2000) {
                resetState()
            }
        }
        // 满足三连击条件
        if (clickCount >= 3) {
//            navHostController?.navigate(NsScreen.Home.name)
            resetState()
        }
    }


    Box(
        modifier = modifier
            .clickable {
                handleClick()
            }
    ) {
        // 其他内容保持不变...
        Image(
            painter = painterResource(id = R.drawable.ic_all_app),
            contentDescription = "应用图标",
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp)
        )

        Text(
            text = "连续点击三次解锁",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 46.dp)
        )

        DotIndicator(
            count = 3,
            selected = clickCount,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-22).dp)
        )

        if (!isExpanded) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(y = (-16).dp, x = 10.dp)
                    .clickable {
                        onBack()
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_b),
                    contentDescription = "返回",
                    modifier = Modifier.size(24.dp)
                )
                Text("返回", fontSize = 12.sp)
            }
        }
    }
}
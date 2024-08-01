package com.bbluecoder.flightgame.ui.component

import android.util.Log
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bbluecoder.flightgame.data.gamelogic.CollisionLogic
import com.bbluecoder.flightgame.ui.viewmodels.MissilesVM
import com.bbluecoder.flightgame.ui.viewmodels.PlanePositionVM

@Composable
fun Game(modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current

    val density = LocalDensity.current

    val screenWidth = with(density) { configuration.screenWidthDp.dp.roundToPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.roundToPx() }

    val planePositionVM: PlanePositionVM =
        viewModel(factory = PlanePositionVM.PlanePositionVMFactory(screenWidth,screenHeight))

    val missilesVM: MissilesVM = viewModel(factory = MissilesVM.MissilesVMFactory(screenWidth,screenHeight))

    val planePosition by planePositionVM.planePositionState.collectAsState()
    val missilesState by missilesVM.missilesStates.collectAsState()

    Log.d("PlanePositionState","$planePosition")

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = {
                    println("drag")
                    println(it.x)
                },
                onDrag = { change, dragAmount ->
                    println("change = ${change.position.x}")
                    planePositionVM.updatePosition(change.position.x)
                }
            )
        }) {
        Box(modifier = modifier
            .height(25.dp)
            .width(25.dp)
            .offset {
                IntOffset(x = planePosition.plane.x.toInt(), y = planePosition.plane.y.toInt())
            }
            .background(Color.Green))

        Log.d("MissilesState","$missilesState")
        for (missile in missilesState.missiles) {
            if(missile == null)
                continue

            if(CollisionLogic.isCollided(planePosition.plane,missile)) {
                Box(modifier = modifier.align(Alignment.Center)) {
                    Text(text = "Game Over")
                }
                missilesVM.stop()
                break
            }
            val offset by animateIntOffsetAsState(targetValue = IntOffset(x = missile.x.toInt(), y = missile.y.toInt()),
                label = "offset"
            )
            Box(modifier = modifier
                .offset {
                    offset
                }
                .height(10.dp)
                .width(2.dp)
                .background(Color.Red))
        }

    }
}
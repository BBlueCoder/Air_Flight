package com.bbluecoder.flightgame.ui.viewmodels

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bbluecoder.flightgame.data.gamelogic.Plane
import com.bbluecoder.flightgame.data.gamelogic.PlaneLogic
import com.bbluecoder.flightgame.data.gamelogic.PlaneShapeDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class PlanePositionUIState(
    val plane: Plane
)

class PlanePositionVM(private val screenWidth: Int, private val screenHeight: Int) : ViewModel() {

    private val _planePositionState = MutableStateFlow(PlanePositionUIState(Plane(x = 0f, y = 0f)))
    val planePositionState : StateFlow<PlanePositionUIState> = _planePositionState

    private lateinit var planeLogic: PlaneLogic

    init {
        start()
    }

    fun start() {
        planeLogic = PlaneLogic(screenWidth, screenHeight)

        _planePositionState.update {
            PlanePositionUIState(planeLogic.updatePosition(null))
        }
    }

    fun updatePosition(x: Float) {
        _planePositionState.update {
            PlanePositionUIState(planeLogic.updatePosition(x))
        }
    }

    class PlanePositionVMFactory(private val screenWidth: Int, private val screenHeight: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlanePositionVM(screenWidth, screenHeight) as T
        }
    }
}


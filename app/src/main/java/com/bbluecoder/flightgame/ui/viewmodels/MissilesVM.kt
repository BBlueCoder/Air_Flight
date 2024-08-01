package com.bbluecoder.flightgame.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bbluecoder.flightgame.data.gamelogic.Missile
import com.bbluecoder.flightgame.data.gamelogic.MissileLogic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MissileUIState(
    val missiles : List<Missile?>
)

class MissilesVM(private val screenWidth : Int,private val screenHeight : Int) : ViewModel() {

    private var _missilesStates = MutableStateFlow(MissileUIState(emptyList()))
    val missilesStates : StateFlow<MissileUIState> = _missilesStates

    private lateinit var missileLogic: MissileLogic

    init {
        start()
    }

    fun start() {
        viewModelScope.launch(Dispatchers.Default) {
            missileLogic = MissileLogic(screenWidth,screenHeight)
            missileLogic.start().collect {
                Log.d("MissilesVM","collected value = $it")
                _missilesStates.value = MissileUIState(it.toList())
                Log.d("MissilesVMValue","${_missilesStates.value}")
            }
        }
    }

    fun stop() {
        missileLogic.stop()
    }

    class MissilesVMFactory(private val screenWidth: Int,private val screenHeight : Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MissilesVM(screenWidth,screenHeight) as T
        }
    }
}
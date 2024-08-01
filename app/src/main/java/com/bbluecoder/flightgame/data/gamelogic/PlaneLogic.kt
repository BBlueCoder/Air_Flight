package com.bbluecoder.flightgame.data.gamelogic

import android.util.Log
import kotlinx.coroutines.flow.flow

data class Plane(
    var x: Float,
    var y: Float,
    var shapeDetails: PlaneShapeDetails = PlaneShapeDetails(
        PlaneLogic.DEFAULT_PLANE_HEIGHT,
        PlaneLogic.DEFAULT_PLANE_WIDTH
    )
)

data class PlaneShapeDetails(
    var height: Int,
    var width: Int
)

class PlaneLogic(private val screenWidth: Int, private var screenHeight: Int) {

    companion object {
        const val DEFAULT_PLANE_WIDTH = 25
        const val DEFAULT_PLANE_HEIGHT = 25
        const val DEFAULT_PLANE_VERTICAL_POSITION = -300
    }

    private var plane = Plane(
        x = 0f, y = 0f
    )

    init {
        setInitialPosition()
    }

    private fun setInitialPosition() {

        // Calculate x coordinate (it should start in the middle of the screen)
        val x = (screenWidth.toFloat() - plane.shapeDetails.width) / 2

        val y = screenHeight + DEFAULT_PLANE_VERTICAL_POSITION

        Log.d("ScreenHeight","$screenHeight")
        Log.d("ScreenHeightY","$y")

        plane = plane.copy(x = x, y = y.toFloat())
    }

    fun updatePosition(newX: Float?): Plane {
        if (newX == null)
            return plane
        return plane.copy(x = newX)
    }

}
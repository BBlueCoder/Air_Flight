package com.bbluecoder.flightgame.data.gamelogic

import android.util.Log
import com.bbluecoder.flightgame.ui.viewmodels.MissileUIState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class Missile(
    val id: Int,
    val x: Float,
    val y: Float,
    val speed: Float,
    val acceleration: Float,
    val shapeDetails: MissileShapeDetails = MissileShapeDetails(height = 10, width = 2)
)

data class MissileShapeDetails(
    val height : Int,
    val width : Int
)

class MissileLogic(private val screenWidth: Int, private val screenHeight: Int) {

    companion object {
        const val INITIAL_SPEED = 21f
        const val INITIAL_ACCELERATION = 1f
        const val INITIAL_SPAWN_TIME = 1.3
        const val INITIAL_SPAWN_NUMBER = 4
    }

    private var spawnTime = INITIAL_SPAWN_TIME
    private var spawnNumber = INITIAL_SPAWN_NUMBER

    private var missiles: MutableList<Missile?> = mutableListOf()

    private var isGameRunning = false

    private var id = 1

    private var previousTime = 0
    fun start() = flow {
        isGameRunning = true
        var isCalled = false
        while (isGameRunning) {
            delay(50)
            val currentTime = System.currentTimeMillis() / 1000
            if (currentTime - previousTime > spawnTime) {
                spawnMissile()

                previousTime = currentTime.toInt()
            }

            for (i in 0..<missiles.size) {
                missiles[i] = updatePosition(missiles[i])
            }

            emit(missiles)
        }
    }

    fun stop() {
        isGameRunning = false
    }

    private fun spawnMissile() {
        var randomPosY = 100
        for (i in 0..<spawnNumber) {
            val randomPos = Random.nextInt(screenWidth)
            missiles.add(
                Missile(
                    id = id++,
                    x = randomPos.toFloat(),
                    y = 0f - randomPosY,
                    speed = INITIAL_SPEED,
                    acceleration = INITIAL_ACCELERATION
                )
            )
            randomPosY += 400
        }
    }

    private fun updatePosition(missile: Missile?): Missile? {
        if (missile == null)
            return null
        val newPos = missile.y + missile.speed * 1f + 0.5f * missile.acceleration * 1f

        if (newPos - 15 > screenHeight)
            return null
        Log.d("updatePosition(${missile.id})", "old pos = ${missile.y} new pos = $newPos")
        return missile.copy(y = newPos)
    }
}
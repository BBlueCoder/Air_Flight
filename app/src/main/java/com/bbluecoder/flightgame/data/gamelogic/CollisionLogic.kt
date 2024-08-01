package com.bbluecoder.flightgame.data.gamelogic

import android.util.Log

object CollisionLogic {

    fun isCollided(plane : Plane, missile: Missile): Boolean {
        val planeTop = plane.y
        val planeBottom = plane.y + plane.shapeDetails.height
        val planeLeft = plane.x
        val planeRight = plane.x + plane.shapeDetails.width

        val missileTop = missile.y
        val missileBottom = missile.y + plane.shapeDetails.height
        val missileLeft = missile.x
        val missileRight = missile.x + missile.shapeDetails.width

        Log.d("Collison","pt = $planeTop mb = $missileBottom pb = $planeBottom mt = $missileTop pl = $planeLeft mr = $missileRight pr $planeRight ml $missileLeft")

        return planeRight > missileLeft &&
                planeLeft < missileRight &&
                planeBottom > missileTop &&
                planeTop < missileBottom
    }
}
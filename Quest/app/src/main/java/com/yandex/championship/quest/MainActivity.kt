package com.yandex.championship.quest

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val animDurationSleep = 2

    val text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        val rootView = findViewById<RelativeLayout>(R.id.main_layout)

        var wasRectangle = false
        var wasCircle = false

        val blackColor = getColor(android.R.color.black)
        val yellowColor = getColor(android.R.color.holo_orange_dark)
        val redColor = getColor(android.R.color.holo_red_dark)

        text.lines().forEachIndexed { index, it ->
            if (it.contains("rectangle")) {
                wasRectangle = true
                wasCircle = false

                var temp2 = it.removePrefix("rectangle ")

                val centerX = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val centerY = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val width = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val height = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val angle = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val color = when (temp2) {
                    "yellow" -> yellowColor
                    "red" -> redColor
                    else -> blackColor
                }

                rectangles.add(Rectangle(centerX, centerY, width, height, angle, color, null, null, null))
            } else if (it.contains("circle")) {
                wasRectangle = false
                wasCircle = true

                var temp2 = it.removePrefix("circle ")

                val centerX = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val centerY = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val radius = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val color = when (temp2) {
                    "yellow" -> yellowColor
                    "red" -> redColor
                    else -> blackColor
                }

                circles.add(Circle(centerX, centerY, radius, color, null, null, null))
            } else if (it.contains("move")) {
                var temp2 = it.removePrefix("move ")

                val toX = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                val toY = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                var cycle: Boolean
                val timeMillis = try {
                    cycle = true
                    temp2.substring(0, temp2.indexOf(' ')).toFloat()
                } catch (e: Exception) {
                    cycle = false
                    temp2.toFloat()
                }

                val move = MoveAnim(toX, toY, timeMillis, cycle)
                if (wasRectangle) {
                    rectangles.last().moveAnim = move
                } else if (wasCircle) {
                    circles.last().moveAnim = move
                }
            } else if (it.contains("rotate")) {
                var temp2 = it.removePrefix("rotate ")

                val toAngle = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                var cycle: Boolean
                val timeMillis = try {
                    cycle = true
                    temp2.substring(0, temp2.indexOf(' ')).toFloat()
                } catch (e: Exception) {
                    cycle = false
                    temp2.toFloat()
                }

                val rotation = RotateAnim(toAngle, timeMillis, cycle)
                if (wasRectangle) {
                    rectangles.last().rotateAnim = rotation
                } else if (wasCircle) {
                    circles.last().rotateAnim = rotation
                }
            } else if (it.contains("scale")) {
                var temp2 = it.removePrefix("scale ")

                val toScale = temp2.substring(0, temp2.indexOf(' ')).toFloat()
                temp2 = temp2.removeRange(0, temp2.indexOf(' ') + 1)

                var cycle: Boolean
                val timeMillis = try {
                    cycle = true
                    temp2.substring(0, temp2.indexOf(' ')).toFloat()
                } catch (e: Exception) {
                    cycle = false
                    temp2.toFloat()
                }

                val scale = ScaleAnim(toScale, timeMillis, cycle)
                if (wasRectangle) {
                    rectangles.last().scaleAnim = scale
                } else if (wasCircle) {
                    circles.last().scaleAnim = scale
                }
            }
        }

        draw2(rootView)
    }

    private fun draw2(rootView: RelativeLayout) {
        rectangles.forEach {
            val view = createRectangle(it)
            rootView.addView(view)

            view.allAnim(it.moveAnim, it.rotateAnim, it.scaleAnim)
        }
    }

    private val rectangles = mutableListOf<Rectangle>()
    private val circles = mutableListOf<Circle>()

    data class Rectangle(
            val centerX: Float,
            val centerY: Float,
            val width: Float,
            val height: Float,
            val angle: Float,
            val color: Int,
            var moveAnim: MoveAnim?,
            var rotateAnim: RotateAnim?,
            var scaleAnim: ScaleAnim?
    )

    data class Circle(
            val centerX: Float,
            val centerY: Float,
            val radius: Float,
            val color: Int,
            var moveAnim: MoveAnim?,
            var rotateAnim: RotateAnim?,
            var scaleAnim: ScaleAnim?
    )

    data class MoveAnim(
            val toX: Float,
            val toY: Float,
            val timeMillis: Float,
            val cycle: Boolean
    )

    data class RotateAnim(
            val toAngle: Float,
            val timeMillis: Float,
            val cycle: Boolean
    )

    data class ScaleAnim(
            val toScale: Float,
            val timeMillis: Float,
            val cycle: Boolean
    )

    private fun createRectangle(rectangle: Rectangle): View {
        val view = View(this)
        view.setBackgroundColor(rectangle.color)
        val params = RelativeLayout.LayoutParams(rectangle.width.toInt(), rectangle.height.toInt())
        params.leftMargin = rectangle.centerX.toInt() - rectangle.width.toInt() / 2
        params.topMargin = rectangle.centerY.toInt() - rectangle.height.toInt() / 2

        view.rotation = rectangle.angle

        view.layoutParams = params
        return view
    }

    private fun View.allAnim(moveAnim: MoveAnim?, rotateAnim: RotateAnim?, scaleAnim: ScaleAnim?) {
        Handler().postDelayed({
            val animList = mutableListOf<Animator>()

            val animSet = AnimatorSet()

            moveAnim?.let {
                val moveAnimatorX = ObjectAnimator.ofFloat(this, "x", it.toX - layoutParams.width / 2)
                moveAnimatorX.duration = (it.timeMillis * animDurationSleep).toLong()

                val moveAnimatorY = ObjectAnimator.ofFloat(this, "y", it.toY - layoutParams.height / 2)
                moveAnimatorY.duration = (it.timeMillis * animDurationSleep).toLong()

                if(it.cycle) {
                    moveAnimatorX.repeatMode = ValueAnimator.REVERSE
                    moveAnimatorY.repeatMode = ValueAnimator.REVERSE

                    moveAnimatorX.repeatCount = ValueAnimator.INFINITE
                    moveAnimatorY.repeatCount = ValueAnimator.INFINITE
                }

                animList.add(moveAnimatorX)
                animList.add(moveAnimatorY)
            }

            rotateAnim?.let {
                val rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", this.rotation + it.toAngle)
                rotateAnimator.duration = (it.timeMillis * animDurationSleep).toLong()

                if(it.cycle) {
                    rotateAnimator.repeatMode = ValueAnimator.REVERSE
                    rotateAnimator.repeatCount = ValueAnimator.INFINITE
                }

                animList.add(rotateAnimator)
            }

            scaleAnim?.let {
                val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", it.toScale)
                scaleXAnimator.duration = (it.timeMillis * animDurationSleep).toLong()

                val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", it.toScale)
                scaleYAnimator.duration = (it.timeMillis * animDurationSleep).toLong()

                if(it.cycle) {
                    scaleXAnimator.repeatMode = ValueAnimator.REVERSE
                    scaleYAnimator.repeatMode = ValueAnimator.REVERSE

                    scaleXAnimator.repeatCount = ValueAnimator.INFINITE
                    scaleYAnimator.repeatCount = ValueAnimator.INFINITE
                }

                animList.add(scaleXAnimator)
                animList.add(scaleYAnimator)
            }

            animSet.playTogether(animList)
            animSet.start()
        }, 200)
    }
}
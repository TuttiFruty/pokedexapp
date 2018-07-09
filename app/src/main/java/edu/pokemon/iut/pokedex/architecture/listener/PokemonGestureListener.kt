package edu.pokemon.iut.pokedex.architecture.listener

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Custom implementation of [GestureDetector.SimpleOnGestureListener] and [View.OnTouchListener]<br></br>
 * This GestureListener allow any view through the setOnTouchListener to receive an event of Fling(Swipe) on it.<br></br>
 * Call is [PokemonGestureListener.Listener] with this values : <br></br>
 * {@value #UP} Swipe from Bottom to Top<br></br>
 * {@value #DOWN} Swipe from Top to Bottom<br></br>
 * {@value #LEFT} Swipe from Right to Left<br></br>
 * {@value #RIGHT} Swipe from Left to Right<br></br>
 */
class PokemonGestureListener
/**
 * Constructor for the [GestureDetector.SimpleOnGestureListener]
 *
 * @param listener        callBack interface [PokemonGestureListener.Listener]
 * @param gestureDetector custom [GestureDetector], if not given a new instance will be create
 * @param context         the context needed for instantiate a new [GestureDetector]
 */
(//The interface that allow the caller to have a callback of the swipe
        private val listener: Listener, gestureDetector: GestureDetector?, context: Context) : GestureDetector.SimpleOnGestureListener(), View.OnTouchListener {

    /**
     * Getter for testing purpose only
     * @return [GestureDetector]
     */
    @get:VisibleForTesting
    val gestureDetector: GestureDetector

    init {
        if (gestureDetector == null) {
            this.gestureDetector = GestureDetector(context, this)
        } else {
            this.gestureDetector = gestureDetector
        }
    }

    /**
     * Override of onTouch to detect the swipe. <br></br>
     * We don't call performClick for accessibility purpose, because the swipe will not be take in account in such case
     *
     * @param view        [View] that's touch
     * @param motionEvent [MotionEvent] that's triggered
     * @return true if we propagate the event, false if not
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(motionEvent)
        return true
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        //We first test if the swipe is short enough to take account of it and in TOP BOTTOM Direction
        if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH) {
            //Then we test if the swipe is fast enough and not with some LEFT RIGHT Direction
            if (Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY || Math.abs(e1.x - e2.x) > SWIPE_MAX_OFF_PATH) {
                return false
            }

            //If the distance is enough we trigger the callBack
            if (e1.y - e2.y > SWIPE_MIN_DISTANCE) {
                listener.onSwipe(DOWN)
            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE) {
                listener.onSwipe(UP)
            }
        } else {
            //Then we test if the swipe is fast enough on the other direction
            if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                return false
            }

            //If the distance is enough we trigger the callBack
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE) {
                listener.onSwipe(RIGHT)
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE) {
                listener.onSwipe(LEFT)
            }
        }
        return true
    }

    /**
     * Inner interface for allow callBack from the listener
     */
    interface Listener {
        /**
         * Called when an onFling(Swipe) is detected.
         * Return one of this values :
         * {@value #UP} Swipe from Bottom to Top<br></br>
         * {@value #DOWN} Swipe from Top to Bottom<br></br>
         * {@value #LEFT} Swipe from Right to Left<br></br>
         * {@value #RIGHT} Swipe from Left to Right<br></br>
         *
         * @param direction an int from the constant.
         */
        fun onSwipe(direction: Int)
    }

    companion object {
        //The for direction detected
        val UP = 1
        val DOWN = 2
        val LEFT = 3
        val RIGHT = 4
        private val TAG = PokemonGestureListener::class.java.simpleName
        //The values of each velocity limit to consider it's a swipe
        private val SWIPE_MAX_OFF_PATH = 250f
        private val SWIPE_THRESHOLD_VELOCITY = 200f
        private val SWIPE_MIN_DISTANCE = 120f
    }
}

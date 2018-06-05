package edu.pokemon.iut.pokedex.architecture.listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class PokemonGestureListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{
    private static final String TAG = PokemonGestureListener.class.getSimpleName();

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    private static final float SWIPE_MAX_OFF_PATH = 250;
    private static final float SWIPE_THRESHOLD_VELOCITY = 200;
    private static final float SWIPE_MIN_DISTANCE = 120;

    private Listener listener;
    private GestureDetector gestureDetector;

    public PokemonGestureListener(Listener listener, GestureDetector gestureDetector, Context context) {
        super();
        this.listener = listener;
        if(gestureDetector == null){
            this.gestureDetector = new GestureDetector(context, this);
        }else{
            this.gestureDetector = gestureDetector;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public interface Listener{

        void onFling (int direction);

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //We first test if the swipe is short enough to take account of it and in TOP BOTTOM Direction
        if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
            //Then we test if the swipe is fast enough and not with some LEFT RIGHT Direction
            if(Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY || Math.abs(e1.getX()- e2.getX()) > SWIPE_MAX_OFF_PATH){
                return false;
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                listener.onFling(DOWN);
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                listener.onFling(UP);
            }
        } else {
            //Then we test if the swipe is fast enough on the other direction
            if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                return false;
            }

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                listener.onFling(RIGHT);
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
               listener.onFling(LEFT);
            }
        }

        return true;
    }
}

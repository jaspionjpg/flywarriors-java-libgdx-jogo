package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;


public class TouchpadLayout extends Touchpad {

    public TouchpadLayout(TouchpadStyle touchpadStyle, float widthWorld, float heightWorld) {
        super(10, touchpadStyle);
        System.out.println(heightWorld);
        this.setBounds(100, 100, heightWorld*0.15f, heightWorld*0.15f);
    }

}

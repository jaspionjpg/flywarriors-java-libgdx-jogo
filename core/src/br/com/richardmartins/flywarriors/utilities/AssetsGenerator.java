package br.com.richardmartins.flywarriors.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class AssetsGenerator {

    private AssetManager manager;

    public AssetsGenerator(AssetManager manager){
        this.manager = manager;
    }

    public Touchpad.TouchpadStyle getTouchpadStyle(){
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", manager.get("joystick/touchbackground.png", Texture.class));
        touchpadSkin.add("touchKnob", manager.get("joystick/touchknob.png",Texture.class));
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
        touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
        return touchpadStyle;
    }

}

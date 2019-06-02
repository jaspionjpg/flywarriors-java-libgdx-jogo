package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import br.com.richardmartins.flywarriors.utilities.Constants;

public class Tiro extends Sprite {
    private AssetManager manager;

    private float balaSpeed;
    private float knobPercentX;
    private float knobPercentY;

    public Tiro(AssetManager manager, Aviao aviao) {
        super(manager.get("aviao/bala.png", Texture.class));
        this.manager = manager;

        this.setPosition(aviao.getX() + aviao.getOriginX(), aviao.getY() + aviao.getOriginY());
        setOrigin(12, 4);
        this.flip(aviao.isFlipX(), false);
        this.setSize(25, 8);
        this.setRotation(aviao.getRotation());

        this.balaSpeed = 2000;
        this.knobPercentX = aviao.knobPercentX != 0 ? aviao.knobPercentX : 0;
        this.knobPercentY = aviao.isFlipX() ? aviao.angulo / 90 : aviao.angulo * -1 / 90;
    }

    public boolean update(float dt) {
        this.setX(this.getX() + (knobPercentX * this.balaSpeed * dt));
        this.setY(this.getY() + (knobPercentY * this.balaSpeed * dt));

        if (this.getX() > Constants.MAP_WIDTH - this.getWidth() || this.getX() < 0 || this.getY() > Constants.MAP_HEIGHT - this.getHeight() || this.getY() < 0) {
            return true;
        } else {
            return false;
        }
    }
}
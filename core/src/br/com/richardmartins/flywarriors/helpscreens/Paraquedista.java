package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Paraquedista extends Sprite {
    private AssetManager manager;
    private Viewport viewport;

    public Boolean ejetado;
    public Boolean morreu;

    public Animation<TextureRegion> ejetarAnimation;
    public Animation<TextureRegion> paraquedasParaquedistaAnimation;
    public Animation<TextureRegion> paraquedasAnimation;
    public Animation<TextureRegion> caindoAnimation;
    public float stateTime;

    Integer ESTADO_PARAQUEDISTA; // 1 - EJEÇAO, 2 - CAINDO DE PARAQUEDAS, 3 - CAINDO, 4 - MORRENDO, 5 - MORTO

    public Paraquedista(AssetManager manager, Viewport viewport){
        this.manager = manager;
        this.viewport = viewport;

        this.flip(false, false);
        this.setSize(50, 80);
        ESTADO_PARAQUEDISTA = 1;
        morreu = false;
        ejetado = false;
        criaAnimacoes();
    }

    public void morrer() {
        morreu = true;
        ESTADO_PARAQUEDISTA = 5;
    }

    public void ejetar(float x, float y){
        this.setPosition(x, y);
        ejetado = true;
    }

    public void update(float dt) {
        if(ejetado) {
            switch (ESTADO_PARAQUEDISTA) {
                case 1:
                    setPosition(getX(), getY() + 200 * dt);
                    stateTime += dt;
                    setRegion(ejetarAnimation.getKeyFrame(stateTime, false));
                    if (stateTime > 4.2) {
                        stateTime = 0;
                        ESTADO_PARAQUEDISTA = 2;
                        this.setSize(56, 80);
                    }
                    break;
                case 2:
                    setPosition(getX(), getY() - 100 * dt);
                    stateTime += dt;
                    setRegion(paraquedasAnimation.getKeyFrame(stateTime, true));
                    if (getY() < viewport.getWorldHeight() * 0.15f + 100 * 3.9) {
                        ESTADO_PARAQUEDISTA = 4;
                        this.setSize(49, 40);
                        stateTime = 0;
                    }
                    break;

                case 4:
                    if (stateTime < 3.9) {
                        setPosition(getX(), getY() - 100 * dt);
                    }
                    stateTime += dt;
                    setRegion(caindoAnimation.getKeyFrame(stateTime, false));
                    if(caindoAnimation.isAnimationFinished(stateTime)){
                        morrer();
                    }
                    break;
            }
        }
    }

    private void criaAnimacoes() {
        Texture caindo = manager.get("aviao/caindo.png", Texture.class);
        Texture ejetando = manager.get("aviao/ejetando.png", Texture.class);
        Texture paraquedas = manager.get("aviao/paraquedas2.png", Texture.class);

        TextureRegion[][] tmp = TextureRegion.split(ejetando, ejetando.getWidth() / 3, ejetando.getHeight());
        TextureRegion[] frames = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            frames[i] = tmp[0][i];
        }
        ejetarAnimation = new Animation<TextureRegion>(1.4f, frames);

        tmp = TextureRegion.split(paraquedas,paraquedas.getWidth() / 9 , paraquedas.getHeight());
        frames = new TextureRegion[9];
        for (int i = 0; i < 9; i++) {
            frames[i] = tmp[0][i];
        }
        System.out.println(paraquedas.getWidth() / 9);
        paraquedasAnimation = new Animation<TextureRegion>(0.4f, frames);

        List<Rectangle> list = new ArrayList<Rectangle>();
        list.add(new Rectangle(0, 0, 27, 40));
        list.add(new Rectangle(27, 0, 31, 40));
        list.add(new Rectangle(58, 0, 32, 40));
        list.add(new Rectangle(90, 0, 42, 40));
        list.add(new Rectangle(132, 0, 47, 40));
        list.add(new Rectangle(179, 0, 48, 40));
        list.add(new Rectangle(227, 0, 47, 40));
        list.add(new Rectangle(274, 0, 48, 40));
        list.add(new Rectangle(322, 0, 42, 40));
        list.add(new Rectangle(364, 0, 32, 40));
        list.add(new Rectangle(396, 0, 31, 40));
        list.add(new Rectangle(427, 0, 38, 40));
        list.add(new Rectangle(465, 0, 48, 40));
        list.add(new Rectangle(513, 0, 45, 40));
        list.add(new Rectangle(558, 0, 47, 40));
        list.add(new Rectangle(605, 0, 44, 40));
        list.add(new Rectangle(649, 0, 44, 40));
        list.add(new Rectangle(693, 0, 49, 40));
        list.add(new Rectangle(742, 0, 49, 40));

        frames = new TextureRegion[19];
        for (int i = 0; i < 19; i++) {
            frames[i] = new TextureRegion(caindo, list.get(i).x, list.get(i).y, list.get(i).width, list.get(i).height);
        }

        caindoAnimation = new Animation<TextureRegion>(0.3f, frames);

        stateTime = 0f;
    }
}

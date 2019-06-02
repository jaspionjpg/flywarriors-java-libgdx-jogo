package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.text.View;

import br.com.richardmartins.flywarriors.utilities.Constants;

public class Player {
    private Viewport viewport;

    public Boolean isEjetado;
    public Aviao aviao;
    public Paraquedista paraquedista;

    public Player(AssetManager manager, Viewport viewport, TouchpadLayout touchpad) {
        paraquedista = new Paraquedista(manager, viewport);
        this.isEjetado = false;

        this.viewport = viewport;
        aviao = new Aviao(manager, viewport, touchpad, this);
    }

    public Player(AssetManager manager, Viewport viewport) {
        this.isEjetado = false;
        this.viewport = viewport;
        aviao = new Aviao(manager, viewport);
    }

    public void draw(SpriteBatch ambiente) {
        if(isEjetado){
            paraquedista.draw(ambiente);
        }
        aviao.draw(ambiente);
    }

    public void update(float dt) {
        aviao.update(dt);
        paraquedista.update(dt);
    }

    public void ejetar(){
        isEjetado = true;
        paraquedista.ejetar(aviao.getX(), aviao.getY());
    }

    public void checkarDano(Rectangle rectangle, float dano) {
        if(isEjetado){
            if(paraquedista.getBoundingRectangle().contains(rectangle)){
            }
        }
        if(!aviao.explodido){
            if(aviao.getBoundingRectangle().contains(rectangle)){
                aviao.tomarDano(dano);
            }
        }
    }

    public float[] getPositionsGameCamPerspective() {
        float[] posicoes = new float[2];
        float x = isEjetado ? paraquedista.getX() : aviao.getX();
        float y = isEjetado ? paraquedista.getY() : aviao.getY();
        float width = isEjetado ? paraquedista.getWidth() : aviao.getWidth();
        float height = isEjetado ? paraquedista.getHeight() : aviao.getHeight();

        //MOVIMENTO DA CAMERA USO EM TODOS OS CASOS
        if (x + width / 2 >= viewport.getWorldWidth() / 2 &&
                x + width / 2 <= Constants.MAP_WIDTH - viewport.getWorldWidth() / 2) {
            posicoes[0] = x + width / 2;
        } else if (x + width / 2 < viewport.getWorldWidth() / 2) {
            posicoes[0] = viewport.getWorldWidth() / 2;
        } else {
            posicoes[0] = Constants.MAP_WIDTH - viewport.getWorldWidth() / 2;
        }
        if (y + height / 2 >= viewport.getWorldHeight() / 2 &&
                y + height / 2 <= Constants.MAP_HEIGHT - viewport.getWorldHeight() / 2) {
            posicoes[1] = y + height / 2;
        } else if (y + height / 2 < viewport.getWorldHeight() / 2) {
            posicoes[1] = viewport.getWorldHeight() / 2;
        } else {
            posicoes[1] = Constants.MAP_HEIGHT - viewport.getWorldHeight() / 2;
        }

        return posicoes;
    }

    public void dispose() {
        aviao.dispose();
    }
}

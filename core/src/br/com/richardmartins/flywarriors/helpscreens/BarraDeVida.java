package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

import br.com.richardmartins.flywarriors.screens.GameScreenJoystick;

public class BarraDeVida {

    private Aviao aviao;
    private ShapeRenderer shapeRenderer;

    private Viewport viewport;

    public BarraDeVida(Aviao aviao, Viewport viewport) {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.aviao = aviao;
        this.viewport = viewport;
    }

    public void draw() {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin();

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(aviao.getX(), aviao.getY()+aviao.getHeight(), aviao.getWidth(), 20);

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(aviao.getX(), aviao.getY()+aviao.getHeight(), aviao.getWidth() * aviao.vida / aviao.vidaTotal, 20);

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

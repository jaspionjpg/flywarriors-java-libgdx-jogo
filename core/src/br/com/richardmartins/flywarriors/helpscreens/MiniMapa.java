package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class MiniMapa {

    private ShapeRenderer shapeRenderer;
    private Rectangle miniMapa;
    private Player player;
    private List<Tiro> tiros;

    public MiniMapa(Player player, List<Tiro> tiros) {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        miniMapa = new Rectangle((float) (Gdx.graphics.getWidth() * 0.01), (float) (Gdx.graphics.getHeight() * 0.99) - 200, 300, 200);
        this.player = player;
        this.tiros = tiros;
    }

    public void draw() {
        shapeRenderer.begin();

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(miniMapa.getX(), miniMapa.getY(), miniMapa.width, miniMapa.height);

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(miniMapa.getX() + player.aviao.getX() / 10, miniMapa.getY() + player.aviao.getY() / 10, 5, 5);

        for(int i = 0; i < tiros.size(); i++){
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(miniMapa.getX() + tiros.get(i).getX() / 10, miniMapa.getY() + tiros.get(i).getY() / 10, 1, 1);
        }
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

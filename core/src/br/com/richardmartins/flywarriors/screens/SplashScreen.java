package br.com.richardmartins.flywarriors.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

    private Game game;
    private AssetManager manager;

    private SpriteBatch batch;

    private Texture barra;

    private float time = 0;

    public SplashScreen(Game game, AssetManager manager){
        this.game = game;
        this.manager = manager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        barra = new Texture("bar.png");
    }

    @Override
    public void render(float delta) {
        time += delta;

        if(manager.update() && time >= 2){
            this.game.setScreen(new GameScreenJoystick(game, manager));
        }

        Gdx.gl.glClearColor(6/256f, 123/256f, 141/256f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(barra, 0.1f*Gdx.graphics.getWidth(), 0.15f*Gdx.graphics.getHeight(),
                    0.8f*Gdx.graphics.getWidth()*Math.min(manager.getProgress(), time/1f), 0.1f*Gdx.graphics.getHeight());

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        barra.dispose();
    }
}

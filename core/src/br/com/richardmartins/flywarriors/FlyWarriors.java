package br.com.richardmartins.flywarriors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.richardmartins.flywarriors.screens.SplashScreen;

public class FlyWarriors extends Game {

	private AssetManager manager;
	
	@Override
	public void create () {
		manager = new AssetManager();

		manager.load("game/ground.png", Texture.class);
		manager.load("game/paisagem.jpg", Texture.class);
		manager.load("aviao/aviao1.png", Texture.class);
		manager.load("aviao/bala.png", Texture.class);
		manager.load("aviao/explosao.png", Texture.class);
		manager.load("aviao/aviao1explodido.png", Texture.class);

		manager.load("aviao/caindo.png", Texture.class);
		manager.load("aviao/caraparaquedas.png", Texture.class);
		manager.load("aviao/ejetando.png", Texture.class);
		manager.load("aviao/paraquedas2.png", Texture.class);


		manager.load("joystick/block.png", Texture.class);
		manager.load("joystick/touchbackground.png", Texture.class);
		manager.load("joystick/touchknob.png", Texture.class);


		this.setScreen(new SplashScreen(this, manager));
	}

	@Override
	public void dispose () {
		manager.dispose();
	}
}

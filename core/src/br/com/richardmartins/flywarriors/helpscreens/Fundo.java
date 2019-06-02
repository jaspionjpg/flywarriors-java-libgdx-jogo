package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.com.richardmartins.flywarriors.utilities.Constants;

public class Fundo {

    private AssetManager manager;
    private Viewport viewport;

    private Texture fundo;
    private Texture chao;

    public Rectangle chaoBox;

    public Fundo(AssetManager manager, Viewport viewport) {
        this.manager = manager;
        this.viewport = viewport;
        fundo = manager.get("game/paisagem.jpg", Texture.class);
        chao = manager.get("game/ground.png", Texture.class);
        chaoBox = new Rectangle(0, 0, 3000, viewport.getWorldHeight() * 0.15f);
    }

    public void draw(SpriteBatch ambiente) {
        chaoBox.set(0, 0, Constants.MAP_WIDTH, viewport.getWorldHeight() * 0.15f);
        ambiente.draw(fundo, 0, 0, Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
        ambiente.draw(chao, chaoBox.x, chaoBox.y, chaoBox.width, chaoBox.height);
    }

    public Boolean contains(float x, float y) {
        return chaoBox.contains(x, y);
    }

    public void dispose() {
        fundo.dispose();
        chao.dispose();
    }
}

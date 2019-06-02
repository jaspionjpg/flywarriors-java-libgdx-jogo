package br.com.richardmartins.flywarriors.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

import br.com.richardmartins.flywarriors.helpscreens.Aviao;
import br.com.richardmartins.flywarriors.helpscreens.Fundo;
import br.com.richardmartins.flywarriors.helpscreens.MiniMapa;
import br.com.richardmartins.flywarriors.helpscreens.Player;
import br.com.richardmartins.flywarriors.helpscreens.Tiro;
import br.com.richardmartins.flywarriors.helpscreens.TouchpadLayout;
import br.com.richardmartins.flywarriors.utilities.AssetsGenerator;
import br.com.richardmartins.flywarriors.utilities.Constants;

public class GameScreenJoystick implements Screen {

    private Game game;
    private AssetManager manager;
    private AssetsGenerator assetsGenerator;

    private OrthographicCamera gamecam;
    private Viewport viewport;
    private SpriteBatch ambiente;

    private Stage stage;

    Player player;
    Fundo fundo;
    MiniMapa miniMapa;
    TouchpadLayout touchpad;

    List<Tiro> tiros;

    Button botaoDeTiro;
    Button botaoDeParaquedas;
    Rectangle areaBotaoDeTiro;
    Rectangle areaBotaoDeParaquedas;

    public GameScreenJoystick(Game game, final AssetManager manager) {
        assetsGenerator = new AssetsGenerator(manager);
        this.game = game;
        this.manager = manager;
        ambiente = new SpriteBatch();

        gamecam = new OrthographicCamera(Constants.APPLICATION_WIDTH, Constants.APPLICATION_HEIGHT);
        viewport = new ScreenViewport(gamecam);
        gamecam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", manager.get("joystick/touchbackground.png", Texture.class));

        botaoDeTiro = new Button(touchpadSkin.getDrawable("touchBackground"));
        botaoDeParaquedas = new Button(touchpadSkin.getDrawable("touchBackground"));
        areaBotaoDeTiro = new Rectangle(0, 0, 150, 150);
        areaBotaoDeParaquedas = new Rectangle(0, 0, 100, 100);
        botaoDeTiro.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                tiros.add(new Tiro(manager, player.aviao));
            }
        });
        botaoDeParaquedas.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                botaoDeTiro.setVisible(false);
                botaoDeParaquedas.setVisible(false);
                touchpad.setVisible(false);
                player.ejetar();
            }
        });

        fundo = new Fundo(manager, viewport);
        touchpad = new TouchpadLayout(assetsGenerator.getTouchpadStyle(), gamecam.viewportWidth,gamecam.viewportHeight);

        player = new Player(manager, viewport, touchpad);

        stage = new Stage(viewport, ambiente);
        stage.addActor(touchpad);
        stage.addActor(botaoDeTiro);
        stage.addActor(botaoDeParaquedas);

        Gdx.input.setInputProcessor(stage);

        tiros = new ArrayList<Tiro>();
        miniMapa = new MiniMapa(player, tiros);
    }

    @Override
    public void show() {
    }

    public void update(float dt) {
        float[] posicoes = player.getPositionsGameCamPerspective();
        gamecam.position.x = posicoes[0];
        gamecam.position.y = posicoes[1];

        touchpad.setPosition(gamecam.position.x - viewport.getWorldWidth() / 2 + 35, gamecam.position.y - viewport.getWorldHeight() / 2 + viewport.getScreenY() + 35);
        if(botaoDeParaquedas.isVisible()){
            areaBotaoDeTiro.setPosition(gamecam.position.x + viewport.getWorldWidth() / 2 + viewport.getScreenX() - 200, gamecam.position.y - viewport.getWorldHeight() / 2 + viewport.getScreenY() + 35);
            areaBotaoDeParaquedas.setPosition(gamecam.position.x + viewport.getWorldWidth() / 2 + viewport.getScreenX() - 150, gamecam.position.y + viewport.getWorldHeight() / 2 + viewport.getScreenY() - 135);
            botaoDeTiro.setBounds( areaBotaoDeTiro.x, areaBotaoDeTiro.y, areaBotaoDeTiro.width, areaBotaoDeTiro.height);
            botaoDeParaquedas.setBounds( areaBotaoDeParaquedas.x, areaBotaoDeParaquedas.y, areaBotaoDeParaquedas.width, areaBotaoDeParaquedas.height);
        }
        player.update(dt);

        for(int i = 0; i < tiros.size(); i++){
            player.checkarDano(tiros.get(i).getBoundingRectangle(), 40);

            if(tiros.get(i).update(dt)){
                tiros.remove(i);
            }
        }

        if(fundo.contains(player.aviao.getX(), player.aviao.getY())){
            player.aviao.bateuParede();
        }
        if(Gdx.input.justTouched()){
            if(player.paraquedista.morreu){
                this.game.setScreen(new GameScreenJoystick(game, manager));
            }
            float x = Gdx.input.getX();
            float y = viewport.getWorldHeight() - Gdx.input.getY();
        }

        gamecam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ambiente.setProjectionMatrix(gamecam.combined);

        ambiente.begin();
        fundo.draw(ambiente);
        player.draw(ambiente);

        if(player.aviao.drawExplosao(ambiente, delta)){
            botaoDeTiro.setVisible(false);
            botaoDeParaquedas.setVisible(false);
            touchpad.setVisible(false);
        };

        for(Tiro tiro: tiros){
            tiro.draw(ambiente);
        }

        ambiente.end();

        player.aviao.barraDeVida.draw();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        player.dispose();
        stage.dispose();
        fundo.dispose();
        miniMapa.dispose();
    }
}

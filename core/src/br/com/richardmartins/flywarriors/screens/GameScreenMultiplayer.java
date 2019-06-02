package br.com.richardmartins.flywarriors.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.richardmartins.flywarriors.helpscreens.Fundo;
import br.com.richardmartins.flywarriors.helpscreens.Player;
import br.com.richardmartins.flywarriors.helpscreens.TouchpadLayout;
import br.com.richardmartins.flywarriors.utilities.AssetsGenerator;
import br.com.richardmartins.flywarriors.utilities.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class GameScreenMultiplayer implements Screen {
    private final float UPDATE_TIME = 1/60f;

    private Game game;
    private AssetManager manager;
    private AssetsGenerator assetsGenerator;

    private Stage stage;

    private OrthographicCamera gamecam;
    private Viewport viewport;
    private SpriteBatch ambiente;

    Player player;
    Fundo fundo;
    TouchpadLayout touchpad;

    private Socket socket;
    private String socketID;
    float timerServer;
    HashMap<String, Player> outrosPlayers;

    public GameScreenMultiplayer(Game game, final AssetManager manager) {
        assetsGenerator = new AssetsGenerator(manager);
        this.game = game;
        this.manager = manager;
        ambiente = new SpriteBatch();

        gamecam = new OrthographicCamera(Constants.APPLICATION_WIDTH, Constants.APPLICATION_HEIGHT);
        viewport = new ScreenViewport(gamecam);
        gamecam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", manager.get("joystick/touchbackground.png", Texture.class));

        fundo = new Fundo(manager, viewport);
        touchpad = new TouchpadLayout(assetsGenerator.getTouchpadStyle(), gamecam.viewportWidth,gamecam.viewportHeight);

        player = new Player(manager, viewport, touchpad);

        stage = new Stage(viewport, ambiente);
        stage.addActor(touchpad);

        Gdx.input.setInputProcessor(stage);
        outrosPlayers = new HashMap<String, Player>();
        connectSocket();
        configSocketEvents();
    }

    public GameScreenMultiplayer(Game game, final AssetManager manager, Socket socket, String socketID, HashMap<String, Player> outrosPlayers) {
        assetsGenerator = new AssetsGenerator(manager);
        this.game = game;
        this.manager = manager;
        ambiente = new SpriteBatch();

        gamecam = new OrthographicCamera(Constants.APPLICATION_WIDTH, Constants.APPLICATION_HEIGHT);
        viewport = new ScreenViewport(gamecam);
        gamecam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", manager.get("joystick/touchbackground.png", Texture.class));

        fundo = new Fundo(manager, viewport);
        touchpad = new TouchpadLayout(assetsGenerator.getTouchpadStyle(), gamecam.viewportWidth,gamecam.viewportHeight);

        player = new Player(manager, viewport, touchpad);

        stage = new Stage(viewport, ambiente);
        stage.addActor(touchpad);

        Gdx.input.setInputProcessor(stage);
        this.outrosPlayers = outrosPlayers;
        this.socket = socket;
        this.socketID = socketID;
        configSocketEvents();
    }

    @Override
    public void show() {
    }

    public void updateServer(float dt){
        timerServer += dt;
        if(timerServer >= UPDATE_TIME && player != null && player.aviao.hasMoved()){
            JSONObject data = new JSONObject();
            try {
                data.put("x", player.aviao.getX());
                data.put("y", player.aviao.getY());
                data.put("an", player.aviao.angulo);
                socket.emit("playerMoved", data);
            } catch (Exception e){
                Gdx.app.log("SOCKET .IO", "ERROR SOMETHING UPDATE DATA");
            }
        }
    }

    public void update(float dt) {
        float[] posicoes = player.getPositionsGameCamPerspective();
        gamecam.position.x = posicoes[0];
        gamecam.position.y = posicoes[1];

        touchpad.setPosition(gamecam.position.x - viewport.getWorldWidth() / 2 + 35, gamecam.position.y - viewport.getWorldHeight() / 2 + viewport.getScreenY() + 35);
        player.update(dt);

        if(fundo.contains(player.aviao.getX(), player.aviao.getY())){
            player.aviao.bateuParede();
        }
        if(Gdx.input.justTouched()){
            if(player.paraquedista.morreu){
                this.game.setScreen(new GameScreenMultiplayer(game, manager));
            }
            float x = Gdx.input.getX();
            float y = viewport.getWorldHeight() - Gdx.input.getY();
        }
        updateServer(dt);

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
        for(HashMap.Entry<String, Player> entry : outrosPlayers.entrySet()){
            entry.getValue().draw(ambiente);
        }

        if(player.aviao.drawExplosao(ambiente, delta)){
            touchpad.setVisible(false);
        };

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
    }

    public void connectSocket(){
        try{
            socket = IO.socket("http://192.168.0.111:3000");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                    socketID = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + data.getString("id"));
                } catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connect: " + playerId);
                    outrosPlayers.put(playerId, new Player(manager, viewport));

                } catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting new PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                } catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting new PlayerID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                    String playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    Double an = data.getDouble("an");
                    if(outrosPlayers.get(playerId) != null){
                        outrosPlayers.get(playerId).aviao.setPosition(x.floatValue(), y.floatValue());
                    }
                } catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting new PlayerID");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try{
                    for(int i = 0 ; i < objects.length(); i ++){
                    }
                } catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting new PlayerID");
                }
            }
        });
    }
}

package br.com.richardmartins.flywarriors.helpscreens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.com.richardmartins.flywarriors.utilities.Constants;

public class Aviao extends Sprite {
    private Vector2 posicaoAnterior;
    private float anguloAnterior;

    private TouchpadLayout touchpad;
    private AssetManager manager;
    public Boolean explodido = false;
    public Boolean bateuParede = false;

    public float vida;
    public float vidaTotal;

    public float defesa;

    public float aviaoSpeed;

    public float angulo;
    public float timeInative;
    public float knobPercentX;
    public float knobPercentY;

    public BarraDeVida barraDeVida;

    public Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    public float stateTime;

    private Player player;

    public Aviao(AssetManager manager, Viewport viewport, TouchpadLayout touchpad, Player player) {
        super(manager.get("aviao/aviao1.png", Texture.class));
        posicaoAnterior = new Vector2(getX(), getY());

        this.manager = manager;
        this.touchpad = touchpad;
        this.player = player;

        this.setPosition(30, 200);
        this.setOrigin(75, 33);
        this.flip(true, false);
        this.setSize(150, 67);

        this.aviaoSpeed = 400;
        this.angulo = 0;
        this.anguloAnterior = angulo;
        this.timeInative = 0;

        barraDeVida = new BarraDeVida(this, viewport);
        criaAnimacaoExplosao();
        setStatusBasicos();
    }

    public Aviao(AssetManager manager, Viewport viewport) {
        super(manager.get("aviao/aviao1.png", Texture.class));
        posicaoAnterior = new Vector2(getX(), getY());

        this.manager = manager;

        this.setPosition(30, 200);
        this.setOrigin(75, 33);
        this.flip(true, false);
        this.setSize(150, 67);

        this.aviaoSpeed = 400;
        this.angulo = 0;
        this.anguloAnterior = angulo;
        this.timeInative = 0;

        criaAnimacaoExplosao();
        setStatusBasicos();
    }

    private void setStatusBasicos() {
        vidaTotal = 650;
        vida = vidaTotal;

        defesa = 40;
    }

    public Boolean drawExplosao(SpriteBatch ambiente, float delta) {
        if (explodido) {
            stateTime += delta; // Accumulate elapsed animation time
            TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, false);
            if (!walkAnimation.isAnimationFinished(stateTime)) {
                ambiente.draw(currentFrame, getX(), getY()); // Draw current frame at (50, 50)
                return true;
            }
        }
        return false;
    }

    public void update(float dt) {
        if (!bateuParede) {

            if (touchpad.getKnobPercentX() == 0.0 && touchpad.getKnobPercentY() == 0.0 || explodido) {
                timeInative += dt;
                knobPercentY = knobPercentY - dt * 0.10f;
                knobPercentX = isFlipX() ? (knobPercentX <= 0 ? knobPercentX : knobPercentX - dt * 0.05f) : (knobPercentX >= 0 ? knobPercentX : knobPercentX + dt * 0.05f);

                this.setY(this.getY() - (timeInative * 0) * dt);
            } else {
                timeInative = 0;
                knobPercentX = touchpad.getKnobPercentX();
                knobPercentY = touchpad.getKnobPercentY();
            }

            this.setX(this.getX() + (knobPercentX * this.aviaoSpeed * dt));
            this.setY(this.getY() + (knobPercentY * this.aviaoSpeed * dt));

            double x = knobPercentX - 0;
            double y = knobPercentY - 1;

            if (knobPercentX != 0.0 && knobPercentY != 0.0) {
                angulo = (float) ((float) Math.atan2(y, x) * 100 * 1.150 + 90);

                if (angulo <= -90) {
                    if (this.isFlipX()) {
                        this.flip(false, this.isFlipY());
                    }
                    if (!this.isFlipY()) {
                        this.flip(this.isFlipX(), false);
                    }
                    angulo += 180;
                } else {
                    if (!this.isFlipX()) {
                        this.flip(true, this.isFlipY());
                    }
                    if (this.isFlipY()) {
                        this.flip(this.isFlipX(), true);
                    }
                }
                this.setRotation(angulo);
            }
        }

        if (vida < 1) {
            morreu();
        }

        if (getX() > Constants.MAP_WIDTH - getWidth() || getY() > Constants.MAP_HEIGHT - getHeight()) {
            bateuParede();
        }
    }

    public void dispose() {
    }

    private void criaAnimacaoExplosao() {
        Texture explosao = manager.get("aviao/explosao.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(explosao,
                explosao.getWidth() / 5,
                explosao.getHeight() / 3);

        TextureRegion[] explosaoFrames = new TextureRegion[5 * 3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                explosaoFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation<TextureRegion>(0.155f, explosaoFrames);
        stateTime = 0f;
    }

    public void morreu() {
        vida = 0;
        this.explodido = true;
        setTexture(manager.get("aviao/aviao1explodido.png", Texture.class));
        if (!player.isEjetado) {
            player.paraquedista.morrer();
        }
    }

    public void bateuParede() {
        this.bateuParede = true;
        morreu();
    }

    public void tomarDano(float dano) {
        if (vida > dano) {
            vida -= dano;
        } else {
            vida = 0;
        }
    }

    public boolean hasMoved(){
        if(posicaoAnterior.x != getX() || posicaoAnterior.y != getY() || anguloAnterior != angulo){
            posicaoAnterior.x = getX();
            posicaoAnterior.y = getY();
            anguloAnterior = angulo;
            return true;
        }
        return false;
    }
}

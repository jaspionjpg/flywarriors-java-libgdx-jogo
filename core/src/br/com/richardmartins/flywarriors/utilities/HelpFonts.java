package br.com.richardmartins.flywarriors.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import static br.com.richardmartins.flywarriors.utilities.Constants.APPLICATION_HEIGHT;

/**
 * Created by richard.souza on 07/12/2017.
 */
public class HelpFonts {


    public static BitmapFont getTextPlacarGame(){
        GetFont getFont = new GetFont().invoke("font.ttf");
        getFont.parameter.size = (int)(0.07f* APPLICATION_HEIGHT);
        getFont.parameter.color = new Color(1, 0, 0, 1);
        return getFont.generateFont();
    }

    public static BitmapFont getTextNamesPlayers(){
        GetFont getFont = new GetFont().invoke("font.ttf");
        getFont.parameter.size = (int)(0.04f* APPLICATION_HEIGHT);
        getFont.parameter.color = new Color(1, 0, 0, 1);
        return getFont.generateFont();
    }

    public static float getSize(BitmapFont font, String texto, boolean width){
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.reset();
        glyphLayout.setText(font, texto);
        return width ? glyphLayout.width :  glyphLayout.height;
    }

    private static class GetFont {
        private FreeTypeFontGenerator generator;
        private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

        public FreeTypeFontGenerator getGenerator() {
            return generator;
        }

        public BitmapFont generateFont() {
            return generator.generateFont(parameter);
        }

        public FreeTypeFontGenerator.FreeTypeFontParameter getParameter() {
            return parameter;
        }

        public GetFont invoke(String font) {
            FreeTypeFontGenerator.setMaxTextureSize(2048);
            generator = new FreeTypeFontGenerator(Gdx.files.internal(font));
            parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            return this;
        }
    }
}

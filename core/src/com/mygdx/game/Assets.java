package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.mygdx.game.outils.Font;

public class Assets {
    private InternalFileHandleResolver resolver=new  InternalFileHandleResolver();
    private final AssetManager assetManager = new AssetManager(resolver);

    public Assets() {
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        //UIs
        assetManager.load("UI/startUp.png",Texture.class);
        assetManager.load("UI/startDown.png",Texture.class);
        assetManager.load("UI/homeUp.png",Texture.class);
        assetManager.load("UI/homeDown.png",Texture.class);
        assetManager.load("UI/regleUp.png",Texture.class);
        assetManager.load("UI/regleDown.png",Texture.class);
        //backGround
        assetManager.load("UI/fajrbackground.png",Texture.class);
        assetManager.load("UI/noonbackground.png",Texture.class);
        assetManager.load("UI/nightbackgroundwithmoon.png",Texture.class);
        //fonts
        FreetypeFontLoader.FreeTypeFontLoaderParameter parms = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parms.fontFileName = "fonts/Retro_Gaming.ttf";
        parms.fontParameters.size = 60;
        parms.fontParameters.color=Color.RED;
        parms.fontParameters.borderColor=Color.BLACK;
        assetManager.load( "fonts/Retro_Gaming.ttf", BitmapFont.class, parms);
        //sprites
        assetManager.load("sprites/cherry.png", Texture.class);

        //load
        System.out.println(assetManager.getProgress());
        assetManager.finishLoading();

    }

    public AssetManager getManager() {
        return assetManager;
    }
}

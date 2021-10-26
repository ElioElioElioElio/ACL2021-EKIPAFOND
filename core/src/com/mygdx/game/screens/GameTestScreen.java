package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.ACLGame;
import com.mygdx.game.listeners.ACLGameListener;
import com.mygdx.game.systems.*;

public class GameTestScreen extends GameScreen {

    public GameTestScreen(ACLGame game) {
        super(game, game.getAssets());
        this.engine.addSystem(new RenderSystem(this.game.batcher));
        this.engine.addSystem(new MovementSystem());
        this.engine.addSystem(new HeroSystem());
        this.engine.addSystem(new PhysicsSystem());
        this.engine.addSystem(new DebugRenderSystem(this.game.batcher, this.game.camera));

        this.assets.getManager().finishLoading();
        this.world.createMap();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.input.setInputProcessor(new ACLGameListener(this));

    }
}


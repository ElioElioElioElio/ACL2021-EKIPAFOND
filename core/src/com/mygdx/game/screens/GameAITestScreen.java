package com.mygdx.game.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.ACLGame;
import com.mygdx.game.Assets;
import com.mygdx.game.SteeringPresets;
import com.mygdx.game.components.*;
import com.mygdx.game.listeners.ACLGameListener;
import com.mygdx.game.systems.*;

public class GameAITestScreen extends GameScreen{
    public ACLGame game;
    private Entity hero;
    private Entity monster;
    public GameAITestScreen(ACLGame game, Assets assets) {
        super(game, assets);
        this.game = game;
        this.engine.addSystem(new RenderSystem(this.game.batcher));
        this.engine.addSystem(new MovementSystem());
        this.engine.addSystem(new HeroSystem());
        this.engine.addSystem(new PhysicsSystem());

        this.assets.getManager().finishLoading();
        createHero();

        this.engine.addSystem(new MonsterSystem(hero));
        this.engine.addSystem(new AISystem());
        this.engine.addSystem(new DebugRenderSystem(this.game.batcher, this.game.camera));
        createMonster();
    }

    private void createHero(){

        hero = new Entity();

        //Add Texture
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.setRegion(new TextureRegion(this.assets.getManager().get("sprites/cherry.png", Texture.class)));
        hero.add(textureComponent);

        //Add Position
        DirectionComponent directionComponent = new DirectionComponent();
        hero.add(directionComponent);

        //add Movement
        MovementComponent movementComponent = new MovementComponent(HeroComponent.HERO_VELOCITY);
        hero.add(movementComponent);

        //add Hero
        HeroComponent heroComponent = new HeroComponent();
        hero.add(heroComponent);

        //Add Transform
        TransformComponent transformComponent = new TransformComponent(new Vector3(10,20,10));
        hero.add(transformComponent);

        // Body creation
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(8, 8);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16, 16);

        PhysicsSystem physicsSystem = this.engine.getSystem(PhysicsSystem.class);

        Body body = physicsSystem.addDynamicBody(0, 40, 10, 10);
        body.setLinearVelocity(new Vector2(0,0));

        //Add Body
        hero.add(new BodyComponent(body));

        //Add Steering
        SteeringComponent steeringComponent = new SteeringComponent(body);
        hero.add(steeringComponent);

        this.engine.addEntity(hero);
    }

    private void createMonster(){

        monster = new Entity();

        //Add Position
        DirectionComponent directionComponent = new DirectionComponent();
        monster.add(directionComponent);

        //Add Movement
        MovementComponent movementComponent = new MovementComponent(HeroComponent.HERO_VELOCITY);
        monster.add(movementComponent);

        // Add Monster component
        MonsterComponent monsterComponent = new MonsterComponent();
        monster.add(monsterComponent);


        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(8, 8);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16, 16);

        PhysicsSystem physicsSystem = this.engine.getSystem(PhysicsSystem.class);

        Body body = physicsSystem.addDynamicBody(0, 40, 10, 10);
        body.setLinearVelocity(new Vector2(10,10));



        // Add steering
        SteeringComponent steeringComponent = new SteeringComponent(body);
        monster.add(steeringComponent);
        monster.getComponent(SteeringComponent.class).steeringBehavior  = SteeringPresets.getArrive(monster.getComponent(SteeringComponent.class),hero.getComponent(SteeringComponent.class));
        monster.getComponent(SteeringComponent.class).currentMode = SteeringComponent.SteeringState.ARRIVE;


        //Add Texture
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.setRegion(new TextureRegion(this.assets.getManager().get("sprites/spr_orange.png", Texture.class)));
        monster.add(textureComponent);


        // Add transform
        TransformComponent transformComponent = new TransformComponent(new Vector3(10,20,10));
        monster.add(transformComponent);
        this.engine.addEntity(monster);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.input.setInputProcessor(new ACLGameListener(this));
    }
}
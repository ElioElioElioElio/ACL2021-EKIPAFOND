package com.mygdx.game.factory.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Assets;
import com.mygdx.game.World;
import com.mygdx.game.components.*;
import com.mygdx.game.systems.physics.PhysicsSystem;

public class TreasureBuilder extends PhysicalEntityBuilder{

    public TreasureBuilder(Assets assetManager, PhysicsSystem physicsSystem) {
        super(assetManager, physicsSystem);
    }

    @Override
    public Entity buildEntity(float x, float y) {

        Entity treasure = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.setRegion(new TextureRegion(assets.getManager().get("tiles/treasure32x32.png", Texture.class)));
        treasure.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent(new Vector3(x , y,5));
        treasure.add(transformComponent);

        Body body = physicsSystem.addSensorBody(x , y, World.CASE_DIMENSION,World.CASE_DIMENSION);
        body.setUserData(treasure);
        treasure.add(new SteeringComponent(body));
        treasure.add(new CollisionComponent());
        treasure.add(new TypeComponent(TypeComponent.TYPE_TREASURE));
        return treasure;
    }
}
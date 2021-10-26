package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.components.*;
import com.mygdx.game.systems.PhysicsSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class World {

    private char[][] grid;
    private int width, height, ctr;
    private Scanner SizeReader,TileReader;
    private File mapFile;
    private String MapDir;
    private List<File> Maps;
    private Engine engine;
    private Assets assets;
    PhysicsSystem physicsSystem;
    Entity staticEntity;
    Entity hero;


    public World(Engine engine, Assets assets) {
        this.Maps = new ArrayList<File>();
        this.MapDir = "core/assets/maps/map1.txt" ;
        this.ctr = 0;
        this.engine = engine;
        this.assets = assets;
        this.physicsSystem = this.engine.getSystem(PhysicsSystem.class);

    }

    public World() {
    }

    public void createMap(){
        mapFile = new File(this.MapDir);
        try{
            this.SizeReader = new Scanner(mapFile);
            } catch (FileNotFoundException e){
                System.out.println("File not found");
        }
        /* Get the width and the height of the map */
        while (SizeReader.hasNext()) {
            String data = SizeReader.nextLine();
            if (data.length() > this.width) {
                this.width = data.length();
            }
            this.height++;
        }

        /* In case the file does not contain a single column and/or row */

        if (this.height == 0){
            this.height = 1;
        }
        if (this.width == 0){
            this.width = 1;
        }

        SizeReader.close();

        try{
            this.TileReader = new Scanner(mapFile);
            } catch(FileNotFoundException e){
                System.out.println("File not found");
        }
        this.grid = new char[this.height][this.width];
        while (TileReader.hasNext()) {
            String data = TileReader.nextLine();
                for (int j = 0; j < this.width ; j++){
                    grid[ctr][j] = data.charAt(j);
                    switch (data.charAt(j)){
                        case '-':
                            Entity wall = new Entity();
                            this.engine.addEntity(wall);
                            TextureComponent textureComponent = new TextureComponent();
                            textureComponent.setRegion(new TextureRegion(this.assets.getManager().get("sprites/damage_up.png", Texture.class)));
                            wall.add(textureComponent);
                            System.out.print(new Vector3((float)(j+ 0.5) * 16 * 2 , 480-(float)(ctr+0.5) * 16 * 2,0));
                            TransformComponent transformComponent = new TransformComponent(new Vector3((float)(j+ 0.5) * 16 * 2 , 480-(float)(ctr+0.5) * 16 * 2,0));
                            wall.add(transformComponent);
                            PhysicsSystem physicsSystem = this.engine.getSystem(PhysicsSystem.class);
                            Body body = physicsSystem.addStaticBody((float)(j+ 0.5) * 16 * 2 , (float)(ctr+0.5) * 16 * 2,10,10);
                            System.out.print("  Wall  ");
                            wall.add(new BodyComponent(body));
                            break;
                        case '+':
                            Entity ground = new Entity();
                            this.engine.addEntity(ground);
                            System.out.print(" Ground ");
                            break;
                        case '1':
                            createHero((float)(ctr+0.5) * 16 * 2, 480-(float)(j+0.5) * 16 * 2);
                            System.out.print("  Hero  ");
                            break;
                        default :
                            System.out.print("Ground-d");
                    }
                }
            ctr++;
            System.out.println("");
        }
        this.TileReader.close();
    }

    public void createHero(float posx, float posy){

        this.hero = new Entity();

        //Add Texture
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.setRegion(new TextureRegion(this.assets.getManager().get("sprites/cherry.png", Texture.class)));
        hero.add(textureComponent);


        //Add Position
        TransformComponent transformComponent = new TransformComponent(new Vector3(posx,posy,0));
        hero.add(transformComponent);

        //Add Position
        DirectionComponent directionComponent = new DirectionComponent();
        hero.add(directionComponent);

        MovementComponent movementComponent = new MovementComponent(HeroComponent.HERO_VELOCITY);
        hero.add(movementComponent);

        HeroComponent heroComponent = new HeroComponent();
        hero.add(heroComponent);

        //hero.add(transformComponent);

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(8, 8);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16, 16);

        PhysicsSystem physicsSystem = this.engine.getSystem(PhysicsSystem.class);

        Body body = physicsSystem.addDynamicBody(posx, posy, 10, 10);
        body.setLinearVelocity(new Vector2(0,-10));

        hero.add(new BodyComponent(body));
        this.engine.addEntity(hero);
    }

    /*public Monster? createMonster(){

    }*/

    public char[][] getGrid() {
        return grid;
    }

    public void setGrid(char[][] grid) {
        this.grid = grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
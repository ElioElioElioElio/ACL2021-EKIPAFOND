package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.ACLGame;
import com.mygdx.game.components.*;
import com.mygdx.game.screens.EndScreenLoose;
import com.mygdx.game.systems.physics.PhysicsSystem;

public class AnimationSystem extends IteratingSystem {
    //componentmappers
    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<TransformComponent> trm;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<DirectionComponent> dm;
    private ComponentMapper<HeroComponent> hm;
    private ComponentMapper<HealthComponent> heam;
    private ComponentMapper<SteeringComponent> sm;
    //attack boolean
    private boolean attack=false;
    //all the deltas for Timing
    private float sumDelta;
    private float stateTime;
    private float deathDelta;
    private ACLGame game;

    /**
     * Constructor
     * @param game
     */
    public AnimationSystem(ACLGame game) {
        super(Family.all(TextureComponent.class, TransformComponent.class, AnimationComponent.class,HeroComponent.class,HealthComponent.class,SteeringComponent.class).get());
        this.game=game;
        tm = ComponentMapper.getFor(TextureComponent.class);
        trm = ComponentMapper.getFor(TransformComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        dm = ComponentMapper.getFor(DirectionComponent.class);
        hm = ComponentMapper.getFor(HeroComponent.class);
        heam=ComponentMapper.getFor(HealthComponent.class);
        sm=ComponentMapper.getFor(SteeringComponent.class);
        sumDelta=0;
        deathDelta=0;
        stateTime=0f;
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent txtComp = tm.get(entity);
        AnimationComponent amComp = am.get(entity);
        DirectionComponent dComp = dm.get(entity);
        HeroComponent hComp= hm.get(entity);
        HealthComponent healthComponent=heam.get(entity);
        SteeringComponent sc=sm.get(entity);


        stateTime += deltaTime;
        //if the hero is not dead,the animation will play
        if(healthComponent.getHealthPoint()>0) {
            switch (dComp.getDirection()) {
                case DirectionComponent.UP:
                    //if player click button attack and animation attack is not finished,the animation will play a complete loop
                    if (attack || (sumDelta > 0 && sumDelta <= 0.4)) {
                        txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationAttackUp().getKeyFrame(sumDelta)));
                        sumDelta += deltaTime;
                        //if the complete loop of the animation is in the end ,the state of hero Will revert to walking and timing initialize
                        if (sumDelta > amComp.getAnimationAttackUp().getFrameDuration() * 4) {
                            attack = false;
                            hComp.setState(hComp.STATE_WALKING);
                            sumDelta = 0;
                        }
                    } else {
                        //if the hero is not attacking ,always walks
                        txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationUp().getKeyFrame(stateTime)));
                    }
                    break;

                case DirectionComponent.DOWN:
                    //if player click button attack and animation attack is not finished,the animation will play a complete loop
                    if (attack || (sumDelta > 0 && sumDelta <= 0.4)) {
                        txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationAttackDown().getKeyFrame(sumDelta)));
                        sumDelta += deltaTime;
                        //if the complete loop of the animation is in the end ,the state of hero Will revert to walking and timing initialize
                        if (sumDelta > amComp.getAnimationAttackUp().getFrameDuration() * 4) {
                            attack = false;
                            hComp.setState(hComp.STATE_WALKING);
                            sumDelta = 0;
                        }
                    } else {
                        //if the hero is not attacking ,always walks
                        txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationDown().getKeyFrame(stateTime)));
                    }
                    break;

                case DirectionComponent.RIGHT:
                    //if player click button attack and animation attack is not finished,the animation will play a complete loop
                    if (attack || (sumDelta > 0 && sumDelta <= 0.4)) {
                        txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationAttackRight().getKeyFrame(sumDelta)));
                        sumDelta += deltaTime;
                        //if the complete loop of the animation is in the end ,the state of hero Will revert to walking and timing initialize
                        if (sumDelta > amComp.getAnimationAttackUp().getFrameDuration() * 4) {
                            attack = false;
                            hComp.setState(hComp.STATE_WALKING);
                            sumDelta = 0;
                        }
                    } else {
                        //if the hero is not attacking ,always walks
                        txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationRight().getKeyFrame(stateTime)));
                    }
                    break;

                case DirectionComponent.LEFT:
                    if (attack || (sumDelta > 0 && sumDelta <= 0.4)) {
                        if (attack || (sumDelta > 0 && sumDelta <= 0.4)) {
                            txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationAttackLeft().getKeyFrame(sumDelta)));
                            sumDelta += deltaTime;
                        }
                        //if the complete loop of the animation is in the end ,the state of hero Will revert to walking and timing initialize
                        if (sumDelta > amComp.getAnimationAttackUp().getFrameDuration() * 4) {
                            attack = false;
                            hComp.setState(hComp.STATE_WALKING);
                            sumDelta = 0;
                        }
                    } else {
                        //if the hero is not attacking ,always walks
                        txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationLeft().getKeyFrame(stateTime)));
                    }
                    break;
            }
        } else {
            //if hero is dead,the death animation will play and finally destroy the body of the hero and entity of the hero
            txtComp.setRegion(((TextureAtlas.AtlasRegion) amComp.getAnimationDeath().getKeyFrame(deathDelta)));
            deathDelta += deltaTime;
            hComp.death();
            if (deathDelta > amComp.getAnimationDeath().getFrameDuration() * 8) {
                getEngine().removeEntity(entity);
                getEngine().getSystem(PhysicsSystem.class).getPhysicsWorld().destroyBody(sc.getBody());
                game.setScreen(new EndScreenLoose(game));
            }
        }
    }


    /**
     * verify if the hero is attacking or not.
     */
    public void attack(){
        attack = true;
    }


}

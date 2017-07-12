/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.mochi.deeppixeldungeon.actors.mobs.bosses;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.actors.mobs.YellowMushroom;
import com.mochi.deeppixeldungeon.Badges;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.Blob;
import com.mochi.deeppixeldungeon.actors.blobs.SporeCloud;
import com.mochi.deeppixeldungeon.actors.blobs.ToxicGas;
import com.mochi.deeppixeldungeon.actors.buffs.LockedFloor;
import com.mochi.deeppixeldungeon.actors.buffs.Terror;
import com.mochi.deeppixeldungeon.actors.buffs.Toxic;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.effects.Flare;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.artifacts.CapeOfThorns;
import com.mochi.deeppixeldungeon.items.keys.SkeletonKey;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Grim;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.ForestMobsSprites.ElderShroomSprite;
import com.mochi.deeppixeldungeon.ui.BossHealthBar;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ElderShroom extends Mob {

    private static final int MAX_ARMY_SIZE	= 0;

    {
        spriteClass = ElderShroomSprite.class;

        HP = HT = 100;
        EXP = 10;
        rooted = true;
        defenseSkill = 8;

        loot = new CapeOfThorns().identify();
        lootChance = 0.333f;

        properties.add(Property.BOSS);
    }

    private int TurnCounter;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 15 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 13;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public boolean act() {

        TurnCounter++;

        if (TurnCounter>9 && TurnCounter<11){
            summon();
        }

        if (TurnCounter>10){
            TurnCounter=-1;
        }

        GameScene.add( Blob.seed( pos, 30, SporeCloud.class ) );

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !immunities().contains(src.getClass())) lock.addTime(dmg*1.5f);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth  ), pos ).sprite.drop();

        Badges.validateBossSlain();

        GLog.n( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        GLog.n( Messages.get(this, "notice") );
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    static {
        RESISTANCES.add( Grim.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Toxic.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        BossHealthBar.assignBoss(this);
    }
    private int maxArmySize() {
        return 1 + MAX_ARMY_SIZE;
    }

    private void summon() {

        sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
        Sample.INSTANCE.play( Assets.SND_CHALLENGE );

        boolean[] passable = Level.passable.clone();
        for (Char c : Actor.chars()) {
            passable[c.pos] = false;
        }

        int undeadsToSummon = maxArmySize();

        PathFinder.buildDistanceMap( pos, passable, undeadsToSummon );
        PathFinder.distance[pos] = Integer.MAX_VALUE;
        int dist = 1;

        undeadLabel:
        for (int i=0; i < undeadsToSummon; i++) {
            do {
                for (int j=0; j < Dungeon.level.length(); j++) {
                    if (PathFinder.distance[j] == dist) {

                        YellowMushroom yellowMushroom = new YellowMushroom();
                        yellowMushroom.pos = j;
                        GameScene.add( yellowMushroom);

                        ScrollOfTeleportation.appear( yellowMushroom, j );
                        new Flare( 3, 32 ).color( 0x000000, false ).show( yellowMushroom.sprite, 2f ) ;

                        PathFinder.distance[j] = Integer.MAX_VALUE;

                        continue undeadLabel;
                    }
                }
                dist++;
            } while (dist < undeadsToSummon);
        }

        GLog.n( Messages.get(this, "spawn") );
    }

}

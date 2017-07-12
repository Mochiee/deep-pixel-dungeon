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
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.ToxicGas;
import com.mochi.deeppixeldungeon.actors.buffs.Paralysis;
import com.mochi.deeppixeldungeon.actors.buffs.Vertigo;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.effects.particles.SparkParticle;
import com.mochi.deeppixeldungeon.items.Generator;
import com.mochi.deeppixeldungeon.items.keys.SkeletonKey;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Grim;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.levels.traps.LightningTrap;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.GnollMobsSprites.BlueSoulBombSprite;
import com.mochi.deeppixeldungeon.sprites.GnollMobsSprites.RedSoulBombSprite;
import com.mochi.deeppixeldungeon.sprites.GnollMobsSprites.ShamanLeaderSprite;
import com.mochi.deeppixeldungeon.sprites.CharSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ShamanLeader extends Mob implements Callback {

    private static final float TIME_TO_ZAP = 1f;

    private static final int BOMBS_TO_SUMMON = 1;

    {
        spriteClass = ShamanLeaderSprite.class;

        HP = HT = 100;
        defenseSkill = 30;

        EXP = 20;
        baseSpeed=0.5f;

        loot = Generator.Category.SCROLL;
        lootChance = 1f;

        state = SLEEPING;
    }

    private int TurnCounter;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 16);
    }

    @Override
    public int attackSkill(Char target) {
        return 13;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    protected boolean act() {

        if (HP<HT){
        TurnCounter++;
        }

        if (TurnCounter > 8) {
            TurnCounter = -1;
        }
        if (TurnCounter > 0 && TurnCounter < 2) {
            return super.doAttack(enemy);

        } else if (TurnCounter > 2 && TurnCounter < 4) {

            GLog.n(Messages.get(this, "summon"));
            summon();


            if (TurnCounter > 3 && TurnCounter < 2) {
                return super.doAttack(enemy);

            } else if (TurnCounter > 6 && TurnCounter < 8) {
                GLog.n(Messages.get(this, "charging"));

                summon();

            } else if (TurnCounter > 7 && TurnCounter < 9) {

                GLog.n(Messages.get(this, "fire"));

                boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
                if (visible) {
                    sprite.zap(enemy.pos);
                }

                spend(TIME_TO_ZAP);

                if (hit(this, enemy, true)) {
                    int dmg = Random.NormalIntRange(15, 25);
                    if (Level.water[enemy.pos] && !enemy.flying) {
                        dmg *= 2.5f;
                    }
                    enemy.damage(dmg, LightningTrap.LIGHTNING);

                    enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                    enemy.sprite.flash();

                    if (enemy == Dungeon.hero) {

                        Camera.main.shake(4, 0.4f);

                        if (!enemy.isAlive()) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(this, "zap_kill"));
                        }
                    }
                } else {
                    enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
                }

                return !visible;

            }
            return super.act();
        } return super.act();
    }

    @Override
    public int defenseProc(Char Hero, int damage) {

        if (HT-1 < HP){
            damage = 1;
            GLog.n(Messages.get(this, "awake"));
        }

        if (HT > HP){
            damage = 1;
            GLog.n(Messages.get(this, "attacked"));
        }

        return super.defenseProc(Hero, damage);
    }


    private int BombsToSummon() {
        return BOMBS_TO_SUMMON;
    }

    int BombsToSummon = BombsToSummon() - King.Undead.count;
    int dist = 1;

    private void summon() {

        sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        boolean[] passable = Level.passable.clone();
        for (Char c : Actor.chars()) {
            passable[c.pos] = false;
        }

        int bombsToSummon = BombsToSummon() - King.Undead.count;

        PathFinder.buildDistanceMap(pos, passable,bombsToSummon);
        PathFinder.distance[pos] = Integer.MAX_VALUE;
        int dist = 1;

        bombLabel:
        for (int i = 0; i < bombsToSummon; i++) {
            do {
                for (int j = 0; j < Dungeon.level.length(); j++) {
                    if (PathFinder.distance[j] == dist) {

                        RedSoulBomb redsoulbomb = new RedSoulBomb();
                        redsoulbomb.pos = j;
                        GameScene.add(redsoulbomb);

                        ScrollOfTeleportation.appear(redsoulbomb, j);

                        PathFinder.distance[j] = Integer.MAX_VALUE;

                        continue bombLabel;
                    }
                }
                dist++;
            } while (dist < bombsToSummon);
        }
    }

    @Override
    protected boolean doAttack(Char enemy) {

        if (Dungeon.level.distance(pos, enemy.pos) <= 1) {

            return super.doAttack(enemy);

        } else {

            boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap(enemy.pos);
            }

            spend(TIME_TO_ZAP);

            if (hit(this, enemy, true)) {
                int dmg = Random.NormalIntRange(3, 10);
                if (Level.water[enemy.pos] && !enemy.flying) {
                    dmg *= 1.5f;
                }
                enemy.damage(dmg, LightningTrap.LIGHTNING);

                enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                enemy.sprite.flash();

                if (enemy == Dungeon.hero) {

                    Camera.main.shake(2, 0.3f);

                    if (!enemy.isAlive()) {
                        Dungeon.fail(getClass());
                        GLog.n(Messages.get(this, "zap_kill"));
                    }
                }
            } else {
                enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
            }

            return !visible;
        }
    }
        @Override
        public void die( Object cause ) {

            GLog.p(Messages.get(this, "death"));

            Dungeon.level.unseal();

            GameScene.bossSlain();
            Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();


            super.die( cause );

        }
        @Override
        public void call () {
            next();
        }

        private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
        static {
            RESISTANCES.add(LightningTrap.Electricity.class);
        }

        @Override
        public HashSet<Class<?>> resistances () {
            return RESISTANCES;
        }


    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
    IMMUNITIES.add( Paralysis.class );
    IMMUNITIES.add( Vertigo.class );
    }
    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    public static class BlueSoulBomb extends Mob {

        {
            spriteClass = BlueSoulBombSprite.class;

            HP = HT = 1;
            defenseSkill = 0;

            EXP = 0;

            state = HUNTING;

        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 1, 1 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 99;
        }

        @Override
        public int attackProc( Char enemy, int damage ) {
          HP-=2;
            return damage;
        }

        @Override
        public void damage( int dmg, Object src ) {
            super.damage( dmg, src );
            if (src instanceof ToxicGas) {
                ((ToxicGas)src).clear( pos );
            }
        }

        @Override
        public void die( Object cause ) {
            super.die( cause );

            boolean heroKilled = false;
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
                if (ch != null && ch.isAlive()) {
                    int damage = 20;
                    ch.damage( damage, this );
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        heroKilled = true;
                    }
                }
            }

            if (Dungeon.visible[pos]) {
                Sample.INSTANCE.play( Assets.SND_BONES );
            }

            if (heroKilled) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "explo_kill") );
            }
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 1);
        }

        private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
        static {
            IMMUNITIES.add( Grim.class );
            IMMUNITIES.add( Paralysis.class );
        }

        @Override
        public HashSet<Class<?>> immunities() {
            return IMMUNITIES;
        }
    }
    public static class RedSoulBomb extends Mob {

        {
            spriteClass = RedSoulBombSprite.class;

            HP = HT = 1;
            defenseSkill = 0;

            EXP = 0;

            state = HUNTING;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 1, 1 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 99;
        }

        @Override
        public void damage( int dmg, Object src ) {
            super.damage( dmg, src );
            if (src instanceof ToxicGas) {
                ((ToxicGas)src).clear( pos );
            }
        }

        @Override
        public void die( Object cause ) {

            super.die( cause );

            boolean heroKilled = false;
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
                if (ch != null && ch.isAlive()) {
                    int damage = Math.max( 0,  damageRoll()*10 );
                    ch.damage( damage, this );
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        heroKilled = true;
                    }
                }
            }

            if (Dungeon.visible[pos]) {
                Sample.INSTANCE.play( Assets.SND_BONES );
            }

            if (heroKilled) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "explo_kill") );
            }
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 1);
        }

        private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
        static {
            IMMUNITIES.add( Grim.class );
            IMMUNITIES.add( Paralysis.class );
        }

        @Override
        public HashSet<Class<?>> immunities() {
            return IMMUNITIES;
        }
    }
}




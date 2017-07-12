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

import com.mochi.deeppixeldungeon.actors.mobs.LesserRat;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Burning;
import com.mochi.deeppixeldungeon.actors.buffs.Corruption;
import com.mochi.deeppixeldungeon.actors.buffs.Ooze;
import com.mochi.deeppixeldungeon.actors.buffs.Poison;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.effects.Pushing;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.keys.IronKey;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.levels.Terrain;
import com.mochi.deeppixeldungeon.levels.features.Door;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.SewerMobSprites.GoolemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Goolem extends Mob {

    {
        spriteClass = GoolemSprite.class;

        HP = HT = 70;
        EXP = 8;
        defenseSkill = 6;

        maxLvl = 5;

    }
    private static final float SPLIT_DELAY	= 1f;
    int generation	= 0;
    private static final String GENERATION	= "generation";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( GENERATION, generation );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        generation = bundle.getInt( GENERATION );
        if (generation > 0) EXP = 0;
    }


    @Override
    public int damageRoll() {
        return Random.NormalIntRange(7, 10);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

    @Override
    public boolean act() {

        if (Level.water[pos] && HP < HT) {
            sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
        }
        HP++;
        return super.act();

    }
    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Ooze.class);
            enemy.sprite.burst(0x000000, 5);
        }

        return damage;
    }
        @Override
        public int defenseProc( Char enemy, int damage ) {
          if (Random.Int(2)==0)
            if (HP >= damage + 2) {
                ArrayList<Integer> candidates = new ArrayList<>();
                boolean[] passable = Level.passable;

                int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
                for (int n : neighbours) {
                    if (passable[n] && Actor.findChar( n ) == null) {
                        candidates.add( n );
                    }
                }

                if (candidates.size() > 0) {

                    LesserRat clone = split();
                    clone.HP = (HP - damage) / 2;
                    clone.pos = Random.element( candidates );
                    clone.state = clone.HUNTING;

                    if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
                        Door.enter( clone.pos );
                    }

                    GameScene.add( clone, SPLIT_DELAY );
                    Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

                    HP -= clone.HP;
                }
            }

            return super.defenseProc(enemy, damage);
        }

        @Override
        public int attackSkill( Char target ) {
            return 10;
        }

    private LesserRat split() {
        LesserRat clone = new LesserRat();
        if (buff( Burning.class ) != null) {
            Buff.affect( clone, Burning.class ).reignite( clone );
        }
        if (buff( Poison.class ) != null) {
            Buff.affect( clone, Poison.class ).set(2);
        }
        if (buff(Corruption.class ) != null) {
            Buff.affect( clone, Corruption.class);
        }
        return clone;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.unseal();

        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos ).sprite.drop();

    }
}

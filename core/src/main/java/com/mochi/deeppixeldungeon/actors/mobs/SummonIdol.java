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
package com.mochi.deeppixeldungeon.actors.mobs;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.actors.mobs.bosses.King;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.Gold;
import com.mochi.deeppixeldungeon.items.food.MysteryMeat;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.IdolSprite;
import com.mochi.deeppixeldungeon.sprites.SewerMobSprites.CrabSprite;
import com.mochi.deeppixeldungeon.sprites.SewerMobSprites.GnollSprite;
import com.mochi.deeppixeldungeon.sprites.SewerMobSprites.RatSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SummonIdol extends Mob {

    private static final int MobsToSummon1 = 7;
    private static final int MobsToSummon2 = 4;
    private static final int MobsToSummon3 = 2;
    public int Rats = 0;
    public int Gnolls = 0;
    public int Crabs = 0;

    {
        spriteClass = IdolSprite.class;

        HP = HT = Dungeon.depth * 5 + 15;
        defenseSkill = Dungeon.depth;
        EXP = Dungeon.depth / 2;

        state = PASSIVE;

        maxLvl = 25;
    }

    private void scream() {
        sprite.centerEmitter().start(Speck.factory(Speck.EVOKE), 0.4f, 2);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(0, 0);
    }

    @Override
    public int attackSkill(Char target) {
        return 0;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        scream();

        return damage;
    }

    private int MobsToSummonSewer1() {
        return MobsToSummon1;
    }

    private int MobsToSummonSewer2() {
        return MobsToSummon2;
    }

    private int MobsToSummonSewer3() {
        return MobsToSummon3;
    }

    int MobsToSummonSewer1 = MobsToSummonSewer1() - King.Undead.count;
    int dist = 1;

    private void sewersummon1() {

        sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        boolean[] passable = Level.passable.clone();
        for (Char c : chars()) {
            passable[c.pos] = false;
        }

        int mobsToSummonSewer1 = MobsToSummonSewer1() - King.Undead.count;

        PathFinder.buildDistanceMap(pos, passable, mobsToSummonSewer1);
        PathFinder.distance[pos] = Integer.MAX_VALUE;
        int dist = 1;

        sewerLabel:
        for (int i = 0; i < mobsToSummonSewer1; i++) {
            do {
                for (int j = 0; j < Dungeon.level.length(); j++) {
                    if (PathFinder.distance[j] == dist) {

                        Rat rat = new Rat();
                        rat.pos = j;
                        GameScene.add(rat);

                        ScrollOfTeleportation.appear(rat, j);

                        PathFinder.distance[j] = Integer.MAX_VALUE;

                        Rats+=1;

                        continue sewerLabel;

                    }
                }
                dist++;
            } while (dist < mobsToSummonSewer1);
        }
    }

    private void sewersummon2() {

        sprite.centerEmitter().start(Speck.factory(Speck.EVOKE), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        boolean[] passable = Level.passable.clone();
        for (Char c : chars()) {
            passable[c.pos] = false;
        }

        int mobsToSummonSewer2 = MobsToSummonSewer2() - King.Undead.count;

        PathFinder.buildDistanceMap(pos, passable, mobsToSummonSewer2);
        PathFinder.distance[pos] = Integer.MAX_VALUE;
        int dist = 1;

        sewerLabel1:
        for (int i = 0; i < mobsToSummonSewer2; i++) {
            do {
                for (int j = 0; j < Dungeon.level.length(); j++) {
                    if (PathFinder.distance[j] == dist) {

                        Gnoll gnoll = new Gnoll();
                        gnoll.pos = j;
                        GameScene.add(gnoll);

                        ScrollOfTeleportation.appear(gnoll, j);

                        PathFinder.distance[j] = Integer.MAX_VALUE;

                        Gnolls-=9;

                        continue sewerLabel1;
                    }
                }
                dist++;
            } while (dist < mobsToSummonSewer2);
        }
    }

    private void sewersummon3() {

        sprite.centerEmitter().start(Speck.factory(Speck.EVOKE), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        boolean[] passable = Level.passable.clone();
        for (Char c : chars()) {
            passable[c.pos] = false;
        }

        int mobsToSummonSewer3 = MobsToSummonSewer3() - King.Undead.count;

        PathFinder.buildDistanceMap(pos, passable, mobsToSummonSewer3);
        PathFinder.distance[pos] = Integer.MAX_VALUE;
        int dist = 1;

        sewerLabel2:
        for (int i = 0; i < mobsToSummonSewer3; i++) {
            do {
                for (int j = 0; j < Dungeon.level.length(); j++) {
                    if (PathFinder.distance[j] == dist) {

                        Crab crab = new Crab();
                        crab.pos = j;
                        GameScene.add(crab);

                        ScrollOfTeleportation.appear(crab, j);

                        PathFinder.distance[j] = Integer.MAX_VALUE;

                        Crabs-=9;

                        continue sewerLabel2;
                    }
                }
                dist++;
            } while (dist < mobsToSummonSewer3);
        }
    }


    @Override
    public int defenseProc(Char enemy, int damage) {
        if (HP < HT && Rats < 1){
            sewersummon1();
        }
        if (Rats > 8) {
            sewersummon2();
        }
        if (Gnolls > 3) {
            sewersummon3();
        }
        if (Crabs > 1) {
            die(null);
        }
        return super.defenseProc(enemy, damage);
    }
    public class Rat extends Mob {

        {
            spriteClass = RatSprite.class;

            HP = HT = 8;
            defenseSkill = 2;

            maxLvl = 5;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(1, 4);
        }

        @Override
        public int attackSkill(Char target) {
            return 8;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 1);
        }

        @Override
        public void die( Object cause ) {
            Rats+=1;
            super.die( cause );
        }
    }

    public class Gnoll extends Mob {

        {
            spriteClass = GnollSprite.class;

            HP = HT = Dungeon.depth*5;
            defenseSkill = Dungeon.depth;

            EXP = 0;
            maxLvl = 30;

            loot = Gold.class;
            lootChance = 0.5f;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(1, 6);
        }

        @Override
        public int attackSkill(Char target) {
            return 10;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 2);
        }

        @Override
        public void die( Object cause ) {
            Gnolls+=1;
            super.die( cause );
        }
    }

    public class Crab extends Mob {

        {
            spriteClass = CrabSprite.class;

            HP = HT = 15;
            defenseSkill = 5;
            baseSpeed = 2f;

            EXP = 4;
            maxLvl = 9;

            loot = new MysteryMeat();
            lootChance = 0.167f;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(1, 8);
        }

        @Override
        public int attackSkill(Char target) {
            return 12;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 4);
        }

        @Override
        public void die( Object cause ) {
            Crabs +=1;
            super.die( cause );
        }
    }
}
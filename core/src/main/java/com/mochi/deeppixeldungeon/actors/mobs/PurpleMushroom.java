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

import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.Blob;
import com.mochi.deeppixeldungeon.actors.blobs.ParalyticGas;
import com.mochi.deeppixeldungeon.actors.blobs.StenchGas;
import com.mochi.deeppixeldungeon.actors.blobs.ToxicGas;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Paralysis;
import com.mochi.deeppixeldungeon.actors.buffs.Poison;
import com.mochi.deeppixeldungeon.items.Generator;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.ForestMobsSprites.PurpleMushroomSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class PurpleMushroom extends Mob {

    {
        spriteClass = PurpleMushroomSprite.class;

        HP = HT = 20;
        defenseSkill = 14;

        EXP = 5;
        maxLvl = 21;

        loot = Generator.Category.SEED;
        lootChance = 1f;

    }

    private int TurnCounter;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 3);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }


    @Override
    public int attackProc(Char enemy, int damage) {

        if (Random.Int(5)==1)
        Buff.affect(enemy, Poison.class);

        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

        if (TurnCounter > 2) {

            GameScene.add(Blob.seed(pos, 100, StenchGas.class));
            GameScene.add(Blob.seed(pos, 100, ParalyticGas.class));
            GameScene.add(Blob.seed(pos, 100, ToxicGas.class));
        }
        return super.defenseProc(enemy, damage);
    }
    @Override
    protected boolean act() {

        if (HP<HT){
            TurnCounter++;
        }

        if (TurnCounter>2){
            GLog.n("The Purple Mushroom withdraws in its cap!");
        }

        if (TurnCounter>4){
            TurnCounter = -1;
        }

        return super.act();
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Poison.class);
        IMMUNITIES.add(Paralysis.class);
    }
}
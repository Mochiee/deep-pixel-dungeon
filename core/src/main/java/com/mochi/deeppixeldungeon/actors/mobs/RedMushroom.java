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
import com.mochi.deeppixeldungeon.actors.buffs.Amok;
import com.mochi.deeppixeldungeon.actors.buffs.Terror;
import com.mochi.deeppixeldungeon.items.Generator;
import com.mochi.deeppixeldungeon.sprites.ForestMobsSprites.RedMushroomSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class RedMushroom extends Mob {

    {
        spriteClass = RedMushroomSprite.class;

        HP = HT = 30;
        defenseSkill = 12;

        EXP = 5;
        maxLvl = 21;

        loot = Generator.Category.SEED;
        lootChance = 0.9f;

    }

    private int TurnCounter;

    @Override
    public int damageRoll() {
        if (TurnCounter > 8) {
            return Random.NormalIntRange(18, 30);
        } else return Random.NormalIntRange(9, 15);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    protected float attackDelay() {
        if (TurnCounter > 8) {
            return 2f;
        } else return 1f;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }


    @Override
    public int attackProc(Char enemy, int damage) {
        TurnCounter++;
        return super.defenseProc(enemy, damage);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        TurnCounter++;
        return damage;
    }

    @Override
    protected boolean act() {

        if (TurnCounter > 13) {
            TurnCounter = 0;
        }

        if (TurnCounter > 8) {
            GLog.n("The Red Mushroom begins to charge up!");
        }

        return super.act();
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Terror.class);
    }
}
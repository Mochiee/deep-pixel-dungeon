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

import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.items.Gold;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.levels.Terrain;
import com.mochi.deeppixeldungeon.levels.traps.GrippingTrap;
import com.mochi.deeppixeldungeon.sprites.GnollThiefSprite;
import com.watabou.utils.Random;

public class GnollThief extends Mob {

    {
        spriteClass = GnollThiefSprite.class;

        HP = HT = 30;
        defenseSkill = 20;

        EXP = 8;

        loot = Gold.class;
        lootChance = 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 18);
    }

    @Override
    public int attackSkill(Char target) {
        return 16;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    private void lay() {

        for (int i = 0; i < 4; i++) {
            int trapPos;
            do {
                trapPos = enemy.pos;
            } while (!Level.fieldOfView[trapPos] || Level.solid[trapPos]);

            if (Dungeon.level.map[trapPos] == Terrain.EMPTY) {
                Dungeon.level.setTrap(new GrippingTrap().reveal(), trapPos);
                Level.set(trapPos, Terrain.TRAP);
                ScrollOfMagicMapping.discover(trapPos);
            }
        }
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        lay();
        return damage;
    }
}




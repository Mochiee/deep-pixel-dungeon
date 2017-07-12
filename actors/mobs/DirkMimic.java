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
import com.mochi.deeppixeldungeon.items.weapon.melee.Dirk;
import com.mochi.deeppixeldungeon.sprites.sprites.DirkMimicSprite;
import com.watabou.utils.Random;

public class DirkMimic extends Mob {

    {
        spriteClass = DirkMimicSprite.class;

        HP = HT = 12;
        defenseSkill = 2;

        maxLvl = 5;
        loot = new Dirk();
        lootChance = 1f;
        state = PASSIVE;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 6);
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
    public void damage(int dmg, Object src) {

        if (state == PASSIVE) {
            state = HUNTING;

        }

        super.damage(dmg, src);
    }
}

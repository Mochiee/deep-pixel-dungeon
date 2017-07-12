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
package com.mochi.deeppixeldungeon.items.weapon.melee;

import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;

public class Hex extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Hex;

        tier = 3;
        RCH = 9;     //lots of extra reach
        ACC = 0.7f + 0.03f; //70% accuracy + 3%
    }

    @Override
    public int min(int lvl) {
        return  tier + shardlvl/3 +        //base + 1 per 3 shard levels
                lvl;            //level scaling

    }

    @Override
    public int max(int lvl) {
        return  4*(tier-1) + shardlvl/3 +   //8 base, down from 20 + 1 per 3 shard levels.
                lvl*(tier-1);   //+2 per level, down from +4
    }

}

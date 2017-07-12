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

public class Rapier extends MeleeWeapon {

    {
        image = ItemSpriteSheet.RAPIER;

        tier = 2;
        DLY = 0.67f - shardlvl/20; //1.33x speed + 0.05 per shard level
        RCH = 2;
        ACC = 1.2f + shardlvl/10; //20% boost to accuracy + 10% per shard level
    }

    @Override
    public int max(int lvl) {
        return  3*(tier+1) + shardlvl/2 +   //6 base, down from 15 + 1 per 2 shard levels
                lvl*tier;       //+2 per level, down from +3
    }

}

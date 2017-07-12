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

public class Knuckles extends MeleeWeapon {

	{
		image = ItemSpriteSheet.KNUCKLEDUSTER;

		tier = 1;
		DLY = 0.5f - shardlvl/20; //2x speed + 0.05 per shard level
	}

	@Override
	public int min(int lvl) {
		return  tier + shardlvl/3 +        //base + 1 per 3 shard levels
				lvl;            //level scaling

	}

	@Override
	public int max(int lvl) {
		return  3*(tier+1) + shardlvl/3 +   //6 base, down from 10 + 1 per 3 shard levels
				lvl*tier;       //+1 per level, down from +2
	}

}

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

public class Glaive extends MeleeWeapon {

	private int RangeBoost;

	{
		image = ItemSpriteSheet.GLAIVE;

		tier = 5;
		DLY = 1.5f; //0.67x speed
		RCH = 2 + RangeBoost;    //extra reach

		if (shardlvl>4){
			RangeBoost = 1;
		} else RangeBoost = 0;  //Reach +1 when shard level +5 or above
	}

	@Override
	public int max(int lvl) {
		return  Math.round(6.67f*(tier+1)) + shardlvl + shardlvl/2 +   //40 base, up from 30 +1.5 per shard level
				lvl*Math.round(1.33f*(tier+1)); //+8 per level, up from +6
	}

}

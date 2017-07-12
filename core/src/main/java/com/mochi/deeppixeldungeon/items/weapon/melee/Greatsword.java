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

public class Greatsword extends MeleeWeapon {

	{
		image = ItemSpriteSheet.GREATSWORD;

		tier = 5;
		DLY = 2f;  //0.5x speed
	}

	@Override
	public int min(int lvl) {
		return  tier + 2*shardlvl +        //base + 2 per shard level
				lvl;            //level scaling

	}

	@Override
	public int max(int lvl) {
		return  Math.round(9*(tier)) + 2*shardlvl +  //45 base, up from 30 + 2 per shard level
				lvl*Math.round(2*(tier)); //+10 per level, up from +6
	}

}

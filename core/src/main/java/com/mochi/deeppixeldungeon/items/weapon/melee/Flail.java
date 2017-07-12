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

public class Flail extends MeleeWeapon {

	{
		image = ItemSpriteSheet.FLAIL;

		tier = 4;
		DLY = 1.25f - shardlvl/40; //0.8x speed - 0.025f
		//also cannot surprise attack, see Hero.canSurpriseAttack
	}

	@Override
	public int min(int lvl) {
		return  tier + shardlvl +        //base + 1 per shard level
				lvl;            //level scaling

	}

	@Override
	public int max(int lvl) {
		return  Math.round(7*(tier+1)) + 2*shardlvl +  //35 base, up from 25 + 2 per shard level
				lvl*Math.round(1.6f*(tier+1));  //+8 per level, up from +5
	}
}

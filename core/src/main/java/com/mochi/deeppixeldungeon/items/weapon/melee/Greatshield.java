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

import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;

public class Greatshield extends MeleeWeapon {

	{
		image = ItemSpriteSheet.GREATSHIELD;

		tier = 5;
	}

	@Override
	public int min(int lvl) {
		return  tier + shardlvl/2 +        //base
				lvl;            //level scaling

	}

	@Override
	public int max(int lvl) {
		return  2*(tier+1) + shardlvl/2 +   //12 base, down from 30
				lvl*(tier-2);   //+3 per level, down from +6
	}

	@Override
	public int defenseFactor(Hero hero) {
		return 10+3*level() + shardlvl;    //10 extra defence, plus 3 per level;
	}
}

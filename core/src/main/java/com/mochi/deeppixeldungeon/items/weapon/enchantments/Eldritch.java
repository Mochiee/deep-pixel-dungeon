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
package com.mochi.deeppixeldungeon.items.weapon.enchantments;

import com.mochi.deeppixeldungeon.items.weapon.Weapon;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Terror;
import com.mochi.deeppixeldungeon.actors.buffs.Vertigo;
import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite;
import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Eldritch extends Weapon.Enchantment {

	private static ItemSprite.Glowing GREY = new ItemSprite.Glowing( 0x222222 );
	
	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 20%
		// lvl 1 - 33%
		// lvl 2 - 43%
		int level = Math.max( 0, weapon.level() );
		
		if (Random.Int( level + 5 ) >= 4) {

			if (defender == Dungeon.hero) {
				Buff.affect( defender, Vertigo.class, Vertigo.duration(defender) );
			} else {
				Buff.affect( defender, Terror.class, Terror.DURATION ).object = attacker.id();
			}

		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return GREY;
	}
}

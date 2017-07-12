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
import com.mochi.deeppixeldungeon.actors.buffs.Berserk;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.hero.HeroSubClass;
import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite;
import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Lucky extends Weapon.Enchantment {

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x00FF00 );
	
	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max( 0, weapon.level() );

		if (Random.Int(100) < (55 + level)){
			int exStr = 0;
			if (attacker == Dungeon.hero) exStr = Math.max(0, Dungeon.hero.STR() - weapon.STRReq());
			damage = weapon.imbue.damageFactor(weapon.max()) + exStr - defender.drRoll();
		} else {
			damage = weapon.imbue.damageFactor(weapon.min()) - defender.drRoll();
		}

		if (attacker == Dungeon.hero && Dungeon.hero.subClass == HeroSubClass.BERSERKER ){
			damage = Buff.affect(Dungeon.hero, Berserk.class).damageFactor(damage);
		}

		return Math.max(0, damage);
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}
}

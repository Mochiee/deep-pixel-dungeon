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
package com.mochi.deeppixeldungeon.items.armor.glyphs;

import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.effects.Lightning;
import com.mochi.deeppixeldungeon.items.armor.Armor;
import com.mochi.deeppixeldungeon.items.armor.Armor.Glyph;
import com.mochi.deeppixeldungeon.levels.traps.LightningTrap;
import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite;
import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite.Glowing;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Potential extends Glyph {
	
	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.6f );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, armor.level() );

		if (Random.Int( level + 20 ) >= 18) {

			int shockDmg = Random.NormalIntRange( defender.HT/20, defender.HT/10 );

			shockDmg *= Math.pow(0.9, level);

			defender.damage( shockDmg, LightningTrap.LIGHTNING );
			
			checkOwner( defender );
			if (defender == Dungeon.hero) {
				Dungeon.hero.belongings.charge(1f);
				Camera.main.shake( 2, 0.3f );
			}

			attacker.sprite.parent.add( new Lightning( attacker.pos, defender.pos, null ) );

		}
		
		return damage;
	}

	@Override
	public Glowing glowing() {
		return WHITE;
	}
}

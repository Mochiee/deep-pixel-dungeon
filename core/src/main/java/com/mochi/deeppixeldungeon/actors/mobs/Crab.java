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
package com.mochi.deeppixeldungeon.actors.mobs;

import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.items.food.MysteryMeat;
import com.mochi.deeppixeldungeon.sprites.SewerMobSprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab extends Mob {

	{
		spriteClass = CrabSprite.class;
		
		HP = HT = 30;
		defenseSkill = 12;
		baseSpeed = 2f;
		
		EXP = 6;
		
		loot = new MysteryMeat();
		lootChance = 0.167f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 3, 12 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 14;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
	}
}
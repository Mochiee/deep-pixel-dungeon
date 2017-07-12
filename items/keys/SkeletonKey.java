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
package com.mochi.deeppixeldungeon.items.keys;

import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.sprites.sprites.ItemSpriteSheet;

public class SkeletonKey extends Key {
	
	{
		image = ItemSpriteSheet.SKELETON_KEY;
		stackable = false;
	}
	
	public SkeletonKey() {
		this( 0 );
	}
	
	public SkeletonKey( int depth ) {
		super();
		this.depth = depth;
	}

	@Override
	public boolean doPickUp(Hero hero) {
		Dungeon.hero.belongings.specialKeys[depth]++;
		return super.doPickUp(hero);
	}
	
	@Override
	public boolean isSimilar( Item item ) {
		return false;
	}

}

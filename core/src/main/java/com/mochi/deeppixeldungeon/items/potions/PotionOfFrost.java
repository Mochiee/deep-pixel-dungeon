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
package com.mochi.deeppixeldungeon.items.potions;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.blobs.Fire;
import com.mochi.deeppixeldungeon.actors.blobs.Freezing;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotionOfFrost extends Potion {
	
	private static final int DISTANCE	= 2;

	{
		initials = 1;
	}
	
	@Override
	public void shatter( int cell ) {
		
		PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlocking, null ), DISTANCE );
		
		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );

		boolean visible = false;
		for (int i=0; i < Dungeon.level.length(); i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				visible = Freezing.affect( i, fire ) || visible;
			}
		}

		if (visible) {
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );

			setKnown();
		}
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}

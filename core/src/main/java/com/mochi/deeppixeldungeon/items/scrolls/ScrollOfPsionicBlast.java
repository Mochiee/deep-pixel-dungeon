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
package com.mochi.deeppixeldungeon.items.scrolls;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.buffs.Blindness;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Invisibility;
import com.mochi.deeppixeldungeon.actors.buffs.Paralysis;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfPsionicBlast extends Scroll {

	{
		initials = 5;

		bones = true;
	}
	
	@Override
	protected void doRead() {
		
		GameScene.flash( 0xFFFFFF );
		
		Sample.INSTANCE.play( Assets.SND_BLAST );
		Invisibility.dispel();
		
		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (Level.fieldOfView[mob.pos]) {
				mob.damage(mob.HT, this );
			}
		}

		Item.curUser.damage(Math.max(Item.curUser.HT/5, Item.curUser.HP/2), this);
		Buff.prolong( Item.curUser, Paralysis.class, Random.Int( 4, 6 ) );
		Buff.prolong( Item.curUser, Blindness.class, Random.Int( 6, 9 ) );
		Dungeon.observe();
		
		setKnown();

		Item.curUser.spendAndNext( TIME_TO_READ ); //no animation here, the flash interrupts it anyway.

		if (!Item.curUser.isAlive()) {
			Dungeon.fail( getClass() );
			GLog.n( Messages.get(this, "ondeath") );
		}
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
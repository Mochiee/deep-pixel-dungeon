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
package com.mochi.deeppixeldungeon.actors.blobs;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.Journal;
import com.mochi.deeppixeldungeon.Journal.Feature;
import com.mochi.deeppixeldungeon.actors.buffs.Hunger;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.effects.BlobEmitter;
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.effects.particles.ShaftParticle;
import com.mochi.deeppixeldungeon.items.DewVial;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.items.potions.PotionOfHealing;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WaterOfHealth extends WellWater {
	
	@Override
	protected boolean affectHero( Hero hero ) {
		
		Sample.INSTANCE.play( Assets.SND_DRINK );
		
		PotionOfHealing.heal( hero );
		hero.belongings.uncurseEquipped();
		((Hunger)hero.buff( Hunger.class )).satisfy( Hunger.STARVING );
		
		CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

		Dungeon.hero.interrupt();
	
		GLog.p( Messages.get(this, "procced") );
		
		Journal.remove( Feature.WELL_OF_HEALTH );
		
		return true;
	}
	
	@Override
	protected Item affectItem( Item item ) {
		if (item instanceof DewVial && !((DewVial)item).isFull()) {
			((DewVial)item).fill();
			Journal.remove( Feature.WELL_OF_HEALTH );
			return item;
		}
		
		return null;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.HEALING ), 0.5f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}

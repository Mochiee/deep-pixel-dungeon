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
package com.mochi.deeppixeldungeon.levels.traps;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.buffs.Buff;
import com.mochi.deeppixeldungeon.buffs.Paralysis;
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RockfallTrap extends Trap {

	{
		color = GREY;
		shape = DIAMOND;
	}

	@Override
	public void activate() {

		boolean seen = false;

		for (int i : PathFinder.NEIGHBOURS9){

			if (Level.solid[pos+i])
				continue;

			if (Dungeon.visible[ pos+i ]){
				CellEmitter.get( pos + i - Dungeon.level.width() ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
				if (!seen) {
					Camera.main.shake(3, 0.7f);
					Sample.INSTANCE.play(Assets.SND_ROCKS);
					seen = true;
				}
			}

			Char ch = Actor.findChar( pos+i );

			if (ch != null){
				int damage = Random.NormalIntRange(Dungeon.depth, Dungeon.depth*2);
				damage -= ch.drRoll();
				ch.damage( Math.max(damage, 0) , this);

				Buff.prolong( ch, Paralysis.class, Paralysis.duration(ch)/2);

				if (!ch.isAlive() && ch == Dungeon.hero){
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(this, "ondeath") );
				}
			}
		}

	}
}

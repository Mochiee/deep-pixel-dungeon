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
package com.mochi.deeppixeldungeon.sprites;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.DungeonTilemap;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.mobs.Grieflord;
import com.mochi.deeppixeldungeon.effects.Beam;
import com.mochi.deeppixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class GrieflordSprite extends MobSprite {

	private int zapPos;

	private Animation charging;
	private Emitter chargeParticles;

	public GrieflordSprite() {
		super();
		
		texture( Assets.Grieflord );
		
		TextureFilm frames = new TextureFilm( texture, 21, 17 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 1, 2, 3 );

		charging = new Animation( 12, false);
		charging.frames( frames, 4, 5 );

		chargeParticles = centerEmitter();
		chargeParticles.autoKill = false;
		chargeParticles.pour(MagicMissile.MagicParticle.FACTORY, 0.05f);
		chargeParticles.on = false;
		
		run = new Animation( 12, true );
		run.frames( frames, 0 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 4, 5 );
		zap = attack.clone();
		
		die = new Animation( 8, false );
		die.frames( frames, 6, 7, 8, 9, 10 );
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (((Grieflord)ch).beamCharged) play(charging);
	}

	@Override
	public void update() {
		super.update();
		chargeParticles.pos(center());
		chargeParticles.visible = visible;
	}

	public void charge( int pos ){
		turnTo(ch.pos, pos);
		play(charging);
	}

	@Override
	public void play(Animation anim) {
		chargeParticles.on = anim == charging;
		super.play(anim);
	}

	@Override
	public void zap( int pos ) {
		zapPos = pos;
		super.zap( pos );
	}
	
	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		
		if (anim == zap) {
			if (Dungeon.visible[ch.pos] || Dungeon.visible[zapPos]) {
				parent.add( new Beam.DeathRay( center(), DungeonTilemap.tileCenterToWorld( zapPos ) ) );
			}
			((Grieflord)ch).deathGaze();
			ch.next();
		} else if (anim == die){
			chargeParticles.killAndErase();
		}
	}
}

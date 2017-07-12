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
import com.mochi.deeppixeldungeon.actors.mobs.FlyingCat;
import com.mochi.deeppixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class FlyingCatSprite extends MobSprite {

    public FlyingCatSprite() {
        super();

        texture( Assets.FlyingCat );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0, 1, 0, 2 );

        run = new Animation( 10, true );
        run.frames( frames, 3, 4, 5, 6 );

        attack = new Animation( 12, false );
        attack.frames( frames, 7, 7 );

        zap = attack.clone();

        die = new Animation( 5, false );
        die.frames( frames, 8, 9, 10, 11, 12 );

        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.RAINBOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((FlyingCat)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }
    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }
}

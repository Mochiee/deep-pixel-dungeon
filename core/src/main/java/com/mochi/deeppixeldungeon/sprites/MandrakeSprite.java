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
import com.watabou.noosa.TextureFilm;

public class MandrakeSprite extends MobSprite {

    public MandrakeSprite() {
        super();

        texture( Assets.Mandrake );

        TextureFilm frames = new TextureFilm( texture, 11, 13 );

        idle = new Animation( 12, true );
        idle.frames( frames, 0, 1, 0, 2, 0, 6, 0, 1, 2, 0, 6, 0, 1, 2 );

        run = new Animation( 15, true );
        run.frames( frames, 3, 4, 5 );

        attack = new Animation( 15, false );
        attack.frames( frames, 6, 7, 8, 9, 8, 7, 10, 11 );

        die = new Animation( 8, false );
        die.frames( frames, 12, 13, 14 );

        zap = new Animation( 1, false);
        zap.frames( frames, 14, 14);

        play( zap );
    }
}

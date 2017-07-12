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

public class AssassinSprite extends MobSprite {

    public AssassinSprite() {
        super();

        texture( Assets.Assassin);

        TextureFilm frames = new TextureFilm( texture, 18, 17 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0 );

        run = new Animation( 25, true );
        run.frames( frames, 1, 2, 3 );

        attack = new Animation( 25, false );
        attack.frames( frames, 4, 4, 5, 5, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 );

        die = new Animation( 15, false );
        die.frames( frames, 19, 20, 21, 22, 23, 24, 25 , 26, 27 );

        play( idle );
    }
}

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
package com.mochi.deeppixeldungeon.sprites.SewerMobSprites;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class GoolemSprite extends MobSprite {

    public GoolemSprite() {
        super();

        texture( Assets.Goolem );

        TextureFilm frames = new TextureFilm( texture, 30, 27 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0, 2, 1, 2, 0, 2, 3, 2 );

        run = new Animation( 10, true );
        run.frames( frames, 0, 4, 0, 5 );

        attack = new Animation( 15, false );
        attack.frames( frames, 0, 6, 7, 8, 7, 6, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 9, 10, 11, 12, 13, 14);

        play( idle );
    }
}
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
package com.mochi.deeppixeldungeon.sprites.GnollMobsSprites;

        import com.mochi.deeppixeldungeon.Assets;
        import com.mochi.deeppixeldungeon.sprites.MobSprite;
        import com.watabou.noosa.TextureFilm;

public class BlueSoulBombSprite extends MobSprite {

    public BlueSoulBombSprite() {
        super();

        texture( Assets.ShamanLeader );
        TextureFilm film = new TextureFilm( texture, 30, 19 );

        idle = new Animation( 12, true );
        idle.frames( film, 8, 9, 10, 11 );

        run = new Animation( 15, true );
        run.frames( film, 8, 9, 10, 11 );

        die = new Animation( 10, false );
        die.frames( film, 13, 14, 15 );

        attack = new Animation( 5, false );
        attack.frames( film, 12, 11 );

        idle();
    }
}

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.mochi.deeppixeldungeon.ui;

import com.mochi.deeppixeldungeon.actors.Char;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

public class MPBar extends Component {

    private static final int COLOR_BG	= 0xFFCC0000;
    private static final int COLOR_MP	= 0x007BEE00;

    private static final int HEIGHT	= 2;

    private ColorBlock Bg;
    private ColorBlock Mp;

    private float mp;

    @Override
    protected void createChildren() {
        Bg = new ColorBlock( 1, 1, COLOR_BG );
        add( Bg );

        Mp = new ColorBlock( 1, 1, COLOR_MP );
        add( Mp );

        height = HEIGHT;
    }

    @Override
    protected void layout() {

        Bg.x = Mp.x = x;
        Bg.y = Mp.y = y;

        Bg.size( width, HEIGHT );
        Mp.size( width * mp, HEIGHT );

        height = HEIGHT;
    }

    public void level( float value ) {
        level( value, 0f );
    }

    public void level( float mp, float shield ){
        this.mp = mp;
        layout();
    }

    public void level(Char c){
        float mp = c.MP;
        float shield = c.SHLD;
        float max = Math.max(mp+shield, c.HT);

        level(mp/max, (mp+shield)/max);
    }
}

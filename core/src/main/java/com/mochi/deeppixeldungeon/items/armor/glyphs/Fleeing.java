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
package com.mochi.deeppixeldungeon.items.armor.glyphs;

import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Invisibility;
import com.mochi.deeppixeldungeon.items.armor.Armor;
import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite;
import com.watabou.utils.Random;

public class Fleeing extends Armor.Glyph {

    private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x2FFF9F );

    @Override
    public int proc( Armor armor, Char attacker, Char defender, int damage ) {

        int level = Math.max( 0, armor.level() );

        if (Random.Int( 3 ) == 0) {

            Buff.prolong( defender, Invisibility.class, 5 );
        }

        return damage;
    }


    @Override
    public int tierDRAdjust() {
        return -2;
    }

  @Override
    public ItemSprite.Glowing glowing() {
        return GREEN;
    }
}

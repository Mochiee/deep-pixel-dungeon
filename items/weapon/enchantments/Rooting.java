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
package com.mochi.deeppixeldungeon.items.weapon.enchantments;

import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.buffs.Buff;
import com.mochi.deeppixeldungeon.buffs.Paralysis;
import com.mochi.deeppixeldungeon.buffs.Roots;
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.effects.particles.EarthParticle;
import com.mochi.deeppixeldungeon.items.weapon.Weapon;
import com.mochi.deeppixeldungeon.sprites.sprites.ItemSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Rooting extends Weapon.Enchantment {

    private static ItemSprite.Glowing BROWN = new ItemSprite.Glowing( 0xB46C00 );

    @Override
    public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
        // lvl 0 - 13%
        // lvl 1 - 22%
        // lvl 2 - 30%
        int level = Math.max( 0, weapon.level() );

        if (Random.Int( level + 8 ) >= 7) {

            Buff.prolong( defender, Roots.class, Random.Float( 1, 2f + level ) );
            CellEmitter.bottom( defender.pos ).start( EarthParticle.FACTORY, 0.05f, 8 );

        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return BROWN;
    }
}

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
package com.mochi.deeppixeldungeon.items.weapon.melee;

import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;

public class Sorrowblade extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Sorrowblade;

        tier = 6;
    }

    @Override
    public int max(int lvl) {
        return  9*(tier) +    //54 base, up from 30
                lvl*(tier+1);   //scaling unchanged
    }

    @Override
    public int STRReq(int lvl) {
        lvl = Math.max(0, lvl);
        //20 base strength req, up from 18
        return (12 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
    }

}

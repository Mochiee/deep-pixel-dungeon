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

import com.mochi.deeppixeldungeon.sprites.sprites.ItemSpriteSheet;

public class SacrificialKnife extends MeleeWeapon {

    {
        image = ItemSpriteSheet.SacrificialKnife;

        tier = 1;
        DLY = 0.15f; //7~x speed
    }

    @Override
    public int max(int lvl) {
        return  2*(tier+1) +    //4 base, down from 10
                lvl*tier/2;       //+0.5 per level, down from +2
    }

}
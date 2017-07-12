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
package com.mochi.deeppixeldungeon.actors.mobs;

import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.items.Stylus;
import com.mochi.deeppixeldungeon.sprites.sprites.CharSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.DummySprite;
import com.mochi.deeppixeldungeon.sprites.sprites.RatSprite;
import com.watabou.utils.Random;

public class Dummy extends Mob {

    {
        spriteClass = DummySprite.class;

        HP = HT = 1999999999;
        defenseSkill = 0;

        maxLvl = 1999999999;

        state=PASSIVE;

        loot = new Stylus();
        lootChance = 3f;
    }

}

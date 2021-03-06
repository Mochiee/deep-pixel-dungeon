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

import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.buffs.Buff;
import com.mochi.deeppixeldungeon.buffs.Paralysis;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.weapon.Weapon;
import com.mochi.deeppixeldungeon.sprites.sprites.ItemSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class PinSol extends MeleeWeapon {

    {
        image = ItemSpriteSheet.PinSol;

        tier = 6;
    }

    @Override
    public int max(int lvl) {
        return 6 * (tier + 1) +    //42 base
                lvl * (tier + 1);   //7 per upgrade


        }


    }

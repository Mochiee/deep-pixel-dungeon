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
package com.mochi.deeppixeldungeon.items.weapon.missiles;

import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Cards extends MissileWeapon {

    {
        image = ItemSpriteSheet.Cards;

        bones = true; //Finding them in bones would be semi-frequent and disappointing. -Evan No Evan, cards are cool.
    }

    @Override
    public int min(int lvl) {
        return -50;
    }

    @Override
    public int max(int lvl) {
        return 50;
    }

    @Override
    public int STRReq(int lvl) {
        return 10;
    }

    public Cards() {
        this( 1 );
    }

    public Cards( int number ) {
        super();
        quantity = number;
    }

    @Override
    public Item random() {
        quantity = Random.Int( 16,32 );
        return this;
    }

    @Override
    public int price() {
        return quantity * 1;
    }
}

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
package com.mochi.deeppixeldungeon.items.potions;

import com.mochi.deeppixeldungeon.Badges;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.sprites.CharSprite;
import com.mochi.deeppixeldungeon.utils.GLog;

public class PotionOfPureDestruction extends Potion {

    {
        initials = 13;

        bones = true;
    }

    @Override
    public void apply( Hero hero ) {
        setKnown();

        hero.STR++;
        hero.STR++;
        hero.STR++;
        hero.STR++;
        hero.STR++;
        hero.STR++;
        hero.STR++;
        hero.STR++;
        hero.STR++;
        hero.STR++;


        hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "msg_1") );
        GLog.p( Messages.get(this, "msg_2") );

        Badges.validateStrengthAttained();
    }

    @Override
    public int price() {
        return isKnown() ? 50 * quantity : super.price();
    }
}

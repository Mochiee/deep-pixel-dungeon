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
package com.mochi.deeppixeldungeon.items.food;

import com.mochi.deeppixeldungeon.actors.buffs.BloodrageBuff;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.EarthImbue;
import com.mochi.deeppixeldungeon.actors.buffs.Hunger;
import com.mochi.deeppixeldungeon.actors.buffs.Slow;
import com.mochi.deeppixeldungeon.actors.buffs.Weakness;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.plants.Earthroot;
import com.mochi.deeppixeldungeon.plants.Sungrass;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class SpiritsChalice extends Food {

    {
        image = ItemSpriteSheet.SpiritsChalice;
        energy = Hunger.STARVING - Hunger.HUNGRY;
        hornValue = 1;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_EAT )) {
            effect(hero);
        }
    }

    public int price() {
        return 5 * quantity;
    }

    public static void effect(Hero hero) {
        switch (Random.Int(5)) {
            case 0:
                Buff.affect(hero, Sungrass.Health.class).boost(1);
                break;
            case 1:
                Buff.affect(hero, Earthroot.Armor.class);
                break;
            case 2:
                Buff.affect(hero, BloodrageBuff.class);
                break;
            case 3:
                Buff.affect(hero, EarthImbue.class);
                break;
            case 4:
                Buff.affect(hero, Slow.class);
                Buff.affect(hero, Weakness.class);

        }
    }
}
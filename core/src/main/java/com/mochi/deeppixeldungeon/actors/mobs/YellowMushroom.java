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

import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Toxic;
import com.mochi.deeppixeldungeon.items.potions.PotionOfPurity;
import com.mochi.deeppixeldungeon.sprites.ForestMobsSprites.YellowMushroomSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class YellowMushroom extends Mob {

    {
        spriteClass = YellowMushroomSprite.class;

        HP = HT = 12;
        defenseSkill = 11;

        maxLvl = 5;
        baseSpeed=2f;

        loot = PotionOfPurity.class;
        lootChance = 1f;
    }
    @Override
    public void die( Object cause ) {

        Dungeon.level.drop( new PotionOfPurity(), pos ).sprite.drop();

        super.die( cause );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 5 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 14;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 2);
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( Toxic.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}

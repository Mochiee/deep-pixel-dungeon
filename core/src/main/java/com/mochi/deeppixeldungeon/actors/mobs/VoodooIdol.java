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
import com.mochi.deeppixeldungeon.actors.buffs.Amok;
import com.mochi.deeppixeldungeon.items.Generator;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfRage;
import com.mochi.deeppixeldungeon.plants.Sorrowmoss;
import com.mochi.deeppixeldungeon.sprites.IdolSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class VoodooIdol extends Mob {

    {
        spriteClass = IdolSprite.class;

        state = PASSIVE;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.DEMONIC);

        HP = HT = Dungeon.hero.HT - 1;
        defenseSkill = 0;

        state = PASSIVE;

        loot = Generator.Category.IDOL;
        lootChance = 1f;

        maxLvl = 5;
    }
    @Override
    protected boolean act() {

        rooted = true;

        return super.act();
    }

     @Override
    public int damageRoll() {
        return Random.NormalIntRange(0, 0);
    }

    @Override
    public int attackSkill(Char target) {
        return 0;
    }

    @Override
    public void damage(int dmg, Object src) {

        Dungeon.hero.damage(dmg, src);

        super.damage(dmg, src);
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(ScrollOfRage.class);
        IMMUNITIES.add(Sorrowmoss.class);

    }
}

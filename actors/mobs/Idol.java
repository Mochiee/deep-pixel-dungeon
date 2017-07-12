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

import android.provider.DocumentsContract;

import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.buffs.Amok;
import com.mochi.deeppixeldungeon.buffs.Buff;
import com.mochi.deeppixeldungeon.buffs.Levitation;
import com.mochi.deeppixeldungeon.buffs.Roots;
import com.mochi.deeppixeldungeon.buffs.Slow;
import com.mochi.deeppixeldungeon.items.Generator;
import com.mochi.deeppixeldungeon.items.artifacts.UnstableSpellbook;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfRage;
import com.mochi.deeppixeldungeon.items.weapon.missiles.ShadowShard;
import com.mochi.deeppixeldungeon.plants.Sorrowmoss;
import com.mochi.deeppixeldungeon.sprites.sprites.IdolSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.StatueSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Idol extends Mob {

    {
        spriteClass = IdolSprite.class;

        state = PASSIVE;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.DEMONIC);

        HP = HT = 12;
        defenseSkill = 0;
        baseSpeed = 0.0000001f;

        loot = Generator.Category.IDOL;
        lootChance = 1f;

        maxLvl = 5;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(666, 666);
    }

    @Override
    public int attackSkill(Char target) {
        return 999;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (state == PASSIVE) {
            state = HUNTING;

            if (state == HUNTING)
                rooted = true;
        }

        super.damage(dmg, src);
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(ScrollOfRage.class);
        IMMUNITIES.add(Sorrowmoss.class);

    }
}

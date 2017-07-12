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
import com.mochi.deeppixeldungeon.items.upgrade.WeaponGhostShard;
import com.mochi.deeppixeldungeon.sprites.GhostSprite;
import com.watabou.utils.Random;

public class Spirit extends Mob {


    {
        spriteClass = GhostSprite.class;

        HP = HT = Dungeon.hero.lvl*3;
        defenseSkill = Dungeon.hero.lvl*3;
        EXP = 1;
        loot = new WeaponGhostShard();
        lootChance = 1f;

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( Dungeon.hero.HT/10, Dungeon.hero.HT/10 );
    }

    @Override
    public int attackSkill( Char target ) {
        return Dungeon.hero.lvl*2 + Dungeon.hero.lvl/2;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.hero.lvl/2);
    }
}

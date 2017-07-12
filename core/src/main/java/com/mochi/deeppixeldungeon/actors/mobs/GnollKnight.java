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
import com.mochi.deeppixeldungeon.items.Gold;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.GnollMobsSprites.GnollKnightSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class GnollKnight extends Mob {

    {
        spriteClass = GnollKnightSprite.class;

        HP = HT = 50;
        defenseSkill = 15;

        EXP = 7;

        loot = Gold.class;
        lootChance = 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 4, 17 );
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {

        if (Random.Int(2) == 0) {
            defenseSkill = 7;
            GLog.p(Messages.get(this, "attacked"));

        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(2) == 0) {
            defenseSkill = 25;
            GLog.n(Messages.get(this, "guard"));
        }
     return damage;
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }
}

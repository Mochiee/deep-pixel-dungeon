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
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.Gold;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.GnollThiefBossSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;

public class GnollNinja extends Mob {

    {
        spriteClass = GnollThiefBossSprite.class;

        HP = HT = 90;
        defenseSkill = 6;

        EXP = 0;
        maxLvl = 15;

        loot = Gold.class;
        lootChance = 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 9);
    }

    @Override
    public int attackSkill(Char target) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
        return attack.collisionPos == enemy.pos;
    }

    public static class Quest {

        private static boolean spawned;
        public static boolean GnollNinjaKilled;

        public static void reset() {
            spawned = false;
        }

        private static final String SPAWNED = "spawned";

        public static void storeInBundle(Bundle bundle) {

            Bundle node = new Bundle();

            node.put(SPAWNED, spawned);

        }
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        if (HP < 60) {
            die(null);
            GLog.n(Messages.get(this, "escape"));
            CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
        }
        return super.defenseProc(enemy, damage);
    }
}







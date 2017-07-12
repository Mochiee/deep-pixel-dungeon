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
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.Gold;
import com.mochi.deeppixeldungeon.levels.SewerLevel;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.GnollThiefBossSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;

public class GnollNinja1 extends Mob {

    {
        spriteClass = GnollThiefBossSprite.class;

        HT = 90;
        HP = 60;
        defenseSkill = 9;

        EXP = 0;
        maxLvl = 15;

        loot = Gold.class;
        lootChance = 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 10);
    }

    @Override
    public int attackSkill(Char target) {
        return 16;
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
        public static boolean GnollNinja1Killed;

        public static void reset() {
            spawned = false;
        }

        private static final String SPAWNED = "spawned";
        private static final String NODE = "node";

        public static void storeInBundle(Bundle bundle) {

            Bundle node = new Bundle();

            node.put(SPAWNED, spawned);
            bundle.put(NODE,node);
        }

        public static void restoreFromBundle(Bundle bundle) {

            Bundle node = bundle.getBundle(NODE);

            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
            }
        }

        public static boolean spawn(SewerLevel level) {
            if ((GnollNinja.Quest.GnollNinjaKilled && Dungeon.depth > 1 && (2 - Dungeon.depth) == 0) && !spawned) {

                GnollNinja1 gnollNinja1 = new GnollNinja1();
                do {
                    gnollNinja1.pos = level.randomRespawnCell();
                } while (gnollNinja1.pos == -1);
                level.mobs.add(gnollNinja1);

            }
            return false;

        }
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        if (HP < 30) {
            die(null);
            GLog.n(Messages.get(this, "escape"));
            CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        Quest.GnollNinja1Killed = true;
    }
}







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
import com.mochi.deeppixeldungeon.items.quest.RatSkull;
import com.mochi.deeppixeldungeon.levels.SewerLevel;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.GnollThiefBossSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;

public class GnollNinja2 extends Mob {

    {
        spriteClass = GnollThiefBossSprite.class;

        HT = 90;
        HP = 30;
        defenseSkill = 9;

        EXP = 9;
        maxLvl = 15;
        baseSpeed = 2f;

        loot = RatSkull.class;
        lootChance = 3f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 12);
    }

    @Override
    public int attackSkill(Char target) {
        return 17;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
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
            if ((GnollNinja1.Quest.GnollNinja1Killed && Dungeon.depth > 2 && (3 - Dungeon.depth) == 0) && !spawned) {

                GnollNinja2 gnollNinja2 = new GnollNinja2();
                do {
                    gnollNinja2.pos = level.randomRespawnCell();
                } while (gnollNinja2.pos == -1);
                level.mobs.add(gnollNinja2);

            }
            return false;

        }
    }
    @Override
    public void die( Object cause ) {
        GLog.p(Messages.get(this, "dead"));

        }
    }








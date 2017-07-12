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
import com.mochi.deeppixeldungeon.actors.buffs.Blindness;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Slow;
import com.mochi.deeppixeldungeon.items.keys.ChestKeys.CursedKey;
import com.mochi.deeppixeldungeon.items.keys.ChestKeys.ElvenKey;
import com.mochi.deeppixeldungeon.items.keys.ChestKeys.RoughKey;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Grim;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.KeyCreatureSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class KeyCreature extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 1f;

    {
        spriteClass = KeyCreatureSprite.class;

        if (Dungeon.depth<12) {
            HP = HT = 20;
        } else HP = HT = Dungeon.depth*2 - Dungeon.depth/4;

        defenseSkill = Dungeon.depth;

        EXP = 1;
        maxLvl = 25;

        loot = RoughKey.class;
        lootChance = 1;

        properties.add(Property.DEMONIC);
    }

    private int TurnCounter;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 1 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 99;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    protected boolean act() {

        TurnCounter+=1;

        if (TurnCounter>10){
            GLog.p("A soft singing can be heard from a distance");
        }

        if (TurnCounter>15) {
            GLog.p("Jingling of keys...");
        }

        if (TurnCounter>20){
            TurnCounter = -10;
        }

        return super.act();
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap( enemy.pos );
            } else {
                zap();
            }

            return !visible;
        }
    }

    private void zap() {
        spend(TIME_TO_ZAP);

        if (hit(this, enemy, true)) {

            if (Random.Int(3) < 2) {
                Buff.prolong(enemy, Blindness.class, 2);
            } else Buff.affect(enemy, Slow.class, 2);

            int dmg = Random.Int(1, 1);
            enemy.damage(dmg, this);

            if (!enemy.isAlive() && enemy == Dungeon.hero) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "bolt_kill"));
            }
        }
    }


    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (Random.Int(100)<11){
        Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new RoughKey(),pos ).sprite.drop();
        } else if (Random.Int(100)<26) {
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new RoughKey(),pos ).sprite.drop();
        } else if (Random.Int(100)<51) {
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new RoughKey(),pos ).sprite.drop();
        } else if (Random.Int(100)<2){
            Dungeon.level.drop( new CursedKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();
            Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();

        } else  Dungeon.level.drop( new ElvenKey(),pos ).sprite.drop();

        if (Random.Int(100)<26) {
            if (Random.Int(100) < 6) {
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new RoughKey(), pos).sprite.drop();
            } else if (Random.Int(100) < 16) {
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new RoughKey(), pos).sprite.drop();
            } else if (Random.Int(100) < 26) {
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new RoughKey(), pos).sprite.drop();
            } else if (Random.Int(100) < 2) {
                Dungeon.level.drop(new CursedKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();

            } else Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
        }

        if (Random.Int(100)<26) {
            if (Random.Int(100) < 6) {
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new RoughKey(), pos).sprite.drop();
            } else if (Random.Int(100) < 11) {
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new RoughKey(), pos).sprite.drop();
            } else if (Random.Int(100) < 16) {
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new RoughKey(), pos).sprite.drop();
            } else if (Random.Int(200) < 2) {
                Dungeon.level.drop(new CursedKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
                Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();

            } else Dungeon.level.drop(new ElvenKey(), pos).sprite.drop();
        }
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    static {
        RESISTANCES.add( Grim.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }
}

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

import com.mochi.deeppixeldungeon.Badges;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.buffs.Buff;
import com.mochi.deeppixeldungeon.buffs.Ooze;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.keys.IronKey;
import com.mochi.deeppixeldungeon.items.keys.SkeletonKey;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.sprites.DarkRatSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.GooSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.RatSprite;
import com.mochi.deeppixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Goolem extends Mob {

    {
        spriteClass = DarkRatSprite.class;

        HP = HT = 70;
        EXP = 8;
        defenseSkill = 6;

        maxLvl = 5;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(7, 10);
    }

    @Override
    public int attackSkill(Char target) {
        return 13;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

    @Override
    public boolean act() {

        if (Level.water[pos] && HP < HT) {
            sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
        }
        HP++;
        return super.act();

    }
    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int( 4 ) == 0) {
            Buff.affect( enemy, Ooze.class );
            enemy.sprite.burst( 0x000000, 5 );
        }

        return damage;
    }
    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.unseal();

        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos ).sprite.drop();

    }
}

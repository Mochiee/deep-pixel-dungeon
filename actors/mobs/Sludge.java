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
import com.mochi.deeppixeldungeon.buffs.Buff;
import com.mochi.deeppixeldungeon.buffs.Ooze;
import com.mochi.deeppixeldungeon.buffs.Speed;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.sprites.sprites.DarkRatSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.GooSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.RatSprite;
import com.mochi.deeppixeldungeon.sprites.sprites.SludgeSprite;
import com.mochi.deeppixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Sludge extends Mob {

    {
        spriteClass = SludgeSprite.class;

        HP = HT = 25;
        state = HUNTING;
        defenseSkill = 0;
        rooted = true;
        baseSpeed = 0.5f;

        maxLvl = 5;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(0, 0);
    }

    @Override
    public int attackSkill(Char target) {
        return 999;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(1) == 0) {
            Buff.affect(enemy, Ooze.class);
            enemy.sprite.burst(0x000000, 5);
        }

        return damage;
    }

}





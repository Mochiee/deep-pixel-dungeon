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
import com.mochi.deeppixeldungeon.sprites.CavesMobsSprites.DM300Sprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Dummy extends Mob {
private int TurnCounter;
    {
        spriteClass = DM300Sprite.class;

        HP = HT = 1999999999;
        defenseSkill = 0;
        baseSpeed = 1f;
        state = PASSIVE;
        EXP = 0;
        maxLvl = 0;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 0, 0 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    protected boolean act() {
        //heals 1 health per turn
        HP = Math.min( HT, HP+1 );

        TurnCounter+=1;

        if (TurnCounter==3) {
            GLog.n("DAMAGE OUTPUT+1");
            baseSpeed=2;
        }

        if (TurnCounter==4){
            GLog.n("DAMAGE OUTPUT-1");
            TurnCounter-=5;
            baseSpeed=1;
        }

        return super.act();
    }
}

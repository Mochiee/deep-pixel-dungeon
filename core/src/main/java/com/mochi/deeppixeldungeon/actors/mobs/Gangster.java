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
import com.mochi.deeppixeldungeon.sprites.PrisonMobSprites.FemaleGangsterSprite;
import com.mochi.deeppixeldungeon.sprites.PrisonMobSprites.MaleGangsterSprite;
import com.watabou.utils.Random;

public class Gangster extends Mob {

    {
        if (Random.Int(2)==0) {
            spriteClass = MaleGangsterSprite.class;
        }
        else {
            spriteClass = FemaleGangsterSprite.class;
        }

        HP = HT = 40;
        defenseSkill = 12;

        EXP = 5;
        maxLvl = 16;

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(10, 25);
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 6);
    }

}

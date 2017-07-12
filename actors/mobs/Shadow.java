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
import com.mochi.deeppixeldungeon.actors.blobs.Blob;
import com.mochi.deeppixeldungeon.actors.blobs.ParalyticGas;
import com.mochi.deeppixeldungeon.actors.blobs.StenchGas;
import com.mochi.deeppixeldungeon.items.Stylus;
import com.mochi.deeppixeldungeon.items.food.MysteryMeat;
import com.mochi.deeppixeldungeon.items.potions.PotionOfParalyticGas;
import com.mochi.deeppixeldungeon.items.weapon.missiles.ShadowShard;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.sprites.ShadowSprite;
import com.watabou.utils.Random;

public class Shadow extends Mob {

    {
        spriteClass = ShadowSprite.class;

        HP = HT = 10;
        defenseSkill = 5;
        baseSpeed = 2f;

        EXP = 17;
        maxLvl = 25;

        loot = new PotionOfParalyticGas();
        lootChance = 3f;
    }
    @Override
    public int defenseProc( Char enemy, int damage ) {

        GameScene.add(Blob.seed(pos, 20, ParalyticGas.class));

        return super.defenseProc(enemy, damage);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 8 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }
}

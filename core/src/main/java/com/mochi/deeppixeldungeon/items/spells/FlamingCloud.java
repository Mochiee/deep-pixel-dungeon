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
package com.mochi.deeppixeldungeon.items.spells;

import com.mochi.deeppixeldungeon.items.weapon.melee.MagesStaff;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.Blob;
import com.mochi.deeppixeldungeon.actors.blobs.FireBlob;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Blazing;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

public class FlamingCloud extends Spell {

    {
        image = ItemSpriteSheet.WAND_FROST;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;

        MP_cost = 15;
    }

    @Override
    protected void onCast(Ballistica bolt) {
        if (Dungeon.hero.MP >= MP_cost) {
            Dungeon.hero.MP -= MP_cost;
            Blob frozenCloud = Blob.seed(bolt.collisionPos, 50 + 5 * level() + Dungeon.hero.MAG, FireBlob.class);
            GameScene.add(frozenCloud);

            for (int i : PathFinder.NEIGHBOURS9) {
                Char ch = Actor.findChar(bolt.collisionPos + i);
                if (ch != null) {
                }
            }
        } else
            GLog.n(Messages.get(Spell.class, "nomp"));

    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //acts like blazing enchantment
        new Blazing().proc(staff, attacker, defender, damage);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0xEE7722 );
        particle.am = 0.5f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, -40);
        particle.setSize( 0f, 3f);
        particle.shuffleXY( 1.5f );
    }
}




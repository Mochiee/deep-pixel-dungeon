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

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Venomous;
import com.mochi.deeppixeldungeon.items.weapon.melee.MagesStaff;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.Blob;
import com.mochi.deeppixeldungeon.actors.blobs.VenomGas;
import com.mochi.deeppixeldungeon.effects.MagicMissile;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;

public class

VenomCloud extends Spell {

    {
        image = ItemSpriteSheet.WAND_VENOM;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;

        MP_cost = 10;
    }

    @Override
    protected void onCast(Ballistica bolt) {
        if (Dungeon.hero.MP >= MP_cost) {
            Dungeon.hero.MP-=MP_cost;
            Blob venomGas = Blob.seed(bolt.collisionPos, 50 + 5 * level() + Dungeon.hero.MAG, VenomGas.class);
            GameScene.add(venomGas);

            for (int i : PathFinder.NEIGHBOURS9) {
                Char ch = Actor.findChar(bolt.collisionPos + i);
                if (ch != null) {
                }
            }
        } else
            GLog.n(Messages.get(Spell.class,"nomp"));
    }

    protected void fx( Ballistica bolt, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.POISON,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //acts like venomous enchantment
        new Venomous().proc(staff, attacker, defender, damage);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random( 0x8844FF, 0x00FF00) );
        particle.am = 0.6f;
        particle.setLifespan( 1f );
        particle.acc.set(0, 20);
        particle.setSize( 0.5f, 2f);
        particle.shuffleXY( 1f );
    }
}

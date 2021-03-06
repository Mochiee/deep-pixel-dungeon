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
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Amok;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Corruption;
import com.mochi.deeppixeldungeon.effects.MagicMissile;
import com.mochi.deeppixeldungeon.items.weapon.melee.MagesStaff;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Possess extends Spell {

    {
        image = ItemSpriteSheet.WAND_CORRUPTION;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;

        MP_cost = 15;
    }

    @Override
    protected void onCast(Ballistica bolt) {
        Char ch = Actor.findChar(bolt.collisionPos);

        if (ch != null){

                if(ch.buff(Corruption.class) != null){
                GLog.w( Messages.get(this, "already_corrupted") );
                return;
            }

            if (ch.properties().contains(Char.Property.BOSS) || ch.properties().contains(Char.Property.MINIBOSS)){
                GLog.w( Messages.get(this, "boss") );
                return;
            }

            int basePower = 10 + 2*level();
            int mobPower = Random.IntRange(0, ch.HT) + ch.HP*2;
            for ( Buff buff : ch.buffs()){
                if (buff.type == Buff.buffType.NEGATIVE){
                    mobPower *= 0.67;
                    break;
                }
            }

            int extraCharges = 0;
            //try to use extra charges to overpower the mob
            while (basePower <= mobPower){
                extraCharges++;
                basePower += 5 + level();
            }

            //if we fail, lose all charges, remember we have 1 left to lose from using the wand.
            if (extraCharges >= curCharges){
                curCharges = 1;
                GLog.w( Messages.get(this, "fail") );
                return;
            }

            //otherwise corrupt the mob & spend charges
            Buff.append(ch, Corruption.class);
            ch.HP = ch.HT;
            curCharges -= extraCharges;
            usagesToKnow -= extraCharges;

            processSoulMark(ch, extraCharges+chargesPerCast());
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        // lvl 0 - 25%
        // lvl 1 - 40%
        // lvl 2 - 50%
        if (Random.Int( level() + 4 ) >= 3){
            Buff.prolong( defender, Amok.class, 3+level());
        }
    }

    protected void fx( Ballistica bolt, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.SHADOW,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0 );
        particle.am = 0.6f;
        particle.setLifespan(2f);
        particle.speed.set(0, 5);
        particle.setSize( 0.5f, 2f);
        particle.shuffleXY(1f);
    }

}

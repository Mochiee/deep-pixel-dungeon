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
        import com.mochi.deeppixeldungeon.items.weapon.melee.MagesStaff;
        import com.mochi.deeppixeldungeon.Dungeon;
        import com.mochi.deeppixeldungeon.actors.Actor;
        import com.mochi.deeppixeldungeon.actors.Char;
        import com.mochi.deeppixeldungeon.actors.blobs.Blob;
        import com.mochi.deeppixeldungeon.actors.blobs.FreezeBlob;
        import com.mochi.deeppixeldungeon.actors.buffs.Buff;
        import com.mochi.deeppixeldungeon.actors.buffs.Chill;
        import com.mochi.deeppixeldungeon.actors.buffs.FlavourBuff;
        import com.mochi.deeppixeldungeon.actors.buffs.Frost;
        import com.mochi.deeppixeldungeon.effects.MagicMissile;
        import com.mochi.deeppixeldungeon.mechanics.Ballistica;
        import com.mochi.deeppixeldungeon.messages.Messages;
        import com.mochi.deeppixeldungeon.scenes.GameScene;
        import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
        import com.mochi.deeppixeldungeon.utils.GLog;
        import com.watabou.noosa.audio.Sample;
        import com.watabou.utils.Callback;
        import com.watabou.utils.PathFinder;
        import com.watabou.utils.PointF;
        import com.watabou.utils.Random;

public class FrozenCloud extends Spell {

    {
        image = ItemSpriteSheet.WAND_FROST;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;

        MP_cost = 15;
    }

    @Override
    protected void onCast(Ballistica bolt) {
        if (Dungeon.hero.MP > MP_cost - 1) {
            Dungeon.hero.MP -= MP_cost;
            Blob frozenCloud = Blob.seed(bolt.collisionPos, 50 + 10 * level(), FreezeBlob.class);
            GameScene.add(frozenCloud);

            for (int i : PathFinder.NEIGHBOURS9) {
                Char ch = Actor.findChar(bolt.collisionPos + i);
                if (ch != null) {
                }
            }
        } else
            GLog.n(Messages.get(Spell.class, "nomp"));
    }

    protected void fx( Ballistica bolt, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FROST,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }


    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Chill chill = defender.buff(Chill.class);
        if (chill != null && Random.IntRange(2, 10) > chill.cooldown()){
            //need to delay this through an actor so that the freezing isn't broken by taking damage from the staff hit.
            new FlavourBuff(){
                {actPriority = Integer.MIN_VALUE;}
                public boolean act() {
                    Buff.affect(target, Frost.class, Frost.duration(target) * Random.Float(1f, 2f));
                    return super.act();
                }
            }.attachTo(defender);
        }
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x88CCFF);
        particle.am = 0.6f;
        particle.setLifespan(2f);
        float angle = Random.Float(PointF.PI2);
        particle.speed.polar( angle, 2f);
        particle.acc.set( 0f, 1f);
        particle.setSize( 0f, 1.5f);
        particle.radiateXY(Random.Float(1f));
    }


}


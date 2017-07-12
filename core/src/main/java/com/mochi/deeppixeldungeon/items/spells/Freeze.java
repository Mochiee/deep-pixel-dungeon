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
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Chill;
import com.mochi.deeppixeldungeon.actors.buffs.FlavourBuff;
import com.mochi.deeppixeldungeon.actors.buffs.Frost;
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.Lightning;
import com.mochi.deeppixeldungeon.effects.particles.SparkParticle;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.utils.BArray;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Freeze extends DamageSpell {

    {
        image = ItemSpriteSheet.WAND_FIREBOLT;

        MP_cost = 3;
    }

    private ArrayList<Char> affected = new ArrayList<>();

    ArrayList<Lightning.Arc> arcs = new ArrayList<>();

    public int min(int lvl){
        return 0;
    }

    public int max(int lvl){ return 0; }

    @Override
    protected void onCast( Ballistica bolt ) {
        if (Dungeon.hero.MP > MP_cost - 1) {

            int min = 0;
            int max = 0;

            for (Char ch : affected) {
                Buff.prolong(ch, Frost.class, 5 + level()*2);

                if (ch == Dungeon.hero) Camera.main.shake(2, 0.3f);
                ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                ch.sprite.flash();
            }

            if (!curUser.isAlive()) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "ondeath"));
            }
        } else
            GLog.n(Messages.get(Spell.class,"nomp"));
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

    private void arc(Char ch) {

        affected.add(ch);

        int dist;
        if (Level.water[ch.pos] && !ch.flying)
            dist = 2;
        else
            dist = 1;

        PathFinder.buildDistanceMap(ch.pos, BArray.not(Level.solid, null), dist);
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char n = Actor.findChar(i);
                if (n == Dungeon.hero && PathFinder.distance[i] > 1)
                    //the hero is only zapped if they are adjacent
                    continue;
                else if (n != null && !affected.contains(n)) {
                    arcs.add(new Lightning.Arc(ch.pos, n.pos));
                    arc(n);
                }
            }
        }
    }

    @Override
    protected void fx( Ballistica bolt, Callback callback ) {

        affected.clear();
        arcs.clear();

        if (Dungeon.hero.MP > MP_cost-1) {
            arcs.add(new Lightning.Arc(bolt.sourcePos, bolt.collisionPos));

            int cell = bolt.collisionPos;

            Char ch = Actor.findChar(cell);
            if (ch != null) {
                arc(ch);
            } else {
                CellEmitter.center(cell).burst(SparkParticle.FACTORY, 3);
            }
        }
        //don't want to wait for the effect before processing damage.
        curUser.sprite.parent.add( new Lightning( arcs, null ) );
        callback.call();
    }

}

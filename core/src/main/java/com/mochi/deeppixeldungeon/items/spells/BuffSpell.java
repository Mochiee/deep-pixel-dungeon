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
import com.mochi.deeppixeldungeon.items.bags.Bag;
import com.mochi.deeppixeldungeon.items.bags.WandHolster;
import com.mochi.deeppixeldungeon.items.potions.Potion;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.LockedFloor;
import com.mochi.deeppixeldungeon.actors.buffs.Recharging;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class BuffSpell extends Item {

    private static final int USAGES_TO_KNOW = 20;

    public static final String AC_CAST = "CAST";

    public int MP_cost = 5;

    public int Cooldown;

    public int maxCharges = initialCharges();
    public int curCharges = maxCharges;

    protected int usagesToKnow = USAGES_TO_KNOW;

    private boolean curChargeKnown = false;

    private static float TIME_TO_CAST = 1f;

    public float partialCharge = 0f;

    protected int collisionProperties = Ballistica.MAGIC_BOLT;

    protected BuffSpell.Charger charger;

    {
        defaultAction = AC_CAST;
        usesTargeting = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_CAST);
        return actions;
    }

    private static final String UNFAMILIRIARITY     = "unfamiliarity";
    private static final String CUR_CHARGES			= "curCharges";
    private static final String CUR_CHARGE_KNOWN	= "curChargeKnown";
    private static final String PARTIALCHARGE 		= "partialCharge";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( UNFAMILIRIARITY, usagesToKnow );
        bundle.put( CUR_CHARGES, curCharges );
        bundle.put( CUR_CHARGE_KNOWN, curChargeKnown );
        bundle.put( PARTIALCHARGE , partialCharge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if ((usagesToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
            usagesToKnow = USAGES_TO_KNOW;
        }
        curCharges = bundle.getInt( CUR_CHARGES );
        curChargeKnown = bundle.getBoolean( CUR_CHARGE_KNOWN );
        partialCharge = bundle.getFloat( PARTIALCHARGE );
    }

    @Override
    public void onDetach( ) {
        stopCharging();
    }

    public void stopCharging() {
        if (charger != null) {
            charger.detach();
            charger = null;
        }
    }

    @Override
    public void execute(final Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_CAST)) {
            cast(hero);
        }
    }


    protected void cast(Hero hero) {

        if (curCharges > maxCharges-1 && Dungeon.hero.MP > MP_cost) {
            curCharges-=maxCharges;
            hero.spend(TIME_TO_CAST);
            hero.busy();
            apply(hero);

        } else {

            GLog.n("No Cooldown");
        }

        hero.sprite.operate(hero.pos);

        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    public void apply(Hero hero) {
        shatter(hero.pos);
    }

    public void shatter(int cell) {
        if (Dungeon.visible[cell]) {
            GLog.i(Messages.get(Potion.class, "shatter"));
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }
    }

    public void updateLevel() {
        maxCharges = Math.min(initialCharges() + level(), 10);
        curCharges = Math.min(curCharges, maxCharges);
    }

    protected int initialCharges() {
        return 2;
    }

    protected int chargesPerCast() {
        return curCharges;
    }

    @Override
    public String status() {
        if (levelKnown) {
            return curCharges + "/" + maxCharges;
        } else {
            return null;
        }
    }

    @Override
    public boolean collect( Bag container ) {
        if (super.collect( container )) {
            if (container.owner != null) {
                if (container instanceof WandHolster)
                    charge( container.owner, ((WandHolster) container).HOLSTER_SCALE_FACTOR );
                else
                    charge( container.owner );
            }
            return true;
        } else {
            return false;
        }
    }

    public void charge( Char owner ) {
        if (charger == null) charger = new BuffSpell.Charger();
        charger.attachTo( owner );
    }

    public void charge( Char owner, float chargeScaleFactor ){
        charge( owner );
        charger.setScaleFactor( chargeScaleFactor );
    }


    public class Charger extends Buff {

        private static final float BASE_CHARGE_DELAY = 1f;
        private static final float SCALING_CHARGE_ADDITION = 1f;
        private static final float NORMAL_SCALE_FACTOR = 1f;

        private static final float CHARGE_BUFF_BONUS = 1f;

        float scalingFactor = NORMAL_SCALE_FACTOR;

        @Override
        public boolean attachTo( Char target ) {
            super.attachTo( target );

            return true;
        }

        @Override
        public boolean act() {
            if (curCharges < maxCharges)
                recharge();

            if (partialCharge >= 1 && curCharges < maxCharges) {
                partialCharge--;
                curCharges++;
                updateQuickslot();
            }

            spend( TICK );

            return true;
        }

        private void recharge(){
            int missingCharges = maxCharges - curCharges;

            float turnsToCharge = (float) (BASE_CHARGE_DELAY
                    + (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));

            LockedFloor lock = target.buff(LockedFloor.class);
            if (lock == null || lock.regenOn())
                partialCharge += 1f/turnsToCharge;

            Recharging bonus = target.buff(Recharging.class);
            if (bonus != null && bonus.remainder() > 0f){
                partialCharge += CHARGE_BUFF_BONUS * bonus.remainder();
            }
        }

        public void gainCharge(float charge){
            partialCharge += charge;
            while (partialCharge >= 1f){
                curCharges++;
                partialCharge--;
            }
            curCharges = Math.min(curCharges, maxCharges);
            updateQuickslot();
        }

        private void setScaleFactor(float value){
            this.scalingFactor = value;
        }
    }
}

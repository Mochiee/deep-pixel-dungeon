/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.mochi.deeppixeldungeon.items.weapon;

import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.items.weapon.melee.MeleeWeapon;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class ArtifactWeapon extends Weapon {



        @Override
        public int min(int lvl) {
            return  tier +  //base
                    lvl;    //level scaling
        }

        @Override
        public int max(int lvl) {
            return  5*(tier+1) +    //base
                    lvl*(tier+1);   //level scaling
        }

        @Override
        public Item upgrade() {
            return upgrade( false );
        }

        public Item safeUpgrade() {
            return upgrade( enchantment != null );
        }

        public int STRReq(int lvl){
            lvl = Math.max(0, lvl);
            //strength req decreases at +1,+3,+6,+10,etc.
            return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
        }


        private static final int HITS_TO_KNOW    = 20;

        private static final String TXT_TO_STRING		= "%s :%d";

        public float    ACC = 1f;	// Accuracy modifier
        public float	DLY	= 1f;	// Speed modifier
        public int      RCH = 1;    // Reach modifier (only applies to melee hits)


        public int tier;

        private static final float TIME_TO_EQUIP = 1f;

        public Buff passiveBuff;
        protected Buff activeBuff;

        public int charge = 0;

        public int chargeCap = 0;

        protected int cooldown = 0;

        @Override
        public String info() {

            String info = desc();

            if (levelKnown) {
                info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, imbue.damageFactor(min()), imbue.damageFactor(max()), STRReq());
                if (STRReq() > Dungeon.hero.STR()) {
                    info += " " + Messages.get(Weapon.class, "too_heavy");
                } else if (Dungeon.hero.STR() > STRReq()){
                    info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
                }
            } else {
                info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
                if (STRReq(0) > Dungeon.hero.STR()) {
                    info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
                }
            }

            String stats_desc = Messages.get(this, "stats_desc");
            if (!stats_desc.equals("")) info+= "\n\n" + stats_desc;

            switch (imbue) {
                case LIGHT:
                    info += "\n\n" + Messages.get(Weapon.class, "lighter");
                    break;
                case HEAVY:
                    info += "\n\n" + Messages.get(Weapon.class, "heavier");
                    break;
                case NONE:
            }

            if (enchantment != null && (cursedKnown || !enchantment.curse())){
                info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
                info += " " + Messages.get(enchantment, "desc");
            }

            if (cursed && isEquipped( Dungeon.hero )) {
                info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
            } else if (cursedKnown && cursed) {
                info += "\n\n" + Messages.get(Weapon.class, "cursed");
            }

            return info;
        }

        @Override
        public boolean doEquip(Hero hero) {

            activate(hero);

            return super.doEquip(hero);

        }

        public void activate(Hero hero) {
            passiveBuff = passiveBuff();
            passiveBuff.attachTo(hero);

        }

        @Override
        public boolean doUnequip(Hero hero, boolean collect, boolean single) {

            if (super.doUnequip(hero, collect, single)) {

                if (passiveBuff != null) {
                    passiveBuff.detach();
                    passiveBuff = null;
                }

                hero.belongings.weapon = null;
                return true;

            } else {

                return false;

            }
        }


        protected WeaponBuff passiveBuff() {
            return null;

        }

        public class WeaponBuff extends Buff {

            private static final String CHARGE = "charge";

            @Override
            public void storeInBundle(Bundle bundle) {
                super.storeInBundle(bundle);
                bundle.put(CHARGE, charge);
            }

            @Override
            public void restoreFromBundle(Bundle bundle) {
                super.restoreFromBundle(bundle);
                charge = bundle.getInt(CHARGE);
            }
            public int STRReq(int lvl) {
                return 0;
            }
        }
    }


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
package com.mochi.deeppixeldungeon.items.weapon.melee;

import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.items.weapon.ArtifactWeapon;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.ui.BuffIndicator;
import com.mochi.deeppixeldungeon.utils.GLog;

import java.util.ArrayList;


public class Immortality extends ArtifactWeapon {
    protected float partialCharge = 0;

    {
        image = ItemSpriteSheet.QUARTERSTAFF;

        tier = 5;

        charge = 0;
        chargeCap = 99;

        defaultAction = AC_HEAL;

    }

    public static final String AC_HEAL = "HEAL";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && !cursed)
            actions.add(AC_HEAL);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_HEAL) && charge > 19) {
            GLog.p("You are rejuvenated.");
            charge -= 20 - shardlvl; //-1 charge per shard level
            Dungeon.hero.HP += Dungeon.hero.HT / 7 + level() * 3 + (Dungeon.hero.HT / 7 + level() * 3)*shardlvl/10 ; //+10% bonus healing per shard letter
            hero.spend(3f);
            if (Dungeon.hero.HP>Dungeon.hero.HT){
                Dungeon.hero.HP-=Dungeon.hero.HP-Dungeon.hero.HT;
            }
        } else {
            super.execute(hero, action);
        }
        if (action.equals(AC_HEAL) && charge <(20 - shardlvl)) {
            GLog.n("Immortality shines weakly.");
        }
    }

    @Override
    public int max(int lvl) {
        return (tier + 1) +  shardlvl +  //7 base, down from 40 + 1`per shard level
               lvl * (tier - 2);  //+5 per level, down from +7
    }


    public class RegenCounter extends WeaponBuff {

        @Override
        public boolean act() {
            if (charge < chargeCap) {
                charge+=1;
                if (charge >= chargeCap) {
                    GLog.w("Immortality glimmers in your bag.");
                }
                updateQuickslot();
            }
            spend(TICK);
            return true;
        }

        @Override
        public int icon() {
            if (cooldown == 0)
                return BuffIndicator.NONE;
            else
                return BuffIndicator.NONE;
        }

        @Override
        public void detach() {
            cooldown = 0;
            charge = 0;
            super.detach();
        }
    }


    @Override
    protected WeaponBuff passiveBuff() {
        return new RegenCounter();
    }

    @Override
    public boolean doEquip(Hero hero) {

        activate(hero);

        return super.doEquip(hero);

    }

    @Override
    public String info() {

        final String p = "\n\n";

        StringBuilder info = new StringBuilder(desc());

        if (charge >= chargeCap) {
            info.append("\n\nIt is fully charged." + "\n\nIt is enhanced with" + shardlvl + "ghost shard and has" + shardlvl*10 + "10% bonus healing and consumes" + shardlvl + "less charges per cast.");
        } else {
            info.append("\n\nIt has " + charge + " out of " + chargeCap + " charges." + "\n\nIt is enhanced with" + shardlvl + "ghost shard and has" + shardlvl*10 + "10% bonus healing and consumes" + shardlvl + "less charges per cast.");
        }
        return info.toString();
    }
}
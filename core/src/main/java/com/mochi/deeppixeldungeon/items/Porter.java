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
package com.mochi.deeppixeldungeon.items;


import java.util.ArrayList;

import com.mochi.deeppixeldungeon.items.artifacts.TimekeepersHourglass;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
        import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
        import com.mochi.deeppixeldungeon.items.artifacts.DriedRose;
import com.mochi.deeppixeldungeon.scenes.InterlevelScene;
        import com.mochi.deeppixeldungeon.sprites.MiscMobsSprites.ItemSprite.Glowing;
        import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
        import com.mochi.deeppixeldungeon.utils.GLog;
        import com.watabou.noosa.Game;
        import com.watabou.utils.Bundle;


public class Porter extends Item {

    private static final String TXT_PREVENTING = "Strong magic aura of this place prevents you from using the coin!";
    private static final String TXT_PREVENTING2 = "You need to kill the bandit king first!";


    public static final float TIME_TO_USE = 1;


    public static final String AC_PORT = "OPEN PORTAL";


    private int specialLevel = 20;
    private int returnDepth = -1;
    private int returnPos;


    {
        name = "ancient coin";
        image = ItemSpriteSheet.DAGGER;


        stackable = false;
        unique = true;
    }

    private static final String DEPTH = "depth";
    private static final String POS = "pos";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DEPTH, returnDepth);
        if (returnDepth != +20) {
            bundle.put(POS, returnPos);
        }
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        returnDepth = bundle.getInt(DEPTH);
        returnPos = bundle.getInt(POS);
    }


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PORT);

        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {


        if (action == AC_PORT) {


            if (Dungeon.bossLevel()) {
                hero.spend(TIME_TO_USE);
                GLog.w(TXT_PREVENTING);
                return;
            }




        }


        if (action == AC_PORT) {


            hero.spend(TIME_TO_USE);


            Buff buff = Dungeon.hero
                    .buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null)
                buff.detach();


            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                if (mob instanceof DriedRose.GhostHero)
                    mob.destroy();
            if (Dungeon.depth<25 && !Dungeon.bossLevel()){

                returnDepth = Dungeon.depth;
                returnPos = hero.pos;
                InterlevelScene.mode = InterlevelScene.Mode.DANK;
            } else {

                this.doDrop(hero);

                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
            }
            InterlevelScene.returnDepth = returnDepth;
            InterlevelScene.returnPos = returnPos;
            Game.switchScene(InterlevelScene.class);

        } else {


            super.execute(hero, action);


        }
    }

    public void reset() {
        returnDepth = -1;
    }

    private static final Glowing BLACK = new Glowing(0x00000);


    @Override
    public Glowing glowing() {
        return BLACK;
    }


    @Override
    public String info() {
        return "This coin radiates an eerie power. Reading the inscription will take you to the Bandit King. ";
    }
}


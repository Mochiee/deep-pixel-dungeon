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
package com.mochi.deeppixeldungeon.items.upgrade;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.effects.particles.PurpleParticle;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.items.weapon.Weapon;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.mochi.deeppixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class WeaponEnhancer extends Item {

    private static final float TIME_TO_INSCRIBE = 1;

    private static final String AC_ENHANCE = "ENHANCE";

    {
        image = ItemSpriteSheet.STYLUS;

        stackable = true;

        bones = true;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_ENHANCE );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_ENHANCE)) {

            curUser = hero;
            GameScene.selectItem( itemSelector, WndBag.Mode.WEAPON, Messages.get(this, "prompt") );

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private void inscribe( Weapon weapon ) {

        if (!weapon.isIdentified() ){
            GLog.w( Messages.get(this, "identify"));
            return;
        } else if (weapon.cursed || weapon.hasCurseEnchant()){
            GLog.w( Messages.get(this, "cursed"));
            return;
        }

        detach(curUser.belongings.backpack);

        GLog.w( Messages.get(this, "inscribed"));

        weapon.enhance();

        curUser.sprite.operate(curUser.pos);
        curUser.sprite.centerEmitter().start(PurpleParticle.MISSILE, 0.05f, 10);
        Sample.INSTANCE.play(Assets.SND_BURNING);

        curUser.spend(TIME_TO_INSCRIBE);
        curUser.busy();
    }

    @Override
    public int price() {
        return 30 * quantity;
    }

    private final WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect( Item item ) {
            if (item != null) {
                WeaponEnhancer.this.inscribe( (Weapon)item );
            }
        }
    };
}

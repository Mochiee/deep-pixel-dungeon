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
package com.mochi.deeppixeldungeon.actors.mobs.bosses;

import com.mochi.deeppixeldungeon.Badges;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.ToxicGas;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Burning;
import com.mochi.deeppixeldungeon.actors.buffs.LockedFloor;
import com.mochi.deeppixeldungeon.actors.buffs.Terror;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.items.keys.SkeletonKey;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Grim;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.CavesMobsSprites.TerminatorSprite;
import com.mochi.deeppixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Terminator extends Mob {

    {
        spriteClass = TerminatorSprite.class;

        HP = HT = 180;
        EXP = 40;
        defenseSkill = 20;
        baseSpeed = 0.5f;
    }

    @Override
    public int damageRoll() {
        if (HP > HT * 0.75)
            return Random.NormalIntRange(10, 15);
        if (HP < HT * 0.75)
            return Random.NormalIntRange(20, 25);
        if (HP < HT * 0.25)
            return Random.NormalIntRange(27, 32);

        return Random.NormalIntRange(10, 15);
    }
    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Burning.class ).reignite( enemy );
        }

        return damage;
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);

        if (HP<HT*0.75 && HP>HT*0.5)
            yell(Messages.get(this, "75hp"));

        if (HP<HT*0.5 && HP>HT*0.25)
            yell(Messages.get(this, "50hp"));
            baseSpeed = 0.5f;

        if (HP<HT*0.25 && HP>HT*0)
            yell(Messages.get(this, "25hp"));
            baseSpeed = 1f;
            if (HP>HT*0.1)
                HP--;
                HP--;

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !immunities().contains(src.getClass())) lock.addTime(dmg*1.5f);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth  ), pos ).sprite.drop();

        Badges.validateBossSlain();

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell( Messages.get(this, "notice") );
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    static {
        RESISTANCES.add( Grim.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Terror.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        BossHealthBar.assignBoss(this);
    }
}

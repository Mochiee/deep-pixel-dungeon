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
package com.mochi.deeppixeldungeon.items.artifacts;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.items.Heap;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.ToxicGas;
import com.mochi.deeppixeldungeon.actors.blobs.VenomGas;
import com.mochi.deeppixeldungeon.actors.buffs.Amok;
import com.mochi.deeppixeldungeon.actors.buffs.Buff;
import com.mochi.deeppixeldungeon.actors.buffs.Burning;
import com.mochi.deeppixeldungeon.actors.buffs.Invisibility;
import com.mochi.deeppixeldungeon.actors.buffs.LockedFloor;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.actors.mobs.Mimic;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.actors.mobs.Wraith;
import com.mochi.deeppixeldungeon.actors.mobs.npcs.NPC;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.sprites.SewerMobSprites.RatSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.mochi.deeppixeldungeon.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class KingsCrown extends Artifact {

    private static final int NIMAGES	= 9;

    {
        image = ItemSpriteSheet.ARTIFACT_ROSE1;

        levelCap = 10;
        //Max charge is 150 after upgrade
        if (level() > 8){
            chargeCap = 150;
        } else {
            chargeCap = 100;
        }

        defaultAction = AC_SUMMON;
    }

    protected static boolean talkedTo = false;
    protected static boolean firstSummon = false;
    protected static boolean spawned = false;

    public static final String AC_SUMMON = "ACTIVATE";
    public static final String AC_FLAUNT = "FLAUNT";
    public static final String AC_UNLEASH = "UNLEASH";

    public KingsCrown(){
        super();
        talkedTo = firstSummon = spawned = false;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && !cursed)
            actions.add(AC_SUMMON);
        if (isEquipped( hero ) && !cursed)
            actions.add(AC_FLAUNT);
        if (isEquipped( hero ) && !cursed)
            actions.add(AC_UNLEASH);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute(hero, action);

        if (action.equals(AC_SUMMON)) {

            ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = curUser.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
                    respawnPoints.add(p);
                    charge = charge - 100;
                }
            }

            int nImages = NIMAGES;
            while (nImages > 0 && respawnPoints.size() > 0) {
                int index = Random.index(respawnPoints);

                PetRat mob = new PetRat();
                GameScene.add(mob);
                ScrollOfTeleportation.appear(mob, respawnPoints.get(index));

                respawnPoints.remove(index);
                nImages--;
            }
        } else if (action.equals(AC_FLAUNT)) {
            if (charge != chargeCap) GLog.i(Messages.get(this, "no_charge"));
            {
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    mob.beckon(curUser.pos);
                    if (Level.fieldOfView[mob.pos]) {
                        Buff.prolong(mob, Amok.class, 5f);
                        charge = charge - 50;
                    }

                }

                for (Heap heap : Dungeon.level.heaps.values()) {
                    if (heap.type == Heap.Type.MIMIC) {
                        Mimic m = Mimic.spawnAt(heap.pos, heap.items);
                        if (m != null) {
                            m.beckon(curUser.pos);
                            heap.destroy();
                        }
                    }
                }
            }
            //only active at 150
        if (action.equals(AC_UNLEASH) && charge>149) {
            GameScene.flash( 0xFFFFFF );

            Sample.INSTANCE.play( Assets.SND_BLAST );
            Invisibility.dispel();

            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                if (Level.fieldOfView[mob.pos]) {
                    mob.damage(mob.HT/4 + Dungeon.hero.lvl * 2, this );
                }
            }
        }

            }
        }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped( Dungeon.hero )){
            if (!cursed){

                if (level() < levelCap)
                    desc+= "\n\n" + Messages.get(this, "desc_hint");

            } else
                desc += "\n\n" + Messages.get(this, "desc_cursed");
        }

        return desc;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new roseRecharge();
    }

    @Override
    public Item upgrade() {
    GLog.p(Messages.get(this, "upgrade"));
        return super.upgrade();
    }

    private static final String TALKEDTO =      "talkedto";
    private static final String FIRSTSUMMON =   "firstsummon";
    private static final String SPAWNED =       "spawned";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        bundle.put( TALKEDTO, talkedTo );
        bundle.put( FIRSTSUMMON, firstSummon );
        bundle.put( SPAWNED, spawned );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        talkedTo = bundle.getBoolean( TALKEDTO );
        firstSummon = bundle.getBoolean( FIRSTSUMMON );
        spawned = bundle.getBoolean( SPAWNED );
    }

    public class roseRecharge extends ArtifactBuff {

        @Override
        public boolean act() {

            LockedFloor lock = target.buff(LockedFloor.class);
            if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
                partialCharge += 1/5f; //500 turns to a full charge
                if (partialCharge > 1){
                    charge++;
                    partialCharge--;
                    if (charge == chargeCap){
                        partialCharge = 0f;
                        GLog.p( Messages.get(DriedRose.class, "charged") );
                    }
                }
            } else if (cursed && Random.Int(100) == 0) {

                ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int p = target.pos + PathFinder.NEIGHBOURS8[i];
                    if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
                        spawnPoints.add(p);
                    }
                }

                if (spawnPoints.size() > 0) {
                    Wraith.spawnAt(Random.element(spawnPoints));
                    Sample.INSTANCE.play(Assets.SND_CURSED);
                }

            }

            updateQuickslot();

            spend( TICK );

            return true;
        }

    }

    public static class PetRat extends NPC {

        {
            spriteClass = RatSprite.class;

            flying = true;

            state = HUNTING;
            enemy = null;
            baseSpeed = 2f;

            ally = true;
        }

        public PetRat() {
            super();

            //equal heroes defence skill
            defenseSkill = (Dungeon.hero.lvl + 4) * 1;
            //equals to depth*2
            //enough to take one hit, but not more.
            HP = HT = (Dungeon.depth) * 2;

        }

        @Override
        protected Char chooseEnemy() {
            if (enemy == null || !enemy.isAlive() || !Dungeon.level.mobs.contains(enemy) || state == WANDERING) {

                HashSet<Mob> enemies = new HashSet<Mob>();
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.hostile && Level.fieldOfView[mob.pos] && mob.state != mob.PASSIVE) {
                        enemies.add(mob);
                    }
                }
                enemy = enemies.size() > 0 ? Random.element(enemies) : null;
            }
            return enemy;
        }

        @Override
        public int attackSkill(Char target) {
            //same accuracy as the hero.
            return (defenseSkill / 2) + 5;
        }

        @Override
        public int damageRoll() {
            int lvl = (HT - 10) / 3;
            return Random.NormalIntRange(lvl / 2, 5 + lvl);
        }

        @Override
        public int drRoll() {
            //defence is equal to the level of rose.
            return Random.NormalIntRange(0, (HT - 10) / 3);
        }

        @Override
        public void add(Buff buff) {
            //in other words, can't be directly affected by buffs/debuffs.
        }

        @Override
        public boolean interact() {
            if (!DriedRose.talkedTo) {
                DriedRose.talkedTo = true;
                GameScene.show(new WndQuest(this, Messages.get(this, "introduce")));
                return false;
            } else {
                int curPos = pos;

                moveSprite(pos, Dungeon.hero.pos);
                move(Dungeon.hero.pos);

                Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
                Dungeon.hero.move(curPos);

                Dungeon.hero.spend(1 / Dungeon.hero.speed());
                Dungeon.hero.busy();
                return true;
            }
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
        }

        @Override
        public void destroy() {
            DriedRose.spawned = false;
            super.destroy();
        }

        private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

        static {
            IMMUNITIES.add(ToxicGas.class);
            IMMUNITIES.add(VenomGas.class);
            IMMUNITIES.add(Burning.class);
            IMMUNITIES.add(ScrollOfPsionicBlast.class);
        }
    }
}
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
package com.mochi.deeppixeldungeon.actors.mobs;

import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Light;
import com.mochi.deeppixeldungeon.actors.buffs.Terror;
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.particles.PurpleParticle;
import com.mochi.deeppixeldungeon.items.Dewdrop;
import com.mochi.deeppixeldungeon.items.wands.WandOfDisintegration;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Grim;
import com.mochi.deeppixeldungeon.items.weapon.enchantments.Vampiric;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.GrieflordSprite;
import com.mochi.deeppixeldungeon.sprites.CharSprite;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Grieflord extends Mob {

    {
        spriteClass = GrieflordSprite.class;

        HP = HT = 50 + Dungeon.depth*15;
        defenseSkill = Dungeon.depth + 7;
        viewDistance = Light.DISTANCE;

        EXP = 15;
        maxLvl = 50;

        HUNTING = new Hunting();

        loot = new Dewdrop();
        lootChance = 0.5f;

        properties.add(Property.DEMONIC);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(0, 0);
    }

    @Override
    public int attackSkill(Char target) {
        return Dungeon.depth + 10;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    private Ballistica beam;
    private int beamTarget = -1;
    private int beamCooldown;
    public boolean beamCharged;

    @Override
    protected boolean canAttack(Char enemy) {

        if (beamCooldown == 0) {
            Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);

            if (enemy.invisible == 0 && Level.fieldOfView[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos)) {
                beam = aim;
                beamTarget = aim.collisionPos;
                return true;
            } else
                //if the beam is charged, it has to attack, will aim at previous location of hero.
                return beamCharged;
        } else
            return super.canAttack(enemy);
    }

    @Override
    protected boolean act() {

        rooted = true;

        if (beam == null && beamTarget != -1) {
            beam = new Ballistica(pos, beamTarget, Ballistica.STOP_TERRAIN);
            sprite.turnTo(pos, beamTarget);
        }
        if (beamCooldown > 0)
            beamCooldown--;
        return super.act();
    }

    @Override
    protected Char chooseEnemy() {
        if (beamCharged && enemy != null) return enemy;
        return super.chooseEnemy();
    }

    @Override
    protected boolean doAttack(Char enemy) {

        if (beamCooldown > 0) {
            return super.doAttack(enemy);
        } else if (!beamCharged) {
            ((GrieflordSprite) sprite).charge(enemy.pos);
            spend(2f);
            beamCharged = true;
            return true;
        } else {

            spend(attackDelay());

            if (Dungeon.visible[pos]) {
                sprite.zap(beam.collisionPos);
                return false;
            } else {
                deathGaze();
                return true;
            }
        }

    }

    @Override
    public void damage(int dmg, Object src) {
        if (beamCharged) dmg /= 4;
        super.damage(dmg, src);
    }

    public void deathGaze() {
        if (!beamCharged || beamCooldown > 0 || beam == null)
            return;

        beamCharged = false;
        beamCooldown = Random.IntRange(3, 6);

        boolean terrainAffected = false;

        for (int pos : beam.subPath(1, beam.dist)) {

            if (Level.flamable[pos]) {

                Dungeon.level.destroy(pos);
                GameScene.updateMap(pos);
                terrainAffected = true;

            }

            Char ch = findChar(pos);
            if (ch == null) {
                continue;
            }

            if (hit(this, ch, true)) {
                ch.damage(Random.NormalIntRange(Dungeon.hero.HT - Dungeon.hero.HT/10, Dungeon.hero.HT - Dungeon.hero.HT/10 + Dungeon.hero.HT/50 ), this);

                if (Dungeon.visible[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }

                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n("Grieflord's beam disintegrated you...");
                }
            } else {
                ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        beam = null;
        beamTarget = -1;
        sprite.idle();
    }

    private static final String BEAM_TARGET = "beamTarget";
    private static final String BEAM_COOLDOWN = "beamCooldown";
    private static final String BEAM_CHARGED = "beamCharged";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BEAM_TARGET, beamTarget);
        bundle.put(BEAM_COOLDOWN, beamCooldown);
        bundle.put(BEAM_CHARGED, beamCharged);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(BEAM_TARGET))
            beamTarget = bundle.getInt(BEAM_TARGET);
        beamCooldown = bundle.getInt(BEAM_COOLDOWN);
        beamCharged = bundle.getBoolean(BEAM_CHARGED);
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(WandOfDisintegration.class);
        RESISTANCES.add(Grim.class);
        RESISTANCES.add(Vampiric.class);
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Terror.class);
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        beamCooldown = 3;
        beamCharged = false;
        ((GrieflordSprite) sprite).idle();
        return super.defenseProc(enemy, damage);
    }
    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            //always attack if the beam is charged, no exceptions
            if (beamCharged && enemy != null)
                enemyInFOV = true;
            return super.act(enemyInFOV, justAlerted);
        }
    }
}

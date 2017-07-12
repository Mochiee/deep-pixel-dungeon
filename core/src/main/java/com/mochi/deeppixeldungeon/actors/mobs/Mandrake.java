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

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.actors.mobs.bosses.King;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.MandrakeSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Mandrake extends Mob {

    private static final int MANDRAKE_TO_SUMMON = 1;

    {
        spriteClass = MandrakeSprite.class;

        HP = HT = 5 + Dungeon.depth*3 - 3;
        defenseSkill = Dungeon.depth;
        EXP = Dungeon.depth/2;

        maxLvl = 25;
    }

    private void scream() {
        sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(Dungeon.depth, Dungeon.depth*2 - 2);
    }

    @Override
    public int attackSkill(Char target) {
        return Dungeon.depth + 5;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        scream();

        return damage;
    }

    private int MandrakesToSummon() {
        return MANDRAKE_TO_SUMMON;
    }

    int MandrakesToSummon = MandrakesToSummon() - King.Undead.count;
    int dist = 1;

    private void summon() {

        sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        boolean[] passable = Level.passable.clone();
        for (Char c : chars()) {
            passable[c.pos] = false;
        }

        int mandrakesToSummon = MandrakesToSummon() - King.Undead.count;

        PathFinder.buildDistanceMap(pos, passable, mandrakesToSummon);
        PathFinder.distance[pos] = Integer.MAX_VALUE;
        int dist = 1;

        mandrakeLabel:
        for (int i = 0; i < mandrakesToSummon; i++) {
            do {
                for (int j = 0; j < Dungeon.level.length(); j++) {
                    if (PathFinder.distance[j] == dist) {

                        MandrakeClone mandrakeClone = new MandrakeClone();
                        mandrakeClone.pos = j;
                        GameScene.add(mandrakeClone);

                        ScrollOfTeleportation.appear(mandrakeClone, j);

                        PathFinder.distance[j] = Integer.MAX_VALUE;

                        continue mandrakeLabel;
                    }
                }
                dist++;
            } while (dist < mandrakesToSummon);
        }
    }
    @Override
    public int defenseProc(Char enemy, int damage) {
        if (HP<HT*0.5)
            summon();
        return super.defenseProc(enemy, damage);
    }
    @Override
    public boolean act() {

        if (Level.water[pos] && HP < HT) {
            sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            HP++;
            HP++;
            HP++;
            HP++;
        }
        return super.act();
    }
}
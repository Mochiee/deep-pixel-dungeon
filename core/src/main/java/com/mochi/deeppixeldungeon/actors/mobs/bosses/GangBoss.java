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

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.mobs.Gangster;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.PrisonMobSprites.GangBossSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GangBoss extends Mob {

    private static final int GANGSTERS_TO_SUMMON = 3;

    {
        spriteClass = GangBossSprite.class;

        HP = HT = 60;
        defenseSkill = 15;
        EXP = 10;

        maxLvl = 25;
    }


    @Override
    public int damageRoll() {
        return Random.NormalIntRange(Dungeon.depth, Dungeon.depth * 2 - 2);
    }

    @Override
    public int attackSkill(Char target) {
        return Dungeon.depth + 5;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth);
    }

    private int GangstersToSummon() {
        return GANGSTERS_TO_SUMMON;
    }

    int GangstersToSummon = GangstersToSummon() - King.Undead.count;
    int dist = 1;

    private void summon() {

        sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        boolean[] passable = Level.passable.clone();
        for (Char c : Actor.chars()) {
            passable[c.pos] = false;
        }

        int gangstersToSummon = GangstersToSummon() - King.Undead.count;

        PathFinder.buildDistanceMap(pos, passable, gangstersToSummon);
        PathFinder.distance[pos] = Integer.MAX_VALUE;
        int dist = 1;

        mandrakeLabel:
        for (int i = 0; i < gangstersToSummon; i++) {
            do {
                for (int j = 0; j < Dungeon.level.length(); j++) {
                    if (PathFinder.distance[j] == dist) {

                        Gangster Gangster = new Gangster();
                        Gangster.pos = j;
                        GameScene.add(Gangster);

                        ScrollOfTeleportation.appear(Gangster, j);

                        PathFinder.distance[j] = Integer.MAX_VALUE;

                        continue mandrakeLabel;
                    }
                }
                dist++;
            } while (dist < gangstersToSummon);
        }
    }



    public void notice() {
            yell(Messages.get(this,"notice"));
            summon();
            super.notice();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(30) == 0)
            yell(Messages.get(this, "yaboi"));

        return damage;
    }
}
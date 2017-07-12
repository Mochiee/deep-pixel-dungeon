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
package com.mochi.deeppixeldungeon.levels;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.Bones;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.ShatteredPixelDungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.Blob;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.actors.mobs.Goolem;
import com.mochi.deeppixeldungeon.effects.CellEmitter;
import com.mochi.deeppixeldungeon.effects.Speck;
import com.mochi.deeppixeldungeon.items.Heap;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.items.keys.IronKey;
import com.mochi.deeppixeldungeon.items.keys.SkeletonKey;
import com.mochi.deeppixeldungeon.levels.painters.MazePainter;
import com.mochi.deeppixeldungeon.levels.traps.SpearTrap;
import com.mochi.deeppixeldungeon.levels.traps.Trap;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.plants.Plant;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.ui.CustomTileVisual;
import com.mochi.deeppixeldungeon.ui.HealthIndicator;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerMiniLevel extends Level {

    {
        color1 = 0x6a723d;
        color2 = 0x88924c;
    }

    private enum State {
        START,
        FIGHT_START,
        MAZE,
        FIGHT_ARENA,
        WON
    }

    private State state;
    private Goolem Goolem;

    //keep track of that need to be removed as the level is changed. We dump 'em back into the level at the end.
    private ArrayList<Item> storedItems = new ArrayList<>();

    @Override
    public String tilesTex() {
        return Assets.TILES_SEWERS;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_SEWERS;
    }

    private static final String STATE = "state";
    private static final String GOOLEM = "Goolem";
    private static final String STORED_ITEMS = "storeditems";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STATE, state);
        bundle.put(GOOLEM, Goolem);
        bundle.put(STORED_ITEMS, storedItems);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        state = bundle.getEnum(STATE, State.class);

        //in some states Goolem won't be in the world, in others he will be.
        if (state == State.START || state == State.MAZE) {
            Goolem = (Goolem) bundle.get(GOOLEM);
        } else {
            for (Mob mob : mobs) {
                if (mob instanceof Goolem) {
                    Goolem = (Goolem) mob;
                    break;
                }
            }
        }

        for (Bundlable item : bundle.getCollection(STORED_ITEMS)) {
            storedItems.add((Item) item);
        }
    }

    @Override
    protected boolean build() {

        map = MAP_START.clone();
        decorate();

        buildFlagMaps();
        cleanWalls();

        state = State.START;
        entrance = 8 + 2 * 32;
        exit = 3 + 2 * 32;

        resetTraps();

        return true;
    }

    @Override
    protected void decorate() {
        //do nothing, all decorations are hard-coded.
    }

    @Override
    protected void createMobs() {
        Goolem = new Goolem(); //We want to keep track of Goolem independently of other mobs, he's not always in the level.
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            drop(item, randomRespawnCell()).type = Heap.Type.REMAINS;
        }
        drop(new IronKey(1), randomPrisonCell());
    }

    private int randomPrisonCell() {
        int pos = 5 + 3 * 32;

        return pos;
    }

    @Override
    public void press(int cell, Char ch) {

        super.press(cell, ch);

        if (ch == Dungeon.hero) {
            //hero enters Goolem's chamber
            if (state == State.START
                    && ((Room) new Room().set(2, 25, 8, 32)).inside(cellToPoint(cell))) {
                progress();
            }

            //hero finishes the maze
            else if (state == State.MAZE
                    && ((Room) new Room().set(4, 0, 7, 4)).inside(cellToPoint(cell))) {
                progress();
            }
        }
    }

    @Override
    public int randomRespawnCell() {
        return 5 + 2 * 32 + PathFinder.NEIGHBOURS8[Random.Int(8)]; //random cell adjacent to the entrance.
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(SewerLevel.class, "water_name");
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(SewerLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(SewerLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    private void resetTraps() {
        traps.clear();

        for (int i = 0; i < length(); i++) {
            if (map[i] == Terrain.INACTIVE_TRAP) {
                Trap t = new SpearTrap().reveal();
                t.active = false;
                setTrap(t, i);
                map[i] = Terrain.INACTIVE_TRAP;
            }
        }
    }

    private void changeMap(int[] map) {
        this.map = map.clone();
        buildFlagMaps();
        cleanWalls();

        exit = entrance = 0;
        for (int i = 0; i < length(); i++)
            if (map[i] == Terrain.ENTRANCE)
                entrance = i;
            else if (map[i] == Terrain.EXIT)
                exit = i;

        visited = mapped = new boolean[length()];
        for (Blob blob : blobs.values()) {
            blob.fullyClear();
        }
        addVisuals(); //this also resets existing visuals
        resetTraps();


        GameScene.resetMap();
        Dungeon.observe();
    }

    private void clearEntities(Room safeArea) {
        for (Heap heap : heaps.values()) {
            if (safeArea == null || !safeArea.inside(cellToPoint(heap.pos))) {
                for (Item item : heap.items)
                    storedItems.add(item);
                heap.destroy();
            }
        }
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])) {
            if (mob != Goolem && (safeArea == null || !safeArea.inside(cellToPoint(mob.pos)))) {
                mob.destroy();
                if (mob.sprite != null)
                    mob.sprite.killAndErase();
            }
        }
        for (Plant plant : plants.values()) {
            if (safeArea == null || !safeArea.inside(cellToPoint(plant.pos))) {
                plants.remove(plant.pos);
            }
        }
    }

    public void progress() {
        switch (state) {
            //moving to the beginning of the fight
            case START:
                seal();
                set(8 + 28 * 32, Terrain.WALL);
                GameScene.updateMap(8 + 28 * 32);

                Goolem.state = Goolem.HUNTING;
                Goolem.pos = 4 + 20 * 32; //in the middle of the fight room
                GameScene.add(Goolem);
                Goolem.notice();

                state = State.FIGHT_START;
                break;

        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        SewerLevel.addSewerVisuals(this, visuals);
        return visuals;
    }

    private static final int W = Terrain.WALL;
    private static final int D = Terrain.DOOR;
    private static final int L = Terrain.LOCKED_DOOR;
    private static final int e = Terrain.EMPTY;
    private static final int S = Terrain.SIGN;
    private static final int w = Terrain.WATER;
    private static final int C = Terrain.CHASM;

    private static final int T = Terrain.INACTIVE_TRAP;

    private static final int E = Terrain.ENTRANCE;
    private static final int X = Terrain.EXIT;

    private static final int M = Terrain.WALL_DECO;
    private static final int P = Terrain.PEDESTAL;

    //TODO if I ever need to store more static maps I should externalize them instead of hard-coding
    //Especially as I means I won't be limited to legal identifiers
    private static final int[] MAP_START =
            {W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, M, W, W, W, W, M, W, W, W, W, W, W, W, W, W,
                    W, e, e, X, e, e, W, e, E, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, e, e, L, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, e, e, W, S, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, e, W, e, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, W, e, W, e, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, e, W, e, W, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, L, W, W, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, T, T, T, T, T, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, T, T, T, T, T, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, w, w, w, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, w, w, w, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, w, w, w, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, w, w, w, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, w, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, w, w, w, w, w, w, w, w, w, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, e, e, e, e, e, e, e, e, e, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, w, w, w, w, w, w, w, w, w, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, W, W, W, W, W, W, W, W, W, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, w, w, w, W, w, w, w, w, w, W, W, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, e, e, e, W, e, e, e, e, e, W, w, w, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, e, e, e, W, e, e, e, e, e, D, e, e, e, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, e, e, e, e, e, e, e, e, e, e, e, e, W, w, w, w, w, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, w, w, w, w, w, w, w, W, w, w, w, w, w, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, M, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};


    private static final int[] MAP_MAZE =
            {W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, e, e, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    e, e, e, D, e, e, e, D, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, W, e, e, e, W, W, M, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, W, W, M, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, W, W, D, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, W, W, e, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, M, W, W, e, W, W, M, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, W, W, e, W, W, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, M, W, D, W, M, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, T, T, T, T, T, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, T, T, T, T, T, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, T, T, T, T, T, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
                    W, W, W, T, T, T, T, T, W, e, W, W, W, W, W, W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, T, T, T, T, T, W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,};

    private static final int[] MAP_ARENA =
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
                    W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, M, W, e, e, e, e, e, D, e, e, e, D, e, e, e, e, e, W, M, e, e, e, e, W, W, W, W,
                    W, e, e, W, W, W, W, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, W, W, W, W, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, W, e, e, e, W, W, W, W,
                    W, e, e, W, W, D, W, W, e, e, e, e, W, e, e, e, W, e, e, e, e, W, W, D, W, W, e, e, W, W, W, W,
                    W, e, W, W, e, e, e, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, e, e, e, W, W, e, W, W, W, W,
                    W, e, W, M, e, e, e, M, W, e, e, e, e, e, M, e, e, e, e, e, W, M, e, e, e, M, W, e, W, W, W, W,
                    W, e, W, W, e, e, e, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, e, e, e, W, W, e, W, W, W, W,
                    W, e, e, W, W, D, W, W, e, e, e, e, W, e, e, e, W, e, e, e, e, W, W, D, W, W, e, e, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, W, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, W, W, W, W, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, W, W, W, W, e, e, W, W, W, W,
                    W, e, e, e, e, M, W, e, e, e, e, e, D, e, e, e, D, e, e, e, e, e, W, M, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
                    W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

    private static final int[] MAP_END =
            {W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, e, E, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, S, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, D, e, D, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, M, W, W, e, W, W, M, e, W, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, D, e, D, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, M, W, W, e, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, X, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, M, W, W, e, W, W, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, D, e, D, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, e, e, e, W, e, W, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, M, W, D, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, T, T, P, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};


        }



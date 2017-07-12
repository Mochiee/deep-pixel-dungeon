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
import com.mochi.deeppixeldungeon.levels.rooms.Room;
import com.mochi.deeppixeldungeon.levels.traps.Trap;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.blobs.Blob;
import com.mochi.deeppixeldungeon.actors.mobs.Mob;
import com.mochi.deeppixeldungeon.actors.mobs.bosses.Terminator;
import com.mochi.deeppixeldungeon.items.Heap;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.items.keys.IronKey;
import com.mochi.deeppixeldungeon.levels.traps.SpearTrap;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.plants.Plant;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CavesAnnex extends Level {

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
    private Terminator Terminator;

    //keep track of that need to be removed as the level is changed. We dump 'em back into the level at the end.
    private ArrayList<Item> storedItems = new ArrayList<>();

    @Override
    public String tilesTex() {
        return Assets.TILES_CAVESANNEX;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_HALLS;
    }

    private static final String STATE = "state";
    private static final String TERMINATOR = "Terminator";
    private static final String STORED_ITEMS = "storeditems";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STATE, state);
        bundle.put(TERMINATOR, Terminator);
        bundle.put(STORED_ITEMS, storedItems);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        state = bundle.getEnum(STATE, State.class);

        //in some states Terminator won't be in the world, in others he will be.
        if (state == State.START || state == State.MAZE) {
            Terminator = (Terminator) bundle.get(TERMINATOR);
        } else {
            for (Mob mob : mobs) {
                if (mob instanceof Terminator) {
                    Terminator = (Terminator) mob;
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
        entrance = 8 + 26 * 32;
        exit = 3 + 2 * 32;

        resetTraps();

        return true;
    }

    protected void decorate() {
        //do nothing, all decorations are hard-coded.
    }

    @Override
    protected void createMobs() {
        Terminator = new Terminator(); //We want to keep track of Terminator independently of other mobs, he's not always in the level.
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {

        drop(new IronKey(1), randomPrisonCell());
    }

    private int randomPrisonCell() {
        int pos = 1 + 1 * 32;

        return pos;
    }

    @Override
    public void press(int cell, Char ch) {

        super.press(cell, ch);

        if (ch == Dungeon.hero) {
            //hero enters Terminator's chamber
            if (state == CavesAnnex.State.START
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
                return Messages.get(CavesAnnex.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CavesAnnex.class, "bookshelf_desc");
            case Terrain.WALL_DECO:
                return Messages.get(CavesAnnex.class, "wall_deco_desc");
            case Terrain.WALL:
                return Messages.get(CavesAnnex.class, "wall_desc");
            case Terrain.STATUE:
                return Messages.get(CavesAnnex.class, "statue_desc");
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
            if (mob != Terminator && (safeArea == null || !safeArea.inside(cellToPoint(mob.pos)))) {
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

                Terminator.state = Terminator.HUNTING;
                Terminator.pos = 15 + 15 * 32; //in the middle of the fight room
                GameScene.add(Terminator);
                Terminator.notice();

                state = State.FIGHT_START;
                break;

        }
    }

    private static final int W = Terrain.WALL;
    private static final int D = Terrain.DOOR;
    private static final int L = Terrain.LOCKED_DOOR;
    private static final int e = Terrain.EMPTY;
    private static final int s = Terrain.SIGN;
    private static final int w = Terrain.WATER;
    private static final int S = Terrain.STATUE;
    private static final int B = Terrain.BOOKSHELF;

    private static final int T = Terrain.INACTIVE_TRAP;

    private static final int E = Terrain.ENTRANCE;
    private static final int X = Terrain.EXIT;

    private static final int M = Terrain.WALL_DECO;
    private static final int P = Terrain.PEDESTAL;

    //TODO if I ever need to store more static maps I should externalize them instead of hard-coding
    //Especially as I means I won't be limited to legal identifiers
    private static final int[] MAP_START =
                   {M, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W,
                    M, e, e, e, e, e, e, e, e, M, e, e, e, e, e, e, e, e, e, M, e, e, e, e, e, e, M, M, M, W, W, W, W, W, e, e, e, e, e, e, e, W, W, e, e, W, W, W, e, e, e, W, e, e, e, e, e, e, e, W, W, W, W, W,
                    M, e, e, e, e, e, e, e, e, M, e, e, W, W, W, W, M, e, e, M, e, e, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, M, W, W, M, e, e, e, e, e, e, M, M, e, M, W, W, M, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, M, e, S, e, S, e, e, e, e, e, D, e, S, e, S, e, M, e, e, e, e, e, M, W, W, W,
                    M, e, e, W, W, W, M, S, e, S, e, S, e, e, e, e, M, M, e, S, e, S, M, W, W, W, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, S, e, S, e, M, M, M, M, M, e, S, e, S, e, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, S, e, S, e, S, e, M, M, M, e, S, e, S, e, S, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, S, e, S, e, S, e, e, e, S, e, S, e, S, e, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, S, e, S, e, S, e, e, e, e, e, S, e, S, e, S, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, S, e, S, e, e, T, e, T, e, e, S, e, S, e, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, M, W, D, W, M, e, S, e, T, e, e, e, e, e, T, e, S, e, M, e, e, e, M, e, e, M, W, W, W,
                    M, e, M, M, e, e, e, M, M, e, e, e, e, M, D, M, e, e, e, e, M, M, e, e, e, M, M, e, M, W, W, W,
                    M, e, M, M, e, e, e, M, M, e, T, e, e, D, P, D, e, e, T, e, M, M, e, e, e, M, M, e, M, W, W, W,
                    M, e, M, M, e, e, e, M, M, e, e, e, e, M, D, M, e, e, e, e, M, M, e, e, e, M, M, e, M, W, W, W,
                    M, e, e, M, e, e, e, M, e, S, e, T, e, e, e, e, e, T, e, S, e, M, M, D, M, M, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, S, e, S, e, e, T, e, T, e, e, S, e, S, e, M, e, M, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, S, e, S, e, S, e, e, e, e, e, S, e, S, e, S, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, S, e, S, e, S, e, e, e, S, e, S, e, S, e, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, S, e, S, e, S, e, M, M, M, e, S, e, S, e, S, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, S, e, S, e, M, W, W, W, W, e, S, e, S, e, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, W, W, W, M, S, e, S, e, S, M, e, e, e, e, S, e, S, e, S, M, W, W, W, e, e, M, W, W, W,
                    M, e, e, e, e, e, M, e, S, e, S, e, D, e, e, e, e, e, S, e, S, e, M, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, M, S, e, S, e, S, M, e, e, e, e, S, e, S, e, S, M, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, M, W, W, M, e, e, M, M, M, M, M, e, e, M, W, W, M, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, e, M, e, e, e, M, M, M, e, e, e, M, e, e, e, e, e, e, e, e, M, W, W, W,
                    M, e, e, e, e, e, e, e, e, M, e, e, e, e, e, e, e, e, e, M, e, e, e, e, e, e, e, e, M, W, W, W,
                    M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};


    private static final int[] MAP_MAZE =
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
                    W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, D, e, e, e, e, e, W, M, e, e, e, e, W, W, W, W,
                    W, e, e, W, W, W, W, e, e, e, e, e, e, e, e, e, W, W, e, e, e, e, W, W, W, W, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, W, e, W, W, W, e, W, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, W, e, W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, W, e, W, e, e, e, e, e, e, e, W, e, W, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, W, W, D, W, W, e, W, e, e, e, e, e, e, e, e, e, W, e, W, e, e, e, e, e, e, W, W, W, W,
                    W, e, W, W, e, e, e, W, W, e, e, e, e, W, D, W, e, e, e, e, W, W, e, e, e, W, W, e, W, W, W, W,
                    W, e, W, M, e, e, e, M, W, e, e, e, e, D, e, D, e, e, e, e, W, M, e, e, e, M, W, e, W, W, W, W,
                    W, e, W, W, e, e, e, W, W, e, e, e, e, W, D, W, e, e, e, e, W, W, e, e, e, W, W, e, W, W, W, W,
                    W, e, e, W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, W, e, W, W, D, W, W, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, W, e, W, e, e, e, e, e, e, e, W, e, W, e, W, e, W, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, W, e, W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, W, e, W, W, W, e, W, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, e, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
                    W, e, e, W, W, W, W, e, e, e, e, W, W, e, e, e, e, e, e, e, e, e, W, W, W, W, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, e, D, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, W, W, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, e, e, e, e, e, W, e, e, e, e, e, W, W, M, W, e, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
                    W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
                    W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
                    W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

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



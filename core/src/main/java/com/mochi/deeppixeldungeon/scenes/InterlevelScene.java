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
package com.mochi.deeppixeldungeon.scenes;

import android.view.textservice.SpellCheckerInfo;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.DeepPixelDungeon;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.Statistics;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.items.Generator;
import com.mochi.deeppixeldungeon.levels.Level;
import com.mochi.deeppixeldungeon.levels.RegularLevel;
import com.mochi.deeppixeldungeon.levels.rooms.special.SpecialRoom;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.ui.GameLog;
import com.mochi.deeppixeldungeon.windows.WndError;
import com.mochi.deeppixeldungeon.windows.WndStory;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.3f;
	
	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, NONE, DANK
	}
	public static Mode mode;
	
	public static int returnDepth;
	public static int returnPos;
	
	public static boolean noStory = false;

	public static boolean fallIntoPit;
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}
	private Phase phase;
	private float timeLeft;
	
	private RenderedText message;
	
	private Thread thread;
	private Exception error = null;
	private float waitingTime;
	
	@Override
	public void create() {
		super.create();

		String text = Messages.get(Mode.class, mode.name());
		
		message = renderText( text, 9 );
		message.x = (Camera.main.width - message.width()) / 2;
		message.y = (Camera.main.height - message.height()) / 2;
		align(message);
		add( message );
		
		phase = Phase.FADE_IN;
		timeLeft = TIME_TO_FADE;

		thread = new Thread() {
			@Override
			public void run() {
				
				try {
					
					Generator.reset();

					switch (mode) {
					case DESCEND:
						descend();
						break;
					case ASCEND:
						ascend();
						break;
					case CONTINUE:
						restore();
						break;
					case RESURRECT:
						resurrect();
						break;
					case RETURN:
						returnTo();
						break;
					case FALL:
						fall();
						break;
					case RESET:
						reset();
						break;
					case DANK:
					portal(1);
					break;
					}
					
					if ((Dungeon.depth % 5) == 0) {
						Sample.INSTANCE.load( Assets.SND_BOSS );
					}
					
				} catch (Exception e) {
					
					error = e;

				}

				if (phase == Phase.STATIC && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				}
			}
		};
		thread.start();
		waitingTime = 0f;
	}
	
	@Override
	public void update() {
		super.update();

		waitingTime += Game.elapsed;
		
		float p = timeLeft / TIME_TO_FADE;
		
		switch (phase) {
		
		case FADE_IN:
			message.alpha( 1 - p );
			if ((timeLeft -= Game.elapsed) <= 0) {
				if (!thread.isAlive() && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				} else {
					phase = Phase.STATIC;
				}
			}
			break;
			
		case FADE_OUT:
			message.alpha( p );

			if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
				Music.INSTANCE.volume( p * (DeepPixelDungeon.musicVol()/10f));
			}
			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
			}
			break;
			
		case STATIC:
			if (error != null) {
				String errorMsg;
				if (error instanceof FileNotFoundException) errorMsg = Messages.get(this, "file_not_found");
				else if (error instanceof IOException) errorMsg = Messages.get(this, "io_error");

				else throw new RuntimeException("fatal error occured while moving between floors", error);

				add( new WndError( errorMsg ) {
					public void onBackPressed() {
						super.onBackPressed();
						Game.switchScene( StartScene.class );
					}
				} );
				error = null;
			} else if ((int)waitingTime == 10){
				waitingTime = 11f;
				DeepPixelDungeon.reportException(
						new RuntimeException("waited more than 10 seconds on levelgen. Seed:" + Dungeon.seed + " depth:" + Dungeon.depth)
				);
			}
			break;
		}
	}

	private void descend() throws IOException {

		Actor.fixTime();
		if (Dungeon.hero == null) {
            Dungeon.init();
            if (noStory) {
                Dungeon.chapters.add( WndStory.ID_FOREST );
                noStory = false;
            }
            GameLog.wipe();
        } else {
            Dungeon.saveAll();
        }

		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		Dungeon.switchLevel( level, level.entrance );
	}
	
	private void fall() throws IOException {

		Actor.fixTime();
		Dungeon.saveAll();

		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		Dungeon.switchLevel( level, level.fallCell( fallIntoPit ));
	}
	
	private void ascend() throws IOException {
		Actor.fixTime();

		Dungeon.saveAll();
		Dungeon.depth--;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, level.exit );
	}
	
	private void returnTo() throws IOException {
		
		Actor.fixTime();

		Dungeon.saveAll();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, returnPos );
	}
	
	private void restore() throws IOException {
		
		Actor.fixTime();

		GameLog.wipe();

		Dungeon.loadGame( StartScene.curClass );
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( Dungeon.loadLevel( StartScene.curClass ), -1 );
		} else {
			Level level = Dungeon.loadLevel( StartScene.curClass );
			Dungeon.switchLevel( level, Dungeon.hero.pos );
		}
	}
	
	private void resurrect() throws IOException {
		
		Actor.fixTime();
		
		if (Dungeon.level.locked) {
			Dungeon.hero.resurrect( Dungeon.depth );
			Dungeon.depth--;
			Level level = Dungeon.newLevel();
			Dungeon.switchLevel( level, level.entrance );
		} else {
			Dungeon.hero.resurrect( -1 );
			Dungeon.resetLevel();
		}
	}
	private void portal(int branch) throws IOException {

        Actor.fixTime();
        Dungeon.saveAll();

            Level level;
            switch (branch) {
                case 1:
                level = Dungeon.CavesAnnex();
				Dungeon.chapters.add( WndStory.ID_CAVESANNEX );
                default:
                level = Dungeon.newLevel();
                }
                Dungeon.switchLevel(level, level.entrance);
    }

	private void reset() throws IOException {

		Actor.fixTime();

		Dungeon.depth--;
		Level level = Dungeon.newLevel();
		//FIXME this only partially addresses issues regarding weak floors.
		Dungeon.switchLevel( level, level.entrance );
	}
	
	@Override
	protected void onBackPressed() {
		//Do nothing
	}
}

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
package com.mochi.deeppixeldungeon.items.weapon.melee;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.Badges;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.hero.Hero;
import com.mochi.deeppixeldungeon.actors.hero.HeroSubClass;
import com.mochi.deeppixeldungeon.effects.particles.ElmoParticle;
import com.mochi.deeppixeldungeon.items.bags.Bag;
import com.mochi.deeppixeldungeon.items.spells.FlamingCloud;
import com.mochi.deeppixeldungeon.items.spells.FrozenCloud;
import com.mochi.deeppixeldungeon.items.spells.Spell;
import com.mochi.deeppixeldungeon.items.spells.Zap;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.scenes.GameScene;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.mochi.deeppixeldungeon.windows.WndBag;
import com.mochi.deeppixeldungeon.windows.WndItem;
import com.mochi.deeppixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MagesStaff extends MeleeWeapon {

	private com.mochi.deeppixeldungeon.items.spells.Spell Spell;

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP  = "ZAP";

	private static final float STAFF_SCALE_FACTOR = 0.75f;
	private int BonusCharge;

	{
		image = ItemSpriteSheet.MAGES_STAFF;

		tier = 1;

		defaultAction = AC_ZAP;
		usesTargeting = true;

		unique = true;
		bones = false;

		if (shardlvl>1 && shardlvl<4){
			BonusCharge = 1;
		} else if (shardlvl>3 && shardlvl<11){
			BonusCharge = 2;
		} else if (shardlvl>9){
			BonusCharge = 3;
		}
	}

	public MagesStaff() {
		Spell = null;
	}

			@Override
	public int min(int lvl) {
		return  tier + shardlvl +        //base + 1 per shard level
				lvl;            //level scaling

	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) + shardlvl + //base damage, down from 10 + 1 per shard level
				lvl*(tier+1);   //scaling unaffected
	}

	public MagesStaff(Spell Spell){
		this();
		Spell.identify();
		Spell.cursed = false;
		this.Spell = Spell;
		Spell.maxCharges = Math.min(Spell.maxCharges + 1 + BonusCharge, 10);
		Spell.curCharges = Spell.maxCharges;
		name = Messages.get(Spell, "staff_name");
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_IMBUE);
		if (Spell!= null && Spell.curCharges > 0) {
			actions.add( AC_ZAP );
		}
		return actions;
	}

	@Override
	public void activate( Char ch ) {
		if(Spell != null) Spell.charge( ch, STAFF_SCALE_FACTOR );
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_IMBUE)) {

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.SPELL, Messages.get(this, "prompt"));

		} else if (action.equals(AC_ZAP)){

			Spell.execute((hero), com.mochi.deeppixeldungeon.items.spells.Spell.AC_CAST);
            Spell.curCharges+=Spell.maxCharges/2;

			if (Spell == null) {
				GameScene.show(new WndItem(null, this, true));
				return;
			}
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (Spell != null && Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE) {
			if (Spell.curCharges < Spell.maxCharges) Spell.partialCharge += 0.33f;
			Spell.curCharges+=Spell.maxCharges;
			Spell.onHit(this, attacker, defender, damage);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int reachFactor(Hero hero) {
		int reach = super.reachFactor(hero);
		if (Spell instanceof Zap && hero.subClass == HeroSubClass.BATTLEMAGE){
			reach++;
		}
		return reach;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null && Spell != null) {
				Spell.charge(container.owner, STAFF_SCALE_FACTOR);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach( ) {
		if (Spell != null) Spell.stopCharging();
	}

	public Item imbueSpell(Spell Spell, Char owner){

		Spell.cursed = false;
		this.Spell = null;

		//syncs the level of the two items.
		int targetLevel = Math.max(this.level(), Spell.level());

		//if the staff's level is being overridden by the Spell, preserve 1 upgrade
		if (Spell.level() >= this.level() && this.level() > 0) targetLevel++;

		int staffLevelDiff = targetLevel - this.level();
		if (staffLevelDiff > 0)
			this.upgrade(staffLevelDiff);
		else if (staffLevelDiff < 0)
			this.degrade(Math.abs(staffLevelDiff));

		int SpellLevelDiff = targetLevel - Spell.level();
		if (SpellLevelDiff > 0)
			Spell.upgrade(SpellLevelDiff);
		else if (SpellLevelDiff < 0)
			Spell.degrade(Math.abs(SpellLevelDiff));

		this.Spell = Spell;
		Spell.maxCharges = Math.min(Spell.maxCharges + 1, 10);
		Spell.curCharges = Spell.maxCharges;
		Spell.identify();
		if (owner != null) Spell.charge(owner);

		name = Messages.get(Spell, "staff_name");

		//This is necessary to reset any particles.
		//FIXME this is gross, should implement a better way to fully reset quickslot visuals
		int slot = Dungeon.quickslot.getSlot(this);
		if (slot != -1){
			Dungeon.quickslot.clearSlot(slot);
			updateQuickslot();
			Dungeon.quickslot.setSlot( slot, this );
			updateQuickslot();
		}

		return this;
	}

	public Class<?extends Spell> SpellClass(){
		return Spell != null ? Spell.getClass() : null;
	}

	@Override
	public Item upgrade(boolean enchant) {
		super.upgrade( enchant );

		if (Spell != null) {
			int curCharges = Spell.curCharges;
			Spell.upgrade();
			//gives the Spell one additional charge
			Spell.maxCharges = Math.min(Spell.maxCharges + 1, 10);
			Spell.curCharges = Math.min(Spell.curCharges + 1, 10);
			updateQuickslot();
		}

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		if (Spell != null) {
			int curCharges = Spell.curCharges;
			Spell.degrade();
			//gives the Spell one additional charge
			Spell.maxCharges = Math.min(Spell.maxCharges + 1, 10);
			Spell.curCharges = curCharges-1;
			updateQuickslot();
		}

		return this;
	}

	@Override
	public String status() {
		if (Spell == null) return super.status();
		else return Spell.status();
	}

	@Override
	public String info() {
		String info = super.info();

		if (Spell == null){
			info += "\n\n" + Messages.get(this, "no_Spell");
		} else {
			info += "\n\n" + Messages.get(this, "has_Spell", Messages.get(Spell, "name")) + " " + Spell.statsDesc();
		}

		return info;
	}

	@Override
	public Emitter emitter() {
		if (Spell == null) return null;
		Emitter emitter = new Emitter();
		emitter.pos(12.5f, 3);
		emitter.fillTarget = false;
		emitter.pour(StaffParticleFactory, 0.1f);
		return emitter;
	}

	private static final String SPELL = "Spell";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SPELL, Spell);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		Spell = (Spell) bundle.get(SPELL);
		if (Spell != null) {
			Spell.maxCharges = Math.min(Spell.maxCharges + 1, 10);
			name = Messages.get(Spell, "staff_name");
		}
	}

	@Override
	public int price() {
		return 0;
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( final Item item ) {
			if (item != null) {

				if (!item.isIdentified()) {
					GLog.w(Messages.get(MagesStaff.class, "id_first"));
					return;
				} else if (item.cursed){
					GLog.w(Messages.get(MagesStaff.class, "cursed"));
					return;
				}

				if (Spell == null){
					applySpell((Spell)item);
				} else {
					final int newLevel =
							item.level() >= level() ?
									level() > 0 ?
											item.level() + 1
											: item.level()
									: level();
					GameScene.show(
							new WndOptions("",
									Messages.get(MagesStaff.class, "warning", newLevel),
									Messages.get(MagesStaff.class, "yes"),
									Messages.get(MagesStaff.class, "no")) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										applySpell((Spell)item);
									}
								}
							}
					);
				}
			}
		}

		private void applySpell(Spell Spell){
			Sample.INSTANCE.play(Assets.SND_BURNING);
			curUser.sprite.emitter().burst( ElmoParticle.FACTORY, 12 );
			evoke(curUser);

			Dungeon.quickslot.clearItem(Spell);

			Spell.detach(curUser.belongings.backpack);
			Badges.validateTutorial();

			GLog.p( Messages.get(MagesStaff.class, "imbue", Spell.name()));
			imbueSpell( Spell, curUser );

			updateQuickslot();
		}
	};

	private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
			if (c == null) {
				c = new StaffParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() {
			return !((Spell instanceof Zap)
					|| (Spell instanceof FrozenCloud)
					|| (Spell instanceof FlamingCloud));
		}
	};

	//determines particle effects to use based on Spell the staff owns.
	public class StaffParticle extends PixelParticle{

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public StaffParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			if (Spell != null)
				Spell.staffFx( this );

		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void shuffleXY(float amt){
			x += Random.Float(-amt, amt);
			y += Random.Float(-amt, amt);
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}
}


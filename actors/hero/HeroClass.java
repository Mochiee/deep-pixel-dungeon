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
package com.mochi.deeppixeldungeon.actors.hero;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.Badges;
import com.mochi.deeppixeldungeon.Challenges;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.items.BrokenSeal;
import com.mochi.deeppixeldungeon.items.Stylus;
import com.mochi.deeppixeldungeon.items.armor.ClothArmor;
import com.mochi.deeppixeldungeon.items.artifacts.CloakOfShadows;
import com.mochi.deeppixeldungeon.items.food.Food;
import com.mochi.deeppixeldungeon.items.food.SpiritsChalice;
import com.mochi.deeppixeldungeon.items.potions.PotionOfHealing;
import com.mochi.deeppixeldungeon.items.potions.PotionOfMindVision;
import com.mochi.deeppixeldungeon.items.potions.PotionOfNerveGas;
import com.mochi.deeppixeldungeon.items.potions.PotionOfParalyticGas;
import com.mochi.deeppixeldungeon.items.potions.PotionOfPureDestruction;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.mochi.deeppixeldungeon.items.wands.WandOfMagicMissile;
import com.mochi.deeppixeldungeon.items.weapon.melee.Aegis;
import com.mochi.deeppixeldungeon.items.weapon.melee.Glaive;
import com.mochi.deeppixeldungeon.items.weapon.melee.Greatsword;
import com.mochi.deeppixeldungeon.items.weapon.melee.Hex;
import com.mochi.deeppixeldungeon.items.weapon.melee.MagesStaff;
import com.mochi.deeppixeldungeon.items.weapon.melee.PinLuna;
import com.mochi.deeppixeldungeon.items.weapon.melee.PinSol;
import com.mochi.deeppixeldungeon.items.weapon.melee.Rapier;
import com.mochi.deeppixeldungeon.items.weapon.melee.SacrificialKnife;
import com.mochi.deeppixeldungeon.items.weapon.melee.Sorrowblade;
import com.mochi.deeppixeldungeon.items.weapon.melee.TheDebugger;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Cards;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Dart;
import com.mochi.deeppixeldungeon.items.weapon.missiles.ShadowShard;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior" ),
	MAGE( "mage" ),
	ROGUE( "rogue" ),
	HUNTRESS( "huntress" );

	private String title;

	HeroClass( String title ) {
		this.title = title;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;
		}

		hero.updateAwareness();
	}

	private static void initCommon( Hero hero ) {
		if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
			(hero.belongings.armor = new ClothArmor()).identify();

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			new Food().identify().collect();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new Aegis()).identify();
		Dart darts = new Dart( 8 );
		darts.identify().collect();

		if ( Badges.isUnlocked(Badges.Badge.TUTORIAL_WARRIOR) ){
			if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
				hero.belongings.armor.affixSeal(new BrokenSeal());
			Dungeon.quickslot.setSlot(0, darts);
		} else {
			if (!Dungeon.isChallenged(Challenges.NO_ARMOR)) {
				BrokenSeal seal = new BrokenSeal();
				seal.collect();
				Dungeon.quickslot.setSlot(0, seal);
			}
			Dungeon.quickslot.setSlot(1, darts);
		}

		new PotionOfHealing().setKnown();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		if ( Badges.isUnlocked(Badges.Badge.TUTORIAL_MAGE) ){
			staff = new MagesStaff(new WandOfMagicMissile());
		} else {
			staff = new MagesStaff();
			new WandOfMagicMissile().identify().collect();
		}

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().setKnown();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new TheDebugger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate( hero );

		Dart darts = new Dart( 8 );
		darts.identify().collect();

		Aegis aegis = new Aegis();
		aegis.identify().collect();

		PinSol pinSol = new PinSol();
		pinSol.identify().collect();

		SacrificialKnife sacrificialKnife = new SacrificialKnife();
		sacrificialKnife.identify().collect();

		PinLuna pinLuna = new PinLuna();
		pinLuna.identify().collect();

        PotionOfPureDestruction potionOfPureDestruction = new PotionOfPureDestruction();
        potionOfPureDestruction.identify().collect();

        Rapier rapier = new Rapier();
        rapier.identify().collect();

        ShadowShard shadowShard = new ShadowShard(64);
        shadowShard.identify().collect();

        Cards cards = new Cards(64);
        cards.identify().collect();

        Stylus stylus = new Stylus();
        stylus.identify().collect();

        ScrollOfMagicalInfusion scrollOfMagicalInfusion = new ScrollOfMagicalInfusion();
        scrollOfMagicalInfusion.identify().collect();

        Greatsword greatsword = new Greatsword();
        greatsword.identify().collect();

		PotionOfNerveGas potionOfNerveGas = new PotionOfNerveGas();
		potionOfNerveGas.identify().collect();

		Sorrowblade sorrowblade = new Sorrowblade();
		sorrowblade.identify().collect();

        SpiritsChalice spiritsChalice = new SpiritsChalice();
        spiritsChalice.identify().collect();


        Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, darts);
		new ScrollOfMagicMapping().setKnown();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new PinLuna()).identify();
		PinLuna pinLuna = new PinLuna();
		pinLuna.identify().collect();

		Aegis aegis = new Aegis();
		aegis.identify().collect();

		new PotionOfMindVision().setKnown();
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public String spritesheet() {
		
		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
		case WARRIOR:
			return new String[]{
					Messages.get(HeroClass.class, "warrior_perk1"),
					Messages.get(HeroClass.class, "warrior_perk2"),
					Messages.get(HeroClass.class, "warrior_perk3"),
					Messages.get(HeroClass.class, "warrior_perk4"),
					Messages.get(HeroClass.class, "warrior_perk5"),
			};
		case MAGE:
			return new String[]{
					Messages.get(HeroClass.class, "mage_perk1"),
					Messages.get(HeroClass.class, "mage_perk2"),
					Messages.get(HeroClass.class, "mage_perk3"),
					Messages.get(HeroClass.class, "mage_perk4"),
					Messages.get(HeroClass.class, "mage_perk5"),
			};
		case ROGUE:
			return new String[]{
					Messages.get(HeroClass.class, "rogue_perk1"),
					Messages.get(HeroClass.class, "rogue_perk2"),
					Messages.get(HeroClass.class, "rogue_perk3"),
					Messages.get(HeroClass.class, "rogue_perk4"),
					Messages.get(HeroClass.class, "rogue_perk5"),
					Messages.get(HeroClass.class, "rogue_perk6"),
			};
		case HUNTRESS:
			return new String[]{
					Messages.get(HeroClass.class, "huntress_perk1"),
					Messages.get(HeroClass.class, "huntress_perk2"),
					Messages.get(HeroClass.class, "huntress_perk3"),
					Messages.get(HeroClass.class, "huntress_perk4"),
					Messages.get(HeroClass.class, "huntress_perk5"),
			};
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}

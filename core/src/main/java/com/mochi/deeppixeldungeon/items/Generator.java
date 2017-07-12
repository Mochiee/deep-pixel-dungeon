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
package com.mochi.deeppixeldungeon.items;

import com.mochi.deeppixeldungeon.DeepPixelDungeon;
import com.mochi.deeppixeldungeon.items.armor.ClothArmor;
import com.mochi.deeppixeldungeon.items.armor.LeatherArmor;
import com.mochi.deeppixeldungeon.items.armor.MailArmor;
import com.mochi.deeppixeldungeon.items.armor.PlateArmor;
import com.mochi.deeppixeldungeon.items.armor.ScaleArmor;
import com.mochi.deeppixeldungeon.items.artifacts.Artifact;
import com.mochi.deeppixeldungeon.items.artifacts.CapeOfThorns;
import com.mochi.deeppixeldungeon.items.artifacts.ChaliceOfBlood;
import com.mochi.deeppixeldungeon.items.artifacts.CloakOfShadows;
import com.mochi.deeppixeldungeon.items.artifacts.DriedRose;
import com.mochi.deeppixeldungeon.items.artifacts.EtherealChains;
import com.mochi.deeppixeldungeon.items.artifacts.LloydsBeacon;
import com.mochi.deeppixeldungeon.items.artifacts.MasterThievesArmband;
import com.mochi.deeppixeldungeon.items.artifacts.TimekeepersHourglass;
import com.mochi.deeppixeldungeon.items.bags.Bag;
import com.mochi.deeppixeldungeon.items.food.Pasty;
import com.mochi.deeppixeldungeon.items.potions.Potion;
import com.mochi.deeppixeldungeon.items.potions.PotionOfFrost;
import com.mochi.deeppixeldungeon.items.potions.PotionOfHealing;
import com.mochi.deeppixeldungeon.items.potions.PotionOfInvisibility;
import com.mochi.deeppixeldungeon.items.potions.PotionOfLevitation;
import com.mochi.deeppixeldungeon.items.potions.PotionOfLiquidFlame;
import com.mochi.deeppixeldungeon.items.potions.PotionOfMight;
import com.mochi.deeppixeldungeon.items.potions.PotionOfMindVision;
import com.mochi.deeppixeldungeon.items.potions.PotionOfParalyticGas;
import com.mochi.deeppixeldungeon.items.potions.PotionOfPurity;
import com.mochi.deeppixeldungeon.items.potions.PotionOfStrength;
import com.mochi.deeppixeldungeon.items.potions.PotionOfToxicGas;
import com.mochi.deeppixeldungeon.items.rings.Ring;
import com.mochi.deeppixeldungeon.items.rings.RingOfElements;
import com.mochi.deeppixeldungeon.items.rings.RingOfEvasion;
import com.mochi.deeppixeldungeon.items.rings.RingOfForce;
import com.mochi.deeppixeldungeon.items.rings.RingOfFuror;
import com.mochi.deeppixeldungeon.items.rings.RingOfHaste;
import com.mochi.deeppixeldungeon.items.rings.RingOfMagic;
import com.mochi.deeppixeldungeon.items.rings.RingOfSharpshooting;
import com.mochi.deeppixeldungeon.items.rings.RingOfTenacity;
import com.mochi.deeppixeldungeon.items.rings.RingOfWealth;
import com.mochi.deeppixeldungeon.items.scrolls.Scroll;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfIdentify;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfLullaby;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfRage;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfRecharging;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTerror;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.mochi.deeppixeldungeon.items.wands.Wand;
import com.mochi.deeppixeldungeon.items.wands.WandOfBlastWave;
import com.mochi.deeppixeldungeon.items.wands.WandOfCorruption;
import com.mochi.deeppixeldungeon.items.wands.WandOfFireblast;
import com.mochi.deeppixeldungeon.items.wands.WandOfFrost;
import com.mochi.deeppixeldungeon.items.wands.WandOfLightning;
import com.mochi.deeppixeldungeon.items.wands.WandOfMagicMissile;
import com.mochi.deeppixeldungeon.items.wands.WandOfPrismaticLight;
import com.mochi.deeppixeldungeon.items.wands.WandOfRegrowth;
import com.mochi.deeppixeldungeon.items.wands.WandOfTransfusion;
import com.mochi.deeppixeldungeon.items.wands.WandOfVenom;
import com.mochi.deeppixeldungeon.items.weapon.Weapon;
import com.mochi.deeppixeldungeon.items.weapon.melee.AssassinsBlade;
import com.mochi.deeppixeldungeon.items.weapon.melee.Dirk;
import com.mochi.deeppixeldungeon.items.weapon.melee.Flail;
import com.mochi.deeppixeldungeon.items.weapon.melee.Glaive;
import com.mochi.deeppixeldungeon.items.weapon.melee.Greataxe;
import com.mochi.deeppixeldungeon.items.weapon.melee.Greatsword;
import com.mochi.deeppixeldungeon.items.weapon.melee.Hex;
import com.mochi.deeppixeldungeon.items.weapon.melee.Knuckles;
import com.mochi.deeppixeldungeon.items.weapon.melee.MagesStaff;
import com.mochi.deeppixeldungeon.items.weapon.melee.Shortsword;
import com.mochi.deeppixeldungeon.items.weapon.melee.PinLuna;
import com.mochi.deeppixeldungeon.items.weapon.melee.PinSol;
import com.mochi.deeppixeldungeon.items.weapon.melee.Quarterstaff;
import com.mochi.deeppixeldungeon.items.weapon.melee.Rapier;
import com.mochi.deeppixeldungeon.items.weapon.melee.Scimitar;
import com.mochi.deeppixeldungeon.items.weapon.melee.WarHammer;
import com.mochi.deeppixeldungeon.items.weapon.melee.Whip;
import com.mochi.deeppixeldungeon.items.weapon.melee.WornShortsword;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Boomerang;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Cards;
import com.mochi.deeppixeldungeon.items.weapon.missiles.CurareDart;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Dart;
import com.mochi.deeppixeldungeon.items.weapon.missiles.IncendiaryDart;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Javelin;
import com.mochi.deeppixeldungeon.items.weapon.missiles.ShadowShard;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Shuriken;
import com.mochi.deeppixeldungeon.items.weapon.missiles.Tamahawk;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.actors.mobs.npcs.Ghost;
import com.mochi.deeppixeldungeon.items.armor.Armor;
import com.mochi.deeppixeldungeon.items.artifacts.AlchemistsToolkit;
import com.mochi.deeppixeldungeon.items.artifacts.HornOfPlenty;
import com.mochi.deeppixeldungeon.items.artifacts.SandalsOfNature;
import com.mochi.deeppixeldungeon.items.artifacts.TalismanOfForesight;
import com.mochi.deeppixeldungeon.items.artifacts.UnstableSpellbook;
import com.mochi.deeppixeldungeon.items.food.Food;
import com.mochi.deeppixeldungeon.items.food.MysteryMeat;
import com.mochi.deeppixeldungeon.items.potions.PotionOfExperience;
import com.mochi.deeppixeldungeon.items.rings.RingOfAccuracy;
import com.mochi.deeppixeldungeon.items.rings.RingOfMight;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.mochi.deeppixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.mochi.deeppixeldungeon.items.wands.WandOfDisintegration;
import com.mochi.deeppixeldungeon.items.weapon.melee.Aegis;
import com.mochi.deeppixeldungeon.items.weapon.melee.BattleAxe;
import com.mochi.deeppixeldungeon.items.weapon.melee.Dagger;
import com.mochi.deeppixeldungeon.items.weapon.melee.Greatshield;
import com.mochi.deeppixeldungeon.items.weapon.melee.HandAxe;
import com.mochi.deeppixeldungeon.items.weapon.melee.Longsword;
import com.mochi.deeppixeldungeon.items.weapon.melee.Mace;
import com.mochi.deeppixeldungeon.items.weapon.melee.RoundShield;
import com.mochi.deeppixeldungeon.items.weapon.melee.RunicBlade;
import com.mochi.deeppixeldungeon.items.weapon.melee.SacrificialKnife;
import com.mochi.deeppixeldungeon.items.weapon.melee.Sai;
import com.mochi.deeppixeldungeon.items.weapon.melee.Spear;
import com.mochi.deeppixeldungeon.items.weapon.melee.Sword;
import com.mochi.deeppixeldungeon.plants.BlandfruitBush;
import com.mochi.deeppixeldungeon.plants.Blindweed;
import com.mochi.deeppixeldungeon.plants.Dreamfoil;
import com.mochi.deeppixeldungeon.plants.Earthroot;
import com.mochi.deeppixeldungeon.plants.Fadeleaf;
import com.mochi.deeppixeldungeon.plants.Firebloom;
import com.mochi.deeppixeldungeon.plants.Icecap;
import com.mochi.deeppixeldungeon.plants.Plant;
import com.mochi.deeppixeldungeon.plants.Rotberry;
import com.mochi.deeppixeldungeon.plants.Sorrowmoss;
import com.mochi.deeppixeldungeon.plants.Starflower;
import com.mochi.deeppixeldungeon.plants.Stormvine;
import com.mochi.deeppixeldungeon.plants.Sungrass;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Generator {

	public static enum Category {
		WEAPON	( 100,	Weapon.class ),
		WEP_T1	( 0, 	Weapon.class),
		WEP_T2	( 0,	Weapon.class),
		WEP_T3	( 0, 	Weapon.class),
		WEP_T4	( 0, 	Weapon.class),
		WEP_T5	( 0, 	Weapon.class),
        WEP_T6  ( 0,    Weapon.class),
		CHEST1WEP( 0,    Weapon.class),
		CHEST2WEP( 0,    Weapon.class),
		CHEST3WEP( 0,    Weapon.class),
        IDOL    ( 0,    Weapon.class),
		ARMOR	( 60,	Armor.class ),
		POTION	( 500,	Potion.class ),
		SCROLL	( 400,	Scroll.class ),
		WAND	( 40,	Wand.class ),
		RING	( 15,	Ring.class ),
		ARTIFACT( 15,   Artifact.class),
		SEED	( 50,	Plant.Seed.class ),
		FOOD	( 0,	Food.class ),
		GOLD	( 500,	Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0, 10, 40, 40, 10},
			{0,  5, 20, 50, 25},
			{0,  2,  8, 20, 70}
	};
	
	private static HashMap<Category,Float> categoryProbs = new HashMap<Category, Float>();

	private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1};
	
	static {

		Category.GOLD.classes = new Class<?>[]{
			Gold.class };
		Category.GOLD.probs = new float[]{ 1 };

		Category.SCROLL.classes = new Class<?>[]{
			ScrollOfIdentify.class,
			ScrollOfTeleportation.class,
			ScrollOfRemoveCurse.class,
			ScrollOfUpgrade.class,
			ScrollOfRecharging.class,
			ScrollOfMagicMapping.class,
			ScrollOfRage.class,
			ScrollOfTerror.class,
			ScrollOfLullaby.class,
			ScrollOfMagicalInfusion.class,
			ScrollOfPsionicBlast.class,
			ScrollOfMirrorImage.class };
		Category.SCROLL.probs = new float[]{ 30, 10, 20, 0, 15, 15, 12, 8, 8, 0, 4, 10 };

		Category.POTION.classes = new Class<?>[]{
			PotionOfHealing.class,
			PotionOfExperience.class,
			PotionOfToxicGas.class,
			PotionOfParalyticGas.class,
			PotionOfLiquidFlame.class,
			PotionOfLevitation.class,
			PotionOfStrength.class,
			PotionOfMindVision.class,
			PotionOfPurity.class,
			PotionOfInvisibility.class,
			PotionOfMight.class,
			PotionOfFrost.class,
		};
		Category.POTION.probs = new float[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		//TODO: add last ones when implemented
		Category.WAND.classes = new Class<?>[]{
			WandOfMagicMissile.class,
			WandOfLightning.class,
			WandOfDisintegration.class,
			WandOfFireblast.class,
			WandOfVenom.class,
			WandOfBlastWave.class,
			//WandOfLivingEarth.class,
			WandOfFrost.class,
			WandOfPrismaticLight.class,
			//WandOfWarding.class,
			WandOfTransfusion.class,
			WandOfCorruption.class,
			WandOfRegrowth.class };
		Category.WAND.probs = new float[]{ 5, 4, 4, 4, 4, 3, /*3,*/ 3, 3, /*3,*/ 3, 3, 3 };

		//see generator.randomWeapon
		Category.WEAPON.classes = new Class<?>[]{};
		Category.WEAPON.probs = new float[]{};

		Category.WEP_T1.classes = new Class<?>[]{
			WornShortsword.class,
            SacrificialKnife.class,
			Knuckles.class,
			Dagger.class,
			MagesStaff.class,
			Boomerang.class,
			Dart.class
		};
		Category.WEP_T1.probs = new float[]{ 1, 10, 1, 1, 0, 0, 1 };

		Category.WEP_T2.classes = new Class<?>[]{
			Shortsword.class,
			HandAxe.class,
			Spear.class,
			Quarterstaff.class,
			Dirk.class,
			IncendiaryDart.class,
            Rapier.class,
            Cards.class
		};
		Category.WEP_T2.probs = new float[]{ 0, 0, 0, 0, 0, 0, 25, 25 };

		Category.WEP_T3.classes = new Class<?>[]{
			Sword.class,
			Mace.class,
			Scimitar.class,
			RoundShield.class,
			Sai.class,
			Whip.class,
			Shuriken.class,
			CurareDart.class,
            Hex.class
		};
		Category.WEP_T3.probs = new float[]{ 0, 0, 0, 0, 0, 0, 0, 25 };

		Category.WEP_T4.classes = new Class<?>[]{
			Longsword.class,
			BattleAxe.class,
			Flail.class,
			RunicBlade.class,
			AssassinsBlade.class,
			Javelin.class
		};
		Category.WEP_T4.probs = new float[]{ 0, 0, 0, 0, 0, 0 };

		Category.WEP_T5.classes = new Class<?>[]{
			Greatsword.class,
			WarHammer.class,
			Glaive.class,
			Greataxe.class,
			Greatshield.class,
			Tamahawk.class
		};
		Category.WEP_T5.probs = new float[]{ 0, 0, 0, 0, 0, 0 };

        Category.WEP_T6.classes = new Class<?>[]{
                PinLuna.class,
                PinSol.class,
                Aegis.class,
        };
        Category.WEP_T6.probs = new float[]{ 25, 25, 25 };

        Category.IDOL.classes = new Class <?>[]{
                Cards.class,
                ShadowShard.class,
                UnstableSpellbook.class,
                Stylus.class,
                Hex.class,
                Gold.class,
                SacrificialKnife.class,
                ScrollOfPsionicBlast.class,

        };
        Category.IDOL.probs = new float[]{ 20, 10, 1, 15, 3, 15, 20, 1 };


		//see Generator.randomArmor
		Category.ARMOR.classes = new Class<?>[]{
			ClothArmor.class,
			LeatherArmor.class,
			MailArmor.class,
			ScaleArmor.class,
			PlateArmor.class };
		Category.ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };

		Category.FOOD.classes = new Class<?>[]{
			Food.class,
			Pasty.class,
			MysteryMeat.class };
		Category.FOOD.probs = new float[]{ 4, 1, 0 };

		Category.RING.classes = new Class<?>[]{
			RingOfAccuracy.class,
			RingOfEvasion.class,
			RingOfElements.class,
			RingOfForce.class,
			RingOfFuror.class,
			RingOfHaste.class,
			RingOfMagic.class, //currently removed from drop tables, pending rework
			RingOfMight.class,
			RingOfSharpshooting.class,
			RingOfTenacity.class,
			RingOfWealth.class};
		Category.RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 };

		Category.ARTIFACT.classes = new Class<?>[]{
			CapeOfThorns.class,
			ChaliceOfBlood.class,
			CloakOfShadows.class,
			HornOfPlenty.class,
			MasterThievesArmband.class,
			SandalsOfNature.class,
			TalismanOfForesight.class,
			TimekeepersHourglass.class,
			UnstableSpellbook.class,
			AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
			DriedRose.class, //starts with no chance of spawning, chance is set directly after beating ghost quest.
			LloydsBeacon.class,
			EtherealChains.class
			};
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();

		Category.SEED.classes = new Class<?>[]{
			Firebloom.Seed.class,
			Icecap.Seed.class,
			Sorrowmoss.Seed.class,
			Blindweed.Seed.class,
			Sungrass.Seed.class,
			Earthroot.Seed.class,
			Fadeleaf.Seed.class,
			Rotberry.Seed.class,
			BlandfruitBush.Seed.class,
			Dreamfoil.Seed.class,
			Stormvine.Seed.class,
			Starflower.Seed.class};
		Category.SEED.probs = new float[]{ 12, 12, 12, 12, 12, 12, 12, 0, 4, 12, 12, 1 };

		Category.CHEST1WEP.classes = new Class<?>[]{
		        //Tier 2
		        Shortsword.class,
				HandAxe.class,
				Spear.class,
				Quarterstaff.class,
				Dirk.class,
				Rapier.class,
				//Tier 3
				Sword.class,
				Mace.class,
				Scimitar.class,
				RoundShield.class,
				Sai.class,
				Whip.class,
				Hex.class,
				//Tier 4
				Longsword.class,
				BattleAxe.class,
				Flail.class,
				RunicBlade.class,
				AssassinsBlade.class,

	};
	Category.CHEST1WEP.probs = new float[]{5, 5, 5, 5, 5, 5, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1};

	Category.CHEST2WEP.classes = new Class<?>[]{
		        //Tier 2
		        Shortsword.class,
				HandAxe.class,
				Spear.class,
				Quarterstaff.class,
				Dirk.class,
				Rapier.class,
				//Tier 3
				Sword.class,
				Mace.class,
				Scimitar.class,
				RoundShield.class,
				Sai.class,
				Whip.class,
				Hex.class,
				//Tier 4
				Longsword.class,
				BattleAxe.class,
				Flail.class,
				RunicBlade.class,
				AssassinsBlade.class,

	};
        if (Dungeon.depth<12) {
            Category.CHEST2WEP.probs = new float[]{3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2};
        } else if (Dungeon.depth>12) {
            Category.CHEST2WEP.probs = new float[]{2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3};
        }

		Category.CHEST3WEP.classes = new Class<?>[]{
				//Tier 3
				Sword.class,
				Mace.class,
				Scimitar.class,
				RoundShield.class,
				Sai.class,
				Whip.class,
				Hex.class,
				//Tier 4
				Longsword.class,
				BattleAxe.class,
				Flail.class,
				RunicBlade.class,
				AssassinsBlade.class,
				//Tier 5
				Greatsword.class,
				WarHammer.class,
				Glaive.class,
				Greataxe.class,
				Greatshield.class,
				//Tier 6
				Aegis.class,
				PinSol.class,
				PinLuna.class,

		};
		Category.CHEST3WEP.probs = new float[]{1, 1, 1, 1, 1, 1, 2, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 0.5f, 0.5f, 0.5f};
}

	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		return random( Random.chances( categoryProbs ) );
	}

	public static Item random( Category cat ) {
		try {
			
			categoryProbs.put( cat, categoryProbs.get( cat ) / 2 );
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			DeepPixelDungeon.reportException(e);
			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			DeepPixelDungeon.reportException(e);
			return null;
			
		}
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Armor a = (Armor)Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])].newInstance();
			a.random();
			return a;
		} catch (Exception e) {
			DeepPixelDungeon.reportException(e);
			return null;
		}
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5,
            Category.WEP_T6
	};

	public static Weapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static Weapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
			Weapon w = (Weapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			DeepPixelDungeon.reportException(e);
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances( cat.probs );

			//if no artifacts are left, return null
			if (i == -1){
				return null;
			}

			Artifact artifact = (Artifact)cat.classes[i].newInstance();

			//remove the chance of spawning this artifact.
			cat.probs[i] = 0;
			spawnedArtifacts.add(cat.classes[i].getSimpleName());

			artifact.random();

			return artifact;

		} catch (Exception e) {
			DeepPixelDungeon.reportException(e);
			return null;
		}
	}

	public static boolean removeArtifact(Artifact artifact) {
		if (spawnedArtifacts.contains(artifact.getClass().getSimpleName()))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact.getClass())) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact.getClass().getSimpleName());
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();

		//checks for dried rose quest completion, adds the rose in accordingly.
		if (Ghost.Quest.completed()) Category.ARTIFACT.probs[10] = 1;

		spawnedArtifacts = new ArrayList<String>();
	}

	private static ArrayList<String> spawnedArtifacts = new ArrayList<String>();

	private static final String ARTIFACTS = "artifacts";

	//used to store information on which artifacts have been spawned.
	public static void storeInBundle(Bundle bundle) {
		bundle.put( ARTIFACTS, spawnedArtifacts.toArray(new String[spawnedArtifacts.size()]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		initArtifacts();

		if (bundle.contains(ARTIFACTS)) {
			Collections.addAll(spawnedArtifacts, bundle.getStringArray(ARTIFACTS));
			Category cat = Category.ARTIFACT;

			for (String artifact : spawnedArtifacts)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}

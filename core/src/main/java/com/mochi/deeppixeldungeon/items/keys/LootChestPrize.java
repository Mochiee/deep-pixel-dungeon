package com.mochi.deeppixeldungeon.items.keys;

import com.mochi.deeppixeldungeon.items.Item;
import com.mochi.deeppixeldungeon.items.Generator;
import com.watabou.utils.Random;

/**
 * Created by Loh Kee Yong on 27/2/2017.
 */

public class LootChestPrize {

    public static Item Tier1Prize() {
        return Generator.random(Random.oneOf(
                Generator.Category.CHEST1WEP,
                Generator.Category.ARMOR
        ));
    }

    public static Item Tier2Prize() {
        return Generator.random(Random.oneOf(
                Generator.Category.CHEST2WEP
        ));
    }
    public static Item Tier3Prize() {
        return Generator.random( Random.oneOf(
                Generator.Category.CHEST3WEP
        ) );}
}
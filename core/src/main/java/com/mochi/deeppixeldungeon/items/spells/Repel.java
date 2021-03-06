package com.mochi.deeppixeldungeon.items.spells;

import com.mochi.deeppixeldungeon.Assets;
import com.mochi.deeppixeldungeon.items.weapon.melee.MagesStaff;
import com.mochi.deeppixeldungeon.Dungeon;
import com.mochi.deeppixeldungeon.DungeonTilemap;
import com.mochi.deeppixeldungeon.actors.Actor;
import com.mochi.deeppixeldungeon.actors.Char;
import com.mochi.deeppixeldungeon.actors.buffs.Paralysis;
import com.mochi.deeppixeldungeon.effects.Effects;
import com.mochi.deeppixeldungeon.effects.MagicMissile;
import com.mochi.deeppixeldungeon.effects.Pushing;
import com.mochi.deeppixeldungeon.mechanics.Ballistica;
import com.mochi.deeppixeldungeon.messages.Messages;
import com.mochi.deeppixeldungeon.sprites.ItemSpriteSheet;
import com.mochi.deeppixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;



public class Repel extends DamageSpell {

    {
        image = ItemSpriteSheet.WAND_BLAST_WAVE;

        MP_cost = 5;

        Cooldown = 10;
    }

    public int min(int lvl){
        return 1+lvl;
    }

    public int max(int lvl){
        return 5+3*lvl;
    }

    @Override
    protected void onCast(Ballistica bolt) {
        Sample.INSTANCE.play( Assets.SND_BLAST );
        Repel.RepelWave.blast(bolt.collisionPos);

        int damage = damageRoll();

        //presses all tiles in the AOE first
        for (int i : PathFinder.NEIGHBOURS9){
            Dungeon.level.press(bolt.collisionPos+i, Actor.findChar(bolt.collisionPos+i));
        }

        //throws other chars around the center.
        for (int i  : PathFinder.NEIGHBOURS8){
            Char ch = Actor.findChar(bolt.collisionPos + i);

            if (ch != null){
                processSoulMark(ch, chargesPerCast());
                ch.damage(Math.round(damage * 0.667f), this);

                if (ch.isAlive()) {
                    Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                    int strength = 1 + Math.round(level() / 2f);
                    throwChar(ch, trajectory, strength);
                }
            }
        }

        //throws the char at the center of the blast
        Char ch = Actor.findChar(bolt.collisionPos);
        if (ch != null){
            processSoulMark(ch, chargesPerCast());
            ch.damage(damage, this);

            if (ch.isAlive() && bolt.path.size() > bolt.dist+1) {
                Ballistica trajectory = new Ballistica(ch.pos, bolt.path.get(bolt.dist + 1), Ballistica.MAGIC_BOLT);
                int strength = level() + 3;
                throwChar(ch, trajectory, strength);
            }
        }

        if (!curUser.isAlive()) {
            Dungeon.fail( getClass() );
            GLog.n( Messages.get( this, "ondeath") );
        }
    }

    public static void throwChar(final Char ch, final Ballistica trajectory, int power){
        int dist = Math.min(trajectory.dist, power);

        if (ch.properties().contains(Char.Property.BOSS))
            dist /= 2;

        if (dist == 0 || ch.properties().contains(Char.Property.IMMOVABLE)) return;

        if (Actor.findChar(trajectory.path.get(dist)) != null){
            dist--;
        }

        final int newPos = trajectory.path.get(dist);

        if (newPos == ch.pos) return;

        final int finalDist = dist;
        final int initialpos = ch.pos;

        Actor.addDelayed(new Pushing(ch, ch.pos, newPos, new Callback() {
            public void call() {
                if (initialpos != ch.pos) {
                    //something cased movement before pushing resolved, cancel to be safe.
                    ch.sprite.place(ch.pos);
                    return;
                }
                ch.pos = newPos;
                if (ch.pos == trajectory.collisionPos) {
                    ch.damage(Random.NormalIntRange((finalDist + 1) / 2, finalDist), this);
                    Paralysis.prolong(ch, Paralysis.class, Random.NormalIntRange((finalDist + 1) / 2, finalDist));
                }
                Dungeon.level.press(ch.pos, ch);
            }
        }), -1);
    }

    @Override
    //behaves just like glyph of Repulsion
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        int level = Math.max(0, staff.level());

        // lvl 0 - 25%
        // lvl 1 - 40%
        // lvl 2 - 50%
        if (Random.Int( level + 4 ) >= 3){
            int oppositeHero = defender.pos + (defender.pos - attacker.pos);
            Ballistica trajectory = new Ballistica(defender.pos, oppositeHero, Ballistica.MAGIC_BOLT);
            throwChar(defender, trajectory, 2);
        }
    }

    protected void fx( Ballistica bolt, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FORCE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }


    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0x664422 ); particle.am = 0.6f;
        particle.setLifespan(3f);
        particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
        particle.setSize( 1f, 2f);
        particle.radiateXY(2.5f);
    }

    public static class RepelWave extends Image {

        private static final float TIME_TO_FADE = 0.2f;

        private float time;

        public RepelWave(){
            super(Effects.get(Effects.Type.RIPPLE));
            origin.set(width / 2, height / 2);
        }

        public void reset(int pos) {
            revive();

            x = (pos % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
            y = (pos / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

            time = TIME_TO_FADE;
        }

        @Override
        public void update() {
            super.update();

            if ((time -= Game.elapsed) <= 0) {
                kill();
            } else {
                float p = time / TIME_TO_FADE;
                alpha(p);
                scale.y = scale.x = (1-p)*3;
            }
        }

        public static void blast(int pos) {
            Group parent = Dungeon.hero.sprite.parent;
            RepelWave b =  (RepelWave) parent.recycle(RepelWave.class);
            parent.bringToFront(b);
            b.reset(pos);
        }

    }
}


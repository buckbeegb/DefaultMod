package SicklyMod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import SicklyMod.DefaultMod;
import SicklyMod.util.TextureLoader;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Iterator;

import static SicklyMod.DefaultMod.makeRelicOutlinePath;
import static SicklyMod.DefaultMod.makeRelicPath;

public class BlightedStrength extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("BlightedStrength");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    public static int num_debuffs = 0;

    public BlightedStrength() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
    }

    // Gain 1 energy on equip.
    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    // Lose 1 energy on unequip.
    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart(){
        this.onRefreshHand();
    }
    @Override
    public void onRefreshHand(){
        int debuffCount = 0;
        AbstractPlayer p = AbstractDungeon.player;
        Iterator<AbstractPower> iter = p.powers.iterator();

        while (iter.hasNext()){
            AbstractPower pow = iter.next();
            if (pow.ID == "Weakened" || pow.ID == "Vulnerable" || pow.ID == "Frail"){
                debuffCount += pow.amount;
            }
            else if (pow.ID == "Focus" || pow.ID == "Dexterity") {
                if (pow.amount < 0) {
                    debuffCount -= pow.amount;
                }
            }
        }
        if (num_debuffs != debuffCount) {
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, (debuffCount - num_debuffs)), (debuffCount - num_debuffs)));
        }
        num_debuffs = debuffCount;
    }
}

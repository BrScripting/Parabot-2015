package scripts.bfighter.main;

import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Items.Option;
import org.rev317.min.api.methods.Players;

import scripts.org.bradsta.framework.utils.CombatUtils;
import scripts.org.bradsta.framework.utils.LootUtils;

@ScriptManifest(author = "Bradsta", category = Category.COMBAT, description = "Attacks things", name = "BFighter", servers = { "PkHonor" }, version = 1.0)
public class Main extends Script implements LoopTask {

	private final int[] npcIds = { 1265, 1267 };
	private final int[] foodIds = { 380 };
	private LootUtils loot = new LootUtils(new int[] { 995, 996 });

	public int loop() {
		if (CombatUtils.getHealth() < 25) {
			CombatUtils.interactWithItem(Option.CONSUME,
					Inventory.getItems(foodIds));
		}
		if (!Players.getMyPlayer().isInCombat()) {
			if (loot.isThereLoot())
				loot.loot();
			else
				CombatUtils.attack(CombatUtils.getNpcs(false, npcIds));
		}
		return 250;
	}

}

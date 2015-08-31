package scripts.bfighter.main;


import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Filter;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Items.Option;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Npc;

@ScriptManifest(author = "Bradsta", category = Category.COMBAT, description = "Attacks things", name = "BFighter", servers = {"PkHonor"}, version = 1.0)
public class Main extends Script implements LoopTask {
	
	private final int[] npcIds = {1265, 1267};

	public int loop() {
		if (Inventory.getItem(4588) != null && Skill.ATTACK.getLevel() >= 60) {
			final Item item = Inventory.getItem(4588);
			if (item != null)
				item.interact(Option.WEAR);
		}
		if (needToEat())
			eat();
		if (canAttack())
			attack();
		
		return 250;
	}
	
	private boolean needToEat() {
		final int health = Players.getMyPlayer().getHealth();
		Logger.addMessage("health: " + health);
		return Players.getMyPlayer().isInCombat() && health < 20;
	}
	
	private boolean canAttack() {
		Logger.addMessage("in cn attac");
		return !Players.getMyPlayer().isInCombat();
	}
	
	private void eat() {
		final Item item = Inventory.getItem(380);
		if (item != null) {
			item.interact(Option.CONSUME);
		}
	}
	
	private void attack() {
		final Npc[] targets = Npcs.getNearest(new Filter<Npc>() {
			
			public boolean accept(Npc arg0) {
				return arg0 != null && (arg0.getDef().getId() == 1265 || arg0.getDef().getId() == 1267) && !arg0.isInCombat();
			}
		});
		
		if (targets.length > 0) {
			Logger.addMessage("npc not null");
			sleep(new SleepCondition() {
				
				public boolean isValid() {
					targets[0].interact(1);
					return targets[0].getInteractingCharacter() != null && Players.getMyPlayer().isInCombat();
				}
			}, Random.between(2000, 3000));
		}
	}

}

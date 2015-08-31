package scripts.org.bradsta.framework.utils;

import org.parabot.environment.api.utils.Filter;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Items.Option;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.NpcDef;

public class CombatUtils {

	/**
	 * Get nearest npcs by id w/ option of them being in combat or not
	 * 
	 * @param inCombat
	 * @param ids
	 * @return list of npcs based on conditions
	 */
	public static Npc[] getNpcs(final boolean inCombat, final int... ids) {
		Npc[] npcs = Npcs.getNearest(new Filter<Npc>() {

			@Override
			public boolean accept(Npc n) {
				if (n != null) {
					final NpcDef def = n.getDef();
					for (int id : ids) {
						if (def != null) {
							if (def.getId() == id) {
								return inCombat ? true : n.isInCombat() ? false : true;
							}
						}
					}
				}
				return false;
			}
		});

		return npcs.length > 0 ? npcs : null;

	}

	public static void attack(Npc[] npcs) {
		if (Players.getMyPlayer().isInCombat())
			return;
		for (final Npc n : npcs) {
			if (n != null && !n.isInCombat()) {
				Time.sleep(new SleepCondition() {

					@Override
					public boolean isValid() {
						n.interact(Npcs.Option.ATTACK);
						return n.isInCombat() || n == null;
					}
				}, Random.between(6000, 7000));

				if ((n.isInCombat() && Players.getMyPlayer().isInCombat()) || Players.getMyPlayer().isInCombat())
					break;
			}
		}
	}

	public static void interactWithItem(final Option option,
			final Item... items) {
		for (final Item item : items) {
			if (item != null) {
				final int id = item.getId();
				final int count = Inventory.getCount(id);
				Time.sleep(new SleepCondition() {

					@Override
					public boolean isValid() {
						item.interact(option);
						return Inventory.getCount(id) < count;
					}
				}, Random.between(1500, 2200));

				if (Inventory.getCount(id) < count)
					break;
			}
		}
	}

	public static int getHealth() {
		return Skill.HITPOINTS.getLevel();
	}

}

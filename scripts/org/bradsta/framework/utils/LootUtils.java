package scripts.org.bradsta.framework.utils;

import org.parabot.environment.api.utils.Random;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.min.api.methods.GroundItems;
import org.rev317.min.api.methods.GroundItems.Option;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.wrappers.GroundItem;

public class LootUtils {

	private int[] lootIds;

	public LootUtils(final int[] lootIds) {
		this.lootIds = lootIds;
	}
	
	public boolean isThereLoot() {
		return GroundItems.getNearest(this.lootIds).length > 0;
	}

	public void loot() {
		final GroundItem[] loot = GroundItems.getNearest(this.lootIds);
		for (final GroundItem item : loot) {
			if (item != null) {
				final int id = item.getId();
				final int count = Inventory.getCount(true, id);
				Time.sleep(new SleepCondition() {

					@Override
					public boolean isValid() {
						item.interact(Option.TAKE);
						if (Inventory.getCount(true, id) > count) {
							System.out.println("Looted item id: " + id);
							return true;
						}
						return false;
					}
				}, Random.between(3000, 4000));
			}
		}
	}

}

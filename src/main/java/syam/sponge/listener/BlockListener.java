/**
 * Sponge - Package: syam.sponge.listener
 * Created: 2012/10/06 1:45:06
 */
package syam.sponge.listener;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import syam.sponge.Sponge;

/**
 * BlockListener (BlockListener.java)
 * @author syam(syamn)
 */
public class BlockListener implements Listener {
	public final static Logger log = Sponge.log;
	private static final String logPrefix = Sponge.logPrefix;
	private static final String msgPrefix = Sponge.msgPrefix;

	private final Sponge plugin;

	public BlockListener(final Sponge plugin){
		this.plugin = plugin;
	}

	// ブロックを設置した
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event){
		// スポンジでなければ返す
		if (!event.getBlock().getType().equals(Material.SPONGE)){
			return;
		}

		Block sponge = event.getBlock();
		World world = sponge.getWorld();
		int radius = plugin.getConfigs().getRadius();

		int x = sponge.getX();
		int y = sponge.getY();
		int z = sponge.getZ();

		for (int cx = -radius; cx <= radius; cx++){ // X軸走査
			for (int cy = -radius; cy <= radius; cy++){ // Y軸走査
				for (int cz = -radius; cz <= radius; cz++){ // Z軸走査
					// ブロックIDチェック
					int id = world.getBlockTypeIdAt(x + cx, y + cy, z + cz);
					// 水か溶岩なら空気に変える
					if (id == 8 || id == 9 || id == 10 || id == 11){
						world.getBlockAt(x + cx, y + cy, z + cz).setTypeId(0);
					}
				}
			}
		}
	}
}

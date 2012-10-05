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
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import syam.sponge.Sponge;

/**
 * BlockListener (BlockListener.java)
 * @author syam(syamn)
 */
public class SpongeListener implements Listener {
	public final static Logger log = Sponge.log;
	private static final String logPrefix = Sponge.logPrefix;
	private static final String msgPrefix = Sponge.msgPrefix;

	private final Sponge plugin;

	public SpongeListener(final Sponge plugin){
		this.plugin = plugin;
	}

	// ブロックを設置した
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event){
		// スポンジでなければ返す 19:SPONGE
		if (event.getBlock().getTypeId() != 19){
			return;
		}

		Block sponge = event.getBlock();
		World world = sponge.getWorld();
		int radius = plugin.getConfigs().getRadius();

		int x = sponge.getX();
		int y = sponge.getY();
		int z = sponge.getZ();

		int id = 0;

		for (int cx = -radius; cx <= radius; cx++){ // X軸走査
			for (int cy = -radius; cy <= radius; cy++){ // Y軸走査
				for (int cz = -radius; cz <= radius; cz++){ // Z軸走査
					// ブロックIDチェック
					id = world.getBlockTypeIdAt(x + cx, y + cy, z + cz);
					// 水か溶岩なら空気に変える
					if (id == 8 || id == 9 || id == 10 || id == 11){
						world.getBlockAt(x + cx, y + cy, z + cz).setTypeId(0);
					}
				}
			}
		}
	}

	// 流体ブロックの流れが変わった
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockFromTo(final BlockFromToEvent event){
		// 流体ブロック以外なら返す
		int id = event.getBlock().getTypeId();
		if (!(id == 8 || id == 9 || id == 10 || id == 11)){
			return;
		}

		Block toBlock = event.getToBlock();
		World world = toBlock.getWorld();
		int radius = plugin.getConfigs().getRadius();

		int x = toBlock.getX();
		int y = toBlock.getY();
		int z = toBlock.getZ();

		for (int cx = -radius; cx <= radius; cx++){ // X軸走査
			for (int cy = -radius; cy <= radius; cy++){ // Y軸走査
				for (int cz = -radius; cz <= radius; cz++){ // Z軸走査
					// ブロックIDチェック: 19(SPONGE)
					if(world.getBlockTypeIdAt(x + cx, y + cy, z + cz) == 19){
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	// 流体ブロックの流れが変わった
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event){
		Block block = event.getBlockClicked().getRelative(event.getBlockFace());
		World world = block.getWorld();
		int radius = plugin.getConfigs().getRadius();

		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();

		for (int cx = -radius; cx <= radius; cx++){ // X軸走査
			for (int cy = -radius; cy <= radius; cy++){ // Y軸走査
				for (int cz = -radius; cz <= radius; cz++){ // Z軸走査
					// ブロックIDチェック: 19(SPONGE)
					if(world.getBlockTypeIdAt(x + cx, y + cy, z + cz) == 19){
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}

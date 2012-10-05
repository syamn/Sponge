/**
 * Sponge - Package: syam.sponge.command
 * Created: 2012/10/06 2:07:05
 */
package syam.sponge.command;

import syam.sponge.util.Actions;

/**
 * ReloadCommand (ReloadCommand.java)
 * @author syam(syamn)
 */
public class ReloadCommand extends BaseCommand {
	public ReloadCommand(){
		bePlayer = false;
		name = "reload";
		argLength = 0;
		usage = "<- reload config.yml";
	}

	@Override
	public void execute() {
		try{
			plugin.getConfigs().loadConfig(false);
		}catch (Exception ex){
			log.warning(logPrefix+"an error occured while trying to load the config file.");
			ex.printStackTrace();
			return;
		}

		Actions.message(sender, "&aConfiguration reloaded!");
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("sponge.reload");
	}
}
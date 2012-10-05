/**
 * Sponge - Package: syam.sponge
 * Created: 2012/10/06 1:20:49
 */
package syam.sponge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import syam.sponge.command.BaseCommand;
import syam.sponge.command.HelpCommand;
import syam.sponge.util.Metrics;


/**
 * Sponge (Sponge.java)
 * @author syam(syamn)
 */
public class Sponge extends JavaPlugin{
	// ** Logger **
		public final static Logger log = Logger.getLogger("Minecraft");
		public final static String logPrefix = "[Sponge] ";
		public final static String msgPrefix = "&6[Sponge] &f";

		// ** Listener **
		//BlockListener blockListener = new BlockListener(this);

		// ** Commands **
		private List<BaseCommand> commands = new ArrayList<BaseCommand>();

		// ** Private Classes **
		private ConfigurationManager config;

		// ** Instance **
		private static Sponge instance;


		/**
		 * プラグイン起動処理
		 */
		@Override
		public void onEnable(){
			instance  = this;
			PluginManager pm = getServer().getPluginManager();
			config = new ConfigurationManager(this);

			// loadconfig
			try{
				config.loadConfig(true);
			}catch (Exception ex){
				log.warning(logPrefix+"an error occured while trying to load the config file.");
				ex.printStackTrace();
			}

			// プラグインを無効にした場合進まないようにする
			if (!pm.isPluginEnabled(this)){
				return;
			}

			// 権限ハンドラセットアップ
			//Perms.setupPermissionHandler();

			// Regist Listeners
			//pm.registerEvents(blockListener, this);

			// コマンド登録
			registerCommands();

			// マネージャ
			//bm = new LikeManager(this);

			// メッセージ表示
			PluginDescriptionFile pdfFile=this.getDescription();
			log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is enabled!");

			setupMetrics(); // mcstats
		}

		/**
		 * プラグイン停止処理
		 */
		@Override
		public void onDisable(){
			// メッセージ表示
			PluginDescriptionFile pdfFile=this.getDescription();
			log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is disabled!");
		}

		/**
		 * コマンドを登録
		 */
		private void registerCommands(){
			// Intro Commands
			commands.add(new HelpCommand());

			// General Commands

			// Admin Commands
			//commands.add(new ReloadCommand());
		}

		/**
	     * Metricsセットアップ
	     */
	    private void setupMetrics(){
	        try {
	            Metrics metrics = new Metrics(this);
	            metrics.start();
	        } catch (IOException ex) {
	            log.warning(logPrefix+"cant send metrics data!");
	            ex.printStackTrace();
	        }
	    }

		/**
		 * コマンドが呼ばれた
		 */
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
			if (cmd.getName().equalsIgnoreCase("sponge")){
				if(args.length == 0){
					// 引数ゼロはヘルプ表示
					args = new String[]{"help"};
				}

				outer:
				for (BaseCommand command : commands.toArray(new BaseCommand[0])){
					String[] cmds = command.name.split(" ");
					for (int i = 0; i < cmds.length; i++){
						if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])){
							continue outer;
						}
						// 実行
						return command.run(this, sender, args, commandLabel);
					}
				}
				// 有効コマンドなし ヘルプ表示
				new HelpCommand().run(this, sender, args, commandLabel);
				return true;
			}
			return false;
		}

		/* getter */
		/**
		 * コマンドを返す
		 * @return List<BaseCommand>
		 */
		public List<BaseCommand> getCommands(){
			return commands;
		}

		/**
		 * 設定マネージャを返す
		 * @return ConfigurationManager
		 */
		public ConfigurationManager getConfigs() {
			return config;
		}

		/**
		 * インスタンスを返す
		 * @return Likesインスタンス
		 */
		public static Sponge getInstance(){
			return instance;
		}
}

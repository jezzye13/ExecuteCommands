package tk.wisdomunit.exe;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ExecuteCommands extends JavaPlugin {

	private Player executer;
	private String argsCommand;
	private boolean worked;
	private int ticks = 0;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("exe") && sender.isOp()) {
			if (args.length <= 2) {
				sender.sendMessage("/exe {PLAYER} {TICKS} {YOUR COMMAND}");
				return true;
			} else {
				executer = Bukkit.getServer().getPlayer(args[0]);
				if (executer == null) {
					sender.sendMessage("Can`t find the player: " + args[0]);
				}

				try {
					ticks = Integer.parseInt(args[1]);

					StringBuilder sb = new StringBuilder();
					for (int i = 2; i < args.length; i++) {
						if (i > 2)
							sb.append(' ');
						sb.append(args[i]);
					}

					argsCommand = sb.toString();

					if (callCommand()) {
						sender.sendMessage("The command " + argsCommand
								+ " was called by " + args[0] + " worked.");
					} else {
						sender.sendMessage("The command " + argsCommand
								+ " failed.");
					}

				} catch (Exception e) {
					getLogger().warning("Oeps!");
					e.printStackTrace();
					for (int i = 0; i < args.length; i++) {
						getLogger().info(args[i] + " | " + i);
					}
				}
			}
			return true;
		}

		return false;
	}

	private boolean callCommand() throws Exception {
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {

				if (!executer.isOp()) {
					executer.setOp(true);
					worked = Bukkit.dispatchCommand(executer, argsCommand);
					executer.setOp(false);
				} else {
					worked = Bukkit.dispatchCommand(executer, argsCommand);
				}

			}
		}, ticks);
		return worked;
	}

}

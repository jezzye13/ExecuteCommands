package tk.wisdomunit.exe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ExecuteCommands extends JavaPlugin {

	private Player executer;
	private String argsCommand;
	private CommandSender sender;
	private int ticks = 0;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("exe") && sender.isOp()) {
			if (args.length <= 2) {
				sender.sendMessage("/exe {PLAYER} {TICKS} {YOUR COMMAND}");
				return true;
			} else {
				try {
					executer = Bukkit.getServer().getPlayer(args[0]);
				} catch (NullPointerException e) {
				}

				try {
					ticks = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage("Parameter {TICKS} has to be a number!");
				}

				try {
					StringBuilder sb = new StringBuilder();
					for (int i = 2; i < args.length; i++) {
						if (i > 2)
							sb.append(' ');
						sb.append(args[i]);
					}

					argsCommand = sb.toString();

					this.sender = sender;
					if (executer == null) {
						sender.sendMessage("Can`t find the player: "
								+ ChatColor.YELLOW + args[0]);
					} else {
						callCommand();
					}

				} catch (Exception e) {
					getLogger()
							.warning("Oeps! Don`t worry some one can fix it");
					getLogger()
							.warning(
									"Please Mail me @ info@wisdomunit.tk and i will help you ;)");
					e.printStackTrace();
				}
			}
			return true;
		}

		return false;
	}

	private void callCommand() throws Exception {
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				boolean worked = false;

				if (!executer.isOp()) {
					executer.setOp(true);
					worked = Bukkit.dispatchCommand(executer, argsCommand);
					executer.setOp(false);
				} else {
					worked = Bukkit.dispatchCommand(executer, argsCommand);
				}

				if (worked) {
					sender.sendMessage("The command " + ChatColor.YELLOW
							+ argsCommand + ChatColor.WHITE + " was called by "
							+ ChatColor.YELLOW + executer.getName()
							+ ChatColor.WHITE + " and worked.");
				} else {
					sender.sendMessage("The command " + ChatColor.YELLOW
							+ argsCommand + ChatColor.WHITE + " failed.");
				}

			}
		}, ticks);

	}

}

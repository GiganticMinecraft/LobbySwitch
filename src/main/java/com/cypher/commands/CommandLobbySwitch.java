package com.cypher.commands;

import com.cypher.LobbySwitch;
import com.cypher.ServerItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommandLobbySwitch implements TabExecutor {

    private final List<String> arguments = new ArrayList<String>() {
        {
            add("add");
            add("edit");
            add("list");
            add("remove");
            add("version");
        }
    };

    private final List<String> editSubArguments = new ArrayList<String>() {
        {
            add("amount");
            add("material");
            add("slot");
            add("target");
        }
    };

    private String PREFIX = "\u00bb ";

    @Override
    public boolean onCommand(final CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender == LobbySwitch.p.getServer().getConsoleSender()) {
            PREFIX = "> ";
        }
        switch (args.length) {
            case 1:
                switch (args[0].toLowerCase()) {
                    case "list":
                    case "l":
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Server list");
                        for (String string : LobbySwitch.p.getConfigManager().getSlots()) {
                            ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(Integer.parseInt(string));
                            commandSender.sendMessage("  " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Slot " + ChatColor.GRAY + string);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Amount: " + ChatColor.GRAY + serverItem.getAmount());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Name: " + ChatColor.GRAY + serverItem.getDisplayName());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Material: " + ChatColor.GRAY + serverItem.getMaterial());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Target: " + ChatColor.GRAY + serverItem.getTargetServer());
                        }
                        return true;
                    case "version":
                    case "v":
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Lobby Switch version " + ChatColor.GRAY + LobbySwitch.p.getDescription().getVersion() + ChatColor.RED + ".");
                        return true;
                    default:
                        commandSender.sendMessage(getInvalidFormat());
                        return true;
                }
            case 2:
                switch (args[0].toLowerCase()) {
                    case "remove":
                    case "r":
                        try {
                            if (LobbySwitch.p.getConfigManager().removeSlot(Integer.parseInt(args[1]))) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Successfully removed server in slot " + ChatColor.GRAY + args[1] + ChatColor.RED + ".");
                            } else {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "No server found in slot " + ChatColor.GRAY + args[1] + ChatColor.RED + ".");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <remove|r> <Slot>");
                            }
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[1] + ChatColor.RED + "\"" + " is not a valid integer.");
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <remove|r> <Slot>");
                        }
                        return true;
                    default:
                        commandSender.sendMessage(getInvalidFormat());
                        return true;
                }
            case 4:
                switch (args[0].toLowerCase()) {
                    case "edit":
                    case "e":
                        int slot;
                        try {
                            slot = Integer.parseInt(args[1]);
                            if (slot < 1) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be greater than " + ChatColor.GRAY + "0" + ChatColor.RED + ".");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch ------------------------------------------------");
                                return true;
                            }
                            if (slot > LobbySwitch.p.getConfigManager().getInventory().getSize()) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be less than or equal to " + ChatColor.GRAY + LobbySwitch.p.getConfigManager().getInventory().getSize() + ChatColor.RED + ".");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch ------------------------------------------------");
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[3] + ChatColor.RED + "\"" + " is not a valid integer.");
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch ------------------------------------------------");
                            return true;
                        }
                        ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(slot);
                        switch (args[2].toLowerCase()) {
                            case "amount":
                                int amount = Integer.parseInt(args[3]);
                                try {
                                    if (amount > 64) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The amount can't exceed " + ChatColor.GRAY + "64" + ChatColor.RED + ".");
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> amount <New Amount>");
                                        return true;
                                    }
                                    if (amount < 1) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The amount must be greater than " + ChatColor.GRAY + "0" + ChatColor.RED + ".");
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> amount <New Amount>");
                                        return true;
                                    }
                                } catch (NumberFormatException e) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[2] + ChatColor.RED + "\"" + " is not a valid integer.");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> amount <New Amount>");
                                    return true;
                                }
                                serverItem.setAmount(amount);
                                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The amount has been changed to " + ChatColor.GRAY + amount + ChatColor.RED + ".");
                                return true;
                            case "material":
                                Material material;
                                try {
                                    material = Material.getMaterial(Integer.parseInt(args[3]));
                                } catch (NumberFormatException e) {
                                    try {
                                        material = Material.valueOf(args[3]);
                                    } catch (IllegalArgumentException exception) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[3] + ChatColor.RED + "\"" + " is not a valid item name or id.");
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> material <New Material>");
                                        return true;
                                    }
                                }
                                serverItem.setMaterial(material);
                                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The material has been changed to " + ChatColor.GRAY + material.name() + ChatColor.RED + ".");
                                return true;
                            case "slot":
                                int newSlot;
                                try {
                                    newSlot = Integer.parseInt(args[3]);
                                    if (newSlot < 1) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be greater than " + ChatColor.GRAY + "0" + ChatColor.RED + ".");
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> slot <New Slot>");
                                        return true;
                                    }
                                    if (newSlot > LobbySwitch.p.getConfigManager().getInventory().getSize()) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be less than or equal to " + ChatColor.GRAY + LobbySwitch.p.getConfigManager().getInventory().getSize() + ChatColor.RED + ".");
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> slot <New Slot>");
                                        return true;
                                    }
                                    if (slot == newSlot) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The new slot number can not be equal to the old slot number.");
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> slot <New Slot>");
                                        return true;
                                    }
                                    LobbySwitch.p.getConfigManager().removeSlot(slot);
                                    LobbySwitch.p.getConfigManager().saveServerItem(serverItem, newSlot);
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Moved the item in slot " + ChatColor.GRAY + slot + ChatColor.RED + " to slot " + ChatColor.GRAY + newSlot + ChatColor.RED + ".");
                                } catch (NumberFormatException e) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[3] + ChatColor.RED + "\"" + " is not a valid integer.");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> slot <New Slot>");
                                    return true;
                                }
                                return true;
                            case "target":
                                String targetServer;
                                if (LobbySwitch.p.getServers().contains(args[3])) {
                                    targetServer = args[3];
                                } else {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[4] + ChatColor.RED + "\"" + " is not a valid target server.");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch edit <Slot> target <New Target>");
                                    return true;
                                }
                                serverItem.setTargetServer(targetServer);
                                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The target has been changed to " + ChatColor.GRAY + targetServer + ChatColor.RED + ".");
                                return true;
                            default:
                                commandSender.sendMessage(getInvalidFormat());
                                return true;
                        }
                    default:
                        commandSender.sendMessage(getInvalidFormat());
                        return true;
                }
            default:
                if (args.length > 5) {
                    if (args[0].equals("add") || args[0].equals("a")) {
                        ItemStack itemStack;
                        int slot;
                        String targetServer;
                        String color;
                        String displayName;

                        try {
                            int amount = Integer.parseInt(args[2]);
                            if (amount > 64) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The amount can't exceed " + ChatColor.GRAY + "64" + ChatColor.RED + ".");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            if (amount < 1) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The amount must be greater than " + ChatColor.GRAY + "0" + ChatColor.RED + ".");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            try {
                                itemStack = new ItemStack(Integer.parseInt(args[1]), amount);
                            } catch (NumberFormatException e) {
                                try {
                                    itemStack = new ItemStack(Material.valueOf(args[1]), amount);
                                } catch (IllegalArgumentException exception) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[1] + ChatColor.RED + "\"" + " is not a valid item name or id.");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                            }
                            try {
                                slot = Integer.parseInt(args[3]);
                                if (slot < 1) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be greater than " + ChatColor.GRAY + "0" + ChatColor.RED + ".");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                                if (slot > LobbySwitch.p.getConfigManager().getInventory().getSize()) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be less than or equal to " + ChatColor.GRAY + LobbySwitch.p.getConfigManager().getInventory().getSize() + ChatColor.RED + ".");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                                if (LobbySwitch.p.getConfigManager().getSlots().contains(String.valueOf(slot))) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number " + ChatColor.GRAY + slot + ChatColor.RED + " is already being used.");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                            } catch (NumberFormatException e) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[3] + ChatColor.RED + "\"" + " is not a valid integer.");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            if (LobbySwitch.p.getServers().contains(args[4])) {
                                targetServer = args[4];
                            } else {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[4] + ChatColor.RED + "\"" + " is not a valid target server.");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 5; args.length > i; i++) {
                                if (i != 5) {
                                    stringBuilder.append(" ");
                                }
                                stringBuilder.append(args[i]);
                            }
                            displayName = stringBuilder.toString().replace("&", "\247");
                            ServerItem serverItem = new ServerItem(itemStack.getType(), itemStack.getAmount(), displayName, targetServer);
                            LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                            commandSender.sendMessage("  " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Slot " + ChatColor.GRAY + slot);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Amount: " + ChatColor.GRAY + serverItem.getAmount());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Name: " + ChatColor.GRAY + serverItem.getDisplayName());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Material: " + ChatColor.GRAY + serverItem.getMaterial());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Target: " + ChatColor.GRAY + serverItem.getTargetServer());

                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[2] + ChatColor.RED + "\"" + " is not a valid integer.");
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                            return true;
                        }
                        return true;
                    }
                }
                commandSender.sendMessage(getInvalidFormat());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> matches = new ArrayList<>();
        String search = args[args.length - 1].toLowerCase();
        if (args.length == 1) {
            for (String argument : arguments) {
                if (argument.toLowerCase().startsWith(search)) {
                    matches.add(argument);
                }
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (Material argument : Material.values()) {
                    if (argument.name().toLowerCase().startsWith(search)) {
                        matches.add(argument.name());
                    }
                }
            }
            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("edit") || args[0].equals("e")) {
                matches.addAll(LobbySwitch.p.getConfigManager().getSlots());
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (int i = 1; 64 >= i; i++) {
                    if (String.valueOf(i).toLowerCase().startsWith(search)) {
                        matches.add(String.valueOf(i));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("edit") || args[0].equals("e")) {
                for (String argument : editSubArguments) {
                    if (argument.toLowerCase().startsWith(search)) {
                        matches.add(argument);
                    }
                }
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                ArrayList<String> used = new ArrayList<String>(LobbySwitch.p.getConfigManager().getSlots());
                for (int i = 1; LobbySwitch.p.getConfigManager().getInventory().getSize() >= i; i++) {
                    if (String.valueOf(i).toLowerCase().startsWith(search) && !used.contains(String.valueOf(i))) {
                        matches.add(String.valueOf(i));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("edit") || args[0].equals("e")) {
                if (args[2].equalsIgnoreCase("amount")) {
                    for (int i = 1; 64 >= i; i++) {
                        if (String.valueOf(i).toLowerCase().startsWith(search)) {
                            matches.add(String.valueOf(i));
                        }
                    }
                }
                if (args[2].equalsIgnoreCase("material")) {
                    for (Material argument : Material.values()) {
                        if (argument.name().toLowerCase().startsWith(search)) {
                            matches.add(argument.name());
                        }
                    }
                }
                if (args[2].equalsIgnoreCase("slot")) {
                    ArrayList<String> used = new ArrayList<String>(LobbySwitch.p.getConfigManager().getSlots());
                    for (int i = 1; LobbySwitch.p.getConfigManager().getInventory().getSize() >= i; i++) {
                        if (String.valueOf(i).toLowerCase().startsWith(search) && !used.contains(String.valueOf(i))) {
                            matches.add(String.valueOf(i));
                        }
                    }
                }
                if (args[2].equalsIgnoreCase("target")) {
                    for (String string : LobbySwitch.p.getServers()) {
                        if (string.toLowerCase().startsWith(search)) {
                            matches.add(string);
                        }
                    }
                }
            }
        }
        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (String string : LobbySwitch.p.getServers()) {
                    if (string.toLowerCase().startsWith(search)) {
                        matches.add(string);
                    }
                }
            }
        }
        return matches;
    }

    private String getInvalidFormat() {
        return
                ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Invalid command format\n" +
                        ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>\n" +
                        ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <edit|e> <Slot> <Amount|Material|Slot|Target> <New Amount|New Material|New Slot|New Target>\n" +
                        ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <list|l>\n" +
                        ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <remove|r> <Slot>\n" +
                        ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <version|v>";
    }
}
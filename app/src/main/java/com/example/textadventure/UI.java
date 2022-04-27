package com.example.textadventure;

public class UI {
    enum Commands {
        LOOK_AROUND("look around", 2),
        INSPECT("inspect", 0),
        TAKE("take", 4),
        DROP("drop", 4),
        MOVE("move", 5),
        ATTACK("attack", 3),
        //DRINK("drink"),
        //CAST("cast"),
        HELP("help", 0),
        EXIT("exit", 0),
        INVENTORY("inventory", 2),
        CLEAR("clear", 0),
        USE("use", 3),
        BOOKMARK("bookmark", 0),
        ENERGY("energy", 0),
        REST("rest", 0),
        HEALTH("health", 0);

        private String strCommand;
        private int speed;

        private Commands(String command, int speed) {
            this.strCommand = command;
            this.speed = speed;
        }

        public String getStrCommand() {
            return this.strCommand;
        }
        public int getSpeedCommand(){
            return this.speed;
        }
    }

    public static String helpCommand(String command){
        if (command.equals("all")){
            // System.out.println("\nThis is a text based adventure game where you fight monsters. How many floors can you decesend?\n");
            String output = "";
            output += "\nCommands:\n";
            for (Commands name : Commands.values()){
                output += String.format("\t%-15s %s %d", name.getStrCommand() + ",", "Energy Cost:", name.getSpeedCommand() + "\n");
            }
            return output += "\nUse help to get back to this screen\nUse help [command name] to learn about that command" + "\nYou can alse use [help how to play] for a description how to play\n";
        } else if (command.equals(Commands.DROP.getStrCommand())){
            return "\tThis will drop an item from your inventory to the ground\n\tUse: drop (item name)";
        } else if (command.equals(Commands.LOOK_AROUND.getStrCommand())) {
            return "\tThis will show the room description again\n\tUse: look around";
        } else if (command.equals(Commands.INVENTORY.getStrCommand())) {
            return "\tThis will show you the inventory of your player\n\tUse: inventory";
        } else if (command.equals(Commands.CLEAR.getStrCommand())) {
            return "\tThis will clear the output of the console and bring your cursor to the top\n\tUse: clear";
        } else if (command.equals(Commands.TAKE.getStrCommand())) {
            return "\tThis will make your character pick up an object in the room or chest and it will go to inventory\n\tUse: take [object]";
        } else if (command.equals(Commands.INSPECT.getStrCommand())) {
            return "\tThis will display an object in the rooms description\n\tUse: inspect [object]";
        } else if (command.equals(Commands.EXIT.getStrCommand())) {
            return "\tThis will exit the game\n\tUse: exit";
        } else if (command.equals(Commands.MOVE.getStrCommand())) {
            return "\tThis will move your character in a direction if possible\n\tDirections (North, north, N, n) / (South, south, S, s) etc..\n\tUse: move [direction]";
        } else if (command.equals(Commands.ATTACK.getStrCommand())) {
            return "\tThis will attack an Actor in the room with a spesificed weapon in your inventory\n\tUse: attack [Actor] with [weapon]";
        } else if (command.equals(Commands.CLEAR.getStrCommand())) {
            return "\tThis will clear the output of the console and bring your cursor to the top\n\tUse: clear";
        } else if (command.equals(Commands.HEALTH.getStrCommand())) {
            return "\tThis will display your health in a status bar\n\tUse: health";
        } else if (command.equals(Commands.USE.getStrCommand())) {
            return "\tThis will use a special item in your inventory Ex: a map (>use map)\n\tUse: use [item]";
        } else if (command.equals(Commands.BOOKMARK.getStrCommand())) {
            return "\tThis will bookmark a room on your map (where you are currently) and save a description of the bookmark\n\tUse: bookmark [visual character] : [description] / Use: bookmark remove [visual char]\nEx. bookmark a : where the stairs are";
            //System.out.println("\tSorry this isn't currently implemented");
        } else if (command.equals(Commands.ENERGY.getStrCommand())){
            return "\tThis will display how much energy you have left for your turn\n\tUse: energy";
        } else if (command.equals("how to play")){
            return "The goal of the game is to save your family at the lowest level of the castle. In order to to that you need to find the stairs on each level in order to decend.\nDefeating whatever bosses you meet along the way. There are several things you can do inorder to help yourself beat the bosses and decend the floor. \nYou can look around the floor moving through rooms to find weapons that do more damage. You can also find spells and magic items to boost your stats for a certain amount of time to help beat the boss. \nThe floors are randomly generated, so you won't the get the same experience twice.";
        } else if (command.equals(Commands.REST.getStrCommand())){
            return "This will make your character rest bringing there energy back to full. But it will make it so enemies can attack you.";
        } else {
            return "\tSorry I don't know what command you wanted";
        }
    }

    public static String move(String command, Player player, Floor floor1, int floorSize){
        int x = player.getXCoord();
        int y = player.getYCoord();

        String output = "";

        // move north
        if (command.equals("N") || command.equals("n")) {
            if (y < (floorSize - 2) && y >= 0) {
                try {
                    if (floor1.getDoor(x, y, Direction.NORTH).isOpen()){ //Sorry Thomas, had to fix the doors
                        player.setYCoord(y + 1);
                        output = floor1.getDescription(player.getXCoord(), player.getYCoord());
                    }
                } catch (ThingNotFoundException e) {
                    output = "You don't see a door in that wall. You can't move that way.";
                }
            }else{
                output = "You don't see a door in that wall. You can't move that way.";
            }
        }
        //move south
        if (command.equals("S") || command.equals("s")) {
            if (y < floorSize - 1 && y > 0) {
                try {
                    if (floor1.getDoor(x, y, Direction.SOUTH).isOpen()){
                        player.setYCoord(y - 1);
                    }
                } catch (ThingNotFoundException e) {
                    output = "You don't see a door in that wall. You can't move that way.";
                }
                output = floor1.getDescription(player.getXCoord(), player.getYCoord());
            } else {
                output = "You don't see a door in that wall. You can't move that way.";
            }
        }
        //move east
        if (command.equals("E") || command.equals("e")) {
            if (x < floorSize - 1 && x >= 0) {
                try {
                    if (floor1.getDoor(x, y, Direction.EAST).isOpen()){
                        player.setXCoord(x + 1);
                        output = floor1.getDescription(player.getXCoord(), player.getYCoord());
                    }
                } catch (ThingNotFoundException e) {
                    output = "You don't see a door in that wall. You can't move that way.";
                }
            } else {
                output = "You don't see a door in that wall. You can't move that way.";
            }
        }
        //move west
        if (command.equals("W") || command.equals("w")) {
            if (x <= floorSize - 1 && x > 0) {
                try {
                    if (floor1.getDoor(x, y, Direction.WEST).isOpen()){
                        player.setXCoord(x - 1);
                        output = floor1.getDescription(player.getXCoord(), player.getYCoord());
                    }
                } catch (ThingNotFoundException e) {
                    output = "You don't see a door in that wall. You can't move that way.";
                }
            } else {
                output = "You don't see a door in that wall. You can't move that way.";
            }

        }
        return output;
    }
}

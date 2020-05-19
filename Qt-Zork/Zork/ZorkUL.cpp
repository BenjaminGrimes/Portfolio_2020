#include "ZorkUL.h"

/**
 * @author Benjamin
 * Functions By Daniel: RandomizeEnemy()
 */

ZorkUL::ZorkUL() : player(100)
{
    setInCombat(false);
	createRooms();
}

ZorkUL::~ZorkUL()
{
    cout << "Destroying ZorkUL..." << endl;
    for(vector<Room*>::iterator itr = rooms.begin(); itr != rooms.end(); itr++)
    {
        //cout << "Deleting " << endl;
        delete (*itr);
    }
    rooms.clear();
}

void ZorkUL::RandomizeEnemy()
{
    int randRoom = getRandom<int>(9)+1;
    rooms.at(randRoom)->addEnemy(new Enemy("Boogy"));
    cout << "Enemy add to room " << randRoom;
}

void ZorkUL::createRooms()
{
    Room *cave = new Room("Cave");
        cave->setRoomDescription("An empty cave...\nTheres nothing to see here, I better get moving...");
        cave->addItem(new Potion(Potion::PotionType::health_potion));
        cave->addItem(new Potion(Potion::PotionType::health_potion));

    Room *woods = new Room("Woods");
        woods->setRoomDescription("Woods?? I hate the woods!\nIt's so spoopy and I'm nowhere near home...");

    Room *abandoned_house = new Room("Abandoned House");
        abandoned_house->setRoomDescription("An abandoned house?\nIt looks like bad things went down around here...");
        abandoned_house->addEnemy(new Enemy("Greeny"));
        abandoned_house->addItem(new Potion(Potion::PotionType::health_potion));

    Room *field = new Room("Field");
        field->setRoomDescription("Nothing as far as the eye can see.\nI should get going.");

    Room *meadow = new Room("Meadow");
        meadow->setRoomDescription("More nothing as far as the eye can see!");

    Room *abandoned_mill = new Room("Abandoned Mill");
        abandoned_mill->setRoomDescription("Not much around here, I should keep moving...");
        abandoned_mill->addItem(new Potion(Potion::PotionType::health_potion));

    Room *bridge = new Room("Bridge");
        bridge->setRoomDescription("Hmm I wonder if there's a troll under this bridge?");
        bridge->addItem(new Potion(Potion::PotionType::health_potion));

    Room *empty_well = new Room("Empty Well");
        empty_well->setRoomDescription("Great the well has dried out...\nI guess I'll wait for a drink then.");
        empty_well->addItem(new Potion(Potion::PotionType::health_potion));
        empty_well->addItem(new Potion(Potion::PotionType::magic_potion));
        empty_well->addItem(new Potion(Potion::PotionType::teleportation_potion));

    Room *stables = new Room("Stables");
        stables->setRoomDescription("Stables...\nBut no horses to around. How strange.");
        stables->addItem(new Potion(Potion::PotionType::health_potion));

    Room *abandoned_town = new Room("Abandoned Town");
        abandoned_town->setRoomDescription("This town is pretty banged up.\nI wonder what happened...");
        abandoned_town->addEnemy(new Enemy("Fred the Fireman"));
        abandoned_town->addItem(new Potion(Potion::PotionType::health_potion));
        abandoned_town->addItem(new Potion(Potion::PotionType::magic_potion));

    Room *destroyed_fort = new Room("Destroyed Fort");
        destroyed_fort->setRoomDescription("Boy oh boy this place has been F'ed up!");
        destroyed_fort->addEnemy(new Enemy("Philip the Orc"));

    Room *ridge = new Room("Ridge");
        ridge->setRoomDescription("I better be careful...\nI don't want to lose my balance here.");
        ridge->addItem(new Potion(Potion::PotionType::health_potion));

    Room *mountains = new Room("Mountains");
        mountains->setRoomDescription("I'm up so high here!\nI think I can see my house.");
        mountains->addEnemy(new Enemy("Todd the Elf"));
        mountains->addItem(new Potion(Potion::PotionType::magic_potion));

    Room *valley = new Room("Valley");
        valley->setRoomDescription("Walking... So much walking.....");
        valley->addItem(new Potion(Potion::PotionType::health_potion));

    Room *shoreline = new Room("Shoreline");
        shoreline->setRoomDescription("Just smell that fresh air!\nI think I'm closer to home.");
        shoreline->addItem(new Potion(Potion::PotionType::health_potion));

    Room *river = new Room("River");
        river->setRoomDescription("Great now I'm wet...");
        river->addItem(new Potion(Potion::PotionType::magic_potion));

    Room *island = new Room("Island");
        island->setRoomDescription("I think I could be King of this Island\nI should get moving, I'm close now.");
        island->addItem(new Potion(Potion::PotionType::health_potion));

    Room *beach = new Room("Beach");
        beach->setRoomDescription("The beach... so sandy");

    Room *rocky_hills = new Room("Rocky Hills");
        rocky_hills->setRoomDescription("Oh my feet! These hills are so rocky.");
        rocky_hills->addItem(new Potion(Potion::PotionType::health_potion));

    Room *graveyard = new Room("Graveyard");
        graveyard->setRoomDescription("Graveyard? I hate graveyards...\nI shouldn't stick around here!");
        graveyard->addEnemy(new Enemy("Betty Birds"));
        graveyard->addItem(new Potion(Potion::PotionType::magic_potion));

    Room *old_castle = new Room("Old Castle");
        old_castle->setRoomDescription("Boy this castle sure is spooky!");
        old_castle->addEnemy(new Enemy("Hairy Harry"));
        old_castle->addItem(new Potion(Potion::PotionType::health_potion));
        old_castle->addItem(new Potion(Potion::PotionType::health_potion));

    Room *grassland = new Room("Grassland");
        grassland->setRoomDescription("I think I'm almost home!!!");

    Room *home = new Room("Home");
        home->setRoomDescription("Finally I'm home!\nIt's about time... I really needed to use the toilet!");

    // (N, E, S, W)
    cave->setExits(woods, field, NULL, NULL);
    woods->setExits(NULL, abandoned_house, cave, NULL);
    abandoned_house->setExits(NULL, meadow, field, woods);
    field->setExits(abandoned_house, stables, NULL, cave);
    meadow->setExits(NULL, abandoned_mill, NULL, abandoned_house);
    abandoned_mill->setExits(NULL, NULL, bridge, meadow);
    bridge->setExits(abandoned_mill, NULL, empty_well, NULL);
    empty_well->setExits(bridge, NULL, NULL, abandoned_town);
    stables->setExits(NULL, NULL, abandoned_town, field);
    abandoned_town->setExits(stables, empty_well, destroyed_fort, NULL);
    destroyed_fort->setExits(abandoned_town, NULL, ridge, NULL);
    ridge->setExits(destroyed_fort, mountains, NULL, NULL);
    mountains->setExits(NULL, NULL, valley, ridge);
    valley->setExits(mountains, NULL, NULL, shoreline);
    shoreline->setExits(NULL, valley, NULL, river);
    river->setExits(island, shoreline, NULL, NULL);
    island->setExits(beach, NULL, river, NULL);
    beach->setExits(rocky_hills, NULL, island, old_castle);
    rocky_hills->setExits(NULL, NULL, beach, graveyard);
    graveyard->setExits(NULL, rocky_hills, old_castle, NULL);
    old_castle->setExits(graveyard, beach, grassland, NULL);
    grassland->setExits(old_castle, NULL, home, NULL);
    home->setExits(grassland, NULL, NULL, NULL);

    currentRoom = cave;

    rooms.push_back(cave);
    rooms.push_back(woods);
    rooms.push_back(abandoned_house);
    rooms.push_back(field);
    rooms.push_back(meadow);
    rooms.push_back(abandoned_mill);
    rooms.push_back(bridge);
    rooms.push_back(empty_well);
    rooms.push_back(stables);
    rooms.push_back(abandoned_town);
    rooms.push_back(destroyed_fort);
    rooms.push_back(ridge);
    rooms.push_back(mountains);
    rooms.push_back(valley);
    rooms.push_back(shoreline);
    rooms.push_back(river);
    rooms.push_back(island);
    rooms.push_back(beach);
    rooms.push_back(rocky_hills);
    rooms.push_back(graveyard);
    rooms.push_back(old_castle);
    rooms.push_back(grassland);
    rooms.push_back(home);

    //RandomizeEnemy();
}

/**
 *  Main play routine.  Loops until end of play.
 */
void ZorkUL::play()
{
    /*
	printWelcome();

	// Enter the main command loop.  Here we repeatedly read commands and
	// execute them until the ZorkUL game is over.

	bool finished = false;
	while (!finished) {
		// Create pointer to command and give it a command.
		Command* command = parser.getCommand();
		// Pass dereferenced command and check for end of game.
		finished = processCommand(*command);
		// Free the memory allocated by "parser.getCommand()"
		//   with ("return new Command(...)")
		delete command;
	}
	cout << endl;
	cout << "end" << endl;
    */
}


string ZorkUL::printWelcome()
{
    return "You have woken in a cave, find your way home...\n";
}

/**
 * Given a command, process (that is: execute) the command.
 * If this command ends the ZorkUL game, true is returned, otherwise false is
 * returned.
 */

/** COMMANDS **/
void ZorkUL::printHelp()
{
    /*
	cout << "valid inputs are; " << endl;
	parser.showCommands();
    */

}

void ZorkUL::printMap()
{
    cout << "[h] --- [f] --- [g]" << endl;
    cout << "         |         " << endl;
    cout << "         |         " << endl;
    cout << "[c] --- [a] --- [b]" << endl;
    cout << "         |         " << endl;
    cout << "         |         " << endl;
    cout << "[i] --- [d] --- [e]" << endl;
    cout << " |                 " << endl;
    cout << " |                 " << endl;
    cout << "[j]                " << endl;
}

void ZorkUL::teleport()
{
    srand(time(0));
    // -1 so that we don't teleport into Home room
    int index = rand() % (rooms.size() - 1);
    Room* nextRoom = rooms[index];
    while(nextRoom->shortDescription().compare(currentRoom->shortDescription()) == 0)
    {
        index = rand() % (rooms.size() - 1);
        nextRoom = rooms[index];
    }
    currentRoom = nextRoom;
}

Room* ZorkUL::getCurrentRoom()
{
    return currentRoom;
}

string ZorkUL::getCurrentRoomName()
{
    return currentRoom->shortDescription();
}

string ZorkUL::getCurrentRoomDescription()
{
    // TODO return story text, not just description of room.
    // i.e. dont need to know current room or know the exits
    return currentRoom->longDescription();
}

string ZorkUL::go(string direction)
{
	//Make the direction lowercase
	//transform(direction.begin(), direction.end(), direction.begin(),:: tolower);
	//Move to the next room
	Room* nextRoom = currentRoom->nextRoom(direction);
	if (nextRoom == NULL)
    {
        // No room in that direction
        return "ERROR";
    }
	else
	{
		currentRoom = nextRoom;
		return currentRoom->longDescription();
	}
    return "";
}

bool ZorkUL::isInCombat()
{
    return isInCombat();
}

void ZorkUL::setInCombat(bool inCombat)
{
    this->inCombat = inCombat;
}

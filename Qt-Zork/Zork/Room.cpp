#include "Room.h"
#include "Command.h"

/**
 * @author Benjamin
 */

Room::Room(string name)
{
    this->name = name;
    this->description = "";
    enemyInRoom = false;
}

Room::~Room()
{
    cout << "Destroying room " << shortDescription() << "..." << endl;
    /*for(vector<Item*>::iterator itr = itemsInRoom.begin(); itr != itemsInRoom.end(); itr++)
    {
        cout << "Deleting item" << endl;
        delete (*itr);
    }
    itemsInRoom.clear();
    */
}

void Room::setRoomDescription(string description)
{
    this->description = description;
}

void Room::setExits(Room *north, Room *east, Room *south, Room *west)
{
	if (north != NULL)
    {
		exits["north"] = north;
        exits_vector.push_back(true);
    }
    else
        exits_vector.push_back(false);

	if (east != NULL)
    {
		exits["east"] = east;
        exits_vector.push_back(true);
    }
    else
        exits_vector.push_back(false);

	if (south != NULL)
    {
		exits["south"] = south;
        exits_vector.push_back(true);
    }
    else
        exits_vector.push_back(false);

	if (west != NULL)
    {
		exits["west"] = west;
        exits_vector.push_back(true);
    }
    else
        exits_vector.push_back(false);

    canTeleport = false;
    if(canTeleport)
        exits_vector.push_back(true);
    else
        exits_vector.push_back(false);
}

string Room::shortDescription()
{
    return name;
}

string Room::longDescription()
{
    if(enemyInRoom)
        return description + "\n" + displayEnemy();
    return description;
}

string Room::exitString()
{
	string returnString = "\nexits =";
    for (map<string, Room*>::iterator i = exits.begin(); i != exits.end(); i++) // Loop through map
        returnString += "  " + i->first;	// access the "first" element of the pair (direction as a string)
	return returnString;
}

Room* Room::nextRoom(string direction)
{
	map<string, Room*>::iterator next = exits.find(direction); //returns an iterator for the "pair"
	if (next == exits.end())
        return NULL;        // if exits.end() was returned, there's no room in that direction.
    return next->second;    // If there is a room, remove the "second" (Room*) part of the "pair" (<string, Room*>) and return it.
}

void Room::addItem(Item *inItem)
{
    itemsInRoom.push_back(inItem);
}

void Room::removeItemFromRoom(int location)
{
    itemsInRoom.erase(itemsInRoom.begin()+location);
    for(unsigned int i = 0; i < itemsInRoom.size(); i++)
        cout << itemsInRoom.at(i)->getShortDescription() << endl;
}

string Room::displayItem()
{
    string tempString = "items in room = ";
    unsigned int sizeItems = (itemsInRoom.size());
    if (itemsInRoom.size() < 1)
    {
        tempString = "no items in room";
    }
    else if (itemsInRoom.size() > 0)
    {
        int x = (0);
        for (unsigned int n = sizeItems; n > 0; n--)
        {
            tempString = tempString + itemsInRoom[x]->getShortDescription() + "  " ;
            x++;
        }
    }
    return tempString;
}

string Room::displayEnemy()
{
    string tempString;
    if(isEnemyInRoom())
    {
        tempString = "An enemy has appeared!";
    }

    return tempString;
}

int Room::numberOfItems()
{
    return itemsInRoom.size();
}

int Room::isItemInRoom(string inString)
{
    unsigned int sizeItems = itemsInRoom.size();
    if (itemsInRoom.size() < 1)
    {
        return false;
    }
    else if (itemsInRoom.size() > 0)
    {
        int x = (0);
        for (unsigned int n = sizeItems; n > 0; n--)
        {
            // compare inString with short description
            int tempFlag = inString.compare( itemsInRoom[x]->getShortDescription());
            if (tempFlag == 0)
            {
                itemsInRoom.erase(itemsInRoom.begin()+x);
                return x;
            }
            x++;
        }
    }
    return -1;
}

vector<bool> Room::getExits()
{
    return exits_vector;
}

vector<Item*>* Room::getItemsInRoom()
{
    return &itemsInRoom;
}

bool Room::isEnemyInRoom()
{
    return enemyInRoom;
}

void Room::addEnemy(Enemy* enemy)
{
    cout << "Adding enemy..." << endl;
    enemyInRoom = true;
    this->enemy = *enemy;
}

void Room::removeEnemy()
{
    enemyInRoom = false;
}

Enemy& Room::getEnemy()
{
    return enemy;
}

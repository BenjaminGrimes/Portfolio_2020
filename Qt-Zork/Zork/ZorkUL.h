#ifndef ZORKUL_H_
#define ZORKUL_H_

#include "Room.h"
#include "item.h"
#include "Player.h"
#include "potion.h"


#include <iostream>
#include <string>
#include <vector>
using namespace std;

/**
 * @author Benjamin
 * Template by Daniel
 */

class ZorkUL {
private:
    vector<Room*> rooms;
    bool inCombat;
    Room *currentRoom;

    void createRooms();
	void printHelp();
    void printMap();
    //void teleport();
    void createItems();
    void displayItems();

public:
    Player player;

	ZorkUL();
    ~ZorkUL();
    string printWelcome();
    void RandomizeEnemy();
    void play();
    void teleport();
    Room* getCurrentRoom();
    string getCurrentRoomName();
    string getCurrentRoomDescription();
    template <typename T> T getRandom(T num){
        qsrand(time(NULL));
        return qrand()%num;
    }

	string go(string direction);

    bool isInCombat();
    void setInCombat(bool inCombat);
};

#endif /*ZORKUL_H_*/

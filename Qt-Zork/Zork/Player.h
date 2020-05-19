#ifndef PLAYER_H
#define PLAYER_H

#include "Character.h"
#include "weapon.h"
#include "item.h"
#include "potion.h"
#include <vector>
#include <QString>
#include <string>
#include <QMessageBox>

/**
 * @author Benjamin
 */

using std::string;

#define MAX_HEALTH 100
#define MIN_HEALTH 0

#define MAX_MAGIC_LEVEL 100
#define MIN_MAGIC_LEVEL 0

class Player : public Character
{
private:
    string name;
    int magic_level;
    int magic_dmg;
    int age;
    string sex;
    vector<Item*> inventory;
    Item *p_weaponPtr;

    void setAttributes();
    void setRace();

public:
    Player(int health);
    ~Player();

    void addItemToInvetory(Item  *item);
    void removeItemFromInventory(int pos);
    void setPlayerInfo(QString name, int age, QString sex);
    vector<Item*>& getInventory();
    string getName();
    int getAge();
    string getSex();
    int getMagicLevel();
    int getMagicDamage();
    int getWeaponDamage();
    void setMagicLevel( int _level);
    void setMagicDamage(int _damage);
    void onDeath();

    Player &operator++(); // Prefix increment
    Player operator++(int); // Postfix increment
    Player &operator--(); // Prefix decrement
    Player operator--(int); // Postfix decrement

    Player operator+=(const int num);
    Player operator-=(const int num);
};

#endif // PLAYER_H

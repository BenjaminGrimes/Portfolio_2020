#ifndef WEAPON_H
#define WEAPON_H

#include "item.h"
#include <string>

/**
 * @author Daniel
 */

class weapon : public Item
{

private:
    int weapon_damage;
    int weapon_durability;
    int weapon_level;
public:
    weapon(string des ,int in_damage, int in_durability );
    int getWeaponDamage();
    int getWeaponLevel();
    int getWeaponDurability();
    void setWeaponDamage( int in_damage);
    void setWeaponLevel();
    void setWeaponDurablilty( int in_durability);
    string getDescription();

    virtual int getDamage();

    weapon operator++();
    weapon operator--();

};
#endif // WEAPON_H

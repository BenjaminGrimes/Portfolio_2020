#ifndef ENEMY_H
#define ENEMY_H

#include "Character.h"
#include <string>

using std::string;

#define MAX_HEALTH 100
#define MIN_HEALTH 0


/**
 * @author Benjamin
 */

class Enemy : public Character
{
private:
    string name;
    int dmg;

public:
    Enemy();
    Enemy(string n);

    void onDeath();

    string getDescription();
    string getName();
    int getDamage();

    Enemy &operator++(); // Prefix increment
    Enemy operator++(int); // Postfix increment
    Enemy &operator--(); // Prefix decrement
    Enemy operator--(int); // Postfix decrement
    Enemy operator+=(const int num);
    Enemy operator-=(const int num);
};

#endif // ENEMY_H

#include "Enemy.h"

/**
 * @author Benjamin
 */

Enemy::Enemy() : Character (MAX_HEALTH)
{}

Enemy::Enemy(string n) : Character (MAX_HEALTH), name(n)
{
    dmg = 5 + rand() % (10-5+1);
}

void Enemy::onDeath()
{
    // TODO remove enemy from room
}

int Enemy::getDamage()
{
    return dmg;
}

Enemy &Enemy::operator++()
{
    health += 10;

    if(health > MAX_HEALTH)
        health = MAX_HEALTH;

    return *this;
}

Enemy Enemy::operator++(int)
{
    health += 10;

    if(health > MAX_HEALTH)
        health = MAX_HEALTH;

    return *this;
}

Enemy &Enemy::operator--()
{
    setHealth(getHealth()-10);
    return *this;
}

Enemy Enemy::operator--(int)
{
    setHealth(getHealth()-10);
    return *this;
}

Enemy Enemy::operator+=(const int num)
{
    health += num;

    if(health > MAX_HEALTH)
        health = MAX_HEALTH;

    return *this;
}

Enemy Enemy::operator-=(const int num)
{
    health -= num;

    if(health < MIN_HEALTH)
        health = MIN_HEALTH;

    return *this;
}

string Enemy::getDescription()
{
    string temp = "Enemy is here! Health = " + std::to_string(health);
    return temp;
}

string Enemy::getName()
{
    return name;
}

#include "race.h"

/**
 * @author Daniel
 */

race::race (){

}

int race::getHealth()
{
    return att_health;
}

int race::getMagicDamage()
{
    return att_magicDamage;
}

int race::getMagicLevel()
{
    return att_magicLevel;
}

/*race::setDamage(int _damage){
    att_damage = _damage;
}*/

void race::setHealth(int _health)
{
    att_health  = _health;
}

void race::setMagicDamage(int _magicDamage)
{
    att_magicDamage = _magicDamage;
}

void race::setMagicLevel(int _magicLevel)
{
    att_magicLevel = _magicLevel;
}

string race::getType()
{
    return type;
}

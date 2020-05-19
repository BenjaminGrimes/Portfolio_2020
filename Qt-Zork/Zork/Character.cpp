/*#include "Character.h"

Characterx::Characterx(string description) {
	this->description = description;
}
void Characterx::addItem(Item &item) {
    itemsInCharacter.push_back(item);
}
void Characterx::addItem(Item *item) {
    itemsInCharacter.push_back(*item);
    delete item;
}
string Characterx::longDescription()
{
  string ret = this->description;
  ret += "\n Item list:\n";
  for (vector<Item>::iterator i = itemsInCharacter.begin(); i != itemsInCharacter.end(); i++)
    ret += "\t"+ (*i).getLongDescription() + "\n";
  return ret;
}
*/

#include "Character.h"

/**
 * @author Benjamin
 */

Character::Character(int h)
    : health( h )
{

}

Character::~Character()
{

}

int Character::getHealth()
{
    return health;
}

void Character::setHealth(int h)
{
    if(h < 0)
        health = 0;
    else if(health > 100)
        health = 100;
    else
        health = h;
}

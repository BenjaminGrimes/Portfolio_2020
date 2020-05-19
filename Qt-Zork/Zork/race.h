#ifndef RACE_H
#define RACE_H
#include <string>
using namespace std;

/**
 * @author Daniel
 */

class race {

private:
    int att_magicDamage;
    int att_magicLevel;
    int att_health;

public:
    race();
    string type;
    void setMagicDamage( int _magicDmg);
    void setMagicLevel(int _magicLvl);
    void setHealth( int _health);
    string getType();
    int getMagicDamage();
    int getMagicLevel();
    int getHealth();


};
#endif // RACE_H

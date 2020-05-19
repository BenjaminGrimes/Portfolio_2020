#ifndef ITEM_H_
#define ITEM_H_

#include <map>
#include <string>
#include <iostream>
using namespace std;

#define DEFAULT_DAMAGE 0

/**
 * @author Benjamin
 */

class Item {
private:

	string longDescription;
    int weightGrams;
    float value;
	bool weaponCheck;

public:
    string description;

    Item (string description);
    Item (string description, int inWeight, float inValue);
    virtual ~Item()=0;

    string getShortDescription();
    string getLongDescription();
    int getWeight();
    void setWeight(int weightGrams);
    float getValue();
    void setValue(float value);
    int getWeaponCheck();
    void setWeaponCheck(int weaponCheck);
    virtual int getDamage();
    virtual int getPotionType();
};

#endif /*ITEM_H_*/

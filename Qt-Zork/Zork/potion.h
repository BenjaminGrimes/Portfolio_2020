#ifndef POTION_H
#define POTION_H

#include "item.h"

#define HEALTH_POTION_AMOUNT 15
#define MAGIC_POTION_AMOUNT 10

/**
 * @author Benjamin
 */

class Potion : public Item
{
public:
    enum PotionType {health_potion, magic_potion, teleportation_potion};

    Potion(PotionType t);

    virtual int getPotionType();

private:
    PotionType type;
};

#endif // POTION_H

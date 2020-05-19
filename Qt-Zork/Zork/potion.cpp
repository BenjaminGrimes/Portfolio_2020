#include "potion.h"

/**
 * @author Benjamin
 */

Potion::Potion(PotionType t) :
    Item(t == health_potion ? "Health Potion" : t == magic_potion ? "Magic Potion" : "Teleportation Potion"), type(t)
{

}

int Potion::getPotionType()
{
    return type;
}

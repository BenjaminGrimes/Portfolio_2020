#include "mage.h"

/**
 * @author Daniel
 */

mage::mage() : race(){
    setAtt();
}
void mage::setAtt(){
    type = "Mage";
    setMagicLevel(80);
    setMagicDamage(25);
}

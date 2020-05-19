#include "human.h"

/**
 * @author Daniel
 */

human::human() : race(){
    setAtt();
}
void human::setAtt(){
    type = "Human";
    setMagicLevel(60);
    setMagicDamage(15);
}

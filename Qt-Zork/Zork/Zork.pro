#-------------------------------------------------
#
# Project created by QtCreator 2018-10-05T23:13:56
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Zork
TEMPLATE = app

# The following define makes your compiler emit warnings if you use
# any feature of Qt which has been marked as deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if you use deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

CONFIG += c++11

SOURCES += \
        main.cpp \
        mainwindow.cpp \
    Character.cpp \
    Command.cpp \
    CommandWords.cpp \
    item.cpp \
    Parser.cpp \
    Room.cpp \
    ZorkUL.cpp \
    Enemy.cpp \
    Player.cpp \
    startmenuwindow.cpp \
    weapon.cpp \
    potion.cpp \
    race.cpp \
    orc.cpp \
    mage.cpp \
    human.cpp

HEADERS += \
        mainwindow.h \
    Character.h \
    Command.h \
    CommandWords.h \
    item.h \
    Parser.h \
    Room.h \
    ZorkUL.h \
    Enemy.h \
    Player.h \
    startmenuwindow.h \
    weapon.h \
    potion.h \
    race.h \
    orc.h \
    mage.h \
    human.h

FORMS += \
        mainwindow.ui \
    startmenuwindow.ui

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target

DISTFILES += \
    orc

RESOURCES += \
    resources.qrc

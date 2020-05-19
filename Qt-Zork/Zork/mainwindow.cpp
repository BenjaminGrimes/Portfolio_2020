#include "mainwindow.h"
#include "ui_mainwindow.h"

/**
 * @author Benjamin
 */

int const MainWindow::EXIT_CODE_REBOOT = -123456789;

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setWindowIcon(QIcon(":/ZORK_APP_ICON.png"));
}

MainWindow::~MainWindow()
{
    delete story_box;
    delete player_box;
    delete nav_box;
    delete inventory_box;
    delete map_box;
    delete ui;
}

void MainWindow::setUpLayout()
{
    grid = new QGridLayout();
    createStoryGroup();
    createMapGroup();
    createPlayerInfoGroup();
    createInventoryGroup();
    createNavigationGroup();

    grid->addWidget(story_box, 0, 0, 2, 2);
    grid->addWidget(map_box, 0, 2, 2, 1);
    grid->addWidget(player_box, 3, 0);
    grid->addWidget(inventory_box, 3, 1);
    grid->addWidget(nav_box, 3,2);

    // need to set central widget to display layout
    auto central = new QWidget(this);
    central->setLayout(grid);
    setCentralWidget(central);

    createActions();

    updateRoomLabel();
    updateStoryText();
    updateNavButtons();
    updateRoomItems();
    updateCombatField();
}

void MainWindow::createActions()
{
    gameMenu = menuBar()->addMenu("Game");
    QToolBar *gameToolBar = addToolBar("Game");
    // Temp image
    const QIcon restartIcon = QIcon::fromTheme("document-restart", QIcon(":/restart.png"));
    restartAct = new QAction(restartIcon, tr("Restart"), this);
    restartAct->setShortcut((QKeySequence::New));
    restartAct->setStatusTip("Restart the game");
    //connect(restartAct, &QAction::triggered, this, &MainWindow::restart);
    gameMenu->addAction(restartAct);
    gameToolBar->addAction(restartAct);

    // Temp image
    const QIcon exitIcon = QIcon::fromTheme("document-exit", QIcon(":/exit.png"));
    exitAct = new QAction(exitIcon, tr("Exit"), this);
    exitAct->setShortcut(QKeySequence::Quit);
    exitAct->setStatusTip(tr("Exit the game"));
    connect(exitAct, &QAction::triggered, this, &QWidget::close);
    gameMenu->addAction(exitAct);
    gameToolBar->addAction(exitAct);
}

void MainWindow::createStatusBar()
{
    statusBar()->showMessage(tr("Ready"));
}

void MainWindow::createMapGroup()
{
    map_box = new QGroupBox("MAP");
    QVBoxLayout *vbox = new QVBoxLayout();

    QLabel *imageLabel = new QLabel(map_box);
    imageLabel->setBackgroundRole(QPalette::Base);
    imageLabel->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    imageLabel->setScaledContents(true);
    QImage image(":/ZORK_MAP.png");
    imageLabel->setPixmap(QPixmap::fromImage(image));

    vbox->addWidget(imageLabel);

    map_box->setLayout(vbox);
}

void MainWindow::createStoryGroup()
{
    story_box = new QGroupBox("Story");

    story_text_browser = new QTextBrowser(this);
    story_text_browser->append(QString::fromStdString(zUL.printWelcome()));

    combat_container = new QVBoxLayout();

    QLabel *enemy_health_label = new QLabel("Enemy Health:", this);

    enemy_name_label = new QLabel("Enemy Name:", this);
    enemy_health_bar = new QProgressBar(this);
    enemy_health_bar->setStyleSheet("QProgressBar::chunk "
                                    "{ background-color: rgb(255, 100, 0); }"
                                    "QProgressBar { text-align: center; }");
    enemy_health_bar->setRange(0, 100);
    enemy_health_bar->setValue(50);

    enemy_status_bar = new QStatusBar(this);
    enemy_status_bar->addPermanentWidget(enemy_health_label, 1);
    enemy_status_bar->addPermanentWidget(enemy_health_bar, 4);

    use_sword_radio = new QRadioButton("Attack using weapon", this);
    use_magic_radio = new QRadioButton("Attack using magic", this);
    use_sword_radio->setChecked(true);

    combat_container->addWidget(enemy_name_label);
    combat_container->addWidget(enemy_status_bar);
    combat_container->addWidget(use_sword_radio);
    combat_container->addWidget(use_magic_radio);

    // Hide combat widgets when starting game.
    enemy_name_label->setVisible(false);
    enemy_status_bar->setVisible(false);
    use_sword_radio->setVisible(false);
    use_magic_radio->setVisible(false);

    attack_btn = new QPushButton("Attack", this);
    connect(attack_btn, SIGNAL(released()), this, SLOT(attack_btn_onclick()));

    room_items_container = new QVBoxLayout();

    take_item_btn = new QPushButton("Take item(s)", this);
    connect(take_item_btn, SIGNAL(released()), this, SLOT(take_item_btn_onclick()));

    QGridLayout *story_grid = new QGridLayout();
    story_grid->addWidget(story_text_browser, 0, 0, 2, 3);
    story_grid->addLayout(combat_container, 2, 0, 2, 2);
    story_grid->addWidget(attack_btn, 4, 0, 1, 2);
    story_grid->addLayout(room_items_container, 2, 2, 2, 1);
    story_grid->addWidget(take_item_btn, 4, 2, 1, 1);

    story_box->setLayout(story_grid);
}

void MainWindow::createPlayerInfoGroup()
{
    player_box = new QGroupBox("Player Info");
    QLabel *label = new QLabel("Health:", player_box);

    player_health_bar = new QProgressBar(player_box);
    player_health_bar->setStyleSheet("QProgressBar { text-align: center; }");
    player_health_bar->setRange(MIN_HEALTH, MAX_HEALTH);
    player_health_bar->setValue(zUL.player.getHealth());

    health_status_bar = new QStatusBar(player_box);
    health_status_bar->addPermanentWidget(label, 1);
    health_status_bar->addPermanentWidget(player_health_bar, 4);

    player_magic_bar = new QProgressBar(player_box);
    player_magic_bar->setStyleSheet("QProgressBar::chunk "
                                    "{ background-color: rgb(0, 100, 255); }"
                                    "QProgressBar { text-align: center; }");
    player_magic_bar->setRange(MIN_MAGIC_LEVEL, MAX_MAGIC_LEVEL);
    player_magic_bar->setValue(zUL.player.getMagicLevel());

    magic_status_bar = new QStatusBar(player_box);
    magic_status_bar->addPermanentWidget(new QLabel("Magic Level:", player_box), 1);
    magic_status_bar->addPermanentWidget(player_magic_bar, 4);

    QLabel *name_title_label = new QLabel("Name:", player_box);
    QLabel *name_label = new QLabel(QString::fromStdString(zUL.player.getName()), player_box);

    QLabel *age_title_label = new QLabel("Age:", player_box);
    QLabel *age_label = new QLabel(QString::number(zUL.player.getAge()), player_box);

    QLabel *sex_title_label = new QLabel("Sex:", player_box);
    QLabel *sex_label = new QLabel(QString::fromStdString(zUL.player.getSex()), player_box);

    QGridLayout *p_info_grid = new QGridLayout();
    p_info_grid->addWidget(name_title_label, 0, 0, 1, 1);
    p_info_grid->addWidget(name_label, 0, 1, 1, 1);
    p_info_grid->addWidget(health_status_bar, 1, 0, 1, 2);
    p_info_grid->addWidget(age_title_label, 2, 0, 1 ,1);
    p_info_grid->addWidget(age_label, 2, 1, 1, 1);
    p_info_grid->addWidget(sex_title_label, 3, 0, 1, 1);
    p_info_grid->addWidget(sex_label, 3, 1, 1, 1);
    p_info_grid->addWidget(magic_status_bar, 4, 0, 1, 2);

    player_box->setLayout(p_info_grid);
}

void MainWindow::createInventoryGroup()
{
    inventory_box = new QGroupBox("Inventory");

    listWidget = new QListWidget(inventory_box);
    listWidget->setSelectionMode(QAbstractItemView::NoSelection);

    use_item_btn = new QPushButton("Use item", inventory_box);
    use_item_btn->connect(use_item_btn, SIGNAL(released()), this, SLOT(use_item_btn_onclick()));

    QGridLayout *inv_grid = new QGridLayout(inventory_box);

    inv_grid->addWidget(listWidget, 0, 0, 2, 2);
    inv_grid->addWidget(use_item_btn, 2, 0, 2, 2);

    inventory_box->setLayout(inv_grid);
}

void MainWindow::createNavigationGroup()
{
    nav_box = new QGroupBox("Navigation");

    current_room_label = new QLabel(nav_box);

    teleport_btn = new QPushButton("Teleport", nav_box);
    teleport_btn->connect(teleport_btn, SIGNAL(released()), this, SLOT(teleport_btn_onclick()));
    teleport_btn->setEnabled(false);

    north_btn = new QPushButton("North", nav_box);
    north_btn->connect(north_btn, SIGNAL(released()), this, SLOT(north_btn_onclick()));

    south_btn = new QPushButton("South", nav_box);
    south_btn->connect(south_btn, SIGNAL(released()), this, SLOT(south_btn_onclick()));

    east_btn = new QPushButton("East", nav_box);
    east_btn->connect(east_btn, SIGNAL(released()), this, SLOT(east_btn_onclick()));

    west_btn = new QPushButton("West", nav_box);
    west_btn->connect(west_btn, SIGNAL(released()), this, SLOT(west_btn_onclick()));

    QGridLayout *nav_grid = new QGridLayout(nav_box);
    nav_grid->addWidget(current_room_label, 0, 0);
    nav_grid->addWidget(north_btn, 0, 1);
    nav_grid->addWidget(west_btn, 1, 0);
    nav_grid->addWidget(teleport_btn, 1, 1);
    nav_grid->addWidget(east_btn, 1, 2);
    nav_grid->addWidget(south_btn, 2, 1);
    nav_box->setLayout(nav_grid);
}

void MainWindow::updateNavButtons()
{
    //teleport_btn->setEnabled(false);

    vector<bool>exits = zUL.getCurrentRoom()->getExits();
    cout << "N:" << exits[0] << " E:" << exits[1] << " S:" << exits[2] << " W:" << exits[3] << endl;
    if(!exits[0])
    {
        north_btn->setEnabled(false);
        north_btn->setToolTip("No north exit");
    }
    else
    {
        north_btn->setEnabled(true);
        north_btn->setToolTip("");
    }

    if(!exits[1])
    {
        east_btn->setEnabled(false);
        east_btn->setToolTip("No east exit");
    }
    else
    {
        east_btn->setEnabled(true);
        east_btn->setToolTip("");
    }

    if(!exits[2])
    {
        south_btn->setEnabled(false);
        south_btn->setToolTip("No south exit");
    }
    else
    {
        south_btn->setEnabled(true);
        south_btn->setToolTip("");
    }
    if(!exits[3])
    {
        west_btn->setEnabled(false);
        west_btn->setToolTip("No west exit");
    }
    else
    {
        west_btn->setEnabled(true);
        west_btn->setToolTip("");
    }

    if(!exits[4])
    {
        //teleport_btn->setEnabled(false);
        //teleport_btn->setToolTip("Teleport unavailable");
    }
    else
    {
        //teleport_btn->setEnabled(true);
        //teleport_btn->setToolTip("");
    }
}

void MainWindow::updateRoomLabel()
{
    string temp = zUL.getCurrentRoomName();
    QString c_room = QString::fromStdString(temp);
    current_room_label->setText("Current Room:\n " + c_room);
}

void MainWindow::updateStoryText()
{
    story_text_browser->append("===============================");
    story_text_browser->append(QString::fromStdString(zUL.getCurrentRoomDescription()));
}

void MainWindow::updateRoomItems()
{
    // Clear and delete checkboxes
    while (auto item = room_items_container->takeAt(0)) {
          delete item->widget();
    }
    room_items_checkboxes.clear();

    // Get items in current room
    current_room = zUL.getCurrentRoom();
    itemsInRoom = current_room->getItemsInRoom();

    if(current_room->getItemsInRoom()->size() == 0)
    {
        take_item_btn->setEnabled(false);
        take_item_btn->setToolTip("No items to take");
    }
    else
    {
        take_item_btn->setEnabled(true);
        take_item_btn->setToolTip("");

        unsigned int i = 0;
        for(Item *item : *itemsInRoom)
        {
            cout << item->getShortDescription() << endl;
            QCheckBox *temp_cbox = new QCheckBox;
            room_items_checkboxes.push_back(temp_cbox);
            temp_cbox->setText(QString::fromStdString(item->getShortDescription()));
            room_items_container->addWidget(room_items_checkboxes.at(i));
            ++i;
        }
    }
}

void MainWindow::updateInventory()
{
    vector<Item*> p_inv = zUL.player.getInventory();

    cout<< "Player inventory: ";
    for(unsigned int i = 0; i < p_inv.size(); i++)
        cout << p_inv.at(i)->getShortDescription() << " ";
    cout << endl;

    QListWidgetItem *it;

    listWidget->clear();

    for(unsigned int i = 0; i < p_inv.size(); i++)
    {
        it = new QListWidgetItem(listWidget);
        listWidget->setItemWidget(it, new QRadioButton(QString::fromStdString(p_inv.at(i)->getShortDescription())));
    }
}

void MainWindow::updatePlayerInfo()
{
    //cout << "Updating player info..." << endl;
    //cout << "Player health: " << zUL.player.getHealth() << endl;
    player_health_bar->setValue(zUL.player.getHealth());
    player_magic_bar->setValue(zUL.player.getMagicLevel());

    if(zUL.player.getHealth() <= 0)
    {
        zUL.player.onDeath();
        QMessageBox mBox;
        mBox.setText("Game Over!");
        QAbstractButton *restartBtn = mBox.addButton("Restart", QMessageBox::YesRole);
        QAbstractButton *exitBtn = mBox.addButton("Exit", QMessageBox::NoRole);
        mBox.exec();

        if(mBox.clickedButton() == restartBtn)
        {
            cout << "Restarting..." << endl;
            restartAct->trigger();
        }
        else if(mBox.clickedButton() == exitBtn)
        {
            cout << "Exiting..." << endl;
            qApp->exit(0);
        }
    }
}

void MainWindow::updateCombatField()
{
    if(zUL.getCurrentRoom()->isEnemyInRoom())
    {
        cout << "Enemy in room..." << endl;

        if(attack_btn->isEnabled() == false)
        {
            attack_btn->setEnabled(true);
            attack_btn->setToolTip("");
        }

        // Enable Enemy info widgets
        enemy_name_label->setVisible(true);
        enemy_status_bar->setVisible(true);
        use_sword_radio->setVisible(true);
        use_magic_radio->setVisible(true);

        // Set widgets
        enemy_name_label->setText("Name: " + QString::fromStdString(zUL.getCurrentRoom()->getEnemy().getName()));
        enemy_health_bar->setValue(zUL.getCurrentRoom()->getEnemy().getHealth());
        use_sword_radio->setChecked(true);
    }
    else if(attack_btn->isEnabled())
    {
        attack_btn->setEnabled(false);
        attack_btn->setChecked(false);
    }

}

void MainWindow::startCombat()
{
    // Start combat
    zUL.setInCombat(true);
    story_text_browser->append("-----------Entering Combat----------");

    attack_btn->setChecked(false);

    // disable nav buttons
    north_btn->setEnabled(false);
    north_btn->setToolTip("Cannot go to room, you are in combat");
    north_btn->setChecked(false);

    east_btn->setEnabled(false);
    east_btn->setToolTip("Cannot go to room, you are in combat");
    east_btn->setChecked(false);

    south_btn->setEnabled(false);
    south_btn->setToolTip("Cannot go to room, you are in combat");
    south_btn->setChecked(false);

    west_btn->setEnabled(false);
    west_btn->setToolTip("Cannot go to room, you are in combat");
    west_btn->setChecked(false);
}

void MainWindow::endCombat()
{
    zUL.setInCombat(false);
    story_text_browser->append("-----------Combat has finished----------");

    if(attack_btn->isEnabled())
    {
        attack_btn->setEnabled(false);
        attack_btn->setToolTip("No enemy in current room");
    }

    enemy_name_label->setVisible(false);
    enemy_status_bar->setVisible(false);
    use_sword_radio->setVisible(false);
    use_magic_radio->setVisible(false);


    // enable nav buttons
    north_btn->setEnabled(true);
    north_btn->setToolTip("");

    east_btn->setEnabled(true);
    east_btn->setToolTip("");

    south_btn->setEnabled(true);
    south_btn->setToolTip("");

    west_btn->setEnabled(true);
    west_btn->setToolTip("");

    updateNavButtons();
}

void MainWindow::teleport_btn_onclick()
{
    if(zUL.getCurrentRoom()->isEnemyInRoom() == false)
    {
        zUL.teleport();
        teleport_btn->setEnabled(false);
        teleport_btn->setToolTip("Teleport unavailable");
        string message = "You have teleported to " + zUL.getCurrentRoomName();
        story_text_browser->append(QString::fromStdString(message));

        updateRoomLabel();
        updateStoryText();
        updateNavButtons();
        updateRoomItems();
        if(zUL.getCurrentRoom()->isEnemyInRoom())
        {
            startCombat();
            updateCombatField();
        }
    }
    else
    {
        QMessageBox mBox;
        mBox.setText("You cannot teleport\n during combat.");
        mBox.exec();
    }
}

void MainWindow::north_btn_onclick()
{
    QString direction = north_btn->text();
    goDirection(direction);
    north_btn->setChecked(false);
}

void MainWindow::south_btn_onclick()
{
    QString direction = south_btn->text();
    goDirection(direction);
    south_btn->setChecked(false);
}

void MainWindow::east_btn_onclick()
{
    QString direction = east_btn->text();
    goDirection(direction);
    east_btn->setChecked(false);
}

void MainWindow::west_btn_onclick()
{
    QString direction = west_btn->text();
    goDirection(direction);
    west_btn->setChecked(false);
}

void MainWindow::use_item_btn_onclick()
{
    // https://forum.qt.io/topic/91040/how-do-i-check-the-state-of-a-qlistwidget-s-item-s-radiobuttons/3
    for(int i = 0; i < listWidget->count(); i++)
    {
        auto temp_radio_btn = static_cast<QRadioButton*>(listWidget->itemWidget(listWidget->item(i)));
        if(temp_radio_btn->isChecked())
        {
            cout << temp_radio_btn->text().toStdString() << " RB is checked" << endl;

            // Remove item from player inventory
            vector<Item*> inv = zUL.player.getInventory();

            int type = inv[i]->getPotionType();

            switch(type)
            {
                case Potion::PotionType::health_potion:
                    cout << "Increasing health..." << endl;
                    story_text_browser->append("You've used a health potion!");
                    zUL.player += HEALTH_POTION_AMOUNT;
                    zUL.player.removeItemFromInventory(i);
                    // Remove item from the list
                    delete listWidget->item(i);
                    break;
                case Potion::PotionType::magic_potion:
                    cout << "Increasing Magic level..." << endl;
                    story_text_browser->append("You've used a magic potion!");
                    zUL.player++;
                    zUL.player.removeItemFromInventory(i);
                    // Remove item from the list
                    delete listWidget->item(i);
                break;
                case Potion::PotionType::teleportation_potion:
                if(zUL.getCurrentRoom()->isEnemyInRoom() == false)
                {
                    cout << "Teleport now available..." << endl;
                    story_text_browser->append("You've used a teleportation potion.\nTeleportation is now available!");
                    teleport_btn->setEnabled(true);
                    teleport_btn->setToolTip("");
                    zUL.player.removeItemFromInventory(i);
                    // Remove item from the list
                    delete listWidget->item(i);
                }
                else
                {
                    QMessageBox mBox;
                    mBox.setText("You cannot use a teleportation potion\nduring combat.");
                    mBox.exec();
                }
                break;
            }
        }
    }
    updatePlayerInfo();
}

void MainWindow::take_item_btn_onclick()
{
    for(int i = 0; i < room_items_checkboxes.size(); )
    {
        // If QCheckBox is checked add it to players inventory
        if(room_items_checkboxes.at(i)->isChecked())
        {
            cout << room_items_checkboxes.at(i)->text().toStdString() << " is checked" << endl;
            zUL.player.addItemToInvetory(itemsInRoom->at(i));
            string storyText = "You've taken " + itemsInRoom->at(i)->getShortDescription();
            story_text_browser->append(QString::fromStdString(storyText));
            // Remove the item from the room
            zUL.getCurrentRoom()->removeItemFromRoom(i);

            // Remove element from vector, this avoids a crash
            room_items_checkboxes.erase(room_items_checkboxes.begin()+i);
        }
        else
            i++;
    }

    // Update layout
    updateRoomItems();
    updateInventory();
    updatePlayerInfo();
}

void MainWindow::attack_btn_onclick()
{
    attack_btn->setDefault(false);
    Enemy &e = zUL.getCurrentRoom()->getEnemy();
    if(use_sword_radio->isChecked())
    {
        e -= zUL.player.getWeaponDamage();
        story_text_browser->append(QString::fromStdString("You attacked " + e.getName() + " with your sword!"));
    }
    else if(use_magic_radio->isChecked())
    {
        // check if magic left
        if(zUL.player.getMagicLevel() > 0)
        {
            e -= zUL.player.getMagicDamage();
            zUL.player--;
            story_text_browser->append(QString::fromStdString("You attacked " + e.getName() + " with magic!"));
        }
    }
    if(e.getHealth() > 0)
    {
        zUL.player -= e.getDamage();
        story_text_browser->append(QString::fromStdString(e.getName() + " hit back!"));
        updatePlayerInfo();
    }
    else
    {
        zUL.getCurrentRoom()->removeEnemy();
        story_text_browser->append(QString::fromStdString(e.getName() + " has been defeated!"));
        endCombat();
    }
    updateCombatField();
}

void MainWindow::goDirection(QString direction)
{
    direction = direction.toLower();
    string response = zUL.go(direction.toStdString());

    updateRoomLabel();
    updateStoryText();
    updateNavButtons();
    updateRoomItems();
    if(zUL.getCurrentRoom()->isEnemyInRoom())
    {
        startCombat();
        updateCombatField();
    }
    else if(zUL.getCurrentRoom()->shortDescription().compare("Home") == 0)
    {
        cout << "You are home!";
        QMessageBox mBox;
        mBox.setText("Well Done!\nYou made it home!");
        QAbstractButton *playAgainBtn = mBox.addButton("Play Again", QMessageBox::YesRole);
        QAbstractButton *exitBtn = mBox.addButton("Exit", QMessageBox::NoRole);
        mBox.exec();

        if(mBox.clickedButton() == playAgainBtn)
        {
            cout << "Restarting..." << endl;
            restartAct->trigger();
        }
        else if(mBox.clickedButton() == exitBtn)
        {
            cout << "Exiting..." << endl;
            qApp->exit(0);
        }
    }
}

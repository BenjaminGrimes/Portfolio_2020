#include "mainwindow.h"
#include "startmenuwindow.h"
#include <QApplication>

/**
 * @author Benjamin
 */

int main(int argc, char *argv[])
{
    int currentExitCode = 0;

    do{
    QApplication a(argc, argv);

    // Show start menu window
    StartMenuWindow sw;
    sw.setWindowTitle("Zork - Start Menu");
    sw.show();
    currentExitCode = a.exec();
    } while(currentExitCode == MainWindow::EXIT_CODE_REBOOT);

    return currentExitCode;
}

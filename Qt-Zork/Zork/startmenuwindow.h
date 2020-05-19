#ifndef STARTMENUWINDOW_H
#define STARTMENUWINDOW_H

#include <QMainWindow>
#include <QPushButton>
#include <QVBoxLayout>
#include <QLabel>
#include <QLineEdit>
#include <QSlider>
#include <QComboBox>
#include <QFormLayout>
#include <QSpinBox>

#include "mainwindow.h"

/**
 * @author Benjamin
 */

namespace Ui {
class StartMenuWindow;
}

class StartMenuWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit StartMenuWindow(QWidget *parent = nullptr);
    ~StartMenuWindow();

private slots:
    void start_btn_onclick();
    void exit_btn_onclick();

private:
    Ui::StartMenuWindow *ui;

    MainWindow w;

    QGridLayout *grid;
    QFormLayout *formLayout;

    QLabel *name_label;
    QLabel *age_label;
    QSpinBox *age_value;
    QLabel *sex_label;
    QLineEdit *name_lineEdit;
    QComboBox *sex_comboBox;
    QSlider *age_slider;

    QLabel *error_label;

    QPushButton *start_btn;
    QPushButton *exit_btn;

    void setUpLayout();
    void clearAndReset();
};

#endif // STARTMENUWINDOW_H

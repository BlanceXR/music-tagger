#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <qfiledialog.h>
#include <qdesktopservices.h>
#include <qmenu.h>
#include <qmessagebox.h>
#include <qtableview.h>
#include "ui_mainwindow.h"
#include "QStandardItemModel"
namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT
    
public:
    explicit MainWindow(QWidget *parent = 0);
    void setupMenus();
    void setupActions();
    ~MainWindow();

public slots:
    void addFiles();
    void about();

    
private:
    QStandardItemModel *model;
    Ui::MainWindow *ui;
    QAction *addFilesAction;
    QAction *exitAction;
    QAction *aboutAction;
    QAction *aboutQtAction;
};

#endif // MAINWINDOW_H

#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "QStandardItemModel"

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent),ui(new Ui::MainWindow)
{
    ui->setupUi(this);


    model = new QStandardItemModel(1,3,this); //2 Rows and 3 Columns
    QStringList headers;
    headers << tr("Title") << tr("Artist") << tr("Album") << tr("Year")<< tr("track No.");
    model->setHorizontalHeaderLabels(headers);
    ui->tableView->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableView->setSelectionBehavior(QAbstractItemView::SelectRows);

    //use following commented out code to add action
//    connect(musicTable, SIGNAL(cellPressed(int,int)),
//            this, SLOT(tableClicked(int,int)));
    ui->tableView->setModel(model);
    setupActions();
    setupMenus  ();
}
void MainWindow::addFiles()
{
    QStringList files = QFileDialog::getOpenFileNames(this, tr("Select Music Files"),
        QDesktopServices::storageLocation(QDesktopServices::MusicLocation));

    if (files.isEmpty())
        return;

    //请在此处补全信息：Todo: fill out the tag info bellow
    int i = 0;
    foreach (QString string, files) {
        //each string represent a file location
        QStandardItem *firstRow = new QStandardItem(string);
        //usage : setItem(rowNum, collumNum, Item)
        model->setItem(i,0,firstRow);
        i++;
    }

}
void MainWindow::setupMenus()
{
    QMenu *fileMenu = menuBar()->addMenu(tr("&File"));
    fileMenu->addAction(addFilesAction);
    fileMenu->addSeparator();
    fileMenu->addAction(exitAction);

    QMenu *aboutMenu = menuBar()->addMenu(tr("&Help"));
    aboutMenu->addAction(aboutAction);
    aboutMenu->addAction(aboutQtAction);
}
void MainWindow::setupActions()
{

    addFilesAction = new QAction(tr("Add &Files"), this);
    addFilesAction->setShortcut(tr("Ctrl+F"));
    exitAction = new QAction(tr("E&xit"), this);
    exitAction->setShortcuts(QKeySequence::Quit);
    aboutAction = new QAction(tr("A&bout"), this);
    aboutAction->setShortcut(tr("Ctrl+B"));
    aboutQtAction = new QAction(tr("About &Qt"), this);
    aboutQtAction->setShortcut(tr("Ctrl+Q"));
    connect(addFilesAction, SIGNAL(triggered()), this, SLOT(addFiles()));
    connect(exitAction, SIGNAL(triggered()), this, SLOT(close()));
    connect(aboutAction, SIGNAL(triggered()), this, SLOT(about()));
    connect(aboutQtAction, SIGNAL(triggered()), qApp, SLOT(aboutQt()));
}

void MainWindow::about()
{
    QMessageBox::information(this, tr("About Music Player"),
        tr("The Music tagger will automatically fix your tags"));
}

MainWindow::~MainWindow()
{
    delete ui;
}

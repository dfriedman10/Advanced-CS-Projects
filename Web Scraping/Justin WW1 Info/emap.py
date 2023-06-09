import sys
import os
from PyQt5.QtWidgets import QApplication, QWidget, QLabel, QVBoxLayout, QPushButton, QMainWindow, QSlider, QDial, QGridLayout, QScrollArea
from PyQt5.QtGui import QPixmap,QImage, QPalette, QBrush, QFont
from PyQt5.QtCore import Qt, QSize, QUrl

from PyQt5.QtWebEngineWidgets import QWebEngineView
from PyQt5.QtMultimedia import QMediaPlayer, QMediaContent, QMediaPlaylist

countries = {
    "German Empire": (440, 250, "royalblue"),
    "French Third Republic": (245, 360, "darkred"),
    "United Kingdom": (200, 150, "darkred"),
    "Russian Empire": (800, 200, "darkred"),
    "Austria-Hungary": (550, 390, "royalblue"),
    "Kingdom of Italy": (420, 490, "darkred"),
    "Kingdom of Serbia": (600, 490, "darkred"),
    "Tsardom of Bulgaria": (680, 520, "royalblue"),
    "Kingdom of Romania": (700, 460, "darkred"),
    "Kingdom of Belgium": (295, 260, "darkred"),
    "Ottoman Empire": (850, 600, "royalblue"),
}

class WikiWindow(QWidget):
    def __init__(self, country):
        super().__init__()

        url = f"https://en.wikipedia.org/wiki/{country.replace(' ', '_')}"

        layout = QVBoxLayout()

        self.web_view = QWebEngineView()
        self.web_view.load(QUrl(url))
        layout.addWidget(self.web_view)

        self.setLayout(layout)
        self.start_anthem(country)
        
        self.setGeometry(500, 500, 1024, 768)
        self.setWindowTitle(f"{country} - Wikipedia")
        self.show()


    def start_anthem(self, country):
        print(f"{country.replace(' ', '_')}.mp3")
        self.player = QMediaPlayer()
        anthem_path = "/Users/prototypers4/Desktop/DSA/" + f"{country.replace(' ', '_')}.mp3"
        self.player.setMedia(QMediaContent(QUrl.fromLocalFile(anthem_path)))
        #self.player.setMedia(QMediaContent(QUrl(f"//Users/prototypers4/Desktop/DSA/{country.replace(' ', '_')}.mp3")))
        self.player.setVolume(80)
        self.player.play()

    def stop_anthem(self):
        self.player.stop()
        self.player = None

    def closeEvent(self, event):
        print('close event')
        self.stop_anthem()


class EventiWindow(QWidget):
    def __init__(self, year):
        super().__init__()

        # label
        self.label = QLabel(self)
        oFont = QFont("Arial", 12)
        oFont.setBold(True)
        self.label.setFont(oFont)
        self.label.setText(f"{year} - Main Events")

        # read file
        self.text = QLabel(self)
        self.text.setWordWrap(True)
        with open(f"{year}.txt", "r", encoding='UTF8') as f:
            self.text.setText(f.read())

        # read map
        oimg = QPixmap(f"{year}.png")
        self.img = oimg.scaled(QSize(800, 600))
        self.lbl_img = QLabel()
        self.lbl_img.setPixmap(self.img)

        # set scroll area
        self.scroll = QScrollArea()
        self.widget = QWidget()

        # add widgets to layout
        self.grid = QVBoxLayout()        
        self.grid.addWidget(self.label)
        self.grid.addWidget(self.lbl_img)
        self.grid.addWidget(self.text)        

        self.widget.setLayout(self.grid)

        self.scroll.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.scroll.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOff)
        self.scroll.setWidgetResizable(True)
        self.scroll.setWidget(self.widget)

        layout = QVBoxLayout()

        layout.addWidget(self.scroll)
        self.setLayout(layout)

        self.setGeometry(1050, 50, 800, 900)
        self.setWindowTitle("Main Events")
        self.show()



    def update(self, year):
        self.label.setText(f"{year} - Main Events")

        with open(f"{year}.txt", "r", encoding='UTF8') as f:
            self.text.setText(f.read())

        oimg = QPixmap(f"{year}.png")
        self.img = oimg.scaled(QSize(800, 600))
        self.lbl_img = QLabel()
        self.lbl_img.setPixmap(self.img)       

class MyApp(QWidget):

    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):

        self.setGeometry(10, 50, 1024, 768)
        oImage = QImage("1914.jpg")
        sImage = oImage.scaled(QSize(1024, 768))
        palette = QPalette()
        palette.setBrush(QPalette.Window, QBrush(sImage))
        self.setPalette(palette)

        self.new_window = None
        
        # legend
        self.label1 = QLabel(self)
        self.label1.setText("Central Powers")
        self.label1.setGeometry(10, 10, 103, 18)
        self.label1.setStyleSheet("border :2px solid ;"
                             "border-top-color : navy;"
                             "border-left-color :navy;"
                             "border-right-color :navy;"
                             "border-bottom-color : navy;"
                             "background-color : royalblue"
                             )

        self.label2 = QLabel(self)
        self.label2.setText("Entente Powers")
        self.label2.setGeometry(10, 30, 103, 18)
        self.label2.setStyleSheet("border :2px solid ;"
                             "border-top-color : darkred;"
                             "border-left-color :darkred;"
                             "border-right-color :darkred;"
                             "border-bottom-color : darkred;"
                             "background-color : firebrick"
                             )


        # slider
        self.slider_txt = QLabel(self)
        self.slider_txt.setText("1911")
        self.slider_txt.setGeometry(20, 90, 100, 18)
        self.slider = QSlider(Qt.Vertical, self)
        self.slider.move(20, 120)
        self.slider.setRange(0, 7)
        self.slider.setFocusPolicy(Qt.StrongFocus)
        self.slider.setTickPosition(QSlider.TicksBothSides)
        self.slider.setTickInterval(1)
        self.slider.setSingleStep(1)

        self.slider.valueChanged.connect(self.year_changed)

        # buttons for countries
        for country, (x, y, color) in countries.items():
            button = QPushButton(country, self)

            # creating colorful border
            background_color = "background-color: "
            background_color += "royalblue" if color == "royalblue" else "firebrick"

            button.setStyleSheet("border :2px solid ;"
                             "border-top-color : "+color+";"
                             "border-left-color :"+color+";"
                             "border-right-color :"+color+";"
                             "border-bottom-color : "+color+";"
                             + background_color
                             )
            button.move(x, y)
            button.clicked.connect(self.open_wiki_article(country))

        self.setWindowTitle('Map of Europe in 1914')
        self.show()

        self.open_events(1911)

    # https://stackoverflow.com/questions/6784084/how-to-pass-arguments-to-functions-by-the-click-of-button-in-pyqt    
    def open_wiki_article(self, country):
        def callback():
            self.new_window = WikiWindow(country)  
        
        return callback
    
    def open_events(self, year):
        self.evt_window = EventiWindow(year)   


    def year_changed(self):
        print(self.slider.value())
        self.slider_txt.setText(str(1911 + self.slider.value()))

        self.evt_window.close()
        self.open_events(1911 + self.slider.value())

if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = MyApp()

    sys.exit(app.exec_())
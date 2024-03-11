import pygame
import math

pygame.init()
win = pygame.display.set_mode((600, 600))
pygame.display.set_caption("First Game")

# deklarowanie kolorów
CZERWONY = (255, 0, 0)
ZIELONY = (0, 255, 0)
ZOLTY = (255, 255, 0)
FIOLETOWY = (128, 0, 128)
JASNY_NIEBIESKI = (0, 255, 255)
POMARANCZOWY = (255, 165, 0)
NIEBIESKI = (0, 0, 255)
SZARY = (128, 128, 128)
CZARNY = (0, 0, 0)

currentDrawMode = 0
lastDrawMode = 0
initX, initY, surfWidth, surfHeight = 200, 200, 600, 600
surface = pygame.display.set_mode((surfWidth, surfHeight))
surface.fill((ZOLTY))

r = 150
scaleX = 1
scaleY = 1
phase = 0
mirrorX = False
mirrorY = False
slash = 0

def getCentroid(x, y):
    return sum(x) / len(x), sum(y) / len(y)
def drawFigure():
    surface = pygame.display.set_mode((surfWidth, surfHeight))
    surface.fill((ZOLTY))
    theta = math.pi - 2.24285714286  # 2.242 RAD = 128 & 4 / 7 for 7 - sided figure
    x = []
    y = []
    x0, y0, x1, y1 = initX, initY, initX + r * math.cos(phase) * scaleX, initY + r * math.sin(phase) * scaleY
    for i in range(7):
        x.append(x0)
        y.append(y0)
        x0 = x1
        y0 = y1
        x1 += r * math.cos((i + 1) * theta + phase) * scaleX
        y1 += r * math.sin((i + 1) * theta + phase) * scaleY

    x.append(x0)
    y.append(y0)

    if mirrorX:
        for i in range(len(x)):
            if x[i] > initX:
                x[i] = x[i] - 2 * math.fabs(x[i] - initX)
            else:
                x[i] = x[i] + 2 * math.fabs(x[i] - initX)

    if mirrorY:
        for i in range(len(y)):
            if y[i] > initY:
                y[i] = y[i] - 2 * math.fabs(y[i] - initY)
            else:
                y[i] = y[i] + 2 * math.fabs(y[i] - initY)

    if slash != 0:
        for i in range(len(y)):
            if y[i] < 335:
                x[i] -= slash

    for i in range(7):
        if i == 2:
            pygame.draw.line(surface, CZERWONY, (x[i], y[i]), (x[i + 1], y[i + 1]), 10)
        else:
            pygame.draw.line(surface, CZARNY, (x[i], y[i]), (x[i + 1], y[i + 1]), 10)

    return getCentroid(x, y)





drawFigure()

run = True
while run:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_1:
            currentDrawMode = 1
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_2:
            currentDrawMode = 2
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_3:
            currentDrawMode = 3
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_4:
            currentDrawMode = 4
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_5:
            currentDrawMode = 5
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_6:
            currentDrawMode = 6
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_7:
            currentDrawMode = 7
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_8:
            currentDrawMode = 8
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_9:
            currentDrawMode = 9
        elif event.type == pygame.KEYDOWN and event.key == pygame.K_0:
            currentDrawMode = 0

    # print("Current:Last drawMode: "+drawMode.__str__()+":"+lastDrawMode.__str__())

    if currentDrawMode != lastDrawMode:
        print("Redraw")
        win.fill(ZOLTY)
        phase = 0
        scaleX = 1
        scaleY = 1
        initX = 200
        initY = 200
        mirrorX = False
        mirrorY = False
        r = 150
        slash = 0
        match currentDrawMode.__str__():
            case "1": #symetrycznie przeskaluj całą figurę o 50%
                scaleX = 0.5
                scaleY = 0.5
                initX = initX * scaleX + r
                initY = initY * scaleY + r
            case "0": ##nie rób nic, bazowe parametry są nadawane zawsze
                print("Restore default")
            case "2": #obróć figurę o 15 stopni i zaktualizuj położenie środka ciężkości
                        # tak aby znajdował się tam gdzie początkowo
                xc, yc = drawFigure()
                phase = 15.0 * math.pi / 180.0  # 45 deg to rad
                xc2, yc2 = drawFigure()
                initX += xc - xc2
                initY += yc - yc2
            case "3": #przeskaluj o 50% w osi X oraz odbij symetrycznie względem osi Y
                scaleX = 0.5
                mirrorY = True
                initX += 75 / 2
                initY += 335
            case "4": #pochyl figurę
                slash = 30
            case "5": #rozszerz figurę o 50% wzdłuz osi X oraz dosuń figurę do górnej krawędzi ekranu
                initY = 0
                scaleX = 1.5
            case "6": #pochyl figurą oraz obróć ją o 90 stopni
                slash = 30
                xc, yc = drawFigure()
                phase = 90.0 * math.pi / 180.0  # 45 deg to rad
                xc2, yc2 = drawFigure()
                initX += xc - xc2
                initY += yc - yc2
            case "7": #odbij figurę symetrycznie względem osi X oraz Y
                scaleX = 0.5
                mirrorX = True
                mirrorY = True
                initX += 75 * 3 / 2
                initY += 335
            case "8": #obróć figurę, wydłuż ją wzdłuż osi X oraz przesuń w dół ekranu
                phase = 45.0 * math.pi / 180.0  # 45 deg to rad
                xc = 275
                yc = initY + 335 / 2
                xc2, yc2 = 143, 363
                initX += xc - xc2
                initY += yc - yc2
                scaleX = 1.5
                scaleY = 0.5
                initY += 100
            case "9": #pochyl figurę oraz obróć ją o 180 stopni i dosuń do prawej krawędi ekranu
                slash = 30
                xc, yc = drawFigure()
                phase = 180.0 * math.pi / 180.0  # 45 deg to rad
                xc2, yc2 = drawFigure()
                initX += xc - xc2+160
                initY += yc - yc2

        drawFigure()

    lastDrawMode = currentDrawMode
    pygame.display.update()

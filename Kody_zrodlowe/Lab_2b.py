import pygame

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

initX, initY = 100, 200




run = True
while run:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False

    pygame.draw.rect(win, NIEBIESKI, ( initX, initY, 400, 200))
    pygame.draw.polygon(win, NIEBIESKI, [( initX+200, initY), (initX+100, initY-150), (initX+300, initY-150)] )
    pygame.draw.polygon(win, NIEBIESKI, [( initX+200, initY+200), (initX+100, initY+350), (initX+300, initY+350)] )


    pygame.display.update()

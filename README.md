# Końtra

Celem projektu jest stworzenie gry wyścigowej `Końtra`. W grze
znajdzie się zarówno część single-player, jak i multiplayer.

## Zarys fabularny i założenia gry
W celu ucieczki od rzeczywistości postanowiłeś wyjechać
ma wieś. Od dawna marzyłeś o karierze wyścigów konnych i teraz jest Twoja okazja!
Zdobywaj konie, ulepszaj je i ścigaj się z innymi graczami w grze `Końtra`!

W rozgrywce możemy wyróżnić dwa główne moduły:

- Stadnina
> Każdy gracz ma swoją stadninę, w której przechowuje konie. Z jej poziomu
> może również zakupić nowe konie czy ulepszać te posiadane.

- Wyścig
> Gracz będzie mógł brać udział w wyścigach konnych sam, tudzież z innymi.

> W trybie jednoosobowym celem jest przebiegnięcie jak najdalej, zbierając w trakcie
> monety, jednocześnie unikając wszelkich przeszkód, których na trasie nie brak.

> W trybie wieloosobowym celem jest jako pierwszym przebiegnięcie określonego dystansu.

W grze obowiązuje system monetarny. Za tę walutę można m.in. kupować oraz ulepszać konie.
Konie można również trenować w trybie wyścigu jednoosobowego. Zarówno trenowanie jak i
ulepszanie konii polepsza ich możliwości w wyścigu.

## Wykorzystane technologie

Architektura projektu przedstawia sie następująco:
- Za podstawę projektu odpowiada `Java` z frameworkiem `LibGDX`,
  w którym napisaliśmy klienta
- Gra odbywa się lokalnie, synchronizując dane z serwerem zasobów
- Serwer oparty jest na `Flask`, który zarządza REST API z którym komunikuje się gracz
- Dane przechowywane są w bazie danych `PostgreSQL`
- Wyścigi oparte o komunikację `Websocket` z serwerem

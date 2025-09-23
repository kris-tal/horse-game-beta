# Końtra

Celem projektu jest stworzenie gry wyścigowej `Końtra`. W grze
znajdzie się zarówno część single-player, jak i multiplayer.

## Zarys fabularny i założenia gry
W celu ucieczki od rzeczywistości postanowiłeś wyjechać
ma wieś. Od dawna marzyłeś o karierze wyścigów konnych i teraz jest Twoja okazja! Zdobywaj konie, ulepszaj je i ścigaj się z innymi graczami w grze `Końtra`!


W rozgrywce możemy wyróżnić dwa główne moduły:

- Stadnina
> Każdy gracz ma swoją stadninę, w której przechowuje konie. Z jej poziomu
> może również zakupić nowe konie, ulepszać te posiadane czy też zobaczyć na
> poziom "prestiżu" innych graczy w ogólnym rankingu.

- Wyścig
> Gracz będzie mógł brać udział w wyścigach konnych sam, tudzież z innymi.

> W trybie jednoosobowym celem jest przebiegnięcie jak najdalej, zbierając w trakcie
> monety, jednocześnie unikając wszelkich przeszkód, których na trasie nie brak.

> W trybie wieloosobowym celem jest przebiegnięcie określonego dystansu w jak najkrótszym czasie.
> Czym wyższa lokata końcowa tym wyższych zdobyczy możemy oczekiwać z wyścigu.

W grze obowiązuje system monetarny. Za tę walutę można m.in. kupować oraz ulepszać konie.
Każdy gracz ma przypisaną również pewną nieujemną wartość zwaną "prestiżem". Jest ona zdobywana
wyłącznie poprzez wyścigi z innymi graczami. Można domniemać, że jego wartość koreluje z 
umiejętnościami i doświadczeniem danego gracza. 

## Wykorzystane technologie

Architektura projektu przedstawia sie następująco:
- Za podstawę projektu odpowiada `Java` z frameworkiem `LibGDX`,
  w którym napisaliśmy clienta
- Gra odbywa się lokalnie, synchronizując dane z serwerem zasobów
- Serwer oparty jest na `Flask`, który zarządza REST API z którym komunikuje się gracz
- Dane przechowywane są w bazie danych `PostgreSQL`
- W przyszłości tryb multiplayer, czyli wyścigi, będą oparte o komunikację `Websocket` z serwerem

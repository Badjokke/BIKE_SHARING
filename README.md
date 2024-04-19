# Semestralni práce pro kurz KIV/PIA, ZS 2023/24

Bike sharing aplikace implementující byznys požadavky:  https://github.com/fidransky/kiv-pia-labs/wiki/Semester-project

  Pro implementaci jsem z edukativních důvodů šel cestou mikroservisní architektury, která je pro scope této aplikace absolutní overkill. 
  Aplikace je roztržena na 3 mikroslužby - BikeSharing, BikeLocation, Authenticator a klientskou aplikaci.
  BikeSharing aplikace obaluje byznys logiku s uživateli - tedy registrace/login skrze vlastní implementaci a použití oauth2, konkrétně google oauth. V resources package existuje api.yaml soubor s definicí endpointů, které aplikace vystrkuje.
  Měla být RESTful - což je porušeno co se týče sémantiky endpointů. Stateless vlastnost je zachována.

  Authenticator aplikace generuje/validuje tokeny - jak vlastní JWT, tak google token skrze jimi poskytnuté API. Aplikace je také stateless - sémanticky bude trpět stejně jako BikeSharing.
  
  BikeLocation implementuje byznys požadavky ohledně jízdy na kole / sledování kol na mapě. Již z principu implementace socketů není aplikace stateless.

  Klientská aplikace neimplementuje téměř žádnou byznys logiku vyjma odesílání lokace kol pro uživatele, který na něm jede.

# Testování
  
  Pro otestování jednoho use caseu jsem si vybral servisování kola. 
  Otestován je samotný kontroler s namockovanou serviskou - tedy jestli vrací korektní http responses. 
  Následně samotná service, jestli se chová dle očekávání podle validních / nevalidních vstupů.
  Jednoduchý integrační test pomocí MockMvc, zda controllery na serveru reálně fungují a odpovídají na http requesty.
  Poslední scénář byla komunikace BikeLocation service s BikeSharing service - je nutné mít BikeSharing aplikaci spuštěnou. Primárně zde testuji, že spolu služby komunikují dle očekávání.

# Nedostatky

  Architektura aplikace je za mě asi v pořádku. Určitě chybí ještě load balancer a API gateway - nýbrž tohle bylo silně out of scope v rámci semestrální práce.
  Větší nedostatky při implementaci, které zabijí systém při vyšší scale: BikeSharing a BikeLocation by měly implementovat modul pro rozbalení JWT tokenu, protože dochází ke zbytečnému provolání autentizační služby.
  API endpointy nejsou pojmenovány korektně dle REST sémantiky.
  Chybí pokrytí testy - silně out of scope. 
  Security websocketů je taková okay~ish, určitě by šla udělat lépe. Zavírá socketů ze serveru není implementováno, v podstatě se hraje na "dobrou vůli" klientské strany, což je špatně. Autentizace by šla řešit WebSocketMessageInterceptorem,
  který by se například mohl podívat, zda odesílaná zpráva není "DISCONNECT", pokud by byla - může zavřít session.
  Mikroslužby porušují pravidlo data autonomity - tím se de facto nejedná o mikroslužby - rozběhání separátní databáze pro BikeLocation službu mi ovšem dravě požralo ramku, takže mají alespoň separátní schéma.

# Technologie

  Mikroslužby implementovány pomocí Spring boot frameworku (verze 3.1.5) + Spring security. Databáze zvolena jako MySQL, klientská aplikace implementována v React.js s Typescript příchutí. 
  Jakýsi InMemoryStorage pro kola spřažený s real time streamováním dat je ukuchtěn na klíně - opět pro scale by bylo chtěné mít jej jako separátní proces (na stejném fyzickém clusteru) - případně použít Redis.  

# Rozběhnutí
  Aplikace je dockerizovaná - stačí pouze spustit docker engine a následně spustit příkazy: docker compose build; docker compose up;
  Pro správný běh je nutné vložit dummy data do databáze - sql skript se nachází v adresáři data

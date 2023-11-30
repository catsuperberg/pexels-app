# Требования. Home Screen AC10, AC11: 
- По нажатию на заголовок под Search Bar, текст заголовка должен устанавливаться в поле ввода. Запрос данных по выбранному заголовку должен произойти без дополнительных действий 
- Если заголовок был выбран или запрос соответствовал тексту одного из заголовков, он должен менять свое состояние на активное. Если был выбран другой заголовок или произошел отличный от текста заголовка запрос, его состояние снова становится неактивным. По умолчанию все заголовки неактивны.

Данные требования можно интерпретировать как необходимость реализовать все запросы через search запросы в API. То есть, независимо от того ввёл пользователь текст в поле поиска или выбрал одну из Featured Collections происходит запрос на поиск. Данная интерпретация немного упрощает логику обработки действий пользователя, но отбирает у пользователя возможность видеть курированные изображения из выбранной коллекции.

В master-ветке при нажатии на заголовок\Collection запрос происходит через collection API, поэтому пользователь видит небольшое количество отобранных изображений. При этом пользователь может нажать на поле ввода и осуществить поиск по авто заполненному тексту из заголовка, тогда пользователь увидит уже “бесконечный” (обычно поисковые запросы возвращают тысячи результатов) список изображений на данную тему. Ввод в поле поиска текста, повторяющего один из заголовков, сделает этот заголовок активным, но оставит источником запроса поиск до непосредственного нажатия на заголовок.

# Диплом
## Задание 2: API.
### Что нужно сделать:
- Нужно протестировать ручки API для [Stellar Burgers](https://stellarburgers.nomoreparties.site/)
- [Документация API](https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf)

---
#### Создание пользователя:
- Создать уникального пользователя
- Создать пользователя, который уже зарегистрирован
- Создать пользователя и не заполнить одно из обязательных полей

#### Логин пользователя:
- Логин под существующим пользователем
- Логин с неверным логином и паролем

#### Изменение данных пользователя:
- С авторизацией
- Без авторизации

Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.

#### Создание заказа:
- С авторизацией
- Без авторизации
- С ингредиентами
- Без ингредиентов
- С неверным хешем ингредиентов

#### Получение заказов конкретного пользователя:
- Авторизованный пользователь
- Неавторизованный пользователь

---
### Отчет Allure
![Jacoco report](https://github.com/gh-Denis/diplom-2/blob/main/src/main/resources/Allure_report_1.PNG)
![Jacoco report](https://github.com/gh-Denis/diplom-2/blob/main/src/main/resources/Allure_report_2.PNG)
![Jacoco report](https://github.com/gh-Denis/diplom-2/blob/main/src/main/resources/Allure_report_3.PNG)
![Jacoco report](https://github.com/gh-Denis/diplom-2/blob/main/src/main/resources/Allure_report_4.PNG)

---
#### Использованы технологии:
- Java 11
- Maven 4
- JUnit 4
- Rest Assured
- Java Faker
- Allure
- Aspectj

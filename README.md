# DOTABOT

![Status Released](https://img.shields.io/badge/status-released-brightgreen)

## Описание
Проект написан на Spring Boot

Дота бот использует Steam API для отслеживания активности игрока в Dota2 и предоставляет возможность взаимодействия с этим API через Telegram бота.

### Список доступных команд:
- `/getmatcheslist` - получить детали матчей за последние 24 часа.

Чтобы бот работал, необходимо предоставить ему свой Steam API Key и Steam ID аккаунта. Также необходимо включить общедоступную историю матчей:
- [Как включить общедоступную историю матчей](https://www.youtube.com/watch?v=l2X_NMj8khw)
- [Как получить Steam API ключ](https://www.youtube.com/watch?v=tpa8lPIeJEo)
- [Как получить Steam ID аккаунта](https://www.youtube.com/watch?v=9tXmFeGtQJQ)

Без общедоступной истории матчей, бот будет отслеживать только факт захода и выхода в Dota2. Для ввода Steam API Key и Steam ID используйте команду `/menu`.

- `/stop` - останавливает слежение. Для возобновления отслеживания необходимо будет повторно ввести Steam ID.

### Как работает:
После использования команды `/start`, бот создает запись с вашим chat ID в MySQL базе данных.

Раз в минуту происходит проверка записей в базе данных. Если у записи есть Steam API Key и Steam ID, выполняется метод, который отправляет GET запрос к Steam API для получения профиля игрока. Если в ответе JSON строка "Dota 2" не равна `null`, считается, что игрок в игре, и создается запись в базе данных с временной меткой. Таким образом, метод проверяет базу данных каждую минуту.

Если после очередного ответа от API игрок вышел из игры, запускается цепочка методов, которая находит сыгранные игроком матчи за время игровой сессии и отправляет результаты вам в Telegram чат.

### Развертывание бота на своем оборудовании:

Если вы хотите развернуть бота на своем оборудовании, вам необходимо добавить в проект файл `application.properties` и создать своего бота в Telegram. В файле необходимо указать следующие переменные:

dev.telegramId = Ваш чат ID с ботом, туда будут приходить сообщения о исключениях, в будущем и другие сообщения тоже.

bot.username = Имя вашего телеграм бота
bot.token = Токен вашего телеграм бота

dota.api.urlDota = https://api.steampowered.com

spring.datasource.url = Адрес вашей MySQL БД
spring.datasource.username = Логин к БД
spring.datasource.password = Пароль к БД


spring.datasource.hikari.max-lifetime = 30000

Значение `spring.datasource.hikari.max-lifetime` - это время жизни соединения (Hikari) и является опциональным.

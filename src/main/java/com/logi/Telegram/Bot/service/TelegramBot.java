package com.logi.Telegram.Bot.service;

import com.logi.Telegram.Bot.config.BotConfig;
import com.logi.Telegram.Bot.model.CountBtcForBuy;
import com.logi.Telegram.Bot.model.User;
import com.logi.Telegram.Bot.repository.CountBtcForBuyRepository;
import com.logi.Telegram.Bot.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// SLF4J Данная аннотация позволяет записывать информацию о выполнении кода в логи
@Slf4j //SLF4J расшифровывается как S реализует F академию для J ava.
// Он обеспечивает простую абстракцию всех каркасов логирования в Java. Таким образом, он позволяет пользователю работать с любой из сред ведения журналов,
@Component // аннотация автоматически создает экземпляр класса
public class TelegramBot extends TelegramLongPollingBot { // обязательное расширение для телеграм бота

    @Autowired
      private UserRepository userRepository;

    @Autowired
      private CountBtcForBuyRepository countBtcForBuyRepository;
      final BotConfig config;
      private final String YES_BUTTON = "YES_BUTTON";
      private final String NO_BUTTON = "NO_BUTTON";




    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommand = new ArrayList<>();
        listOfCommand.add(new BotCommand("/start", "get started!"));
        listOfCommand.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommand.add(new BotCommand("/deletedata", "delete your data"));
        listOfCommand.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommand.add(new BotCommand("/settings", "settings your preference"));
        listOfCommand.add(new BotCommand("/testcommand", "testcommand"));
        listOfCommand.add(new BotCommand("/register", "register"));

        try {
            this.execute( new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null)); // устанавливаем наши команды
        } catch (TelegramApiException e) {
            log.error("Error setting bots command list: " + e.getMessage()); // при возникновении ошибки, запишем ее в наш журнал с логами
        }
    }
    boolean waitCommandBuyBtc = true;
    boolean waitAnswerBuyBtc = false;



    boolean waitCommandSendMessageNeedCryptoWallet = true;
    boolean waitAnswerFromUserWithWallet = false;

    @Override // username
    public String getBotUsername() {
        return config.getBotName(); // получаем имя бота из класса конфиг
    }

    @Override // token
    public String getBotToken() {
        return config.getToken(); // получаем token бота из класса конфиг
    }
    public Long getBotOwner() {
        return config.getBotOwner(); // получаем token бота из класса конфиг
    }



    private void startCommandReceived ( long chatId, String name){
        String data = " Приветствуем тебя, дорогой друг! \n\n" +
                "Купить и продать криптовалюту \n\n" +
                "                Личный кошелёк внутри бота.\n\n" +
                "                Деньги за отзывы и не только.\n\n" +
                "                Реферальная система.\n\n" +
                "                Быстро, удобно, выгодно.\n\n";
        log.info("RepliedUser: " + name + " /// " + " TextMessage: " + data); // записываем в журнал кому мы ответили и что за текст мы ответили





        SendMessage message = new SendMessage(); // создаем объект сообщение
        message.setChatId(String.valueOf(chatId));
        message.setText(data);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // создаем tgобъект кнопок
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>(); // создаем список со списками
        List<InlineKeyboardButton> buttons = new ArrayList<>(); // затем добавим этот список в список со списками
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(); // затем добавим этот список в список со списками









        InlineKeyboardButton BUY_BUTTON = new InlineKeyboardButton();
        BUY_BUTTON.setText("Купить");
        BUY_BUTTON.setCallbackData("BUY"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь

        InlineKeyboardButton SELL_BUTTON = new InlineKeyboardButton();
        SELL_BUTTON.setText("Продать");
        SELL_BUTTON.setCallbackData("Sell");

        InlineKeyboardButton WALLET_BUTTON = new InlineKeyboardButton();
        WALLET_BUTTON.setText("Кошелек");
        WALLET_BUTTON.setCallbackData("Wallet"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь

        InlineKeyboardButton REF_BUTTON = new InlineKeyboardButton();
        REF_BUTTON.setText("Рефералка");
        REF_BUTTON.setCallbackData("REFERENCE");

        InlineKeyboardButton SURPRISE_BUTTON = new InlineKeyboardButton();
        SURPRISE_BUTTON.setText("Розыгрыши");
        SURPRISE_BUTTON.setCallbackData("SURPRISE");



        buttons.add(SELL_BUTTON);
        buttons.add(BUY_BUTTON);
        buttons1.add(REF_BUTTON);
        buttons1.add(SURPRISE_BUTTON);
        buttons1.add(WALLET_BUTTON);

        inlineRows.add(buttons); // добавляем список с кнопками в список списками
        inlineRows.add(buttons1); // добавляем список с кнопками в список списками

        markupInline.setKeyboard(inlineRows); // добавляем список со списком кнопок в tgобъект с кнопками
        message.setReplyMarkup(markupInline);
        executeMessage(message);
    }


//    public void buyButtonReceived(){
//
//    }
//


    private void helpCommandReceived (long chatId, String name){
        String help = "This bot has created to demonstration goal \n\n" +
                "You can execute commands from the main menu on the left or by typing a command: \n"+
                "Type /start to see a welcome message \n"+
                "Type /mydata to see data stored about yourself \n"+
                "Type /help to see this message again"
                ;
        log.info("RepliedUser: " + name + " /// " + " TextMessage: " + help); // записываем в журнал кому мы ответили и что за текст мы ответили
        SendMessage(chatId, help);
    }
    private void chooseCryptoCurrency(long chatId, long messageId){
        String data = "Выбери валюту для покупки: "
                ;

        SendMessage message = new SendMessage(); // создаем объект сообщение
        message.setChatId(String.valueOf(chatId));
        message.setText(data);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // создаем tgобъект кнопок
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>(); // создаем список со списками
        List<InlineKeyboardButton> buttons = new ArrayList<>(); // затем добавим этот список в список со списками

        InlineKeyboardButton USDT = new InlineKeyboardButton();
        USDT.setText("USDT");
        USDT.setCallbackData("USDT"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь

        InlineKeyboardButton BTC = new InlineKeyboardButton();
        BTC.setText("BTC");
        BTC.setCallbackData("BTC_BUTTON");

        InlineKeyboardButton MNR = new InlineKeyboardButton();
        MNR.setText("MNR");
        MNR.setCallbackData("MNR_BUTTON"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь

        InlineKeyboardButton LTC = new InlineKeyboardButton();
        LTC.setText("LTC");
        LTC.setCallbackData("LTC_BUTTON");

        InlineKeyboardButton ETH = new InlineKeyboardButton();
        ETH.setText("ETH");
        ETH.setCallbackData("ETH_BUTTON");

        buttons.add(LTC);
        buttons.add(ETH);
        buttons.add(MNR);
        buttons.add(BTC);
        buttons.add(USDT);

        inlineRows.add(buttons); // добавляем список с кнопками в список списками

        markupInline.setKeyboard(inlineRows); // добавляем список со списком кнопок в tgобъект с кнопками

        executeEditMessageText(chatId, messageId, data, markupInline);
    }
//    public void register(long chatId){
//        SendMessage message = new SendMessage(); // создаем объект сообщение
//        message.setChatId(String.valueOf(chatId));
//        message.setText("Do you really want to register?");
//
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // создаем tgобъект кнопок
//        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>(); // создаем список со списками
//        List<InlineKeyboardButton> buttons = new ArrayList<>(); // затем добавим этот список в список со списками
//
//        InlineKeyboardButton yesButton = new InlineKeyboardButton();
//        yesButton.setText("Yes");
//        yesButton.setCallbackData(YES_BUTTON); // с помощью данного метода бот понимает на какую кнопку нажал пользователь
//
//        InlineKeyboardButton noButton = new InlineKeyboardButton();
//        noButton.setText("No");
//        noButton.setCallbackData("NO_BUTTON");
//
//        buttons.add(yesButton);
//        buttons.add(noButton);
//
//        inlineRows.add(buttons); // добавляем список с кнопками в список списками
//
//        markupInline.setKeyboard(inlineRows); // добавляем список со списком кнопок в tgобъект с кнопками
//
//        message.setReplyMarkup(markupInline); // добавляем объект с кнопками в объект сообщения
//
//        executeMessage(message);
//    }
    @Override // что должен делать бот
    public void onUpdateReceived(Update update) {










//        if (update.hasCallbackQuery()) {
//            // Обработка нажатия на инлайн кнопку
//            CallbackQuery callbackQuery = update.getCallbackQuery();
//            String data = callbackQuery.getData();
//            String chatId = callbackQuery.getMessage().getChatId().toString();
//
//            // Отправка ответа пользователю после нажатия кнопки
//            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
//            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
//            answerCallbackQuery.setText("Вы нажали кнопку!");
//            try {
//                execute(answerCallbackQuery);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//
//            // Ожидание сообщения от пользователя
//            SendMessage message = new SendMessage();
//            message.setChatId(chatId);
//            message.setText("Пожалуйста, отправьте сообщение:");
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        } else if (update.hasMessage()) {
//            // Обработка сообщения от пользователя
//            Message message = update.getMessage();
//            String chatId = message.getChatId().toString();
//            String text = message.getText();
//
//            // Отправка ответа пользователю на его сообщение
//            SendMessage response = new SendMessage();
//            response.setChatId(chatId);
//            response.setText("Вы отправили сообщение: " + text);
//            try {
//                execute(response);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }


        // Update класс содержит в себе сообщения которые пользователи посылают боту, так же инфу о пользователе

        /// если есть новое сообщение && есть текст в этом смс
        if (update.hasMessage() && update.getMessage().hasText()) {



            long chatId = update.getMessage().getChatId();

            String messageText = update.getMessage().getText(); /// сообщение от юзера лежит теперь здесь

            List<Character> chars = new ArrayList<>(); // проверяем количество символов в смс юзера
            for (char c : messageText.toCharArray()) {
                chars.add(c);
            }
            if (chars.size() > 30){
                getBtcAddressFromUserBC1(chatId);
            }
            if (messageText.contains(".")){
                try {
                    howMuchUserShouldPay(messageText, chatId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                waitAnswerBuyBtc = false;
            }

            if (messageText.contains("/send") && getBotOwner() == chatId){
                String textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));// Метод indexOf() в Java · возвращает индекс, под которым символ или строка первый раз появляется в строке
                List<User> users = (List<User>) userRepository.findAll(); // находим всех пользователей
                for (User user : users){ // в методе forEach реализуем отправку в каждый чат с каждым юзером смс рассылку
                    SendMessage(user.getChatId(), textToSend);
                }
            }



            // с пользователем могут общаться множество пользователей одновременно и боту надо знать чат айди с кем он общается и что окму отвечать

            // передаем параметром чат айди, getMessage() и из getChat достаем информацию о пользователе
//            if (update.equals("/start")) {
//                startCommandReceived(update.getMessage().getChatId());
//            } else {
//                sendMessage(chatId, "Прости, бро, команда пока не поддерживается");
//            }
            String firstName = update.getMessage().getChat().getFirstName(); ///
            switch (messageText){
                case "/start" :
                    registerToUser(update.getMessage());
                    startCommandReceived(chatId, firstName);
                    break;
                case "/help" : helpCommandReceived(chatId, firstName);
                    break;
//                case "Покупка" : chooseCryptoCurrency(chatId, update.getMessage().getMessageId());
//                    break;
//                case "/register" : register(chatId);
//                    break;
//                case "buybtc" : howMuchUserShouldPay();
//                break;

            }

        }
        else if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callBackData = callbackQuery.getData(); // .getData() здесь содержится айди той кнопки на которую нажал пользователь (да\нет)

            long messageId = update.getCallbackQuery().getMessage().getMessageId(); // для того что бы изменять сообщение прямо в том же поля у пользователя, нам понадобится айди конкретного сообщения
            long chatId = update.getCallbackQuery().getMessage().getChatId();


//
//            // Отправка ответа пользователю после нажатия кнопки
//            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
//            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
//            answerCallbackQuery.setText("Вы нажали кнопку!");
//
//
//


            if (callBackData.equals("BUY")){
                chooseCryptoCurrency(chatId, messageId);
            }

             if (callBackData.equals("BTC_BUTTON") && waitCommandBuyBtc){
                enterCountToBuy(chatId);

            }
             if(callBackData.equals("YES")){
                try {
                    showSberBankCredentials(chatId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                showAllPaymentsMethodForSelect(chatId, messageId);
            }
//            else if(callBackData.equals("SBER")){
//                return null;
//            }

//            else if(callBackData.equals("SBER")){
//                showSberBankCredentials(chatId);
//            }
//            else if (callBackData.equals("YES")){
//                showAllPaymentsMethodForSelect()
//            }


//            if (callBackData.equals(YES_BUTTON)){
//
//                String text = "You pressed yes button";
//                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // создаем tgобъект кнопок
//                List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>(); // создаем список со списками
//                List<InlineKeyboardButton> buttons = new ArrayList<>(); // затем добавим этот список в список со списками
//
//                InlineKeyboardButton Family = new InlineKeyboardButton();
//                Family.setText("Family");
//                Family.setCallbackData("Family"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь
//
//                InlineKeyboardButton Name = new InlineKeyboardButton();
//                Name.setText("Name");
//                Name.setCallbackData("Name");
//
//                buttons.add(Family);
//                buttons.add(Name);
//
//                inlineRows.add(buttons); // добавляем список с кнопками в список списками
//
//                markupInline.setKeyboard(inlineRows); // добавляем список со списком кнопок в tgобъект с кнопками
//                executeEditMessageText(chatId, messageId, text, markupInline);
//
//
//            } else if (callBackData.equals(NO_BUTTON)){
//                String text = "You pressed NO button";
//
//                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // создаем tgобъект кнопок
//                List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>(); // создаем список со списками
//                List<InlineKeyboardButton> buttons = new ArrayList<>(); // затем добавим этот список в список со списками
//
//                InlineKeyboardButton Family = new InlineKeyboardButton();
//                Family.setText("Test");
//                Family.setCallbackData("Family"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь
//
//                InlineKeyboardButton Name = new InlineKeyboardButton();
//                Name.setText("Test-2");
//                Name.setCallbackData("Name");
//
//                buttons.add(Family);
//                buttons.add(Name);
//
//                inlineRows.add(buttons); // добавляем список с кнопками в список списками
//
//                markupInline.setKeyboard(inlineRows); // добавляем список со списком кнопок в tgобъект с кнопками
//                executeEditMessageText(chatId, messageId, text, markupInline);
//            }
        }

    }

    private void getAddressBtcFromUser1N(long chatId) {
        String text = "Hello from getBtcFromUser1N";
        SendMessage(chatId, text);
    }

    private void getAddressBtcFromUser3J(long chatId) {
        String text = "Hello from getBtcFromUser3J";
        SendMessage(chatId, text);
    }

    private void getBtcAddressFromUserBC1(long chatId) {
        String text = "Hello from getBtcAddresFromUserBc1";
        SendMessage(chatId, text);
    }

    private void showSberBankCredentials(long chatId) throws IOException {
        String text = "Введите адрес биткоин кошелька на который хотите получить монеты: ";

        SendMessage(chatId, text);
    }

    public String findCountByChatId(long chatId){
        CountBtcForBuy countForBuy = countBtcForBuyRepository.findById(chatId).orElseThrow(() -> new IllegalStateException("Not found"));
        Integer CountBtcForBuy = countForBuy.getCountForBuy();
        return Integer.toString(CountBtcForBuy);
    }

    private void showAllPaymentsMethodForSelect(long chatId, long messageId) {
        CountBtcForBuy countForBuy = countBtcForBuyRepository.findById(chatId).orElseThrow(() -> new IllegalStateException("Not found"));
        Integer CountBtcForBuy = countForBuy.getCountForBuy();
        String countBtcString = Integer.toString(CountBtcForBuy);
        String data = "Сумма к оплате: %s RUB\n\n" +
                "Выберите подходящий способ оплаты: ";
        String answer = String.format(data, countBtcString);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // создаем tgобъект кнопок
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>(); // создаем список со списками
        List<InlineKeyboardButton> buttons = new ArrayList<>(); // затем добавим этот список в список со списками
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(); // затем добавим этот список в список со списками









        InlineKeyboardButton BUY_BUTTON = new InlineKeyboardButton();
        BUY_BUTTON.setText("Сбербанк");
        BUY_BUTTON.setCallbackData("SBER"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь

        InlineKeyboardButton SELL_BUTTON = new InlineKeyboardButton();
        SELL_BUTTON.setText("Tinkoff");
        SELL_BUTTON.setCallbackData("TNKF");

        InlineKeyboardButton WALLET_BUTTON = new InlineKeyboardButton();
        WALLET_BUTTON.setText("Alfa-Bank");
        WALLET_BUTTON.setCallbackData("ALFA"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь

        InlineKeyboardButton REF_BUTTON = new InlineKeyboardButton();
        REF_BUTTON.setText("QIWI");
        REF_BUTTON.setCallbackData("QIWI");

        InlineKeyboardButton SURPRISE_BUTTON = new InlineKeyboardButton();
        SURPRISE_BUTTON.setText("ВТБ БАНК");
        SURPRISE_BUTTON.setCallbackData("VTB");



        buttons.add(SELL_BUTTON);
        buttons.add(BUY_BUTTON);
        buttons1.add(REF_BUTTON);
        buttons1.add(SURPRISE_BUTTON);
        buttons1.add(WALLET_BUTTON);

        inlineRows.add(buttons); // добавляем список с кнопками в список списками
        inlineRows.add(buttons1); // добавляем список с кнопками в список списками

        markupInline.setKeyboard(inlineRows); // добавляем список со списком кнопок в tgобъект с кнопками
        executeEditMessageText(chatId, messageId, answer, markupInline);
    }

    private void howMuchUserShouldPay(String messageText, long chatId) throws IOException {
        Double howMuchWantUser = Double.parseDouble(messageText);
        String currentlyBtcPrice = btcPrice();
        Integer priceToRubleForUser = (int) (howMuchWantUser * Integer.parseInt(currentlyBtcPrice));


        CountBtcForBuy countBtcForBuy = new CountBtcForBuy();
        countBtcForBuy.setId(chatId);
        countBtcForBuy.setCountForBuy(priceToRubleForUser);
        countBtcForBuyRepository.save(countBtcForBuy);

        String answer = "Сумма к оплате: " + priceToRubleForUser + " RUB \n\n" +
                "Желаете продолжить?";
        SendMessage message = new SendMessage(); // создаем объект сообщение
        message.setChatId(String.valueOf(chatId));
        message.setText(answer);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(); // создаем tgобъект кнопок
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>(); // создаем список со списками
        List<InlineKeyboardButton> buttons = new ArrayList<>(); // затем добавим этот список в список со списками
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(); // затем добавим этот список в список со списками

        InlineKeyboardButton YES_BUTTON = new InlineKeyboardButton();
        YES_BUTTON.setText("Да");
        YES_BUTTON.setCallbackData("YES"); // с помощью данного метода бот понимает на какую кнопку нажал пользователь

        InlineKeyboardButton NO_BUTTON = new InlineKeyboardButton();
        NO_BUTTON.setText("Нет");
        NO_BUTTON.setCallbackData("NO");




        buttons.add(NO_BUTTON);
        buttons.add(YES_BUTTON);

        inlineRows.add(buttons); // добавляем список с кнопками в список списками
        inlineRows.add(buttons1); // добавляем список с кнопками в список списками

        markupInline.setKeyboard(inlineRows); // добавляем список со списком кнопок в tgобъект с кнопками

        setCountBtcForBuy(priceToRubleForUser, chatId);
        message.setReplyMarkup(markupInline);
        executeMessage(message);
////

        waitCommandBuyBtc = true;
        waitAnswerBuyBtc = false;
    }

    private void sendMessageWithInlineKeyboardsMENU(long chatId, String textToSend){ // метод который отправляет сообщения пользователю параметром принимает айди  чата и то что нужно отправить



            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboardRowList= new ArrayList<>(); // создаем список из рядов с кнопками
            KeyboardRow keyboardRow = new KeyboardRow(); // KeyboardRow объект-ряд который содержит кнопки
            keyboardRow.add("Покупка");
            keyboardRow.add("Продажа");// две кнопки в один ряд
            keyboardRowList.add(keyboardRow);

            KeyboardRow keyboardRow1 = new KeyboardRow();
            keyboardRow1.add("Тех. Поддержка 24/7");
            keyboardRow1.add("Кошелек");
            keyboardRow1.add("Перевод");
            keyboardRowList.add(keyboardRow1);

            replyKeyboardMarkup.setKeyboard(keyboardRowList); // установили в объект клавиатуры наш список с кнопками
            SendMessageWithKeyboard(chatId, textToSend, replyKeyboardMarkup);



        }

        public void registerToUser(Message message){
                if (userRepository.findById(message.getChatId()).isEmpty()){
                    Long chatId = message.getChatId();
                    Chat chat = message.getChat();

                    User user = new User();
                    user.setChatId(chatId);
                    user.setFirstName(chat.getFirstName());
                    user.setLastName(chat.getLastName());
                    user.setUserName(chat.getUserName());
                    user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

                    userRepository.save(user);
                    log.info("User saved "+ user);
                }
        }

        public void setCountBtcForBuy(Integer count, long chatId){
            try {
                CountBtcForBuy countBtcForBuy = new CountBtcForBuy();
                countBtcForBuy.setCountForBuy(count);
                countBtcForBuyRepository.save(countBtcForBuy);
                log.info("Сделка сохранена "+ chatId);
            } catch (RuntimeException exception){
                log.error("Error while setCountBtcForBuy " + exception.getMessage());
            }
        }

        public void enterCountToBuy(long chatId){




            String text = "Укажите сумму в BTC или же RUB: \n\n" +
                    "Пример: 0.001 или 0,001 или 5030";
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(text);
            sendMessage.setChatId(String.valueOf(chatId));
            executeMessage(sendMessage);

            waitCommandBuyBtc = false;
            waitAnswerBuyBtc = true;




        }


        public void executeEditMessageText(long chatId, long messageId, String Text, InlineKeyboardMarkup markupInline){
            EditMessageText editMessageText = new EditMessageText(); // создаем объект изменения сообщений
            editMessageText.setChatId(String.valueOf(chatId));
            editMessageText.setText(Text);
            editMessageText.setMessageId((int) messageId); // установим айди конкретного сообщения что бы изменить его
            editMessageText.setReplyMarkup(markupInline); // добавляем объект с кнопками в объект сообщения


            try {
                execute(editMessageText);
            } catch (TelegramApiException exception){
                log.error("Error occurred: " + exception.getMessage());
            }
        }



        /// просто текстовое сообщение
        private void SendMessage(long chatId, String text){
            SendMessage message = new SendMessage();  // создаем объект нового смс, вызываем его из встроенного телеграм пакета который мы установили в самом начале

            message.setChatId(String.valueOf(chatId));/// установим чат айди в объект нового сообщения что бы понимать с кем общаемся
            ///  String.valueOf(chatId) тип данных long chatId  необходимо преобразовать в тип данных стринг

            message.setText(text); // установим в объект нового смс текст который нам нужно отправить
            executeMessage(message);
        }


    // сообщение с текстом и инлайн кнопками вместо клавиатуры
    private void SendMessageWithKeyboard(long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup){
        SendMessage message = new SendMessage();  // создаем объект нового смс, вызываем его из встроенного телеграм пакета который мы установили в самом начале

        message.setChatId(String.valueOf(chatId));/// установим чат айди в объект нового сообщения что бы понимать с кем общаемся
        ///  String.valueOf(chatId) тип данных long chatId  необходимо преобразовать в тип данных стринг

        message.setText(text); // установим в объект нового смс текст который нам нужно отправить
        message.setReplyMarkup(replyKeyboardMarkup); // привязали к каждому сообщению
        executeMessage(message);

    }



        public void executeMessage(SendMessage message){

            try {
                execute(message);
            } catch (TelegramApiException e){
                log.error("Error occurred: " + e.getMessage());
            }
        }


    public String btcPrice() throws IOException {

        OkHttpClient client = new OkHttpClient();

        // Создание запроса GET к API Binance для получения текущего курса пары BTCRUB
        Request request = new Request.Builder()
                .url("https://api.binance.com/api/v3/ticker/price?symbol=BTCRUB")
                .build();

        // Выполнение запроса
        Response response = client.newCall(request).execute();

        // Парсинг ответа
        String responseBody = response.body().string();
        JSONObject json = new JSONObject(responseBody);
        String lastPrice = json.getString("price");

        // Вывод текущего курса BTCRUB
        System.out.println("Current BTCRUB price: " + lastPrice );

        String[] arr = lastPrice.split("\\.");
        String price = arr[0];

        return price;

    }




}
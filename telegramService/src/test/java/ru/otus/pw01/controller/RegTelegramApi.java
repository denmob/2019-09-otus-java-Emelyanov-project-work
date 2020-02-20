package ru.otus.pw01.controller;

import org.junit.BeforeClass;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.Webhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

public abstract class RegTelegramApi {

    @BeforeClass
    public static void beforeClass() {
        ApiContext.register(BotSession.class, DefaultBotSession.class);
        ApiContext.register(Webhook.class, DefaultWebhook.class);
    }
}
package com.jk.apps.chatbot.model;

public class ChatModel {
    String txtUser;
    String txtBot;
    boolean isLoading;

    public ChatModel(String txtUser, String txtBot, boolean isLoading) {
        this.txtUser = txtUser;
        this.txtBot = txtBot;
        this.isLoading = isLoading;
    }


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getTxtUser() {
        return txtUser;
    }

    public void setTxtUser(String txtUser) {
        this.txtUser = txtUser;
    }

    public String getTxtBot() {
        return txtBot;
    }

    public void setTxtBot(String txtBot) {
        this.txtBot = txtBot;
    }
}

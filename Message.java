package Chatserver;

import java.io.Serializable;

/**
 *
 * @author Asus
 */
public class Message implements Serializable {

    enum Type {
        MESSAGE, PRIVATEMESSAGE, KICK, BAN, ONLINE, CHANGEPASS, CHANGEUSER, ERROR, FILE, YES, NO, LOGOUT
    }
    private final String msg;
    private final String sender;
    private final String receiver;
    private final Type type;

    @SuppressWarnings("NonPublicExported")
    public Message(String msg, String sender, String receiver, Type type) {
        this.msg = msg;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    @SuppressWarnings("NonPublicExported")
    public Type getType() {
        return type;
    }
}

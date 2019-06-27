package Chatserver;

/**
 *
 * @author Asus
 */
public class DuoChat {
    
    private String friend;
    private DuoChatFrame chatFrame;

    /**
     * Creates new form DuoChat
     * @param friend
     */
    public DuoChat(String friend) {
        this.friend = friend;
        this.chatFrame = null;
    }

    public String getFriend() {
        return friend;
    }

    public DuoChatFrame getChatFrame() {
        return chatFrame;
    }

    public void setChatFrame(DuoChatFrame chatFrame) {
        this.chatFrame = chatFrame;
    }
}

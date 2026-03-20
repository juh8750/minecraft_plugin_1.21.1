package org.example.smartphonemod.smartphonemod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class PhoneScreen extends Screen {

    private enum Page {
        HOME,
        CONTACTS,
        CONTACT_ACTIONS,
        ADD_CONTACT_NAME,
        ADD_CONTACT_NUMBER,
        CHAT_ROOM
    }

    private static class Contact {
        private final String name;
        private final String phoneNumber;

        public Contact(String name, String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public String name() {
            return name;
        }

        public String phoneNumber() {
            return phoneNumber;
        }
    }

    private Page page = Page.HOME;
    private int selectedIndex = 0;
    private int selectedContactIndex = 0;
    private String status = "폰 준비 완료";

    private final List<Contact> contacts = new ArrayList<>();

    private static final String[] HOME_MENU = {
            "전화번호부",
            "메시지",
            "설정"
    };

    private static final String[] CONTACT_ACTIONS = {
            "전화 걸기",
            "채팅방 열기",
            "연락처 삭제",
            "뒤로"
    };

    private static final int PHONE_WIDTH = 150;
    private static final int PHONE_HEIGHT = 250;
    private static final int MARGIN_RIGHT = 12;
    private static final int MARGIN_BOTTOM = 12;

    private EditBox inputBox;
    private String draftContactName = "";
    private String draftContactNumber = "";
    private String draftMessage = "";

    public PhoneScreen() {
        super(Component.literal("Smartphone"));

        contacts.add(new Contact("Steve", "010-0000-0001"));
        contacts.add(new Contact("Alex", "010-0000-0002"));
        contacts.add(new Contact("Guard", "010-0000-0003"));
    }

    @Override
    protected void init() {
        super.init();

        int x = this.width - PHONE_WIDTH - MARGIN_RIGHT;
        int y = this.height - PHONE_HEIGHT - MARGIN_BOTTOM;

        this.inputBox = new EditBox(
                this.font,
                x + 10,
                y + PHONE_HEIGHT - 28,
                PHONE_WIDTH - 20,
                18,
                Component.literal("입력")
        );
        this.inputBox.setMaxLength(100);
        this.inputBox.setVisible(false);

        this.addRenderableWidget(this.inputBox);
        updateInputBoxForCurrentPage();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private boolean isTypingPage() {
        return page == Page.ADD_CONTACT_NAME
                || page == Page.ADD_CONTACT_NUMBER
                || page == Page.CHAT_ROOM;
    }

    private Contact getSelectedContact() {
        if (contacts.isEmpty()) {
            return null;
        }
        if (selectedContactIndex < 0 || selectedContactIndex >= contacts.size()) {
            return null;
        }
        return contacts.get(selectedContactIndex);
    }

    private void updateInputBoxForCurrentPage() {
        if (inputBox == null) {
            return;
        }

        if (!isTypingPage()) {
            inputBox.setVisible(false);
            inputBox.setFocused(false);
            return;
        }

        inputBox.setVisible(true);
        inputBox.setFocused(true);
        this.setFocused(inputBox);

        switch (page) {
            case ADD_CONTACT_NAME -> {
                inputBox.setMaxLength(20);
                inputBox.setValue(draftContactName);
            }
            case ADD_CONTACT_NUMBER -> {
                inputBox.setMaxLength(30);
                inputBox.setValue(draftContactNumber);
            }
            case CHAT_ROOM -> {
                inputBox.setMaxLength(100);
                inputBox.setValue(draftMessage);
            }
            default -> {
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int x = this.width - PHONE_WIDTH - MARGIN_RIGHT;
        int y = this.height - PHONE_HEIGHT - MARGIN_BOTTOM;

        String closeKeyName = PhoneKeyHandler.OPEN_PHONE.getTranslatedKeyMessage().getString();

        guiGraphics.fill(x - 2, y - 2, x + PHONE_WIDTH + 2, y + PHONE_HEIGHT + 2, 0xFF101010);
        guiGraphics.fill(x, y, x + PHONE_WIDTH, y + PHONE_HEIGHT, 0xE0202020);

        guiGraphics.drawString(this.font, "SMARTPHONE", x + 10, y + 8, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, status, x + 10, y + PHONE_HEIGHT - 12, 0xAAAAAA, false);

        switch (page) {
            case HOME -> {
                drawMenu(guiGraphics, x, y, "메뉴", HOME_MENU, selectedIndex);
                guiGraphics.drawString(this.font, closeKeyName + ": 닫기", x + 10, y + 120, 0xCCCCCC, false);
            }
            case CONTACTS -> {
                drawContacts(guiGraphics, x, y);
                guiGraphics.drawString(this.font, "A: 추가", x + 10, y + 190, 0xCCCCCC, false);
                guiGraphics.drawString(this.font, "Enter: 선택", x + 10, y + 202, 0xCCCCCC, false);
                guiGraphics.drawString(this.font, "Backspace: 뒤로", x + 10, y + 214, 0xCCCCCC, false);
            }
            case CONTACT_ACTIONS -> {
                Contact contact = getSelectedContact();
                String title = contact == null ? "연락처" : contact.name();
                drawMenu(guiGraphics, x, y, title, CONTACT_ACTIONS, selectedIndex);

                if (contact != null) {
                    guiGraphics.drawString(this.font, contact.phoneNumber(), x + 10, y + 120, 0xCCCCCC, false);
                }
            }
            case ADD_CONTACT_NAME -> {
                guiGraphics.drawString(this.font, "연락처 추가", x + 10, y + 28, 0x00FFAA, false);
                guiGraphics.drawString(this.font, "이름 입력", x + 10, y + 48, 0xFFFFFF, false);
                guiGraphics.drawString(this.font, "Enter: 다음", x + 10, y + 70, 0xCCCCCC, false);
                guiGraphics.drawString(this.font, "Esc: 취소", x + 10, y + 82, 0xCCCCCC, false);
            }
            case ADD_CONTACT_NUMBER -> {
                guiGraphics.drawString(this.font, "연락처 추가", x + 10, y + 28, 0x00FFAA, false);
                guiGraphics.drawString(this.font, "번호 입력", x + 10, y + 48, 0xFFFFFF, false);
                guiGraphics.drawString(this.font, "Enter: 저장", x + 10, y + 70, 0xCCCCCC, false);
                guiGraphics.drawString(this.font, "Esc: 이전", x + 10, y + 82, 0xCCCCCC, false);
            }
            case CHAT_ROOM -> drawChatRoom(guiGraphics, x, y);
        }
    }

    private void drawMenu(GuiGraphics guiGraphics, int x, int y, String title, String[] entries, int selected) {
        guiGraphics.drawString(this.font, title, x + 10, y + 28, 0x00FFAA, false);

        int startY = y + 48;
        for (int i = 0; i < entries.length; i++) {
            int itemY = startY + (i * 16);

            if (i == selected) {
                guiGraphics.fill(x + 8, itemY - 2, x + PHONE_WIDTH - 8, itemY + 10, 0xAA3A6EA5);
            }

            guiGraphics.drawString(this.font, entries[i], x + 12, itemY, 0xFFFFFF, false);
        }
    }

    private void drawContacts(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.drawString(this.font, "전화번호부", x + 10, y + 28, 0x00FFAA, false);

        int startY = y + 48;
        int maxLines = Math.min(8, contacts.size());

        for (int i = 0; i < maxLines; i++) {
            Contact contact = contacts.get(i);
            int itemY = startY + (i * 16);

            if (i == selectedIndex) {
                guiGraphics.fill(x + 8, itemY - 2, x + PHONE_WIDTH - 8, itemY + 10, 0xAA3A6EA5);
            }

            guiGraphics.drawString(this.font, contact.name(), x + 12, itemY, 0xFFFFFF, false);
        }
    }

    private void drawChatRoom(GuiGraphics guiGraphics, int x, int y) {
        Contact contact = getSelectedContact();
        if (contact == null) {
            return;
        }

        guiGraphics.drawString(this.font, contact.name(), x + 10, y + 12, 0x00FFAA, false);
        guiGraphics.drawString(this.font, contact.phoneNumber(), x + 10, y + 24, 0xCCCCCC, false);
        guiGraphics.drawString(this.font, "Enter: 전송", x + 10, y + 38, 0xCCCCCC, false);
        guiGraphics.drawString(this.font, "Esc: 뒤로", x + 80, y + 38, 0xCCCCCC, false);

        int roomTop = y + 52;
        int roomBottom = y + PHONE_HEIGHT - 36;

        guiGraphics.fill(x + 6, roomTop, x + PHONE_WIDTH - 6, roomBottom, 0x44111111);

        List<PhoneChatState.ChatMessage> messages = PhoneChatState.getRoom(contact.phoneNumber());
        int visibleCount = Math.min(8, messages.size());
        int startIndex = Math.max(0, messages.size() - visibleCount);
        int bubbleY = roomTop + 6;

        for (int i = startIndex; i < messages.size(); i++) {
            PhoneChatState.ChatMessage message = messages.get(i);
            String text = message.text();
            int bubbleHeight = 14;

            if (message.mine()) {
                int bubbleX1 = x + 42;
                int bubbleX2 = x + PHONE_WIDTH - 10;
                guiGraphics.fill(bubbleX1, bubbleY, bubbleX2, bubbleY + bubbleHeight, 0xAA3A6EA5);
                guiGraphics.drawString(this.font, text, bubbleX1 + 4, bubbleY + 3, 0xFFFFFF, false);
            } else {
                int bubbleX1 = x + 10;
                int bubbleX2 = x + 108;
                guiGraphics.fill(bubbleX1, bubbleY, bubbleX2, bubbleY + bubbleHeight, 0xAA444444);
                guiGraphics.drawString(this.font, text, bubbleX1 + 4, bubbleY + 3, 0xFFFFFF, false);
            }

            bubbleY += 18;
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (isTypingPage() && inputBox != null && inputBox.isVisible()) {
            return inputBox.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isTypingPage()) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                return handleTypingBack();
            }

            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                return handleTypingEnter();
            }

            if (inputBox != null && inputBox.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }

            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }

        return switch (page) {
            case HOME -> handleHomeKey(keyCode);
            case CONTACTS -> handleContactsKey(keyCode);
            case CONTACT_ACTIONS -> handleContactActionsKey(keyCode);
            default -> super.keyPressed(keyCode, scanCode, modifiers);
        };
    }

    private boolean handleHomeKey(int keyCode) {
        if (keyCode == GLFW.GLFW_KEY_UP) {
            selectedIndex = (selectedIndex - 1 + HOME_MENU.length) % HOME_MENU.length;
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            selectedIndex = (selectedIndex + 1) % HOME_MENU.length;
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            if (selectedIndex == 0 || selectedIndex == 1) {
                page = Page.CONTACTS;
                selectedIndex = 0;
                status = "연락처 선택";
            } else {
                status = "설정은 나중에 추가";
            }
            updateInputBoxForCurrentPage();
            return true;
        }

        return false;
    }

    private boolean handleContactsKey(int keyCode) {
        if (contacts.isEmpty()) {
            if (keyCode == GLFW.GLFW_KEY_A) {
                draftContactName = "";
                draftContactNumber = "";
                page = Page.ADD_CONTACT_NAME;
                status = "이름 입력";
                updateInputBoxForCurrentPage();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                page = Page.HOME;
                selectedIndex = 0;
                status = "메인 메뉴";
                return true;
            }

            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_UP) {
            selectedIndex = (selectedIndex - 1 + contacts.size()) % contacts.size();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            selectedIndex = (selectedIndex + 1) % contacts.size();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_A) {
            draftContactName = "";
            draftContactNumber = "";
            page = Page.ADD_CONTACT_NAME;
            status = "이름 입력";
            updateInputBoxForCurrentPage();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            page = Page.HOME;
            selectedIndex = 0;
            status = "메인 메뉴";
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            selectedContactIndex = selectedIndex;
            selectedIndex = 0;
            page = Page.CONTACT_ACTIONS;
            status = "동작 선택";
            return true;
        }

        return false;
    }

    private boolean handleContactActionsKey(int keyCode) {
        if (keyCode == GLFW.GLFW_KEY_UP) {
            selectedIndex = (selectedIndex - 1 + CONTACT_ACTIONS.length) % CONTACT_ACTIONS.length;
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            selectedIndex = (selectedIndex + 1) % CONTACT_ACTIONS.length;
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            page = Page.CONTACTS;
            selectedIndex = selectedContactIndex;
            status = "연락처 선택";
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            Contact contact = getSelectedContact();
            if (contact == null) {
                return true;
            }

            switch (selectedIndex) {
                case 0 -> {
                    status = contact.name() + " 에게 전화 요청";
                    PhoneBridge.requestCall(contact.name(), contact.phoneNumber());
                    PhoneCallState.startCall(contact.name(), contact.phoneNumber());
                    Minecraft.getInstance().setScreen(null);
                }
                case 1 -> {
                    draftMessage = "";
                    page = Page.CHAT_ROOM;
                    status = contact.name() + " 채팅방";
                    updateInputBoxForCurrentPage();
                }
                case 2 -> {
                    contacts.remove(selectedContactIndex);

                    if (contacts.isEmpty()) {
                        page = Page.CONTACTS;
                        selectedIndex = 0;
                        selectedContactIndex = 0;
                        status = "연락처 삭제됨";
                    } else {
                        if (selectedContactIndex >= contacts.size()) {
                            selectedContactIndex = contacts.size() - 1;
                        }
                        page = Page.CONTACTS;
                        selectedIndex = selectedContactIndex;
                        status = "연락처 삭제됨";
                    }
                }
                case 3 -> {
                    page = Page.CONTACTS;
                    selectedIndex = selectedContactIndex;
                    status = "연락처 선택";
                }
            }
            return true;
        }

        return false;
    }

    private boolean handleTypingEnter() {
        if (inputBox == null) {
            return false;
        }

        String value = inputBox.getValue().trim();

        switch (page) {
            case ADD_CONTACT_NAME -> {
                if (value.isEmpty()) {
                    status = "이름을 입력해";
                    return true;
                }

                draftContactName = value;
                draftContactNumber = "";
                page = Page.ADD_CONTACT_NUMBER;
                status = "번호 입력";
                updateInputBoxForCurrentPage();
                return true;
            }

            case ADD_CONTACT_NUMBER -> {
                if (value.isEmpty()) {
                    status = "번호를 입력해";
                    return true;
                }

                draftContactNumber = value;
                contacts.add(new Contact(draftContactName, draftContactNumber));
                selectedIndex = contacts.size() - 1;
                page = Page.CONTACTS;
                status = "연락처 저장 완료";
                updateInputBoxForCurrentPage();

                PhoneBridge.saveContact(draftContactName, draftContactNumber);

                draftContactName = "";
                draftContactNumber = "";
                return true;
            }

            case CHAT_ROOM -> {
                Contact contact = getSelectedContact();
                if (contact == null) {
                    return true;
                }

                if (value.isEmpty()) {
                    status = "메시지를 입력해";
                    return true;
                }

                draftMessage = value;
                PhoneChatState.addMyMessage(contact.phoneNumber(), draftMessage);
                PhoneBridge.sendMessage(contact.name(), contact.phoneNumber(), draftMessage);
                status = contact.name() + " 에게 메시지 전송";
                draftMessage = "";
                updateInputBoxForCurrentPage();
                return true;
            }

            default -> {
                return false;
            }
        }
    }

    private boolean handleTypingBack() {
        switch (page) {
            case ADD_CONTACT_NAME -> {
                page = Page.CONTACTS;
                status = "연락처 선택";
                updateInputBoxForCurrentPage();
                return true;
            }
            case ADD_CONTACT_NUMBER -> {
                page = Page.ADD_CONTACT_NAME;
                status = "이름 입력";
                updateInputBoxForCurrentPage();
                return true;
            }
            case CHAT_ROOM -> {
                page = Page.CONTACT_ACTIONS;
                selectedIndex = 0;
                status = "동작 선택";
                updateInputBoxForCurrentPage();
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
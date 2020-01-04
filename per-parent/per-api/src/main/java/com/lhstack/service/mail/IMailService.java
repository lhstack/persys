package com.lhstack.service.mail;

public interface IMailService {
    void sendFileAndMsgMail(String[] paths, String template, String subject, Boolean isHtml, String... to) throws Exception;

    void sendFileMail(String[] paths, String subject, String... to) throws Exception;

    void sendHtmlMail(String template, String subject, String... to) throws Exception;

    void sendSimpleMail(String message, String subject, String... to);
}

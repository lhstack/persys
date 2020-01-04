package com.lhstack.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements IMailService{

    /**
     * 创建线程池发送邮箱
     */
    private ExecutorService executorService = new ThreadPoolExecutor(3,5,10000L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(5));

    /**
     * 邮箱模板
     */
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    /**
     *  构建MimeMessageHelper
     * @param form 发送者邮箱
     * @param subject 主题
     * @param mimeMessage 被操作对象
     * @param multipart 是否添加附件
     * @param to 收件人邮箱，支持多个
     * @return
     * @throws Exception
     */
    private MimeMessageHelper buildMimeMessageHelper(String form,String subject,MimeMessage mimeMessage,Boolean multipart,String ...to) throws Exception {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,multipart);
        mimeMessageHelper.setFrom(form);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setSentDate(new Date());
        return mimeMessageHelper;
    }


    /**
     * 发送文件邮箱加内容邮箱
     * @param paths 文件路径，多个
     * @param subject 主题
     * @param template html模板
     * @param to 收件人邮箱，支持多个
     * @throws Exception
     */
    @Override
    public void sendFileAndMsgMail(String[] paths, String template, String subject, Boolean isHtml, String... to) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = buildMimeMessageHelper(mailProperties.getUsername(),subject,mimeMessage,true,to);
        Arrays.asList(paths).forEach(item ->{
            FileSystemResource file = new FileSystemResource(new File(item));
            try {
                mimeMessageHelper.addAttachment(file.getFilename(), file);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        mimeMessageHelper.setText(template,isHtml);
        executorService.execute(() ->{
            mailSender.send(mimeMessage);
        });
    }

    /**
     * 发送文件邮箱
     * @param paths 文件路径
     * @param subject 主题
     * @param to 目的地邮箱
     * @throws Exception
     */
    @Override
    public void sendFileMail(String[] paths, String subject, String... to) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = buildMimeMessageHelper(mailProperties.getUsername(),subject,mimeMessage,true,to);
        Arrays.asList(paths).forEach(item ->{
            FileSystemResource file = new FileSystemResource(new File(item));
            try {
                mimeMessageHelper.addAttachment(file.getFilename(), file);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() ->{
            mailSender.send(mimeMessage);
        });
    }


    /**
     * 发送html邮箱
     * @param template html模板
     * @param subject 主题
     * @param to 收件人邮箱，支持多个
     * @throws Exception
     */
    @Override
    public void sendHtmlMail(String template, String subject, String... to) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = buildMimeMessageHelper(mailProperties.getUsername(),subject,mimeMessage,false,to);
        mimeMessageHelper.setText(template,true);
        executorService.execute(() ->{
            mailSender.send(mimeMessage);
        });
    }

    /**
     * 发送简单邮箱
     * @param to 目的地邮箱
     * @param message 消息
     */
    @Override
    public void sendSimpleMail(String message,String subject,String ...to){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailProperties.getUsername());
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setSentDate(new Date());
        executorService.execute(() ->{
            mailSender.send(simpleMailMessage);
        });
    }
}

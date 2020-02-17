package cn.itrip.auth.service;

public interface MailService {
    /**
     * 发送激活码
     * @param mailTo
     * mailTo收件人地址
     * activationCode激活码
     * @param activationCode
     *
     */
    public void sendActivationMail(String mailTo,String activationCode);

}



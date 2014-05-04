package dmk.deusexmachina.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import dmk.mail.MailSenderService;
import dmk.mapquest.model.BoundingBox;
import dmk.mapquest.model.Filter;
import dmk.mapquest.model.Incident;
import dmk.mapquest.model.MapQuestTrafficResponse;
import dmk.mapquest.service.MapQuestTrafficService;
import dmk.twilio.sms.PhoneSmsService;

public class AntikytheraMechanismImpl implements AntikytheraMechanism {
	Logger logger = LoggerFactory.getLogger(AntikytheraMechanismImpl.class);

	protected String emailRecipient;

	protected MailSenderService mailSenderService;
	protected PortalLoginService portalLoginService;
	protected MapQuestTrafficService mapQuestTrafficService;
	protected static final TrafficMessageNotificationFormatter trafficMessageFormatter = new TrafficMessageNotificationFormatter();
	protected Boolean shouldEmail;
	protected Boolean shouldSms;
	protected String smsTo;
	protected String smsFrom;
	protected PhoneSmsService phoneSmsService;

	protected final String subject = "Deus Ex Machina";

	public AntikytheraMechanismImpl() {
		super();
	}

	@Scheduled(cron = "0 0 10,11,12,13,14,20,21 ? * MON-FRI")
	public void trafficUpdatesWeekday() {
		lookupTrafficInfo();
	}

	@Scheduled(cron = "0 0 11,19 ? * SAT,SUN")
	public void trafficUpdatesWeekend() {
		lookupTrafficInfo();
	}

	@Override
	@Scheduled(cron = "0 0 10 ? * MON-FRI")
	public void commencementOfTheDawn() {
		final String html = portalLoginService.loginHomePage();
		logger.debug(html);
		String[] users = { "Damian Knopp" };
		String[] needCheckIn = portalLoginService.checkUsersStatus(html, users);

		// needCheckIn = users;
		for (final String user : portalLoginService
				.userNameToLogin(needCheckIn)) {
			logger.info("Need to check in user " + user);
			final boolean success = portalLoginService.checkIn(user);
			if (!success) {
				logger.warn("WARNING! failed to check in user " + user);
				final String msg = "AntikytheraMechanism failed to start for user "
						+ user;
				notifyStatus(subject, msg);
			} else {
				logger.info("checked in user " + user + " at " + Instant.now());
				final String msg = "AntikytheraMechanism started for user "
						+ user;
				notifyStatus(subject, msg);
			}
		}
	}

	protected void lookupTrafficInfo() {
		BoundingBox cities[] = { BoundingBox.EASTON, BoundingBox.STEVENSVILLE,
				BoundingBox.ANNAPOLIS, BoundingBox.COLUMBIA };

		List<Incident> notifications = new ArrayList<>();
		for (BoundingBox city : cities) {
			MapQuestTrafficResponse resp = this.mapQuestTrafficService
					.queryForIncidents(city, Filter.CONGESTION,
							Filter.INCIDENTS);
			Incident[] incidents = resp.getIncidents();
			notifications.addAll(Arrays.asList(incidents));
		}

		this.notifyStatus(notifications);

	}

	protected void notifyStatus(final String subject, final String msg) {
		if (shouldEmail) {
			if(logger.isDebugEnabled()){
				logger.debug("emailing " + emailRecipient + " " + subject);
			}
			this.mailSenderService.send(subject, msg, emailRecipient);
		}

		if (shouldSms) {
			if(logger.isDebugEnabled()){
				logger.debug("texting " + this.smsTo);
			}
			final String summaryMsg = msg.length() >= 160 ? "You have traffic notifications!" : msg;
			this.phoneSmsService.sendMessage(this.smsTo, this.smsFrom, summaryMsg);
		}
	}

	protected void notifyStatus(final List<Incident> incidents) {
		final String msg = trafficMessageFormatter.formatter(incidents);
		this.notifyStatus(subject, msg);
	}

	public MailSenderService getMailSenderService() {
		return mailSenderService;
	}

	public void setMailSenderService(MailSenderService mailSenderService) {
		this.mailSenderService = mailSenderService;
	}

	public PortalLoginService getPortalLoginService() {
		return this.portalLoginService;
	}

	public void setPortalLoginService(PortalLoginService portalLoginService) {
		this.portalLoginService = portalLoginService;
	}

	public String getEmailRecipient() {
		return emailRecipient;
	}

	public void setEmailRecipient(String emailRecipient) {
		this.emailRecipient = emailRecipient;
	}

	public MapQuestTrafficService getMapQuestTrafficService() {
		return mapQuestTrafficService;
	}

	public void setMapQuestTrafficService(
			MapQuestTrafficService mapQuestTrafficService) {
		this.mapQuestTrafficService = mapQuestTrafficService;
	}

	public Boolean getShouldEmail() {
		return shouldEmail;
	}

	public void setShouldEmail(Boolean shouldEmail) {
		this.shouldEmail = shouldEmail;
	}

	public Boolean getShouldSms() {
		return shouldSms;
	}

	public void setShouldSms(Boolean shouldSms) {
		this.shouldSms = shouldSms;
	}

	public String getSmsTo() {
		return smsTo;
	}

	public void setSmsTo(String smsTo) {
		this.smsTo = smsTo;
	}

	public String getSmsFrom() {
		return smsFrom;
	}

	public void setSmsFrom(String smsFrom) {
		this.smsFrom = smsFrom;
	}

	public PhoneSmsService getPhoneSmsService() {
		return phoneSmsService;
	}

	public void setPhoneSmsService(PhoneSmsService phoneSmsService) {
		this.phoneSmsService = phoneSmsService;
	}
}
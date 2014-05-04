package dmk.deusexmachina.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import dmk.mapquest.model.Incident;

public class TrafficMessageNotificationFormatter {

	public TrafficMessageNotificationFormatter(){
		super();
	}
	
	public String formatter(List<Incident> incidents){
		StringBuilder sb = new StringBuilder(incidents.size() * 128);
		for (Incident incident : incidents) {
			boolean impacting = incident.isImpacting();
			int severity = incident.getSeverity();
			int type = incident.getType();
			float distance = incident.getDistance();
			float delayFromTypical = incident.getDelayFromTypical();
			float delayFromFreeFlow = incident.getDelayFromTypical();
			Date startTime = incident.getStartTime();
			Date endTime = incident.getEndTime();
			String shortDesc = incident.getShortDesc();
			String fullDesc = incident.getFullDesc();
			LocalDateTime startDt = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(startTime.getTime())
					,ZoneOffset.UTC);
			LocalDateTime endDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime.getTime()),
					ZoneOffset.UTC);
			Duration duration = Duration.between(startDt, endDt);
			sb.append("* " + (impacting ? " Impacting" : " Nonimpacting") + " incident type (" + type + ")")
			.append(" with a severity of " + severity + " for " + distance + " miles\n");
			sb.append("\t" + " delayFromTypical " + delayFromTypical + " delayFromFreeFlow is " + delayFromFreeFlow + "\n");
			sb.append("\t" + fullDesc + "\n");
			sb.append("\t" + "Earliest expected clear up is " + Duration.between(Instant.now(), Instant.ofEpochMilli(endTime.getTime())));
			sb.append("\n");
		}
		return sb.toString();
	}
}
